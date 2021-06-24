//package com.rechit.metime.view.adapter;
//
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.rechit.metime.R;
//import com.rechit.metime.activity.AddEventActivity;
//import com.rechit.metime.model.Activity;
//import com.rechit.metime.model.Item;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//    private ArrayList<Item> items;
//    private ActivityAdapter activityAdapter;
//
//    public DashboardAdapter(ArrayList<Item> items) {
//        this.items = items;
//    }
//
//    @NonNull
//    @NotNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        if(viewType == 0){
//            return new ActivityViewHolder(
//                    LayoutInflater.from(parent.getContext()).inflate(
//                            R.layout.item_activity,
//                            parent,
//                            false
//                    )
//            );
//        } else{
//            return null;
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
//        if(getItemViewType(position) == 0){
//            Activity activity = (Activity) items.get(position).getObject();
//            ((ActivityViewHolder) holder).setTextViewTitle(activity);
//            ((ActivityViewHolder) holder).setTextViewDescription(activity);
//            ((ActivityViewHolder) holder).setTextViewDate(activity);
//            ((ActivityViewHolder) holder).setTextViewTime(activity);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return items.get(position).getType();
//    }
//
//    public class ActivityViewHolder extends RecyclerView.ViewHolder{
//        TextView textViewTitle,textViewDescription, textViewDate, textViewTime;
//
//        public ActivityViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textViewTitle = itemView.findViewById(R.id.tv_title);
//            textViewDescription = itemView.findViewById(R.id.tv_detail);
//            textViewDate = itemView.findViewById(R.id.tv_date);
//            textViewTime = itemView.findViewById(R.id.tv_time);
//        }
//
//        void setTextViewTitle(Activity activity){
//            textViewTitle.setText(activity.getTitle());
//        }
//        void setTextViewDescription(Activity activity){
//            textViewDescription.setText(activity.getDescription());
//        }
//        void setTextViewDate(Activity activity){
//            textViewDate.setText(activity.getDate());
//        }
//        void setTextViewTime(Activity activity){
//            textViewTime.setText(activity.getTime());
//        }
//
//
//
//
//    }
//
//}
