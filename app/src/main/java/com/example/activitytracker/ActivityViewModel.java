package com.example.activitytracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ActivityViewModel extends AndroidViewModel {

    private Repository repository;

    //Live data of all activities
    private final LiveData<List<Activity>> allActivities;



    public ActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allActivities = repository.getAllActivities();

        //All the sorting methods


    }

    LiveData<List<Activity>> getAllActivities() { return allActivities; }

    public void insertActivity(Activity activity) { repository.insertActivity(activity); }

    LiveData<List<Activity>> getAcitvityFromId(Integer id) { return repository.getActivityFromId(id); }

    LiveData<List<Activity>> byActivityType(String activityType) { return repository.getByActivityType(activityType); }


    public void updateComment(String comment, Integer id) { repository.updateComment(comment, id);}
    public void DeleteActivity(Integer id) { repository.deleteActivityWithId(id); }
    public void DeleteAllActivities() { repository.deleteAll(); }


    //All the sorting methods
    LiveData<List<Activity>> getByDistanceLargest() {  LiveData<List<Activity>>  byDistanceLargest = repository.getByDistanceLargest(); return byDistanceLargest; }
    LiveData<List<Activity>> getByDistanceSmallest() { LiveData<List<Activity>> byDistanceSmallest = repository.getByDistanceSmallest(); return byDistanceSmallest; }
    LiveData<List<Activity>> getBySpeed() { LiveData<List<Activity>> bySpeed = repository.getBySpeed(); return bySpeed; }
    LiveData<List<Activity>> getByTopSpeed() { LiveData<List<Activity>> byTopSpeed = repository.getByTopSpeed(); return byTopSpeed; }
    LiveData<List<Activity>> getByTime() { LiveData<List<Activity>> byTime = repository.sortByTime(); return byTime; }
    LiveData<List<Activity>> getByDateRecent() { LiveData<List<Activity>> byDateRecent = repository.sortByDateRecent(); return byDateRecent; }
    LiveData<List<Activity>> getByMostRecent() { LiveData<List<Activity>> byDateOldest = repository.sortByDateOldest(); return byDateOldest; }








}
