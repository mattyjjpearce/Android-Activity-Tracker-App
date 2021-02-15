package com.example.activitytracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.activitytracker.Entities.Activity;
import com.example.activitytracker.R;
import com.example.activitytracker.Activities.singleActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private List<Activity> data;
    private Context context;
    private LayoutInflater layoutInflater;



    public ActivityAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.db_layout_view, parent, false);
        return new ActivityViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(ActivityViewHolder holder, final int position) {
        holder.bind(data.get(position));

        //Method to allow each item in the recycle view to be touchable
        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override //Article explaining how to implement onClickListener
            // https://medium.com/@filswino/setting-onclicklistener-in-recyclerview-android-e6e198f5f0e2
            public void onClick(View v) {

                Intent intent = new Intent(context, singleActivity.class); //New intent and put all values of pressed item into intent

                Activity activityPressed = data.get(position);

                intent.putExtra("id", activityPressed.getId());
                intent.putExtra("title", activityPressed.getTitle());
                intent.putExtra("comment", activityPressed.getComment());
                intent.putExtra("date", activityPressed.getDate());
                intent.putExtra("totalDistance", activityPressed.getDistance());
                intent.putExtra("averageSpeed", activityPressed.getSpeed());
                intent.putExtra("topSpeed", activityPressed.getTopSpeed());
                intent.putExtra("totalTime", activityPressed.getTime());
                intent.putExtra("activityTpe", activityPressed.getActivityType());
                intent.putExtra("rating", activityPressed.getRating());

                //Start new activity, passing all the values through the intent
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size() ;
    }

    public void setActivityData(List<Activity> newData) {
        if (data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;

        }
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder {

        //Initializing all the views and variables needed
        View mView;

        TextView titleView;
        TextView dateView;
        TextView commentView;
        TextView distanceView;
        TextView averageSpeedView;
        TextView topSpeed;
        TextView ratingAndActivityTypeView;
        TextView timeView;

        long secondsDisplay;
        long elapsedMinutes;
        long elapsedSeconds;


        //Getting all the views
        public ActivityViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            titleView = itemView.findViewById(R.id.titleTextView);
            dateView = itemView.findViewById(R.id.dateTextView);
            commentView = itemView.findViewById(R.id.commentTextView);
            distanceView = itemView.findViewById(R.id.distanceView);
            averageSpeedView = itemView.findViewById(R.id.averageSpeedTextView);
            topSpeed = itemView.findViewById(R.id.topSpeedView);
            ratingAndActivityTypeView = itemView.findViewById(R.id.ratingTextView);
            timeView = itemView.findViewById(R.id.timeView);


            itemView.hasOnClickListeners(); //making each individual item pressable
        }
        void bind(final Activity activity) {
            if (activity != null) {

                elapsedSeconds = activity.getTime(); //formatting time for the user
                secondsDisplay = elapsedSeconds % 60;
                elapsedMinutes = elapsedSeconds / 60;

                //Setting appropriate textviews
                titleView.setText(activity.getTitle());
                dateView.setText(activity.getDate());
                commentView.setText(activity.getComment());

                if(activity.getDistance()>1000){ //if distance is greater than 1000, set as Km
                    long distanceInKm = activity.getDistance() / 1000;
                    distanceView.setText(distanceInKm+" Km");

                }else{
                    distanceView.setText(+activity.getDistance()+"m");
                }
                averageSpeedView.setText("Average Speed: "+String.format("%.2f",activity.getSpeed()) + "Km/H");
                topSpeed.setText("Top Speed: "+String.format("%.2f",activity.getTopSpeed()) + "Km/H");
                ratingAndActivityTypeView.setText(activity.getRating()+" "+activity.getActivityType());
                timeView.setText("Time: "+elapsedMinutes+":"+elapsedSeconds);

            }
        }

    }

}



