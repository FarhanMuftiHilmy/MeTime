package com.rechit.metime.view.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rechit.metime.R;
import com.rechit.metime.activity.AddEventActivity;
import com.rechit.metime.model.Activity;
import com.rechit.metime.view.adapter.ActivityAdapter;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalenderFragment extends Fragment implements ActivityAdapter.ActivityAdapterCallback{

    private final String TAG = getClass().getSimpleName();
    private FirebaseFirestore db;
    private ActivityAdapter adapter;
    private RecyclerView rvListActivity;
    private View view;
    private CalendarView calendarView;
    private ArrayList<Activity> activitytList;
    private FloatingActionButton fabAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calender, container, false);

        fabAdd = view.findViewById(R.   id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                startActivity(intent);
            }
        });

        rvListActivity = (RecyclerView) view.findViewById(R.id.rv_activity);
        rvListActivity.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        activitytList = new ArrayList<>();
        adapter = new ActivityAdapter(getActivity(), activitytList, this);
        rvListActivity.setAdapter(adapter);

        calendarView = view.findViewById(R.id.calender_view);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendarView.setDate(calendar.getTimeInMillis());
                firstQuery();
            }
        });

        db=FirebaseFirestore.getInstance();
        firstQuery();

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Activity").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Log.w(getClass().getSimpleName(),"failed get update", error);
                }else if(value !=null){
                    firstQuery();
                    Log.d(getClass().getSimpleName(), "Changes detected");
                }
            }
        });

        return view;
    }

    public void query(String date){
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Activity")
                .whereEqualTo("date", date).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            activitytList.clear();
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Activity activitymodel = document.toObject(Activity.class);
                                activitymodel.setId(document.getId());
                                activitytList.add(activitymodel);
                                Log.d(TAG, "complete" + activitymodel.toString());
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getActivity(), "Data Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Fail To Get Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void DeleteActivity( Activity activity) {
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Activity").document(activity.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(getActivity(), R.string.document_was_delete, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getActivity(), R.string.error_delete_document, Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
    }

    public void firstQuery(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/y", Locale.getDefault());
        Date date = new Date();
        date.setTime(calendarView.getDate());
        String selectedDate = dateFormat.format(date);
        query(selectedDate);
        Log.d(TAG, selectedDate);
    }

    @Override
    public void onActivityDelete(Activity activity) {
        DeleteActivity(activity);
    }
}