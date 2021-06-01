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
import java.util.ArrayList;

public class CalenderFragment extends Fragment {

    private FirebaseFirestore db;
    private ActivityAdapter adapter;
    private RecyclerView rvListActivity;
    private View view;
    private ArrayList<Activity> activitytList;
    private FloatingActionButton fabAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calender, container, false);

        fabAdd = view.findViewById(R.id.fab_add);
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
        adapter = new ActivityAdapter(getActivity(), activitytList);
        rvListActivity.setAdapter(adapter);

        db=FirebaseFirestore.getInstance();
        query();

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Activity").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Log.w(getClass().getSimpleName(),"failed get update", error);
                }else if(value !=null){
                    query();
                    Log.d(getClass().getSimpleName(), "Changes detected");
                }
            }
        });

        return view;
    }

    public void query(){
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            activitytList.clear();
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Activity activitymodel = document.toObject(Activity.class);
                                activitymodel.setId(document.getId());
                                activitytList.add(activitymodel);
                                adapter.notifyDataSetChanged();
                            }
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

}