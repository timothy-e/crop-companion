package com.crop.companion.data;

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
                .headCounts(HeadCounts.builder()
                        .january(10)
                        .february(11)
                        .march(12)
                        .april(13)
                        .may(14)
                        .june(15)
                        .july(16)
                        .august(17)
                        .september(18)
                        .october(19)
                        .november(20)
                        .december(21)
                        .build())
                .build());
        projectDao.insertAll(Project.builder()
                .id(2)
                .name("Project 2")
                .beginningOfSession(LocalDate.now())
                .headCounts(HeadCounts.empty())
                .build());

        sowDao.insertAll(
                Sow.builder().projectId(1).cropId(1).build(),
                Sow.builder().projectId(1).cropId(2).build()
        );

        assertThat(projectDao.loadAll().size(), equalTo(2));
        ProjectWithSows projectWithSows = projectDao.loadOneByIdWithSows(1);
        assertThat(projectWithSows.getProject().getId(), equalTo(1L));
        assertThat(projectWithSows.getProject().getHeadCounts().getJanuary(), equalTo(10));
        assertThat(projectWithSows.getSows().size(), equalTo(2));
    }

    @Test
    public void updateAndDelete() {
        projectDao.insert(Project.builder()
                .id(1)
                .name("Project 1")
                .beginningOfSession(LocalDate.now())
                .headCounts(HeadCounts.builder()
                        .january(10)
                        .february(11)
                        .march(12)
                        .april(13)
                        .may(14)
                        .june(15)
                        .july(16)
                        .august(17)
                        .september(18)
                        .october(19)
                        .november(20)
                        .december(21)
                        .build())
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
        assertThat(project.getHeadCounts().getJanuary(), equalTo(10));

        project.setBeginningOfSession(LocalDate.ofYearDay(2020, 42));
        project.setName("Project 2");
        project.getHeadCounts().setJanuary(12);
        project.setCaloriesPerDayPerPerson(2000);
        projectDao.update(project);

        project = projectDao.loadOneById(1);
        assertThat(project.getName(), equalTo("Project 2"));
        assertThat(project.getBeginningOfSession(), equalTo(LocalDate.ofYearDay(2020, 42)));
        assertThat(project.getCaloriesPerDayPerPerson(), equalTo(2000));
        assertThat(project.getHeadCounts().getJanuary(), equalTo(12));

        projectDao.delete(project);
        assertThat(projectDao.loadOneById(1), equalTo(null));
    }
}