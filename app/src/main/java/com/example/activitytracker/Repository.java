package com.example.activitytracker;


import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.activitytracker.DAOs.ActivityDAO;

import java.util.List;

public class Repository { //manages the back end (database)

    private LiveData<List<Activity>> byDistanceLargest;
    private LiveData<List<Activity>> byDistanceSmallest;
    private LiveData<List<Activity>> bySpeed;
    private LiveData<List<Activity>> byTopSpeed;
    private LiveData<List<Activity>> byTime;
    private LiveData<List<Activity>> byDateRecent;
    private LiveData<List<Activity>> byDateOldest;

    private LiveData<List<Activity>> allActivities;


    ActivityDAO activityDAO;


        public Repository(Application application){
            Database db = Database.getDatabase(application); // making an instance of the database
            activityDAO = db.activityDAO(); //initializing the DAO

            allActivities = activityDAO.getAllActivities();

            //sorting and filtering methods
            byDistanceLargest = activityDAO.sortByDistanceDesc();
            byDistanceSmallest = activityDAO.sortByDistanceAsc();
            bySpeed = activityDAO.sortBySpeed();
            byTopSpeed = activityDAO.sortByTopSpeed();
            byTime = activityDAO.sortByTime();
            byDateRecent = activityDAO.sortByDateRecent();
            byDateOldest = activityDAO.sortByDateOldest();

        }

    public void insertActivity(final Activity activity) { //method to insert a new activity object into the database
        Database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                activityDAO.insert(activity);
            }
        });
    }

    public LiveData<List<Activity>> getAllActivities(){
            return allActivities;
    }

    public LiveData<List<Activity>> getActivityFromId(final int id) {
        return activityDAO.getActivityFromId(id);
    }

    public LiveData<List<Activity>> getByActivityType(final String activityType) {
        return activityDAO.getByActivityType(activityType);
    }

    //Sorting/Filtering methods
    public LiveData<List<Activity>> getByDistanceLargest() { return byDistanceLargest; }
    public LiveData<List<Activity>> getByDistanceSmallest() { return byDistanceSmallest; }
    public LiveData<List<Activity>> getBySpeed() { return bySpeed; }
    public LiveData<List<Activity>> getByTopSpeed() { return byTopSpeed; }
    public LiveData<List<Activity>> sortByTime() { return byTime; }
    public LiveData<List<Activity>> sortByDateRecent() { return byDateRecent; }
    public LiveData<List<Activity>> sortByDateOldest() { return byDateOldest; }

    //Delete selected activity
    public void deleteActivityWithId(final int id) {
        Database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                activityDAO.deleteActivity(id);
            }
        });
    }

    //method to update the comment
    public void updateComment(final String comment, final int id) { //pass the new comment and id of the activity
        Database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                activityDAO.updateComment(comment, id);
            }
        });
    }

    //method to delete all activities (used in MainActivity)
    public void deleteAll() {
        Database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                activityDAO.deleteAllActivities();
            }
        });
    }


}
