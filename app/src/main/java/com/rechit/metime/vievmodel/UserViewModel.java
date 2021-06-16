package com.rechit.metime.vievmodel;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.rechit.metime.callback.OnCallBackUploadImage;
import com.rechit.metime.model.User;
import com.rechit.metime.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private final UserRepository repository = new UserRepository();

    public LiveData<User> getUserLiveData() {
        return repository.getUserLiveData();
    }

    public void query(String uid){
        repository.query(uid);
    }

    public void insert(User user){
        repository.insert(user);
    }

    public void update(User user){
        repository.update(user);
    }

    public CollectionReference getReference(){
        return repository.reference;
    }

    public void uploadImage(Context context, Uri uri, String folderName, String fileName, OnCallBackUploadImage callback){
        repository.uploadImage(context, uri, folderName, fileName, callback);
    }

    public void addSnapshotListener(String uid){
        repository.addSnapshotListener(uid);
    }
}
