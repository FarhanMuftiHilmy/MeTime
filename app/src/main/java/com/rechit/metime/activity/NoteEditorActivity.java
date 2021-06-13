package com.rechit.metime.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rechit.metime.R;
import com.rechit.metime.view.ui.MainActivity;
import com.rechit.metime.view.ui.NoteFragment;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {
    Intent data;
    int noteId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        data = getIntent();


        TextView content = findViewById(R.id.noteEditorContent);
        TextView title = findViewById(R.id.noteEditorTitle);
        AppCompatButton color = findViewById(R.id.color);
        content.setMovementMethod(new ScrollingMovementMethod());

        content.setText(data.getStringExtra("content"));
        title.setText(data.getStringExtra("title"));
        color.setBackgroundTintList(this.getResources().getColorStateList(data.getIntExtra("code", 0)));

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NoteEditorActivity.this, EditNoteActivity.class);
                i.putExtra("title", data.getStringExtra("title"));
                i.putExtra("content", data.getStringExtra("content"));
                i.putExtra("noteId", data.getStringExtra("noteId"));
                startActivity(i);
                finish();
            }
        });

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditNoteActivity.class);
                i.putExtra("title", data.getStringExtra("title"));
                i.putExtra("content", data.getStringExtra("content"));
                i.putExtra("noteId", data.getStringExtra("noteId"));
                startActivity(i);
                finish();
            }
        });

//        EditText editText = findViewById(R.id.editText);

        // Fetch data that is passed from MainActivity
//        Intent intent = getIntent();
//
//        // Accessing the data using key and value
//        noteId = intent.getIntExtra("noteId", -1);
//        if (noteId != -1) {
//            editText.setText(NoteFragment.notes.get(noteId));
//        } else {
//
//            NoteFragment.notes.add("");
//            noteId = NoteFragment.notes.size() - 1;
//            NoteFragment.arrayAdapter.notifyDataSetChanged();
//
//        }
//
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                // add your code here
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                NoteFragment.notes.set(noteId, String.valueOf(charSequence));
//                NoteFragment.arrayAdapter.notifyDataSetChanged();
//                // Creating Object of SharedPreferences to store data in the phone
//                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
//                HashSet<String> set = new HashSet(NoteFragment.notes);
//
//
//                sharedPreferences.edit().putStringSet("notes", set).apply();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                // add your code here
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}