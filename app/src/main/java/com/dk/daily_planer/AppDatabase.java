package com.dk.daily_planer;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dk.daily_planer.interfaces.TaskDao;
import com.dk.daily_planer.models.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
