package com.example.activitytracker;

//Interface class to specify a call back. Service can call back on activity w
public interface LocationCallBack {

    public void durationEvent(double latitude, double longitude);
}
