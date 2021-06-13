package com.rechit.metime.view.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rechit.metime.R;
import com.rechit.metime.activity.AddNoteActivity;
import com.rechit.metime.activity.NoteEditorActivity;
import com.rechit.metime.model.Note;
import com.rechit.metime.view.adapter.NoteAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    public static ArrayList<String> notes = new ArrayList<>();
    public static ArrayAdapter arrayAdapter;

    FirestoreRecyclerAdapter<Note, NoteViewHolder> noteAdapter;


    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

//        GridView gridView = getView().findViewById(R.id.gridView);


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        Query query = firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Notes").orderBy("title", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Note> allNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(allNotes) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull NoteViewHolder holder, int position, @NonNull @NotNull Note model) {
                holder.noteTitle.setText(model.getTitle());
                holder.noteContent.setText(model.getContent());

                final int code = getRandomColor();
                holder.noteColor.setBackgroundTintList(holder.view.getResources().getColorStateList(code));
                final String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), NoteEditorActivity.class);
                        i.putExtra("title", model.getTitle());
                        i.putExtra("content", model.getContent());
                        i.putExtra("code", code);
                        i.putExtra("noteId", docId);
                        v.getContext().startActivity(i);
                    }
                });

                holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new AlertDialog.Builder(view.getContext())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Are you sure?")
                                .setMessage("Do you want to delete this note?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Notes").document(docId);
                                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //note deleted
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                Toast.makeText(getContext(), "Error in deleting Notes", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Toast.makeText(getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("No", null).show();

                        return true;
                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout, parent, false);
                return new NoteViewHolder(view);
            }
        };





        Toolbar toolbar = getView().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        FloatingActionButton fab = getView().findViewById(R.id.fab);

        RecyclerView noteList = getView().findViewById(R.id.noteList);



        noteList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        noteList.setAdapter(noteAdapter);

//
//        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
//        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
//
//        if (set == null) {
//
//            notes.add("Example note");
//        } else {
//            notes = new ArrayList(set);
//        }
//
//        // Using custom listView Provided by Android Studio
//        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, notes){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent){
//                // Cast the grid view current item as a text view
//                TextView tv_cell = (TextView) super.getView(position,convertView,parent);
//
//                // Set the item background color
//                tv_cell.setBackground(getResources().getDrawable(R.drawable.notes_border));
//
//
//
//                // Put item item text in cell center
//                tv_cell.setGravity(Gravity.CENTER);
//
//                // Return the modified item
//                return tv_cell;
//            }
//        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });

        noteList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "Long Clicked", Toast.LENGTH_SHORT).show();

                return true;
            }
        });


//        gridView.setAdapter(arrayAdapter);
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                // Going from MainActivity to NotesEditorActivity
//                Intent intent = new Intent(getActivity(), NoteEditorActivity.class);
//                intent.putExtra("noteId", i);
//                startActivity(intent);
//
//            }
//        });
//
//        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                final int itemToDelete = i;
//                // To delete the data from the App
//                new AlertDialog.Builder(view.getContext())
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Are you sure?")
//                        .setMessage("Do you want to delete this note?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                notes.remove(itemToDelete);
//                                arrayAdapter.notifyDataSetChanged();
//                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
//                                HashSet<String> set = new HashSet(NoteFragment.notes);
//                                sharedPreferences.edit().putStringSet("notes", set).apply();
//                            }
//                        }).setNegativeButton("No", null).show();
//                return true;
//            }
//        });
        super.onViewCreated(view, savedInstanceState);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;
        View view;
        AppCompatButton noteColor;

        public NoteViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.titles);
            noteContent = itemView.findViewById(R.id.content);
            noteColor = itemView.findViewById(R.id.color);
            view = itemView;
        }

    }
    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.red);
        colorCode.add(R.color.orange2);
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.green);
        colorCode.add(R.color.aqua);

        Random randomColor = new Random();
        int number = randomColor.nextInt(colorCode.size());
        return colorCode.get(number);
    }

    @Override
    public void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(noteAdapter != null){
            noteAdapter.stopListening();
        }
    }
}