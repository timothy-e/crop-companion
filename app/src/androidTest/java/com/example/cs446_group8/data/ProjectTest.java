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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public final class ProjectTest {
    private CropDao cropDao;
    private ProjectDao projectDao;
    private CropPlanDao cropPlanDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        cropDao = db.cropDao();
        projectDao = db.projectDao();
        cropPlanDao = db.cropPlanDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void simple() {
        cropDao.insertAll(
                Crop.builder()
                        .id(1)
                        .name("Amaranth leaves")
                        .type(CropType.Green)
                        .caloriesPer100Gram(371)
                        .yieldPer100Sqft(40)
                        .build(),
                Crop.builder()
                        .id(2)
                        .name("Arrow roots")
                        .type(CropType.Colorful)
                        .caloriesPer100Gram(100)
                        .yieldPer100Sqft(27)
                        .build()
        );
        projectDao.insertAll(Project.builder()
                .id(1)
                .name("Project 1")
                .beginningOfSession(LocalDate.now())
                .build());
        projectDao.insertAll(Project.builder()
                .id(2)
                .name("Project 2")
                .beginningOfSession(LocalDate.now())
                .build());

        cropPlanDao.insertAll(
                CropPlan.builder().projectId(1).cropId(1).build(),
                CropPlan.builder().projectId(1).cropId(2).build()
        );

        assertThat(projectDao.loadAll().size(), equalTo(2));
        ProjectWithCropPlans projectWithCropPlans = projectDao.loadOneByIdWithCropPlans(1);
        assertThat(projectWithCropPlans.getProject().getId(), equalTo(1));
        assertThat(projectWithCropPlans.getCropPlansWithCrops().size(), equalTo(2));
    }

    @Test
    public void updateAndDelete() {
        projectDao.insert(Project.builder()
                .id(1)
                .name("Project 1")
                .beginningOfSession(LocalDate.now())
                .caloriesPerDayPerPerson(2500)
                .caloriesFromColorful(30)
                .caloriesFromGreen(20)
                .caloriesFromStarch(50)
                .build()
        );
        Project project = projectDao.loadOneById(1);
        assertThat(project.getName(), equalTo("Project 1"));
        assertThat(project.getBeginningOfSession(), equalTo(LocalDate.now()));
        assertThat(project.getCaloriesPerDayPerPerson(), equalTo(2500));

        project.setBeginningOfSession(LocalDate.ofYearDay(2020, 42));
        project.setName("Project 2");
        project.setCaloriesPerDayPerPerson(2000);
        projectDao.update(project);

        project = projectDao.loadOneById(1);
        assertThat(project.getName(), equalTo("Project 2"));
        assertThat(project.getBeginningOfSession(), equalTo(LocalDate.ofYearDay(2020, 42)));
        assertThat(project.getCaloriesPerDayPerPerson(), equalTo(2000));

        projectDao.delete(project);
        assertThat(projectDao.loadOneById(1), equalTo(null));
    }
}