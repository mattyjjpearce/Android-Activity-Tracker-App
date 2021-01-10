package com.example.activitytracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDate;

public class FinishedActivity extends AppCompatActivity {

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

        totalDistance = (long) bundle.getFloat("totalDistance");
        averageSpeed = bundle.getFloat("averageSpeed");
        topSpeed = bundle.getFloat("topSpeed");
        elapsedSeconds = bundle.getLong("totalTime");

        averageSpeedView = (TextView)findViewById(R.id.avSpeed);
        topSpeedView = (TextView)findViewById(R.id.topSpeed);
        totalTimeView = (TextView)findViewById(R.id.totalTime);
        totalDistanceView = (TextView)findViewById(R.id.totalDistance);



        averageSpeedView.setText("Average Speed: " + averageSpeed+" Km/h");
        topSpeedView.setText("Top Speed: " + topSpeed+" Km/h");

        secondsDisplay = elapsedSeconds % 60;
        elapsedMinutes = elapsedSeconds / 60;

        totalTimeView.setText("Total time: " + elapsedMinutes+":"+secondsDisplay);

        if(totalDistance<999){ //if distance is less than a totalDistanceInKM, show in m
            totalDistanceView.setText("Total Distance: " + totalDistance+"m");

        }else {
            totalDistanceInKM = totalDistance / 1000;
            totalDistanceView.setText("Total Distance: " + String.format("%.2f", totalDistanceInKM) + " totalDistanceInKM");
        }




        db = Database.getDatabase(getApplicationContext());
        activityDAO = db.activityDAO();

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ActivityViewModel.class);

        //receiving today's date to input
        LocalDate x = LocalDate.now();
        date = x.toString();
    }

    public void onClickInsert(View v){

        if(activityType == null){ //checking to see if the user has selected an activity type
            activityDialog(); //if not show a dialog to notify the user
            return;
        }
        if(rating == null){ //checking to see if the user has selected a rating
            ratingDialog();
            return;
        }


        titleView = (EditText) findViewById(R.id.titleView);
        commentView = (EditText) findViewById(R.id.editTextTextMultiLine);

        title = titleView.getText().toString();
        comment = commentView.getText().toString();


        Activity a = new Activity(0,title,comment, totalDistance,averageSpeed,topSpeed,activityType,rating, date, elapsedSeconds);
        viewModel.insertActivity(a);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void cancel(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void setActivityRun(View v){
        activityType = Constants.ACTIVITY_TYPE_RUN;
    }
    public void setActivityWalk(View v){
        activityType = Constants.ACTIVITY_TYPE_WALK;

    }
    public void setActivityJog(View v){
        activityType = Constants.ACTIVITY_TYPE_JOG;
    }
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