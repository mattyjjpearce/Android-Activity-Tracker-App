package com.example.activitytracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class startActivity extends AppCompatActivity {
    private TextView totalDistanceTextView;
    private TextView totalTimeTextView;
    private TextView kmhTextView;

    private TextView averageKmhTextView;

    float distanceFromLocations;
    float totalDistance = 0;
    private Boolean start;
    private Location currentLocation;
    private Location newLocation;

    long ms;
    long elapsedSeconds;
    long secondsDisplay;
    long elapsedMinutes;

    long startTime;
    long elapsedTimeTotal = 0;
    long elapsedCurrentTime = 0;

    float KmH;
    float averageKmH;
    float totalKmH = 0;
    float currentTopSpeed = 0;


    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){ //if receiver does not exist, create a new one
            broadcastReceiver = new BroadcastReceiver() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                @Override
                public void onReceive(Context context, Intent intent) { //when the broadcast get's received this method is called and we can store our new results
                    newLocation = (Location) Objects.requireNonNull(intent.getExtras()).get("location");


                    elapsedCurrentTime = System.currentTimeMillis() - startTime; //getting the time since the start or resume button was pressed

                    ms = elapsedTimeTotal + elapsedCurrentTime; //getting total time elapsed in milliseconds

                    elapsedSeconds = ms / 1000; //converting to seconds
                    secondsDisplay = elapsedSeconds % 60; //converting to be displayed appropriately second wise
                    elapsedMinutes = elapsedSeconds / 60;


                    if(currentLocation == null){
                    currentLocation = newLocation;
                }else
                {
                    distanceFromLocations = currentLocation.distanceTo(newLocation); //get distance from from last location
                    currentLocation = newLocation; //change value to the most recent location
                    totalDistance += distanceFromLocations; //add the change in distance to total distance

                }
                    //Working out the current KmH
                    KmH = totalDistance / elapsedSeconds;
                    KmH = (float) (Math.round(KmH * 100.0) / 100.0); //Setting them two 2 decimal points for simplicity reasons
                    //Average KmH
                    totalKmH += Math.round(KmH * 100.0) / 100.0;;
                    averageKmH = totalKmH / elapsedSeconds; //getting the average km/h
                    averageKmH = (float) (Math.round(averageKmH * 100.0) / 100.0); //rounding the value to two decimal places

                    //Working out top speed

                    if(currentTopSpeed < KmH){
                        currentTopSpeed = KmH;
                    }

                if(totalDistance > 1000){ //if distance is greater than 1000, display distance in KM rather thanm
                    float km = totalDistance / 1000;
                    totalDistanceTextView.setText("Total Distance: "+String.format("%.2f",km)+" km");
                }else {
                    totalDistanceTextView.setText("Total Distance: "+Math.round(totalDistance)+"m");

                }
                //setting the live text for the user to view during the activity
                totalTimeTextView.setText("Time: "+ elapsedMinutes +":" + secondsDisplay);
                kmhTextView.setText(String.format("%.2f",KmH) + "Km/H");
                averageKmhTextView.setText("Average: "+String.format("%.2f",averageKmH) + "Km/H");

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("newLocation")); //receive intent from service class titled newLocation
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver); //unregister when the service is stopped
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //finding the views and initializing them into our variables
        totalTimeTextView = findViewById(R.id.totalTime);
        totalDistanceTextView = findViewById(R.id.totalDistance);
        kmhTextView = findViewById(R.id.kmh);
        averageKmhTextView = findViewById(R.id.kmh2);


        start = true;

    }



    public void startService(View v){
        Button b = (Button) v;
        if(start){
            startTime = System.currentTimeMillis();
            startLocationService();
            b.setText("Pause");
            start = false;
        }
        else {
            elapsedTimeTotal += elapsedCurrentTime;
            stopLocationService();
            b.setText("Resume");
            start = true;
        }


    }
    public void stopServie(View v){

        Intent intent = new Intent(startActivity.this, FinishedActivity.class);
        intent.putExtra("totalDistance", totalDistance);
        intent.putExtra("averageSpeed", averageKmH);
        intent.putExtra("topSpeed", currentTopSpeed);
        intent.putExtra("totalTime", elapsedSeconds);

        stopLocationService();

        startActivity(intent); //start new activity and pass the intent with all the values
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    //Method to start the location service
    private void startLocationService(){
        if (!isLocationServiceRunning()){ //only start if the location is not running
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE); //pass the start constant as the action - referenced in constants class
            startService(intent);
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        }
    }

    //Method to stop the location service
    private void stopLocationService() {
        if (isLocationServiceRunning()) { //only stop if the locationService is running
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE); //pass the stop constant as the action
            startService(intent);
            Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        }
    }

}