package com.example.cs446_group8.data;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface CropPlanDao {
    @Insert
    void insertAll(CropPlan... cropPlans);
}
