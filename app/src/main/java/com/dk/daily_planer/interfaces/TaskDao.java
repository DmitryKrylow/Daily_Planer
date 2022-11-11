package com.dk.daily_planer.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dk.daily_planer.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM Task")
    List<Task> getAll();
    @Query("SELECT * FROM Task WHERE date = :date ORDER BY id DESC")
    List<Task> getAllByDate(String date);
    @Insert
    void insert(Task task);
    @Delete
    void delete(Task task);
    @Update
    void update(Task task);
}
