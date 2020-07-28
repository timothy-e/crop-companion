package com.example.cs446_group8.data;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
        projectDao.insertAll(Project.builder().id(1).name("Project 1").build());
        projectDao.insertAll(Project.builder().id(2).name("Project 2").build());
        cropPlanDao.insertAll(
                CropPlan.builder().projectId(1).cropId(1).build(),
                CropPlan.builder().projectId(1).cropId(2).build()
        );

        assertThat(projectDao.loadAll().size(), equalTo(2));
        ProjectWithCropPlans projectWithCropPlans = projectDao.loadOneByIdWithCropPlans(1);
        assertThat(projectWithCropPlans.getProject().getId(), equalTo(1));
        assertThat(projectWithCropPlans.getCropPlans().size(), equalTo(2));
    }
}