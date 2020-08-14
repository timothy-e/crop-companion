package com.crop.companion.data;

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
    Project loadOneById(long id);

    /**
     * A {@link Project} inner joined with {@link Sow} inner joined with {@link Crop}, returned as
     * one query.
     * @param id the id of the project to load
     * @return the project, its associated sows and crops.
     */
    @Transaction
    @Query("SELECT * FROM projects WHERE id = :id")
    ProjectWithSows loadOneByIdWithSows(long id);

    @Update
    void update(Project project);

    @Delete
    void delete(Project project);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Project project);

    @Insert
    void insertAll(Project... projects);
}
