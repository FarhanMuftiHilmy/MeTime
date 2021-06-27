package com.rechit.metime.repository;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rechit.metime.model.Time;
import com.rechit.metime.view.ui.DashboardFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeRepository {

    private final String TAG = getClass().getSimpleName();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final CollectionReference reference =  db.collection("User");
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private final MutableLiveData<ArrayList<Time>> timeListLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Time>> getTimeListLiveData(){
        return timeListLiveData;
    }

    private final MutableLiveData<Time> timeLiveData = new MutableLiveData<Time>();
    public MutableLiveData<Time> getTimeLiveData(){
        return timeLiveData;
    }

    // blm tambah order by buat query nanti pas di history
    public void query(){
        reference.document(firebaseUser.getUid()).collection("Time")
                .orderBy("timeStamps", Query.Direction.DESCENDING)
               .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        ArrayList<Time> timeList = new ArrayList<>();
                        if (task.getResult() != null) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Time time = document.toObject(Time.class);
                                timeList.add(time);
                                if (time != null) Log.d(TAG, "qury : " + time.getTitleTime());
                            }
                        }
                        timeListLiveData.postValue(timeList);
                        Log.d(TAG, "Document was queried");

                    } else Log.w(TAG, "error quering document", task.getException() );
                });
    }


    public void insert(Time time){
        reference.document(firebaseUser.getUid()).collection("Time").document(time.getId())
                .set(time)
                .addOnCompleteListener( task -> {
                   if(task.isSuccessful()) Log.d(TAG, "Document Was Added");
                   else Log.w(TAG, "Error Adding Document", task.getException());
                });
    }

    public void delete(Time time){
        reference.document(firebaseUser.getUid()).collection("Time").document(time.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) Log.d(TAG, "Document Was Delete");
                    else Log.w(TAG, "Error Delete Document", task.getException());
                });
    }


    public void addSnapshotListener(){
        reference.document(firebaseUser.getUid()).collection("Time")
                .addSnapshotListener((value, error) -> {
                   if (error !=null) Log.w(TAG, "Listen failed", error );
                   else if (value !=null){
                       query();
                       Log.d(TAG, "changes detected");
                   }
                });
    }


    private Map<String,Object> objectToHashMap(Time time){
        Map<String,Object> document = new HashMap<>();
        document.put("id", time.getId());
        document.put("titleTime", time.getTitleTime());
        document.put("trackingTime", time.getTrackingTime());
        document.put("timeStamos", time.getTimeStamps());
        return document;
    }

}
