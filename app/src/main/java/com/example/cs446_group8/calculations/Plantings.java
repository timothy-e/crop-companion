package com.example.cs446_group8.calculations;

import com.example.cs446_group8.data.Crop;
import com.example.cs446_group8.data.CropSchedule;
import com.example.cs446_group8.data.CropType;
import com.example.cs446_group8.data.ProjectWithSows;
import com.example.cs446_group8.data.SowWithCrop;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Plantings {
    public ProjectWithSows projectWithSows;
    public Map<Crop, List<Integer>> plantedCrops;
    public int greenSqft;
    public int starchSqft;
    public int colorSqft;

    // how much of each crop we'll be harvesting each week
    // plant times are simply these numbers, shifted
    public List<Integer> greenWeeklySqft;
    public List<Integer> starchWeeklySqft;
    public List<Integer> colorWeeklySqft;

    List<Crop> crops;

    public Plantings(ProjectWithSows projectWithSows) {
        this.projectWithSows = projectWithSows;
        this.crops = projectWithSows.getSows()
                .stream()
                .map(SowWithCrop::getCrop)
                .collect(Collectors.toList());
        refreshSqftPerPerson();
        refreshWeeklySqft();
    }

    public List<Integer> getMonthlySquareFeet() {
        List<Integer> weeklyConcurrentUse = Workspace.getWeeklyConcurrentUse(crops, greenWeeklySqft, colorWeeklySqft, starchWeeklySqft);
        return IntStream.range(0, 12)
                .map(month -> Workspace.getSqftForMonth(weeklyConcurrentUse, month))
                .boxed()
                .collect(Collectors.toList());
    }

    public List<CropSchedule> getSchedule() {
        refresh();
        LocalDate projectStart = projectWithSows.getProject().getBeginningOfSession();
        return crops.stream()
                .map(crop -> CropSchedule.builder()
                        .crop(crop)
                        .firstPlanting(projectStart.minus(
                                (int) Math.ceil((double) crop.getDays() / 7),
                                ChronoUnit.WEEKS))
                        .weeklyPlantingAmounts(getWeeklySqftForCrop(crop))
                        .build())
                .collect(Collectors.toList());
    }


    public void refresh() {
        refreshSqftPerPerson();
        refreshWeeklySqft();
    }

    private void refreshSqftPerPerson() {
        greenSqft = Workspace.getSquareFeetForType(projectWithSows, CropType.Green);
        colorSqft = Workspace.getSquareFeetForType(projectWithSows, CropType.Colorful);
        starchSqft = Workspace.getSquareFeetForType(projectWithSows, CropType.Starch);
    }

    private void refreshWeeklySqft() {
        greenWeeklySqft = Workspace.getWeeklySquareFeet(
                projectWithSows.getProject().getHeadCounts(),
                greenSqft
        );
        starchWeeklySqft = Workspace.getWeeklySquareFeet(
                projectWithSows.getProject().getHeadCounts(),
                starchSqft
        );
        colorWeeklySqft = Workspace.getWeeklySquareFeet(
                projectWithSows.getProject().getHeadCounts(),
                colorSqft
        );
    }

    private List<Integer> getWeeklySqftForCrop(Crop crop) {
        if (crop.getType() == CropType.Green) {
            return greenWeeklySqft;
        } else if (crop.getType() == CropType.Starch) {
            return starchWeeklySqft;
        } else {
            return colorWeeklySqft;
        }
    }
}
