package com.rechit.metime.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rechit.metime.R;
import com.rechit.metime.model.Activity;
import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityHolder> {

    Context context;
    ArrayList<Activity> activityList;

    public ActivityAdapter(Context context, ArrayList<Activity> activityList) {
        this.activityList = activityList;
        this.context = context;
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent,false);
        return new ActivityHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
        holder.textViewTitle.setText(activityList.get(position).getTitle());
        holder.textViewDescription.setText(activityList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }


    public static class ActivityHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewDescription;

        public ActivityHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewDescription = itemView.findViewById(R.id.tv_detail);
        }
    }
}
