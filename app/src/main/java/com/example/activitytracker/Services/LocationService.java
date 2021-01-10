package com.example.activitytracker.Services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.activitytracker.Constants.Constants;
import com.example.activitytracker.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {
    private LocationCallback locationCallback = new LocationCallback() {
        //Method to get current location
        @Override
        public void onLocationResult (LocationResult locationResult){
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) { //Only call method, if we receive a result from getLastLocation
                Location location = locationResult.getLastLocation();
                Intent i = new Intent("newLocation");
                i.putExtra("location", location);
                sendBroadcast(i);
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("");
    }



    private void startLocationService() { //Method to start the service
        String channelId = "location_notification_channel";
        NotificationManager notificationManager = //creating a new notification manager
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder( //creating a new notification
                getApplicationContext(),
                channelId
        );
        //Setting the info of the notification
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Activity in Progress");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //ensuring SDK is of a minmum that is required

            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) { //check to see notification manager is not null
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH //setting the importance as high to receive updates as often as possible
                );
                notificationChannel.setDescription("Channel created by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        //Creating a location request object
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(900); //Interval in which we want updates - set as every second so it can update the timer efficiently
        locationRequest.setFastestInterval(700); //If the location is available sooner, we get it earlier
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        startForeground(Constants.LOCATION_SERVICE_ID, builder.build());
    } //end of start location method


    //stop Location method
    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Constants.ACTION_START_LOCATION_SERVICE)) { //If the value passed from the activity is Start then start location, otherwise Stop Location service
                    startLocationService();
                } else if (action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)) {
                    stopLocationService();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
