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

import java.util.List;

@RunWith(AndroidJUnit4.class)
public final class CropTest {
    private CropDao cropDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        cropDao = db.cropDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAllAndLoadAll() {
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

        List<Crop> allCrops = cropDao.loadAll();
        assertThat(allCrops.size(), equalTo(2));

        Crop first = allCrops.get(0);
        assertThat(first.getId(), equalTo(1));
        assertThat(first.getName(), equalTo("Amaranth leaves"));
        assertThat(first.getType(), equalTo(CropType.Green));
        assertThat(first.getCaloriesPer100Gram(), equalTo(371d));

        Crop second = allCrops.get(1);
        assertThat(second.getId(), equalTo(2));
        assertThat(second.getName(), equalTo("Arrow roots"));
        assertThat(second.getType(), equalTo(CropType.Colorful));
        assertThat(second.getCaloriesPer100Gram(), equalTo(100d));
    }
}
