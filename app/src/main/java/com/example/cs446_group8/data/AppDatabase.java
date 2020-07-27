package com.example.cs446_group8.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Crop.class}, exportSchema = true, version = 1)
@TypeConverters(CropType.Converter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CropDao cropDao();
}
