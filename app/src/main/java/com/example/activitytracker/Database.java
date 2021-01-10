package com.example.activitytracker;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.activitytracker.DAOs.ActivityDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = Activity.class, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract ActivityDAO activityDAO();

    private static volatile Database INSTANCE;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static Database getDatabase(final Context context) {

        if (INSTANCE == null) {
            //to avoid race conditions
            synchronized (Database.class) {
                //build the actual database
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        Database.class, "activity_table")
                        .fallbackToDestructiveMigration()
                        .build();

            }
        }

        return INSTANCE;
    }

}
