package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private ActivityViewModel viewModel;
    private ActivityAdapter adapter;
    private RecyclerView recyclerView;

    private Spinner sortBySpinner;
    private Spinner sortByActivitySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_LOCATION_PERMISSION
        );

        //finding the spinner and storing it in our local spinner
        sortBySpinner = findViewById(R.id.spinner);

        sortByActivitySpinner = findViewById(R.id.spinner2);



        //Getting the values of all the spinner 1 values and storing them into a string array
        String[] spinner1Values = getResources().getStringArray(R.array.spinner1values);
        //Loading the spinner values into the spinner through a spinner adapter
        ArrayAdapter spinner1Adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, spinner1Values);
        spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(spinner1Adapter);
        sortBySpinner.setOnItemSelectedListener(this);


        //Getting the values of all the spinner 2 values and storing them into a string array
        String[] spinner2Values = getResources().getStringArray(R.array.spinner2values);
        //Loading the spinner values into the spinner through a spinner adapter
        ArrayAdapter spinner2Adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, spinner2Values);
        spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortByActivitySpinner.setAdapter(spinner2Adapter);
        sortByActivitySpinner.setOnItemSelectedListener(this);




        adapter = new ActivityAdapter(this);
        recyclerView = findViewById(R.id.recyclerViewMain);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ActivityViewModel.class);

        viewModel.getAllActivities().observe(this, new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activity) {
                adapter.setActivityData(activity);
            }
        });
    }


    public void startActivity(View v){
        Intent intent = new Intent(this, startActivity.class);
        startActivity(intent);
    }


    //Methods when the spinner has selected an item
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner) {
            String valueFromSpinner = parent.getItemAtPosition(position).toString();

            Log.d("spinner", "onItemSelected: " + valueFromSpinner);


            if (valueFromSpinner.equals("Time")) {
                viewModel.getByTime().observe(this, new Observer<List<Activity>>() {
                    @Override
                    public void onChanged(List<Activity> activity) {
                        adapter.setActivityData(activity);
                    }
                });

            } else if (valueFromSpinner.equals("Distance Desc")) {
                viewModel.getByDistanceLargest().observe(this, new Observer<List<Activity>>() {
                    @Override
                    public void onChanged(List<Activity> activity) {
                        adapter.setActivityData(activity);
                    }
                });

            } else if (valueFromSpinner.equals("Distance Asc")) {
                viewModel.getByDistanceSmallest().observe(this, new Observer<List<Activity>>() {
                    @Override
                    public void onChanged(List<Activity> activity) {
                        adapter.setActivityData(activity);
                    }
                });


            }else if (valueFromSpinner.equals("Top Speed")){
                viewModel.getByTopSpeed().observe(this, new Observer<List<Activity>>() {
                    @Override
                    public void onChanged(List<Activity> activity) {
                        adapter.setActivityData(activity);
                    }
                });

            }else if (valueFromSpinner.equals("Average Speed")) {
                viewModel.getBySpeed().observe(this, new Observer<List<Activity>>() {
                    @Override
                    public void onChanged(List<Activity> activity) {
                        adapter.setActivityData(activity);
                    }
                });

            } else if (valueFromSpinner.equals("Oldest")){
                viewModel.getByDateRecent().observe(this, new Observer<List<Activity>>() {
                    @Override
                    public void onChanged(List<Activity> activity) {
                        adapter.setActivityData(activity);
                    }
                });
            }else if(valueFromSpinner.equals("Recent")){
                viewModel.getByMostRecent().observe(this, new Observer<List<Activity>>() {
                    @Override
                    public void onChanged(List<Activity> activity) {
                        adapter.setActivityData(activity);
                    }
                });
            }
        }
        String valueFromSpinner = parent.getItemAtPosition(position).toString();

         if(valueFromSpinner.equals(Constants.ACTIVITY_TYPE_RUN)){
            viewModel.byActivityType(Constants.ACTIVITY_TYPE_RUN).observe(this, new Observer<List<Activity>>() {
                @Override
                public void onChanged(List<Activity> activity) {
                    adapter.setActivityData(activity);
                }
            });
        }else if(valueFromSpinner.equals(Constants.ACTIVITY_TYPE_JOG)){
            Log.d("spinner", "onItemSelected: " + valueFromSpinner);
            viewModel.byActivityType(Constants.ACTIVITY_TYPE_JOG).observe(this, new Observer<List<Activity>>() {
                @Override
                public void onChanged(List<Activity> activity) {
                    adapter.setActivityData(activity);
                }
            });
        }
        else if(valueFromSpinner.equals(Constants.ACTIVITY_TYPE_WALK)){
            viewModel.byActivityType(Constants.ACTIVITY_TYPE_WALK).observe(this, new Observer<List<Activity>>() {
                @Override
                public void onChanged(List<Activity> activity) {
                    adapter.setActivityData(activity);
                }
            });
        }
         else if(valueFromSpinner.equals("All")){
             viewModel.getAllActivities().observe(this, new Observer<List<Activity>>() {
                 @Override
                 public void onChanged(List<Activity> activity) {
                     adapter.setActivityData(activity);
                 }
             });
         }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void deleteAll(View v){
        viewModel.DeleteAllActivities();
    }
}

