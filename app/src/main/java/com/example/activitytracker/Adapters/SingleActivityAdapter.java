package com.example.activitytracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.activitytracker.Entities.Activity;
import com.example.activitytracker.R;

import java.util.ArrayList;
import java.util.List;

public class SingleActivityAdapter extends RecyclerView.Adapter<SingleActivityAdapter.ActivityViewHolder> { //Same as ActivtityAdapter, but for a single item, so this time is not pressable.

    private List<Activity> data;
    private Context context;
    private LayoutInflater layoutInflater;



    public SingleActivityAdapter(Context context) {
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



        public ActivityViewHolder(View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.titleTextView);
            dateView = itemView.findViewById(R.id.dateTextView);
            commentView = itemView.findViewById(R.id.commentTextView);
            distanceView = itemView.findViewById(R.id.distanceView);
            averageSpeedView = itemView.findViewById(R.id.averageSpeedTextView);
            topSpeed = itemView.findViewById(R.id.topSpeedView);
            ratingAndActivityTypeView = itemView.findViewById(R.id.ratingTextView);
            timeView = itemView.findViewById(R.id.timeView);


            itemView.hasOnClickListeners();
        }
        void bind(final Activity activity) {
            if (activity != null) {

                elapsedSeconds = activity.getTime();
                secondsDisplay = elapsedSeconds % 60;
                elapsedMinutes = elapsedSeconds / 60;

                titleView.setText(activity.getTitle());
                dateView.setText(activity.getDate());
                commentView.setText(activity.getComment());

                if(activity.getDistance()>1000){
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



