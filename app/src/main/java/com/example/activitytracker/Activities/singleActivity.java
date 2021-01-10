package com.example.activitytracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.activitytracker.Activity;
import com.example.activitytracker.ViewModels.ActivityViewModel;
import com.example.activitytracker.Adapters.SingleActivityAdapter;
import com.example.activitytracker.R;

import java.util.List;

public class singleActivity extends AppCompatActivity { //Activity when a activity get's pressed in the recycle viewer

    private int id;

    private RecyclerView recyclerView;
    private SingleActivityAdapter adapter;
    private ActivityViewModel viewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        //Get Intent
        Intent intent = getIntent();

        id = intent.getIntExtra("id",0); //Get the id passed through the intent to then pass on to other methods

        adapter = new SingleActivityAdapter(this);
        recyclerView = findViewById(R.id.singleRecyclerViewer);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ActivityViewModel.class);


        //Create ingredientDAO to receive all ingredients from DAO
        viewModel.getAcitvityFromId(id).observe(this, new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> ingredients) {
                adapter.setActivityData(ingredients);
            }
        });
    }


    public void updateComment(View v){

        //Getting the new comment inputted by the user
        EditText newComment = (EditText) findViewById(R.id.newCommentView);
        String newCommentString = newComment.getText().toString();

        //passing the values to the viewModel to call the appropriate methods to update the view
        viewModel.updateComment(newCommentString, id);
       // onCreate(getSupportParentActivityIntent().getExtras());
    }

    //Delete this activity
    public void deleteActivity(View v){
        viewModel.DeleteActivity(id);
        finish();
    }
    //Done editing activity
    public void done(View v){
        finish();
    }


    }
