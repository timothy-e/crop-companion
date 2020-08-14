package com.crop.companion.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CropDao {
    @Query("SELECT * FROM crops")
    List<Crop> loadAll();

    @Insert
    void insertAll(Crop... crops);
}
