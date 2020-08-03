package com.example.cs446_group8.calculations;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cs446_group8.data.Crop;
import com.example.cs446_group8.data.CropType;
import com.example.cs446_group8.data.HeadCounts;
import com.example.cs446_group8.data.Project;
import com.example.cs446_group8.data.ProjectWithSows;
import com.example.cs446_group8.data.Sow;
import com.example.cs446_group8.data.SowWithCrop;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public final class WorkspaceTest {
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
        assertThat(Workspace.getCaloriesPerSquareFoot(amaranth), equalTo(673.736));
        assertThat(Workspace.getCaloriesPerSquareFoot(arrowRoot), equalTo(122.58));
    }

    @Test
    public void testSquareFeet() {
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

        Project project = Project.builder()
                .name("My Project")
                .beginningOfSession(LocalDate.of(2020, 8, 1))
                .headCounts(
                        new HeadCounts(1, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10)
                )
                .caloriesPerDayPerPerson(2500)
                .caloriesFromColorful(60)
                .caloriesFromStarch(20)
                .caloriesFromGreen(20)
                .build();

        ProjectWithSows projectWithSows = new ProjectWithSows(project, Arrays.asList(
                new SowWithCrop(new Sow(1, 1), amaranth),
                new SowWithCrop(new Sow(1, 1), arrowRoot),
                new SowWithCrop(new Sow(1, 1), carrot)
        ));

        int greenSqft = Workspace.getSquareFeetForType(projectWithSows, CropType.Green);
        int colorSqft = Workspace.getSquareFeetForType(projectWithSows, CropType.Colorful);

        assertThat(greenSqft, equalTo(150));
        assertThat(colorSqft, equalTo(150));

        // make sure that we're actually hitting our calorie goals
        assertThat(greenSqft * (int) Workspace.getCaloriesPerSquareFoot(amaranth), equalTo((int) (2500 * 30 * 0.2d)));
        assertThat(colorSqft * (int) (Workspace.getCaloriesPerSquareFoot(arrowRoot) + Workspace.getCaloriesPerSquareFoot(carrot)), equalTo((int) (2500 * 30 * 0.6)));

        // change some numbers
        project.setCaloriesFromColorful(40);
        project.setCaloriesFromGreen(40);
        project.setCaloriesPerDayPerPerson(5000);

        greenSqft = Workspace.getSquareFeetForType(projectWithSows, CropType.Green);
        colorSqft = Workspace.getSquareFeetForType(projectWithSows, CropType.Colorful);

        assertThat(greenSqft, equalTo(600));
        assertThat(colorSqft, equalTo(200));

        // make sure that we're actually hitting our calorie goals
        assertThat(greenSqft * (int) Workspace.getCaloriesPerSquareFoot(amaranth), equalTo((int) (5000 * 30 * 0.4)));
        assertThat(colorSqft * (int) (Workspace.getCaloriesPerSquareFoot(arrowRoot) + Workspace.getCaloriesPerSquareFoot(carrot)), equalTo((int) (5000 * 30 * 0.4)));
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
        int totalSquareFeet = headCounts.stream().mapToInt(Integer::intValue).sum() * initialSquareFeet;
        List<Integer> weekly = Workspace.getWeeklySquareFeet(headCounts, initialSquareFeet);

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
            int squareFeetForMonth = initialSquareFeet * headCounts.get(month);
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
    public void roundToSunday() {
        assertThat(
                Workspace.roundToNearestSunday(LocalDate.of(2020, 8, 2)), // Sunday
                equalTo(LocalDate.of(2020, 8, 2)));
        assertThat(
                Workspace.roundToNearestSunday(LocalDate.of(2020, 8, 3)), // Monday
                equalTo(LocalDate.of(2020, 8, 2)));
        assertThat(
                Workspace.roundToNearestSunday(LocalDate.of(2020, 8, 4)), // Tuesday
                equalTo(LocalDate.of(2020, 8, 2)));
        assertThat(
                Workspace.roundToNearestSunday(LocalDate.of(2020, 8, 5)), // Wednesday
                equalTo(LocalDate.of(2020, 8, 2)));
        assertThat(
                Workspace.roundToNearestSunday(LocalDate.of(2020, 8, 6)), // Thursday
                equalTo(LocalDate.of(2020, 8, 9)));
        assertThat(
                Workspace.roundToNearestSunday(LocalDate.of(2020, 8, 7)), // Friday
                equalTo(LocalDate.of(2020, 8, 9)));
        assertThat(
                Workspace.roundToNearestSunday(LocalDate.of(2020, 8, 8)), // Saturday
                equalTo(LocalDate.of(2020, 8, 9)));
        assertThat(
                Workspace.roundToNearestSunday(LocalDate.of(2020, 8, 9)), // Sunday
                equalTo(LocalDate.of(2020, 8, 9)));
    }

    @Test
    public void testPlantingDays() {
        Crop amaranth = Crop.builder()
                .name("amaranth leaves")
                .type(CropType.Green)
                .days(60)
                .build();
        List<LocalDate> dates = Workspace.getPlantTimes(LocalDate.of(2020, 8, 1), amaranth);
        assertThat(dates.size(), equalTo(52));
        for (int i = 0; i < 52; i++) {
            assertThat(dates.get(i), equalTo(LocalDate.of(2020, 8, 1).plus(7 * i - 60, ChronoUnit.DAYS)));
        }
    }

    @Test
    public void testWeeksPerMonth() {
        for (int i = 0; i < 12; i++) {
            if (i % 3 == 0) {
                assertThat(Workspace.getWeeksInMonth(i), equalTo(5));
            } else {
                assertThat(Workspace.getWeeksInMonth(i), equalTo(4));
            }
        }

        assertThat(
                IntStream.range(0, 12).map(Workspace::getWeeksInMonth).sum(),
                equalTo(52)
        );
    }

    @Test
    public void testStartWeeks() {
        int startWeek = 0;
        for (int i = 0; i < 12; i++) {
            int actual = Workspace.getStartWeek(i);
            assertThat(Workspace.getStartWeek(i), equalTo(startWeek));
            startWeek += Workspace.getWeeksInMonth(i);
        }
    }

    @Test
    public void testSqftForMonth() {
        List<Integer> weeklyConcurrentUse = new ArrayList<>(Arrays.asList(
                10, 10, 10, 10, 10, // month 1
                5, 5, 5, 5,  // month 2
                5, 8, 5, 5,  // month 3
                30, 40, 28, 29, 51)); // month 4
        while (weeklyConcurrentUse.size() < 52) weeklyConcurrentUse.add(10); // fill up remaining weeks

        assertThat(Workspace.getSqftForMonth(weeklyConcurrentUse, 0), equalTo(10));
        assertThat(Workspace.getSqftForMonth(weeklyConcurrentUse, 1), equalTo(5));
        assertThat(Workspace.getSqftForMonth(weeklyConcurrentUse, 2), equalTo(8));
        assertThat(Workspace.getSqftForMonth(weeklyConcurrentUse, 3), equalTo(51));
        for (int i = 4; i < 12; i++) {
            assertThat(Workspace.getSqftForMonth(weeklyConcurrentUse, i), equalTo(10));
        }
    }

    @Test
    public void testWeeklyConcurrentUse() {
        Crop carrot = Crop.builder()
                .days(21) // 3 weeks
                .name("carrot")
                .type(CropType.Starch)
                .build();
        Crop squash = Crop.builder()
                .days(5) // 1 week
                .name("squash")
                .type(CropType.Starch)
                .build();
        Crop zucchini = Crop.builder()
                .days(10) // 2 weeks
                .name("zucchini")
                .type(CropType.Colorful)
                .build();
        Crop spinach = Crop.builder()
                .days(8) // 2 weeks
                .name("spinach")
                .type(CropType.Green)
                .build();

        List<Integer> greenWeeklyPlantings = IntStream.range(0, 52)
                .map(c -> c < 11 ? 10 : 15) // 10 10 10 10 ... 15 15 15 ...
                .boxed()
                .collect(Collectors.toList());
        List<Integer> starchWeeklyPlantings = IntStream.range(0, 52)
                .map(c -> c < 11 ? 5: 11) // 5 5 5 5 ... 11 11 11 11 ...
                .boxed()
                .collect(Collectors.toList());
        List<Integer> colorWeeklyPlantings = IntStream.range(0, 52)
                .map(c -> c < 11 ? 8 : 12) // 8 8 8 8 ... 12 12 12 12 ...
                .boxed()
                .collect(Collectors.toList());

        List<Integer> weeklyConcurrentUse = Workspace.getWeeklyConcurrentUse(
                Arrays.asList(carrot, squash, zucchini, spinach),
                greenWeeklyPlantings, colorWeeklyPlantings, starchWeeklyPlantings);

        assertThat(weeklyConcurrentUse.size(), equalTo(52 + 3));
        assertThat(
                weeklyConcurrentUse.subList(0, 5),
                equalTo(Arrays.asList(
                        5, // plant carrot (starch)
                        (5) + 5+8+10, // plant carrot, zucchini, and spinach
                        (5)+(5+8+10) + 5+8+5+10, // plant all 4
                        (5)+(5+8+10) + 5+8+5+10, // plant all 4 and harvest all 4
                        (5)+(5+8+10) + 5+8+5+10))); // plant all 4 and harvest all 4

        assertThat(
                weeklyConcurrentUse.get(11 + 3), // 11th week (+ 3 weeks of pre-season growth)
                equalTo(11 + 11+12+15 + 11+12+11+15));
        assertThat(
                weeklyConcurrentUse.subList(8, 16),
                equalTo(Arrays.asList(
                        56, // baseline (harvest 5)
                        56, // baseline (harvest 6)
                        56, // baseline (harvest 7)
                        56 + 6, // baseline + extra carrot (harvest 8)
                        56 + 6 + 6+4+5, // baseline + extra carrot + (carrot, zucchini, spinach) (harvest 9)
                        56 + 6 + 6+4+5 + 6+4+6+5, // plant all 4 (harvest 10)
                        56 + 6 + 6+4+5 + 6+4+6+5, // plant and harvest all (harvest 11)
                        56 + 6 + 6+4+5 + 6+4+6+5))); //  plant and harvest all (harvest 12)

        assertThat(
                weeklyConcurrentUse.subList(weeklyConcurrentUse.size() - 6, weeklyConcurrentUse.size() - 1),
                equalTo(Arrays.asList(
                        11 + 11+12+15 + 11+12+11+15,
                        11 + 11+12+15 + 11+12+11+15,
                        11 + 11+12+15 + 11+12+11+15,
                        11+12+15 + 11+12+11+15,
                        11+12+11+15)));
    }

    @Test
    public void testWeeklyConcurrentUseForCrop() {
        Crop carrot = Crop.builder()
                .days(21)
                .name("carrot")
                .type(CropType.Starch)
                .build();
        Crop squash = Crop.builder()
                .days(18)
                .name("squash")
                .type(CropType.Starch)
                .build();
        Crop zucchini = Crop.builder()
                .days(22)
                .name("zucchini")
                .type(CropType.Colorful)
                .build();

        List<Integer> weeklyPlantings = new ArrayList<>();
        while (weeklyPlantings.size() < 52) weeklyPlantings.add(10); // fill up remaining weeks

        List<Integer> carrotWeeklyConcurrentSqft = Workspace.getWeeklyConcurrentUseForCrop(carrot, weeklyPlantings);
        assertThat(
                carrotWeeklyConcurrentSqft.subList(0, 5),
                equalTo(new ArrayList<>(Arrays.asList(
                        10, // plant
                        20, // plant
                        30, // plant
                        30, // harvest & plant
                        30)))); // harvest & plant
        assertThat(
                carrotWeeklyConcurrentSqft.subList(carrotWeeklyConcurrentSqft.size() - 6, carrotWeeklyConcurrentSqft.size() - 1),
                equalTo(new ArrayList<>(Arrays.asList(30, 30, 30, 20, 10))));
        // squash and carrot both have 3 week grow times, so they should be the same
        assertThat(
                Workspace.getWeeklyConcurrentUseForCrop(squash, weeklyPlantings),
                equalTo(carrotWeeklyConcurrentSqft));

        List<Integer> zucchiniWeeklyConcurrentSqft = Workspace.getWeeklyConcurrentUseForCrop(zucchini, weeklyPlantings);
        assertThat(
                zucchiniWeeklyConcurrentSqft.subList(0, 6),
                equalTo(new ArrayList<>(Arrays.asList(
                        10, // plant
                        20, // plant
                        30, // plant
                        40, // plant
                        40, // harvest & plant
                        40)))); // harvest & plant
        assertThat(
                zucchiniWeeklyConcurrentSqft.subList(zucchiniWeeklyConcurrentSqft.size() - 7, zucchiniWeeklyConcurrentSqft.size() - 1),
                equalTo(new ArrayList<>(Arrays.asList(40, 40, 40, 30, 20, 10))));
        assertThat(zucchiniWeeklyConcurrentSqft.size(), equalTo(52 + 4));
    }

    @Test
    public void testWeeklyConcurrentUseForCropComplex() {
        Crop carrot = Crop.builder()
                .days(21)
                .name("carrot")
                .type(CropType.Starch)
                .build();
        Crop squash = Crop.builder()
                .days(18)
                .name("squash")
                .type(CropType.Starch)
                .build();
        Crop zucchini = Crop.builder()
                .days(22)
                .name("zucchini")
                .type(CropType.Colorful)
                .build();

        List<Integer> weeklyPlantings = IntStream.range(5, 57)
                .boxed()
                .collect(Collectors.toList());

        List<Integer> carrotWeeklyConcurrentSqft = Workspace.getWeeklyConcurrentUseForCrop(carrot, weeklyPlantings);
        assertThat(
                carrotWeeklyConcurrentSqft.subList(0, 5),
                equalTo(new ArrayList<>(Arrays.asList(
                        5, // plant
                        5+6, // plant
                        5+6+7, // plant
                        6+7+8, // harvest & plant
                        8+7+9)))); // harvest & plant
        assertThat(
                carrotWeeklyConcurrentSqft.subList(carrotWeeklyConcurrentSqft.size() - 6, carrotWeeklyConcurrentSqft.size() - 1),
                equalTo(new ArrayList<>(Arrays.asList(
                        52+53+54, // harvest & plant
                        53+54+55, // harvest & plant
                        54+55+56, // harvest & plant
                        55+56, // harvest
                        56)))); // harvest
        // squash and carrot both have 3 week grow times, so they should be the same
        assertThat(
                Workspace.getWeeklyConcurrentUseForCrop(squash, weeklyPlantings),
                equalTo(carrotWeeklyConcurrentSqft));

        List<Integer> zucchiniWeeklyConcurrentSqft = Workspace.getWeeklyConcurrentUseForCrop(zucchini, weeklyPlantings);
        assertThat(
                zucchiniWeeklyConcurrentSqft.subList(0, 6),
                equalTo(new ArrayList<>(Arrays.asList(
                        5, // plant
                        5+6, // plant
                        5+6+7, // plant
                        5+6+7+8, // plant
                        6+7+8+9, // harvest & plant
                        7+8+9+10)))); // harvest & plant
        assertThat(
                zucchiniWeeklyConcurrentSqft.subList(zucchiniWeeklyConcurrentSqft.size() - 7, zucchiniWeeklyConcurrentSqft.size() - 1),
                equalTo(new ArrayList<>(Arrays.asList(
                        51+52+53+54, // harvest & plant
                        52+53+54+55, // harvest & plant
                        53+54+55+56, // harvest & plant
                        54+55+56, // harvest
                        55+56, // harvest
                        56)))); // harvest
        assertThat(zucchiniWeeklyConcurrentSqft.size(), equalTo(52 + 4));
    }
}
