package com.example.activitytracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.activitytracker.Entities.Activity;
import com.example.activitytracker.Repository;

import java.util.List;

//Responsible for getting the room database singleton and pulls all the activity objects / updates and queries them at run time
//provides an extra layer of abstraction

public class ActivityViewModel extends AndroidViewModel {

    private Repository repository;

    //Live data of all activities
    private final LiveData<List<Activity>> allActivities;

    public ActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application); //getting an instance of the repository and making it relevant to the context of this application

        allActivities = repository.getAllActivities();
    }

    //methods to insert,delete or query the database

    public LiveData<List<Activity>> getAllActivities() { return allActivities; }

    public void insertActivity(Activity activity) { repository.insertActivity(activity); }

    public LiveData<List<Activity>> getAcitvityFromId(Integer id) { return repository.getActivityFromId(id); }

    public LiveData<List<Activity>> byActivityType(String activityType) { return repository.getByActivityType(activityType); }


    public void updateComment(String comment, Integer id) { repository.updateComment(comment, id);}
    public void DeleteActivity(Integer id) { repository.deleteActivityWithId(id); }
    public void DeleteAllActivities() { repository.deleteAll(); }


    //All the sorting methods
    public LiveData<List<Activity>> getByDistanceLargest() {  LiveData<List<Activity>>  byDistanceLargest = repository.getByDistanceLargest(); return byDistanceLargest; }
    public LiveData<List<Activity>> getByDistanceSmallest() { LiveData<List<Activity>> byDistanceSmallest = repository.getByDistanceSmallest(); return byDistanceSmallest; }
    public LiveData<List<Activity>> getBySpeed() { LiveData<List<Activity>> bySpeed = repository.getBySpeed(); return bySpeed; }
    public LiveData<List<Activity>> getByTopSpeed() { LiveData<List<Activity>> byTopSpeed = repository.getByTopSpeed(); return byTopSpeed; }
    public LiveData<List<Activity>> getByTime() { LiveData<List<Activity>> byTime = repository.sortByTime(); return byTime; }
    public LiveData<List<Activity>> getByDateRecent() { LiveData<List<Activity>> byDateRecent = repository.sortByDateRecent(); return byDateRecent; }
    public LiveData<List<Activity>> getByMostRecent() { LiveData<List<Activity>> byDateOldest = repository.sortByDateOldest(); return byDateOldest; }








}
