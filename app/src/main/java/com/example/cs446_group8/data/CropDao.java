package com.example.cs446_group8.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CropDao {
    @Query("SELECT * from crops")
    List<Crop> loadAll();

    @Insert
    void insertAll(Crop... crops);
}
