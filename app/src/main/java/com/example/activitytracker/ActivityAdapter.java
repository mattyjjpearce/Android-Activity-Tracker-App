package com.example.activitytracker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override //Article explaining how to implement onClickListener
            // https://medium.com/@filswino/setting-onclicklistener-in-recyclerview-android-e6e198f5f0e2
            public void onClick(View v) {
                Log.d("clicked", "onClick: " + data.get(position).getId());
                Intent intent = new Intent(context, singleActivity.class);

                intent.putExtra("id", data.get(position).getId());
                intent.putExtra("title", data.get(position).getTitle());
                intent.putExtra("comment", data.get(position).getComment());
                intent.putExtra("date", data.get(position).getDate());
                intent.putExtra("totalDistance", data.get(position).getDistance());
                intent.putExtra("averageSpeed", data.get(position).getSpeed());
                intent.putExtra("topSpeed", data.get(position).getTopSpeed());
                intent.putExtra("totalTime", data.get(position).getTime());
                intent.putExtra("activityTpe", data.get(position).getActivityType());
                intent.putExtra("rating", data.get(position).getRating());




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



