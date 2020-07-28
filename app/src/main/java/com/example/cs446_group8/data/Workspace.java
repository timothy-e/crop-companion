package com.example.cs446_group8.data;

import android.content.Context;

import androidx.room.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Workspace {
    private CropDao cropDao;
    private AppDatabase db;

    public Workspace(Context context) {
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        cropDao = db.cropDao();
    }

    public double getCaloriesPerSquareFoot(Crop crop) {
        double gramsYieldPerSqft = crop.getYieldPer100Sqft() * 454 / 100;
        return (crop.getCaloriesPer100Gram() / 100) * gramsYieldPerSqft;
    }

    public int getMonthlyCaloriesByType(int headCount, CropType cropType) {
        int caloriesPerDayPerPerson = 2500;
        int percentGreens = 20;
        int percentStarch = 20;
        int percentColorful = 60;
        switch (cropType) {
            case Green:
                return caloriesPerDayPerPerson * headCount * 30 * percentGreens / 100;
            case Starch:
                return caloriesPerDayPerPerson * headCount * 30 * percentStarch / 100;
            case Colorful:
                return caloriesPerDayPerPerson * headCount * 30 * percentColorful / 100;
        }
        return 0;
    }

    public int getSquareFeetForType(List<Crop> crops, int headCount, CropType cropType) {
        int calorieGoal = getMonthlyCaloriesByType(headCount, cropType);
        double calsPerSqft = crops.stream()
            .filter(c -> c.getType() == cropType)
            .mapToDouble(this::getCaloriesPerSquareFoot)
            .sum();

        return (int) (calorieGoal / calsPerSqft);
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
    public List<Integer> getWeeklySquareFeet(List<Integer> monthlyHeadCounts, int initialSquareFeet) {
        assert(monthlyHeadCounts.size() == 12);

        List<Integer> list = new ArrayList<>();

        // we want to track how much we were off on the previous month by, and account for that
        // in the current month. This way if we had a little more than planned one month, we make a
        // little less than planned the next month
        int excess = 0;

        for (int i = 0; i < 12; i++) {
            int monthSquareFeet = initialSquareFeet * monthlyHeadCounts.get(i) / monthlyHeadCounts.get(0) + excess;
            int weeks = 4 + (i % 3 == 0 ? 1 : 0); // 5 weeks on every 3rd month (approximately)

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
    public List<LocalDate> getPlantTimes(LocalDate startDate, Crop crop) {
        List<LocalDate> list = new ArrayList<>();
        LocalDate firstPlanting = startDate.minus(crop.getDays(), ChronoUnit.DAYS);
        for (int i = 0; i < 52; i++) {
            list.add(firstPlanting.plus(i, ChronoUnit.WEEKS));
        }
        return list;
    }
}
