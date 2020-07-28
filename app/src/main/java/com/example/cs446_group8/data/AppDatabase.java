package com.example.cs446_group8.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.WeakHashMap;

@Database(entities = {Crop.class, Project.class, CropPlan.class}, exportSchema = true, version = 1)
@TypeConverters(CropType.Converter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CropDao cropDao();

    public abstract ProjectDao projectDao();

    public abstract CropPlanDao cropPlanDao();

    /**
     * A weak map is used to keep track of the app database instances.
     */
    private static final WeakHashMap<Context, AppDatabase> DATABASE_INSTANCES = new WeakHashMap<>();

    /**
     * Creates or opens a pre-populated app database.
     *
     * @param context the current application context
     * @return opened database
     */
    public synchronized static AppDatabase getInstance(Context context) {
        AppDatabase instance = DATABASE_INSTANCES.get(context);
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "app_database.db")
                    .createFromAsset("app_database.db")
                    .build();
            DATABASE_INSTANCES.put(context, instance);
        }
        return instance;
    }
}
