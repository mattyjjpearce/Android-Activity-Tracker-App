package com.example.activitytracker;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {

    RemoteCallbackList<MyBinder> remoteCallbackList = new RemoteCallbackList<>();

    //Method to get current location
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) { //Only call method, if we receive a result from getLastLocation
                double latitude = locationResult.getLastLocation().getLatitude(); //users current location
                double longitude = locationResult.getLastLocation().getLongitude();
                Log.d("TAG", "onLocationResult: " + latitude + ", " + longitude);
                doCallBacks(latitude, longitude);

                Intent i = new Intent("locationResult");
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                sendBroadcast(i);

            }
        }
    };

    public void doCallBacks(double latitude, double longitude){
        final int n = remoteCallbackList.beginBroadcast(); //use the remote callback list (which is a broadcast to tell activities there are changes)
        for(int i=0; i<n; i++){
            remoteCallbackList.getBroadcastItem(i).callback.durationEvent(latitude, longitude);//list of the registered callback,
        }
        remoteCallbackList.finishBroadcast();
    }

    public class MyBinder extends Binder implements  IInterface {

        @Override
        public IBinder asBinder() {
            return this;
        }


        //Main activity to receive a callback when something happens
        //Sends back this callback object
        public void registerCallback(LocationCallBack callback){
            this.callback = callback;
            remoteCallbackList.register(MyBinder.this);
        }

        public void unRegisterCallback(LocationCallback callback){
            remoteCallbackList.unregister(MyBinder.this);
        }
        LocationCallBack callback;


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private void startLocationService() {
        String channelId = "location_notification_channel";

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channelId
        );
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Activity in Progress");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("Channel created by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        //Creating a location request object
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000); //Interval in which we want updates
        locationRequest.setFastestInterval(2500); //If the location is available sooner, we get it earlier
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
    }

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
                if (action.equals(Constants.ACTION_START_LOCATION_SERVICE)) {
                    startLocationService();
                } else if (action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)) {
                    stopLocationService();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
