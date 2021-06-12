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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rechit.metime.R;
import com.rechit.metime.activity.AddNoteActivity;
import com.rechit.metime.activity.NoteEditorActivity;
import com.rechit.metime.view.adapter.NoteAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    public static ArrayList<String> notes = new ArrayList<>();
    public static ArrayAdapter arrayAdapter;


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



        FirebaseFirestore firestore = FirebaseFirestore.getInstance();



        Toolbar toolbar = getView().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        FloatingActionButton fab = getView().findViewById(R.id.fab);

        RecyclerView noteList = getView().findViewById(R.id.noteList);


        List<String> titles = new ArrayList<>();
        List<String> content = new ArrayList<>();

        titles.add("Firs Note");
        content.add("First Note Content Sample First Note Content Sample First Note Content Sample First Note Content Sample");

        titles.add("Second Note");
        content.add("Second Note Content Sample");

        titles.add("Firs Note");
        content.add("First Note Content Sample First Note Content Sample First Note Content Sample First Note Content Sample");

        titles.add("Second Note");
        content.add("Second Note Content Sample");

        NoteAdapter adapter = new NoteAdapter(titles, content);




        noteList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        noteList.setAdapter(adapter);
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
}