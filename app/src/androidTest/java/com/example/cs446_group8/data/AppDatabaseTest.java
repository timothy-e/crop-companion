package com.example.cs446_group8.data;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {
    private CropDao cropDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.getInstance(context);
        cropDao = db.cropDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void checkPrepopulatedData() {
        AppDatabase.IO_EXECUTOR.execute(() -> {
            final List<Crop> cropsInDb = cropDao.loadAll();
            final List<Crop> knownCrops = new ArrayList<>(AppDatabase.CROPS);

            final Comparator<Crop> compareByPlantingOrder = Comparator.comparing(Crop::getPlantingOrder);
            Collections.sort(cropsInDb, compareByPlantingOrder);
            Collections.sort(knownCrops, compareByPlantingOrder);

            assertThat(cropsInDb, equalTo(knownCrops));
        });
    }
}
