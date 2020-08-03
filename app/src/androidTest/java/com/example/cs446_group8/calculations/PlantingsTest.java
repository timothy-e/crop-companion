package com.example.cs446_group8.calculations;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.Crop;
import com.example.cs446_group8.data.CropDao;
import com.example.cs446_group8.data.CropSchedule;
import com.example.cs446_group8.data.CropType;
import com.example.cs446_group8.data.HeadCounts;
import com.example.cs446_group8.data.Project;
import com.example.cs446_group8.data.ProjectDao;
import com.example.cs446_group8.data.ProjectWithSows;
import com.example.cs446_group8.data.Sow;
import com.example.cs446_group8.data.SowDao;
import com.example.cs446_group8.data.SowWithCrop;

import org.junit.After;
import org.junit.Before;
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
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public final class PlantingsTest {
    private CropDao cropDao;
    private ProjectDao projectDao;
    private SowDao sowDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        cropDao = db.cropDao();
        projectDao = db.projectDao();
        sowDao = db.sowDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void simple() {
        LocalDate startDate1 = LocalDate.of(2020, 8, 1); // Saturday
        LocalDate startDate2 = LocalDate.of(2009, 7, 20); // Monday
        cropDao.insertAll(
                Crop.builder()
                        .id(1)
                        .name("Amaranth leaves")
                        .type(CropType.Green)
                        .days(15) // 3 weeks
                        .caloriesPer100Gram(100)
                        .yieldPer100Sqft(100 / 4.54)
                        .build(),
                Crop.builder()
                        .id(2)
                        .name("Arrow roots")
                        .days(17) // 3 weeks
                        .type(CropType.Colorful)
                        .caloriesPer100Gram(200)
                        .yieldPer100Sqft(100 / 4.54)
                        .build()
        );

        projectDao.insertAll(Project.builder()
                .id(1)
                .name("Project 1")
                .beginningOfSession(startDate1)
                .headCounts(
                        new HeadCounts(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10)
                )
                .caloriesPerDayPerPerson(2500)
                .caloriesFromColorful(20)
                .caloriesFromGreen(20)
                .caloriesFromStarch(60)
                .build());
        projectDao.insertAll(Project.builder()
                .id(2)
                .name("Project 2")
                .beginningOfSession(startDate2)
                .headCounts(
                        new HeadCounts(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10)
                )
                .caloriesPerDayPerPerson(5000)
                .caloriesFromColorful(20)
                .caloriesFromGreen(20)
                .caloriesFromStarch(60)
                .build());


        sowDao.insertAll(
                Sow.builder().projectId(1).cropId(1).build(),
                Sow.builder().projectId(1).cropId(2).build(),
                Sow.builder().projectId(2).cropId(1).build()
        );

        Plantings plantings1 = new Plantings(db, 1);
        Plantings plantings2 = new Plantings(db, 2);

        List<CropSchedule> schedule1 = plantings1.getSchedule();
        List<CropSchedule> schedule2 = plantings2.getSchedule();

        assertThat(schedule1.size(), equalTo(2));
        assertThat(schedule2.size(), equalTo(1));

        CropSchedule amaranthSchedule1 = schedule1.stream()
                .filter(c -> c.getCrop().getId() == 1)
                .findAny()
                .orElse(null);
        CropSchedule arrowRootsSchedule1 = schedule1.stream()
                .filter(c -> c.getCrop().getId() == 2)
                .findAny()
                .orElse(null);
        CropSchedule amaranthSchedule2 = schedule2.get(0);

        assertThat(amaranthSchedule1, notNullValue());
        assertThat(arrowRootsSchedule1, notNullValue());
        assertThat(amaranthSchedule2, notNullValue());

        assertThat(amaranthSchedule1.firstPlanting, equalTo(LocalDate.of(2020, 7, 19)));
        assertThat(arrowRootsSchedule1.firstPlanting, equalTo(LocalDate.of(2020, 7, 12)));
        assertThat(amaranthSchedule2.firstPlanting, equalTo(LocalDate.of(2009, 7, 5)));

        assertThat(amaranthSchedule1.weeklyPlantingAmounts.size(), equalTo(52));
        assertThat(arrowRootsSchedule1.weeklyPlantingAmounts.size(), equalTo(52));
        assertThat(amaranthSchedule2.weeklyPlantingAmounts.size(), equalTo(52));

        assertThat(
                amaranthSchedule1.weeklyPlantingAmounts.subList(0, 6),
                equalTo(Arrays.asList(300, 300, 300, 300, 300, 375)));
        assertThat(
                amaranthSchedule2.weeklyPlantingAmounts.subList(0, 6),
                equalTo(Arrays.asList(600, 600, 600, 600, 600, 750)));
        assertThat(
                arrowRootsSchedule1.weeklyPlantingAmounts.subList(0, 6),
                equalTo(Arrays.asList(150, 150, 150, 150, 150, 200)));

        assertThat(
                plantings1.getMonthlySquareFeet().subList(0, 4),
                equalTo(Arrays.asList(1700, 1700, 1675, 1700)));
        assertThat(
                plantings2.getMonthlySquareFeet().subList(0, 4),
                equalTo(Arrays.asList(2250, 2250, 2250, 2250)));


        // If we update calorie ratios, we still get correct data
        Project project = projectDao.loadOneById(1);
        project.setCaloriesFromGreen(40);
        project.setCaloriesFromColorful(10);
        project.setCaloriesFromStarch(50);
        projectDao.update(project);

        schedule1 = plantings1.getSchedule();

        amaranthSchedule1 = schedule1.stream()
                .filter(c -> c.getCrop().getId() == 1)
                .findAny()
                .orElse(null);
        arrowRootsSchedule1 = schedule1.stream()
                .filter(c -> c.getCrop().getId() == 2)
                .findAny()
                .orElse(null);

        assertThat(amaranthSchedule1, notNullValue());
        assertThat(arrowRootsSchedule1, notNullValue());

        assertThat(
                amaranthSchedule1.weeklyPlantingAmounts.subList(0, 6),
                equalTo(Arrays.asList(600, 600, 600, 600, 600, 750)));
        assertThat(
                arrowRootsSchedule1.weeklyPlantingAmounts.subList(0, 6),
                equalTo(Arrays.asList(75, 75, 75, 75, 75, 100)));

        assertThat(
                plantings1.getMonthlySquareFeet().subList(0, 4),
                equalTo(Arrays.asList(2525, 2525, 2500, 2550)));

        // if we add more crops, we still get correct data
        cropDao.insertAll(
                Crop.builder()
                        .id(3)
                        .name("Kale")
                        .type(CropType.Green)
                        .days(2) // 1 week
                        .caloriesPer100Gram(100)
                        .yieldPer100Sqft(100 / 4.54)
                        .build());

        sowDao.insertAll(Sow.builder().cropId(3).projectId(1).build());

        schedule1 = plantings1.getSchedule();
        assertThat(schedule1.size(), equalTo(3));

        amaranthSchedule1 = schedule1.stream()
                .filter(c -> c.getCrop().getId() == 1)
                .findAny()
                .orElse(null);
        arrowRootsSchedule1 = schedule1.stream()
                .filter(c -> c.getCrop().getId() == 2)
                .findAny()
                .orElse(null);
        CropSchedule kaleSchedule1 = schedule1.stream()
                .filter(c -> c.getCrop().getId() == 3)
                .findAny()
                .orElse(null);

        assertThat(kaleSchedule1, notNullValue());
        assertThat(amaranthSchedule1, notNullValue());
        assertThat(arrowRootsSchedule1, notNullValue());
        assertThat(kaleSchedule1.getFirstPlanting(), equalTo(LocalDate.of(2020, 8, 2)));

        assertThat(
                kaleSchedule1.weeklyPlantingAmounts.subList(0, 6),
                equalTo(Arrays.asList(300, 300, 300, 300, 300, 375)));
        assertThat(
                amaranthSchedule1.weeklyPlantingAmounts.subList(0, 6),
                equalTo(Arrays.asList(300, 300, 300, 300, 300, 375)));
        assertThat(
                arrowRootsSchedule1.weeklyPlantingAmounts.subList(0, 6),
                equalTo(Arrays.asList(75, 75, 75, 75, 75, 100)));

        assertThat(
                plantings1.getMonthlySquareFeet().subList(0, 4),
                equalTo(Arrays.asList(1775, 1775, 1750, 1800)));
    }

}
