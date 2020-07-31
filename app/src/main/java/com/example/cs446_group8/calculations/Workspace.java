package com.example.cs446_group8.calculations;

import androidx.annotation.VisibleForTesting;

import com.example.cs446_group8.data.Crop;
import com.example.cs446_group8.data.CropType;
import com.example.cs446_group8.data.HeadCounts;
import com.example.cs446_group8.data.Project;
import com.example.cs446_group8.data.ProjectWithSows;
import com.example.cs446_group8.data.SowWithCrop;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Workspace {
    public static double getCaloriesPerSquareFoot(Crop crop) {
        double gramsYieldPerSqft = crop.getYieldPer100Sqft() * 454 / 100;
        return (crop.getCaloriesPer100Gram() / 100) * gramsYieldPerSqft;
    }

    public static List<Integer> getWeeklySquareFeet(HeadCounts headCounts, int initialSquareFeet) {
        return getWeeklySquareFeet(headCounts.toList(), initialSquareFeet);
    }

    /**
     * Determines how much to harvest for every week of the year, independent of date.
     * The output of this function can be changed to a date and shifted by the maturation time of
     * a crop to get planting data
     * @param monthlyHeadCounts the head counts for every month
     * @param initialSquareFeet the square feet for the starting month. the square feet for every
     *                          subsequent month can be determined from this and the headcounts
     * @return the number of square feet to harvest for every week of the year
     */
    public static List<Integer> getWeeklySquareFeet(List<Integer> monthlyHeadCounts, int initialSquareFeet) {
        assert(monthlyHeadCounts.size() == 12);

        List<Integer> list = new ArrayList<>();

        // we want to track how much we were off on the previous month by, and account for that
        // in the current month. This way if we had a little more than planned one month, we make a
        // little less than planned the next month
        int excess = 0;

        for (int i = 0; i < 12; i++) {
            int monthSquareFeet = initialSquareFeet * monthlyHeadCounts.get(i) + excess;
            int weeks = getWeeksInMonth(i);

            int currentSquareFeet = 0;
            for (int j = 0; j < weeks; j++) {
                int newSquareFeet = (int) (25 * Math.round((monthSquareFeet - currentSquareFeet) / (double) (weeks - j) / 25d));
                currentSquareFeet += newSquareFeet;
                list.add(newSquareFeet);
            }
            excess = monthSquareFeet - currentSquareFeet;
        }

        return list;
    }

    /**
     * @param startDate the first harvest date
     * @param crop the crop that we're interested in
     * @return a list of dates corresponding to weekly plant times
     */
    public static List<LocalDate> getPlantTimes(LocalDate startDate, Crop crop) {
        List<LocalDate> list = new ArrayList<>();
        LocalDate firstPlanting = startDate.minus(crop.getDays(), ChronoUnit.DAYS);
        for (int i = 0; i < 52; i++) {
            list.add(firstPlanting.plus(i, ChronoUnit.WEEKS));
        }
        return list;
    }

    /**
     * We can approximate to 5 weeks on every 3rd month
     * @param month integer from 0-1-1
     * @return either 4 or 5, depending on the number of weeks in the month
     */
    public static int getWeeksInMonth(int month) {
        return 4 + (month % 3 == 0 ? 1 : 0);
    }

    /**
     * We are approximating 5 weeks in every 3rd month
     * @param month integer from 0-11
     * @return the number of weeks in a "month"
     */
    public static int getStartWeek(int month) {
        return 4 * month + ((month + 2) / 3);
    }

    /**
     * Returns the number of square feet of space used in a given month
     * @param weeklyConcurrentUse a list of 52+k elements, where the first k elements are before Jan
     * @param month an integer from 0-11
     * @return
     */
    public static int getSqftForMonth(List<Integer> weeklyConcurrentUse, int month) {
        int weeks = getWeeksInMonth(month); // 5 weeks on every 3rd month (approximately)
        int startWeek = getStartWeek(month);
        int offset = weeklyConcurrentUse.size() - 52;

        int sqftUsed = 0;
        for (int i = 0; i < weeks; i++) {
            sqftUsed = Math.max(sqftUsed, weeklyConcurrentUse.get(offset + startWeek + i));
        }
        return sqftUsed;
    }

    /**
     * Returns the total amount of square feet used by all crops, on a weekly basis
     * @param crops a list of the crops to calculate for
     * @param greenWeeklySqft how many square feet we're planting of each green.
     * @param colorWeeklySqft how many square feet we're planting of each colorful veggie.
     * @param starchWeeklySqft how many square feet we're planting of each starch.
     * @return a list of numbers, where the ith term is the number of square feet used in the ith week.
     *         The last element of the list corresponds to the last week of December.
     *         The first few elements of the list correspond to before January: before we started harvesting.
     */
    public static List<Integer> getWeeklyConcurrentUse(List<Crop> crops, List<Integer> greenWeeklySqft, List<Integer> colorWeeklySqft, List<Integer> starchWeeklySqft) {
        int longestUseTime = 52 + crops.stream()
                .mapToInt(c -> (int) Math.ceil((double) c.getDays() / 7))
                .max()
                .orElse(0); // returns 0 if the list was empty

        List<Integer> concurrentUse = new ArrayList<>(longestUseTime);
        while(concurrentUse.size() < longestUseTime) concurrentUse.add(0);
        for (Crop crop : crops) {
            List<Integer> concurrentUseForCrop;
            if (crop.getType() == CropType.Green) {
                concurrentUseForCrop = getWeeklyConcurrentUseForCrop(crop, greenWeeklySqft);
            } else if (crop.getType() == CropType.Colorful) {
                concurrentUseForCrop = getWeeklyConcurrentUseForCrop(crop, colorWeeklySqft);
            } else {
                concurrentUseForCrop = getWeeklyConcurrentUseForCrop(crop, starchWeeklySqft);
            }

            int startWeek = longestUseTime - concurrentUseForCrop.size();
            for (int i = 0; i < concurrentUseForCrop.size(); i++) {
                concurrentUse.set(i + startWeek, concurrentUse.get(i+startWeek) + concurrentUseForCrop.get(i));
            }
        }
        return concurrentUse;
    }

    /**
     * Calculates the total of beds used simultaneously for any given week
     * i.e. if a crop takes three weeks to grow, and we are planting [1, 2, 2, 4] sqft over 4 weeks
     *      then we output [1, 1+2, 1+2+2, 2+2+4, 2+4, 4]
     * @param crop the crop we're interested in
     * @param weeklySqft how many square feet we're planting of each crop.
     * @return a list of numbers, where the ith term is how many square feet of that crop we have
     *         in total during the ith week
     */
    @VisibleForTesting
    public static List<Integer> getWeeklyConcurrentUseForCrop(Crop crop, List<Integer> weeklySqft) {
        int maturationWeeks = (int) Math.ceil((double) crop.getDays() / 7);
        List<Integer> concurrentUse = new ArrayList<>();

        concurrentUse.add(weeklySqft.get(0));
        for (int i = 1; i < 52 + maturationWeeks; i++) {
            int newUse = concurrentUse.get(i - 1); // default to the previous value

            if (i < 52) { // if we're still planting
                newUse += weeklySqft.get(i); // add the new plantings
            }
            if (i - maturationWeeks >= 0) { // if we started harvesting
                newUse -=  weeklySqft.get(i - maturationWeeks); // remove the harvested plantings
            }
            concurrentUse.add(newUse);
        }
        return concurrentUse;
    }

    /**
     *
     * @param project
     * @param cropType
     * @return the number of calories of a given type we need per person per month
     */
    private static int getMonthlyCaloriesByType(Project project, CropType cropType) {
        int caloriesPerDayPerPerson = project.getCaloriesPerDayPerPerson();

        switch (cropType) {
            case Green:
                return caloriesPerDayPerPerson * 30 * project.getCaloriesFromGreen() / 100;
            case Starch:
                return caloriesPerDayPerPerson * 30 * project.getCaloriesFromStarch() / 100;
            case Colorful:
                return caloriesPerDayPerPerson * 30 * project.getCaloriesFromColorful() / 100;
        }
        return 0;
    }

    /**
     * We want an equal amount of square feet for each crop for each person (within the same type)
     * @param projectWithSows
     * @param cropType
     * @return how much of C / S / G to plant per person
     */
    public static int getSquareFeetForType(ProjectWithSows projectWithSows, CropType cropType) {
        int calorieGoal = getMonthlyCaloriesByType(projectWithSows.getProject(), cropType);
        double calsPerSqft = projectWithSows.getSows().stream()
                .map(SowWithCrop::getCrop)
                .filter(c -> c.getType() == cropType)
                .mapToDouble(Workspace::getCaloriesPerSquareFoot)
                .sum();

        if (calsPerSqft == 0)
            return 0;
        return (int) (calorieGoal / calsPerSqft);
    }
}
