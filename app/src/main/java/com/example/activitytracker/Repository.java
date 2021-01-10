package com.example.activitytracker;


import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import java.util.List;

public class Repository {
        ActivityDAO activityDAO;


    private LiveData<List<Activity>> byDistanceLargest;
    private LiveData<List<Activity>> byDistanceSmallest;
    private LiveData<List<Activity>> bySpeed;
    private LiveData<List<Activity>> byTopSpeed;
    private LiveData<List<Activity>> byTime;
    private LiveData<List<Activity>> byDateRecent;
    private LiveData<List<Activity>> byDateOldest;



    LiveData<List<Activity>> allActivities;

        Repository (Application application){
            Database db = Database.getDatabase(application);
            activityDAO = db.activityDAO();

            allActivities = activityDAO.getAllActivities();


            byDistanceLargest = activityDAO.sortByDistanceDesc();
            byDistanceSmallest = activityDAO.sortByDistanceAsc();
            bySpeed = activityDAO.sortBySpeed();
            byTopSpeed = activityDAO.sortByTopSpeed();
            byTime = activityDAO.sortByTime();
            byDateRecent = activityDAO.sortByDateRecent();
            byDateOldest = activityDAO.sortByDateOldest();

        }

    void insertActivity(final Activity activity) {

        Database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                activityDAO.insert(activity);
            }
        });
    }

    LiveData<List<Activity>> getAllActivities(){
            return allActivities;
    }

    LiveData<List<Activity>> getActivityFromId(final int id) {
        return activityDAO.getActivityFromId(id);
    }

    LiveData<List<Activity>> getByActivityType(final String activityType) {
        return activityDAO.getByActivityType(activityType);
    }

    LiveData<List<Activity>> getByDistanceLargest() { return byDistanceLargest; }
    LiveData<List<Activity>> getByDistanceSmallest() { return byDistanceSmallest; }
    LiveData<List<Activity>> getBySpeed() { return bySpeed; }
    LiveData<List<Activity>> getByTopSpeed() { return byTopSpeed; }
    LiveData<List<Activity>> sortByTime() { return byTime; }
    LiveData<List<Activity>> sortByDateRecent() { return byDateRecent; }
    LiveData<List<Activity>> sortByDateOldest() { return byDateOldest; }

    //Delete selected activity
    void deleteActivityWithId(final int id) {
        Database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                activityDAO.deleteActivity(id);
            }
        });
    }

    //method to update the comment
    void updateComment(final String comment, final int id) { //pass the new comment and id of the activity
        Database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                activityDAO.updateComment(comment, id);
            }
        });
    }

    //method to delete all activities (used in MainActivity)
    void deleteAll() {
        Database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                activityDAO.deleteAllActivities();
            }
        });
    }


}
