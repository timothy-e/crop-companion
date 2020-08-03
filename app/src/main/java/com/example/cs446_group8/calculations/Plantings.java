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

    /**
     * Uses the headcounts stored on the project in the database
     * @return A list of length 12 describing the number of square feet we need per month
     */
    public List<Integer> getMonthlySquareFeet() {
        Project project = projectDao.loadOneById(projectId);
        return getMonthlySquareFeet(project.getHeadCounts());
    }

    /**
     * Uses the supplied headcounts for the calculations
     * @param headCounts any random headcounts thing
     * @return A list of length 12 describing the number of square feet we need per month
     */
    public List<Integer> getMonthlySquareFeet(HeadCounts headCounts) {
        WeeklySqft weeklySqft = getWeeklySqft(headCounts, getSqftPerPerson());
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

    /**
     * Calculates the schedule. First planting date is some day before the project start date.
     * The weekly amounts will typically be >0 until the end. Exceptions:
     *    * if it takes 3 weeks for a crop to grow, we won't be planting any during the last 3 weeks
     *    * if we have some months with no people, we will have no plantings for some weeks.
     * @return A list of schedule objects
     */
    public List<CropSchedule> getSchedule() {
        ProjectWithSows projectWithSows = projectDao.loadOneByIdWithSows(projectId);
        LocalDate projectStart = projectWithSows.getProject().getBeginningOfSession();
        WeeklySqft weeklySqft = getWeeklySqft(
                projectWithSows.getProject().getHeadCounts(),
                getSqftPerPerson()
        );

        return projectWithSows.getSows()
                .stream()
                .map(SowWithCrop::getCrop)
                .map(crop -> CropSchedule.builder()
                        .crop(crop)
                        .firstPlanting(projectStart.minus(
                                (int) Math.ceil((double) crop.getDays() / 7),
                                ChronoUnit.WEEKS))
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

    private WeeklySqft getWeeklySqft(HeadCounts headCounts, SqftPerPerson sqftPerPerson) {
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
