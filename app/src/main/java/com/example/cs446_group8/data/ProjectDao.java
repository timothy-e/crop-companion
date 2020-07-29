package com.example.cs446_group8.data;

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

    /**
     * A {@link Project} inner joined with {@link Sow} inner joined with {@link Crop}, returned as
     * one query.
     * @param id the id of the project to load
     * @return the project, its associated sows and crops.
     */
    @Transaction
    @Query("SELECT * FROM projects WHERE id = :id")
    ProjectWithSow loadOneByIdWithSows(int id);

    @Update
    void update(Project project);

    @Delete
    void delete(Project project);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Project project);

    @Insert
    void insertAll(Project... projects);
}
