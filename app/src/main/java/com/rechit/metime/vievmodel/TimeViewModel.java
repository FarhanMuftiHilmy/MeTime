package com.rechit.metime.vievmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.rechit.metime.model.Time;
import com.rechit.metime.repository.TimeRepository;

import java.io.StringReader;
import java.util.ArrayList;

public class TimeViewModel extends ViewModel {
    private final TimeRepository repository = new TimeRepository();

    public LiveData<ArrayList<Time>> getTimeListLiveData(){
        return repository.getTimeListLiveData();
    }

    public LiveData<Time> getTimeLiveData(){
        return  repository.getTimeLiveData();
    }

    public void query(){
        repository.query();
    }


    public void insert(Time time){  repository.insert(time);  }

    public void delete(Time time){
        repository.delete(time);
    }

    public CollectionReference getReference(){
        return repository.reference;
    }

    public void addSnapshotListener(){
        repository.addSnapshotListener();
    }

}
