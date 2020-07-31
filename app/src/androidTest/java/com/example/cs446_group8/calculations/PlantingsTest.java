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
        LocalDate startDate1 = LocalDate.of(2020, 8, 1);
        LocalDate startDate2 = LocalDate.of(2009, 7, 20);
        cropDao.insertAll(
                Crop.builder()
                        .id(1)
                        .name("Amaranth leaves")
                        .type(CropType.Green)
                        .days(13) // 2 weeks
                        .caloriesPer100Gram(100)
                        .yieldPer100Sqft(100 / 4.54)
                        .build(),
                Crop.builder()
                        .id(2)
                        .name("Arrow roots")
                        .days(15) // 3 weeks
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
                Sow.builder().projectId(2).cropId(2).build()
        );

        Plantings plantings1 = new Plantings(ApplicationProvider.getApplicationContext(), 1);
        Plantings plantings2 = new Plantings(ApplicationProvider.getApplicationContext(), 2);

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

        assertThat(amaranthSchedule1.firstPlanting, equalTo(startDate1.minus(2, ChronoUnit.WEEKS)));
        assertThat(arrowRootsSchedule1.firstPlanting, equalTo(startDate1.minus(3, ChronoUnit.WEEKS)));
        assertThat(amaranthSchedule2.firstPlanting, equalTo(startDate2.minus(2, ChronoUnit.WEEKS)));

        assertThat(amaranthSchedule1.weeklyPlantingAmounts.size(), equalTo(52 + 2));
        assertThat(arrowRootsSchedule1.weeklyPlantingAmounts.size(), equalTo(52 + 3));
        assertThat(amaranthSchedule2.weeklyPlantingAmounts.size(), equalTo(52 + 2));

        assertThat(
                amaranthSchedule1.weeklyPlantingAmounts.subList(0, 5),
                equalTo(Arrays.asList(1500, 3000, 3000, 3000, 3000)));
        assertThat(
                amaranthSchedule2.weeklyPlantingAmounts.subList(0, 5),
                equalTo(Arrays.asList(3000, 6000, 6000, 6000, 6000)));
        assertThat(
                arrowRootsSchedule1.weeklyPlantingAmounts.subList(0, 5),
                equalTo(Arrays.asList(750, 1500, 1500, 1500, 1500)));

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

        assertThat(
                amaranthSchedule1.weeklyPlantingAmounts.subList(0, 5),
                equalTo(Arrays.asList(3000, 6000, 6000, 6000, 6000)));
        assertThat(
                arrowRootsSchedule1.weeklyPlantingAmounts.subList(0, 5),
                equalTo(Arrays.asList(375, 150, 150, 150, 150)));


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
        assertThat(kaleSchedule1.getFirstPlanting(), equalTo(startDate1.minus(1, ChronoUnit.WEEKS)));

        assertThat(
                kaleSchedule1.weeklyPlantingAmounts.subList(0, 5),
                equalTo(Arrays.asList(1500, 3000, 3000, 3000, 3000)));
        assertThat(
                amaranthSchedule1.weeklyPlantingAmounts.subList(0, 5),
                equalTo(Arrays.asList(1500, 3000, 3000, 3000, 3000)));
    }

}
