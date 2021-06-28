package com.rechit.metime.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rechit.metime.callback.OnCallBackUploadImage;
import com.rechit.metime.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.rechit.metime.Utils.ImageUtils.convertUriToByteArray;
import static com.rechit.metime.Utils.ImageUtils.getCompressedByteArray;

public class UserRepository {

    private final String TAG = getClass().getSimpleName();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public final CollectionReference reference =  db.collection("User");
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    public MutableLiveData<User> getUserLiveData(){
        return userLiveData;
    }

    public void query(String uid){
        reference.document(firebaseUser.getUid()).collection("Profile").document("new")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if(task.getResult() != null){
                            User user = task.getResult().toObject(User.class);
                            userLiveData.postValue(user);
                            if(user != null) Log.d(TAG, "query; " + user.getUsername());
                        }
                        Log.d(TAG, "Document was queried");
                    }else Log.w(TAG, "Error Querying Document", task.getException() );
                });
    }

    public void insert(User user){
        reference.document(firebaseUser.getUid()).collection("Profile").document("new")
                .set(user)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) Log.d(TAG, "Document Was Added");
                    else Log.w(TAG, "Error Adding Document", task.getException() );
                });
    }

    public void update(User user){
        reference.document(firebaseUser.getUid()).collection("Profile").document("new")
                .update(objectToHashMap(user))
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) Log.d(TAG, "Document Was Update");
                    else Log.w(TAG, "Errror updating doucment", task.getException() );
                });
    }

    public void uploadImage(Context context, Uri uri, String folderName, String fileName, OnCallBackUploadImage callback){
        byte[] image = convertUriToByteArray(context, uri);
        image = getCompressedByteArray(image, folderName.equals("ProfileImg")); // Jika foto profil, perkecil ukuran

        StorageReference reference = storage.getReference().child(folderName + "/" + fileName);
        UploadTask uploadTask = reference.putBytes(image);
        uploadTask.addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uriResult -> {
            callback.onSuccess(uriResult.toString());
            Log.d(TAG, "Image was uploaded");
        })).addOnFailureListener(e -> Log.w(TAG, "Error uploading image", e));
    }

    public void addSnapshotListener(String uid){
        reference.document(firebaseUser.getUid()).collection("Profile").document("new").addSnapshotListener((value, error) -> {
            if (error != null)
                Log.w(TAG, "Listen Failed", error );
            else if  (value !=null){
                query(uid);
                Log.d(TAG, "Changes Detected");
            }
        });
    }

    private Map<String,Object> objectToHashMap(User user){
        Map<String,Object> document = new HashMap<>();
        document.put("id", user.getId());
        document.put("username", user.getUsername());
        document.put("email", user.getEmail());
        document.put("imageUrl", user.getImageUrl());
        return document;
    }


}
