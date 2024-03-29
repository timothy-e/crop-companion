package com.crop.companion.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.WeakHashMap;

@Database(entities = {Crop.class, Project.class, Sow.class}, exportSchema = true, version = 1)
@TypeConverters({CropType.Converter.class, TemporalConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract CropDao cropDao();

    public abstract ProjectDao projectDao();

    public abstract SowDao sowDao();

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
                    .allowMainThreadQueries()
                    .createFromAsset("app_database.db")
                    .build();
            DATABASE_INSTANCES.put(context, instance);
        }
        return instance;
    }
}
