package com.example.activitytracker.ContentProviders;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitytracker.Entities.Activity;
import com.example.activitytracker.R;

public class ProviderAdapter extends RecyclerView.Adapter {

    private Cursor mCursor;

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {
            mCursor.getColumnIndexOrThrow(Activity.COLUMN_NAME);
        }
    }


    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    void setActivity(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

     class ViewHolder extends RecyclerView.ViewHolder {

         TextView mText;

         ViewHolder(ViewGroup parent) {
             super(LayoutInflater.from(parent.getContext()).inflate(
                     android.R.layout.simple_list_item_1, parent, false));
             mText = itemView.findViewById(android.R.id.text1);
         }

     }

}


