package com.example.activitytracker.ContentProviders;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URI;

public class ContentProviderActivity extends AppCompatActivity {

    //cursor adapter;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //   setContentView(R.layout.contentprovideer thing;

        queryRecipes();


        getContentResolver().
                registerContentObserver(
                        ActivityContract.AUTHORITY_URI,
                        true,
                        new ChangeObserver(handler)
                );
    }

    class ChangeObserver extends ContentObserver {

        public ChangeObserver(Handler handler) {
            super(handler);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, (Uri) null);
        }

        public void onChange(boolean selfChange, URI uri) {
            queryRecipes();
        }
    }




    public void  queryRecipes(){

        //Specifying what info I am interested in querying
        String[] projection = new String[]{
                ActivityContract.ACTIVITY_ID,
                ActivityContract.ACTIVITY_TITLE,

        };

        Cursor cursor = getContentResolver().query(ActivityContract.ACTIVITY_URI, projection, null,null,null);

        //adapter = new SimpleCursorAdapter(
//            this,
//            R.layout.item_layout,
//            cursor,
//            projection,
//            projection,
        //    recyclerViewthing or layout to set check code
//            0);

//        ListView listView = (ListView) findViewById(R.id.recipeListView);
//        listView.setAdapter(adapter);
    }




}
