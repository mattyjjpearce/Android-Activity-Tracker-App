package com.example.activitytracker.ContentProviders;

import android.net.Uri;

public class ActivityContract {



        public static final String AUTHORITY = "com.example.activitytracker.ContentProviders.ActivityProvider";
        public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

        public static final Uri ACTIVITY_URI = Uri.parse("content://"+AUTHORITY+"/activity_table/");

        //field names
        public static final String ACTIVITY_ID = "id";
        public static final String ACTIVITY_TITLE = "title";



        public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/ActivityProvider.data.text";
        public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/ActivityProvider.data.text";


}
