package com.example.cs446_group8.data;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.closeTo;

@RunWith(AndroidJUnit4.class)
public final class WorkspaceTest {
    private Workspace workspace;

    @Before
    public void createWorkspace() {
        Context context = ApplicationProvider.getApplicationContext();
        workspace = new Workspace(context);
    }

    @Test
    public void testCaloriesPerSqft() {
        Crop amaranth = Crop.builder()
                .id(1)
                .name("Amaranth leaves")
                .type(CropType.Green)
                .caloriesPer100Gram(371)
                .yieldPer100Sqft(40)
                .build();
        Crop arrowRoot = Crop.builder()
                .id(2)
                .name("Arrow roots")
                .type(CropType.Colorful)
                .caloriesPer100Gram(100)
                .yieldPer100Sqft(27)
                .build();
        assertThat(workspace.getCaloriesPerSquareFoot(amaranth), equalTo(673.736));
        assertThat(workspace.getCaloriesPerSquareFoot(arrowRoot), equalTo(122.58));
    }

    @Test
    public void testSquareFeet() {
        // assume that cal/day = 2500
        // and G = 20%, S=20%, C = 20%
        Crop amaranth = Crop.builder()
                .name("Amaranth leaves")
                .type(CropType.Green)
                .caloriesPer100Gram(100)
                .yieldPer100Sqft(100/4.54)
                .build();

        Crop arrowRoot = Crop.builder()
                .name("Arrow roots")
                .type(CropType.Colorful)
                .caloriesPer100Gram(100)
                .yieldPer100Sqft(200 / 4.54)
                .build();

        Crop carrot = Crop.builder()
                .name("Carrots")
                .type(CropType.Colorful)
                .caloriesPer100Gram(100)
                .yieldPer100Sqft(100 / 4.54)
                .build();

        List<Crop> crops = Arrays.asList(amaranth, arrowRoot, carrot);
        int greenSqft = workspace.getSquareFeetForType(crops, 1, CropType.Green);
        int colorSqft = workspace.getSquareFeetForType(crops, 1, CropType.Colorful);

        assertThat(greenSqft, equalTo(150));
        assertThat(colorSqft, equalTo(150));

        // make sure that we're actually hitting our calorie goals
        assertThat(greenSqft * (int) workspace.getCaloriesPerSquareFoot(amaranth), equalTo(15_000));
        assertThat(colorSqft * (int) (workspace.getCaloriesPerSquareFoot(arrowRoot) + workspace.getCaloriesPerSquareFoot(carrot)), equalTo(45_000));
    }

    @Test
    public void testWeeklySquareFeet() {
        for (int i = 100; i < 250; i += 13) {
            testWeeklySquareFeet(Arrays.asList(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10), i);
            testWeeklySquareFeet(Arrays.asList(10, 10, 10, 10, 20, 20, 40, 40, 50, 50, 10, 10), i);
        }
        // works with 0
        testWeeklySquareFeet(Arrays.asList(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10), 0);
    }

    private void testWeeklySquareFeet(List<Integer> headCounts, int initialSquareFeet) {
        int totalSquareFeet = headCounts.stream().mapToInt(Integer::intValue).sum() * initialSquareFeet / headCounts.get(0);
        List<Integer> weekly = workspace.getWeeklySquareFeet(headCounts, initialSquareFeet);

        // rounded to 25 sqft, so make sure that it adds up to within 25 square feet
        assertThat(
                (double) weekly.stream().mapToInt(Integer::intValue).sum(),
                closeTo(totalSquareFeet, 25)
        );
        // check that each month is close
        int i = 0;
        int month = 0;
        while(i < 52) {
            int weeks = 4 + (month % 3 == 0? 1 : 0);
            int squareFeetForMonth = initialSquareFeet * headCounts.get(month) / headCounts.get(0);
            int calculatedSquareFeet = 0;
            for (int j = 0; j < weeks; j++) {
                calculatedSquareFeet += weekly.get(i);
                i ++;
            }
            assertThat((double) calculatedSquareFeet, closeTo(squareFeetForMonth, 25)); // each month could be 25 sqft off
            month ++;
        }
    }

    @Test
    public void testPlantingDays() {
        Crop amaranth = Crop.builder()
                .name("amaranth leaves")
                .type(CropType.Green)
                .days(60)
                .build();
        List<LocalDate> dates = workspace.getPlantTimes(LocalDate.of(2020, 8, 1), amaranth);
        assertThat(dates.size(), equalTo(52));
        for (int i = 0; i < 52; i++) {
            assertThat(dates.get(i), equalTo(LocalDate.of(2020, 8, 1).plus(7 * i - 60, ChronoUnit.DAYS)));
        }
    }
}
