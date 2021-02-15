package com.example.activitytracker.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.activitytracker.Database;
import com.example.activitytracker.Entities.Activity;


public class ActivityProvider extends ContentProvider {

    // FOR DATA
    public static final String AUTHORITY = "com.example.cw3";
     public static final String TABLE_NAME = "activity_table";
    public static final Uri URI_ITEM = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    Database db = null;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ActivityContract.AUTHORITY, "activity_table", 1);
    }


    @Override
    public boolean onCreate() {
        db = Database.getDatabase(this.getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch(uriMatcher.match(uri)) {
            case 1:
                cursor = db.activityDAO().findAll();
                if (getContext() != null) {
                    cursor.setNotificationUri(getContext()
                            .getContentResolver(), uri);
                    return cursor;
                }else {
                    throw new IllegalArgumentException("Unknown URI: " + uri);
                }
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String contentType;

        if (uri.getLastPathSegment()==null) {
            contentType = ActivityContract.CONTENT_TYPE_MULTIPLE;
        } else {
            contentType = ActivityContract.CONTENT_TYPE_SINGLE;
        }

        return contentType;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        return null;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
