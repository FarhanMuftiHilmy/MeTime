package com.rechit.metime.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rechit.metime.R;
import com.rechit.metime.model.Activity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {
    private static final String TAG = "AddEventActivity";

    private static final String KEY_ID= "id";
    private static final String KEY_TITLE= "title";
    private static final String KEY_DETAILS = "description";
    private Activity activity;

    private EditText editTitle, editDesc;
    private Button btnSave;
    private Boolean isUpdate;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editTitle = findViewById(R.id.edt_title);
        editDesc = findViewById(R.id.edt_detail);
        btnSave = findViewById(R.id.btn_save);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        isUpdate = getIntent().hasExtra("activities");
        if (isUpdate){
            activity = getIntent().getParcelableExtra("activities");
            editTitle.setText(activity.getTitle());
            editDesc.setText(activity.getDescription());
            btnSave.setText("Update");
        }else{
            activity = new Activity();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = UUID.randomUUID().toString();
                String title= editTitle.getText().toString();
                String desc = editDesc.getText().toString();

                if (isUpdate){
                    updateActivity(activity.getId(),title,desc);
                }else {
                    AddActivity(id, title, desc);
                }
            }
        });
    }

    public void AddActivity(String id, String title, String desc){

        if(!title.isEmpty()&&!desc.isEmpty()){
            Map<String, Object> event  = new HashMap<>();
            event.put(KEY_ID, id);
            event.put(KEY_TITLE, title);
            event.put(KEY_DETAILS, desc);

            db.collection("User").document(firebaseUser.getUid())
                    .collection("Activity").document(id).set(event)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddEventActivity.this, "Aktivitas Tersimpan", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddEventActivity.this, "Aktivitas Tidak Tersimpan", Toast.LENGTH_SHORT).show();
                            Log.d(TAG,e.toString());
                        }

                    });
        }else{
            Toast.makeText(this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateActivity(String id, String title, String desc){
        Activity updateActivities = new Activity(id,title,desc);

        db.collection("User").document(firebaseUser.getUid())
                .collection("Activity")
                .document(id)
                .update(objectToHashMap(updateActivities))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddEventActivity.this, "Activity Has Been Updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(getClass().getSimpleName(),"error",e);
                Toast.makeText(AddEventActivity.this, "Fail to Update The Activity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Map<String,Object> objectToHashMap(Activity activity){
        Map<String, Object> document = new HashMap<>();
        document.put("id", activity.getId());
        document.put("title", activity.getTitle());
        document.put("description",activity.getDescription());
        return document;
    }
}