package com.rechit.metime.view.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rechit.metime.R;
import com.rechit.metime.helper.DatabaseHelper;
import com.rechit.metime.model.Time;
import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeHolder> {

    private final ArrayList<Time> timeList = new ArrayList<>();
    private TimeAdapterCallback callback;
    private DatabaseHelper dbHelper;

    public TimeAdapter(TimeAdapterCallback callback) {
        this.callback = callback;
    }

    public void setData (ArrayList<Time> timeList){
        this.timeList.clear();
        this.timeList.addAll(timeList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_tracking, parent,false);
        return new TimeHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeHolder holder, int position) {
        Time time = timeList.get(position);
        holder.textViewTitleTime.setText(timeList.get(position).getTitleTime());
        holder.textViewTimeTracking.setText(timeList.get(position).getTrackingTime());
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setIcon(R.drawable.ic_warning_delete)
                    .setTitle(R.string.delete_time_tracking)
                    .setMessage(R.string.do_you_want_to_remove_this_activity)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callback.onTimeDeleted(time);
                        }
                    }).create().show();
        });
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public class TimeHolder extends RecyclerView.ViewHolder {
        TextView textViewTitleTime, textViewTimeTracking;
        Button btnDelete;

        public TimeHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitleTime = itemView.findViewById(R.id.txt_time_activity);
            textViewTimeTracking = itemView.findViewById(R.id.txt_time_play);
            btnDelete = itemView.findViewById(R.id.delete_time);
        }

    }

    public interface TimeAdapterCallback{
        void onTimeDeleted(Time time);
    }

}
