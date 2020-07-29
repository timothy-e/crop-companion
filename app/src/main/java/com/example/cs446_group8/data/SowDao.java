package com.example.cs446_group8.data;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface SowDao {
    @Insert
    void insertAll(Sow... sows);
}
