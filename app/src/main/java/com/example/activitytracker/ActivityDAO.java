package com.example.activitytracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ActivityDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert (Activity activity);

    @Query("UPDATE activity_table SET comment =:comment WHERE id=:id") //Updates the comment
    void updateComment(String comment, Integer id);

    @Query("DELETE  FROM activity_table")
    void deleteAllActivities();

    @Query("DELETE  FROM activity_table WHERE id = :id")
    void deleteActivity(final int id);


    //Wrapped in  LiveData objects in order for it to become life-cycle aware
    @Query("SELECT * FROM activity_table")
    LiveData<List<Activity>> getAllActivities();

    @Query("SELECT * FROM activity_table WHERE id = :id ")
    LiveData<List<Activity>> getActivityFromId(final int id);

    @Query("SELECT * FROM activity_table ORDER BY distance DESC")
    LiveData<List<Activity>> sortByDistanceDesc();

    @Query("SELECT * FROM activity_table ORDER BY distance ASC")
    LiveData<List<Activity>> sortByDistanceAsc();

    @Query("SELECT * FROM activity_table ORDER BY speed DESC")
    LiveData<List<Activity>> sortBySpeed();

    @Query("SELECT * FROM activity_table ORDER BY topSpeed DESC")
    LiveData<List<Activity>> sortByTopSpeed();

    @Query("SELECT * FROM activity_table ORDER BY displayTime DESC")
    LiveData<List<Activity>> sortByTime();

    @Query("SELECT * FROM activity_table ORDER BY id DESC")
    LiveData<List<Activity>> sortByDateRecent();

    @Query("SELECT * FROM activity_table ORDER BY id ASC")
    LiveData<List<Activity>> sortByDateOldest();

    @Query("SELECT * FROM activity_table WHERE activityType = :activityType ")
    LiveData<List<Activity>> getByActivityType(String activityType);


}
