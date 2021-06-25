package com.rechit.metime.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rechit.metime.R;
import com.rechit.metime.activity.AddEventActivity;
import com.rechit.metime.model.Activity;
import com.rechit.metime.model.Time;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityHolder> {

    private Context context;
    private ArrayList<Activity> activityList;
    public ActivityAdapterCallback activityCallback;

    public ActivityAdapter(Context context, ArrayList<Activity> activityList, ActivityAdapter.ActivityAdapterCallback activityCallback) {
        this.activityList = activityList;
        this.context = context;
        this.activityCallback = activityCallback;
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent,false);
        return new ActivityHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
        Activity activity = activityList.get(position);
        holder.textViewTitle.setText(activityList.get(position).getTitle());
        holder.textViewDescription.setText(activityList.get(position).getDescription());
        holder.textViewDate.setText(activityList.get(position).getDate());
        holder.textViewTime.setText(activityList.get(position).getTime());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setIcon(R.drawable.ic_warning_delete)
                        .setTitle(R.string.delete_activity)
                        .setMessage(R.string.do_you_want_to_remove_this_activity)
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activityCallback.onActivityDelete(activity);
                            }
                        }).create().show();
                return true;
            }

        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewDescription, textViewDate, textViewTime;

        public ActivityHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewDescription = itemView.findViewById(R.id.tv_detail);
            textViewDate = itemView.findViewById(R.id.tv_date);
            textViewTime = itemView.findViewById(R.id.tv_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = activityList.get(getAdapterPosition());
                    Intent intent = new Intent(context, AddEventActivity.class);
                    intent.putExtra("activities", activity);
                    context.startActivity(intent);
                }
            });
        }
    }


    public interface ActivityAdapterCallback{
        void onActivityDelete(Activity activity);
    }
}
