package com.example.cs446_group8.calculations;

import androidx.room.Ignore;

import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.Crop;
import com.example.cs446_group8.data.CropSchedule;
import com.example.cs446_group8.data.CropType;
import com.example.cs446_group8.data.HeadCounts;
import com.example.cs446_group8.data.Project;
import com.example.cs446_group8.data.ProjectDao;
import com.example.cs446_group8.data.ProjectWithSows;
import com.example.cs446_group8.data.SowWithCrop;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class Plantings {
    private ProjectDao projectDao;
    private long projectId;

    public Plantings(AppDatabase db, long projectId) {
        projectDao = db.projectDao();
        this.projectId = projectId;
    }

    public List<Integer> getMonthlySquareFeet() {
        WeeklySqft weeklySqft = getWeeklySqft(getSqftPerPerson());
        ProjectWithSows projectWithSows = projectDao.loadOneByIdWithSows(projectId);

        List<Integer> weeklyConcurrentUse = Workspace.getWeeklyConcurrentUse(
                projectWithSows.getSows().stream().map(SowWithCrop::getCrop).collect(Collectors.toList()),
                weeklySqft.getGreen(),
                weeklySqft.getColorful(),
                weeklySqft.getStarch());

        return IntStream.range(0, 12)
                .map(month -> Workspace.getSqftForMonth(weeklyConcurrentUse, month))
                .boxed()
                .collect(Collectors.toList());
    }

    public List<CropSchedule> getSchedule() {
        ProjectWithSows projectWithSows = projectDao.loadOneByIdWithSows(projectId);
        LocalDate projectStart = projectWithSows.getProject().getBeginningOfSession();
        WeeklySqft weeklySqft = getWeeklySqft(getSqftPerPerson());

        return projectWithSows.getSows()
                .stream()
                .map(SowWithCrop::getCrop)
                .map(crop -> CropSchedule.builder()
                        .crop(crop)
                        .firstPlanting(Workspace.roundToNearestSunday(
                                projectStart.minus(crop.getDays(), ChronoUnit.DAYS)))
                        .weeklyPlantingAmounts(getWeeklySqftForCrop(crop, weeklySqft))
                        .build())
                .collect(Collectors.toList());
    }


    private SqftPerPerson getSqftPerPerson() {
        ProjectWithSows projectWithSows = projectDao.loadOneByIdWithSows(projectId);
        return SqftPerPerson.builder()
                .starch(Workspace.getSquareFeetForType(projectWithSows, CropType.Starch))
                .colorful(Workspace.getSquareFeetForType(projectWithSows, CropType.Colorful))
                .green(Workspace.getSquareFeetForType(projectWithSows, CropType.Green))
                .build();
    }

    private WeeklySqft getWeeklySqft(SqftPerPerson sqftPerPerson) {
        Project project = projectDao.loadOneById(projectId);
        HeadCounts headCounts = project.getHeadCounts();

        return WeeklySqft.builder()
                .colorful(Workspace.getWeeklySquareFeet(headCounts, sqftPerPerson.getColorful()))
                .green(Workspace.getWeeklySquareFeet(headCounts, sqftPerPerson.getGreen()))
                .starch(Workspace.getWeeklySquareFeet(headCounts, sqftPerPerson.getStarch()))
                .build();
    }

    private List<Integer> getWeeklySqftForCrop(Crop crop, WeeklySqft weeklySqft) {
        if (crop.getType() == CropType.Green) {
            return weeklySqft.getGreen();
        } else if (crop.getType() == CropType.Starch) {
            return weeklySqft.getStarch();
        } else {
            return weeklySqft.getColorful();
        }
    }
}

@Getter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
class WeeklySqft {
    // how much of each crop we'll be harvesting each week
    // plant times are simply these numbers, shifted
    private List<Integer> green;
    private List<Integer> starch;
    private List<Integer> colorful;
}


@Getter
@AllArgsConstructor(onConstructor = @__({@Ignore}))
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
class SqftPerPerson {
    private int green;
    private int starch;
    private int colorful;
}
