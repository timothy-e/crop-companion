package com.example.cs446_group8.data;

import androidx.annotation.TransitionRes;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProjectDao {
    @Query("SELECT * FROM projects")
    List<Project> loadAll();

    @Query("SELECT * FROM projects WHERE id = :id")
    Project loadOneById(int id);

    @Transaction
    @Query("SELECT * FROM projects WHERE id = :id")
    ProjectWithCropPlans loadOneByIdWithCropPlans(int id);

    @Update
    void update(Project project);

    @Delete
    void delete(Project project);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Project project);

    @Insert
    void insertAll(Project... projects);
}
