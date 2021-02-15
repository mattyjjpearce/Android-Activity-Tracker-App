package com.example.activitytracker.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.activitytracker.Entities.Activity;
import com.example.activitytracker.ViewModels.ActivityViewModel;
import com.example.activitytracker.Constants.Constants;
import com.example.activitytracker.DAOs.ActivityDAO;
import com.example.activitytracker.Database;
import com.example.activitytracker.R;
import com.example.activitytracker.Dialogs.activityTypeDialog;
import com.example.activitytracker.Dialogs.ratingDialog;

import java.time.LocalDate;

public class FinishedActivity extends AppCompatActivity {
    //Activity display once the user has finished their current activity & Shown a menu where they can finish off the information of their activity

    private String title;
    private String comment;
    private long totalDistance;
    private float averageSpeed;
    private float topSpeed;
    private float totalDistanceInKM;

    private long elapsedSeconds;
    private String activityType;
    private String rating;

    long secondsDisplay;
    long elapsedMinutes;

    //Instantiating all the textViews to fill out from previous activity
    EditText titleView;
    EditText commentView;
    TextView averageSpeedView;
    TextView topSpeedView;
    TextView totalTimeView;
    TextView totalDistanceView;

    String date;

    Database db;
    ActivityDAO activityDAO;
    ActivityViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        //Get bundle from last activity to get values
        Bundle bundle  = getIntent().getExtras();

        //setting all the local variable to the one's passed on from the previous activity
        totalDistance = (long) bundle.getFloat("totalDistance");
        averageSpeed = bundle.getFloat("averageSpeed");
        topSpeed = bundle.getFloat("topSpeed");
        elapsedSeconds = bundle.getLong("totalTime");

        //finding the appropriate text views to later set values in
        averageSpeedView = (TextView)findViewById(R.id.avSpeed);
        topSpeedView = (TextView)findViewById(R.id.topSpeed);
        totalTimeView = (TextView)findViewById(R.id.totalTime);
        totalDistanceView = (TextView)findViewById(R.id.totalDistance);
        //Setting the Text of Speeds
        averageSpeedView.setText("Average Speed: " + averageSpeed+" Km/h");
        topSpeedView.setText("Top Speed: " + topSpeed+" Km/h");
        //Calculating the display time to ensure it is readable for the users
        secondsDisplay = elapsedSeconds % 60;
        elapsedMinutes = elapsedSeconds / 60;

        totalTimeView.setText("Total time: " + elapsedMinutes+":"+secondsDisplay);

        if(totalDistance<999){ //if distance is less than a totalDistanceInKM, show in m
            totalDistanceView.setText("Total Distance: " + totalDistance+"m");

        }else {
            totalDistanceInKM = totalDistance / 1000;
            totalDistanceView.setText("Total Distance: " + String.format("%.2f", totalDistanceInKM) + " totalDistanceInKM");
        }



        //get instance of the database
        db = Database.getDatabase(getApplicationContext());
        //reference the DAO
        activityDAO = db.activityDAO();
        //Getting an instance of the view model with this activities context
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ActivityViewModel.class);

        //receiving today's date to input
        LocalDate x = LocalDate.now();
        date = x.toString();
    }

    public void onClickInsert(View v){ //button when user has finished with this activity and has inputted the necessary info

        if(activityType == null){ //checking to see if the user has selected an activity type
            activityDialog(); //if not show a dialog to notify the user
            return;
        }
        if(rating == null){ //checking to see if the user has selected a rating
            ratingDialog();
            return;
        }

        //Find the edit texts
        titleView = (EditText) findViewById(R.id.titleView);
        commentView = (EditText) findViewById(R.id.editTextTextMultiLine);


        title = titleView.getText().toString(); //setting the variables equal to the users input
        comment = commentView.getText().toString();


        //Creating a new activity object to insert into the database, fill parameters with local variables
        Activity newActivity = new Activity(0,title,comment, totalDistance,averageSpeed,topSpeed,activityType,rating, date, elapsedSeconds);
        viewModel.insertActivity(newActivity);

        //Intent to go back to the main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void cancel(View view){ //Button method to exit the current activity ans cancel the actions, taking them back to main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Buttons which set the local variable activityType
    public void setActivityRun(View v){
        activityType = Constants.ACTIVITY_TYPE_RUN;
    }
    public void setActivityWalk(View v){ activityType = Constants.ACTIVITY_TYPE_WALK; }
    public void setActivityJog(View v){
        activityType = Constants.ACTIVITY_TYPE_JOG;
    }

    //Buttons which set the local variable rating
    public void activityRatingGood(View v){
        rating = Constants.ACTIVITY_RATING_GOOD;
    }
    public void activityRatingBad(View v){
        rating = Constants.ACTIVITY_RATING_BAD;
    }


    public void activityDialog(){ //activity dialog method called when user hasn't selected an ActivityType
        activityTypeDialog nullDialog = new activityTypeDialog();
        nullDialog.show(getSupportFragmentManager(),"Dialog");
    }
    public void ratingDialog(){ //activity dialog method called when user hasn't selected a rating
        ratingDialog nullDialog = new ratingDialog();
        nullDialog.show(getSupportFragmentManager(),"Dialog");
    }
}