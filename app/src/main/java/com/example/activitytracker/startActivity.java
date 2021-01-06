package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

public class startActivity extends AppCompatActivity {

    double longitude;
    double latitude;
    private TextView textView;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){ //if receiver does not exist, create a new one
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) { //when the broadcast get's received this method is called and we can store our new results
                longitude = (double) intent.getExtras().get("longitude");
                latitude = (double) intent.getExtras().get("latitude");
                textView.setText(latitude+","+longitude);

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("newLocation")); //receive intent from service class titled newLocation
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver); //unregisterwhen the service is stopped
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        textView = findViewById(R.id.textView2);
    }


    public void startServie(View v){
        startLocationService();
    }
    public void stopServie(View v){
        stopLocationService();
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

    private void startLocationService(){
        if (!isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        }
    }
}