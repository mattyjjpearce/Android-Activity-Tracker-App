package com.example.activitytracker.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = Activity.TABLE_NAME) //creating our entity for our database
public class Activity { //an activity object

    public static final String COLUMN_NAME = "title";

    public static final String TABLE_NAME = "activity_table"; //naming this table for the database

    //initializing variables
    @PrimaryKey(autoGenerate = true) //setting it as auto-generate so every entry is unique
    public int id;
    @NonNull   @ColumnInfo(name = COLUMN_NAME)

    public final String title;
    public final String comment;
    public final long distance;
    public final double speed;
    public final double topSpeed;
    public final String activityType;
    public final String rating;
    public final String date;
    public final long displayTime;



    //Method to instantiate a new activity object
    public Activity(int id, @NonNull String title, @NonNull String comment, long distance, double speed, double topSpeed, String activityType, String rating, String date, long displayTime) {
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.distance = distance;
        this.speed = speed;
        this.topSpeed = topSpeed;
        this.activityType = activityType;
        this.rating = rating;
        this.date = date;
        this.displayTime = displayTime;
    }

    //methods to return information of the activity
    public int getId(){
        return id;
    }
    public String getTitle() { return title; }
    public String getComment() {
        return comment;
    }
    public long getDistance() {
        return distance;
    }
    public double getSpeed() {
        return speed;
    }
    public double getTopSpeed() {
        return topSpeed;
    }
    public String getActivityType() {
        return activityType;
    }
    public String getRating() {
        return rating;
    }
    public String getDate() {
        return date;
    }
    public long getTime() {
        return displayTime;
    }



}
