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
public interface HeadCountsDao {
    @Query("SELECT * FROM head_counts WHERE project_id = :projectId")
    HeadCounts loadOneByProjectId(int projectId);

    @Update
    void update(HeadCounts headCounts);

    @Delete
    void delete(HeadCounts headCounts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HeadCounts headCounts);
}
