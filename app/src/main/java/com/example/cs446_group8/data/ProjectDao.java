package com.example.cs446_group8.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ProjectDao {
    @Query("SELECT * from projects")
    List<Project> loadAll();

    @Transaction
    @Query("SELECT * from projects WHERE id = :id")
    ProjectWithCropPlans loadOneByIdWithCropPlans(int id);

    @Insert
    void insertAll(Project... projects);
}
