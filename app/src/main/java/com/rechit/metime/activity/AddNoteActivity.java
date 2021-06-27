package com.rechit.metime.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rechit.metime.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AddNoteActivity extends AppCompatActivity {
    private FirebaseFirestore fstore;
    private FirebaseUser firebaseUser;
    EditText noteTitle, noteContent;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fstore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        noteContent = findViewById(R.id.addNoteContent);
        noteTitle = findViewById(R.id.addNoteTitle);

        progressBar = findViewById(R.id.progressBar);

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            String nTitle = noteTitle.getText().toString();
            String nContent = noteContent.getText().toString();

//            if(nTitle.isEmpty() || nContent.isEmpty()){
//                Toast.makeText(this, "Can't save with empty field", Toast.LENGTH_SHORT).show();
//            }

            progressBar.setVisibility(View.VISIBLE);

            //save note to Firebase
            DocumentReference documentReference = fstore.collection("User").document(firebaseUser.getUid()).collection("Notes").document();

            Map<String, Object> note = new HashMap<>();
            note.put("title", nTitle);
            note.put("content", nContent);

            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(AddNoteActivity.this, R.string.note_added, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(AddNoteActivity.this, R.string.error_try_again, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }
            });

            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed (){
        String nTitle = noteTitle.getText().toString();
        String nContent = noteContent.getText().toString();

        if(nTitle.isEmpty() || nContent.isEmpty()){
            Toast.makeText(this, R.string.cannot_save_with_empty_field, Toast.LENGTH_SHORT).show();
        }

        //save note to Firebase
        DocumentReference documentReference = fstore.collection("User").document(firebaseUser.getUid()).collection("Notes").document();

        Map<String, Object> note = new HashMap<>();
        note.put("title", nTitle);
        note.put("content", nContent);

        documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddNoteActivity.this, R.string.note_added, Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(AddNoteActivity.this, R.string.error_try_again, Toast.LENGTH_SHORT).show();
            }
        });

        super.onBackPressed();
    }
}