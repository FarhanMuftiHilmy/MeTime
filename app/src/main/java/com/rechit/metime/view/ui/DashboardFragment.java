package com.rechit.metime.view.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rechit.metime.R;
import com.rechit.metime.activity.NoteEditorActivity;
import com.rechit.metime.activity.ProfileActivity;
import com.rechit.metime.model.Activity;
import com.rechit.metime.model.Item;
import com.rechit.metime.model.Note;
import com.rechit.metime.model.User;
import com.rechit.metime.vievmodel.UserViewModel;
import com.rechit.metime.view.adapter.ActivityAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.rechit.metime.Utils.AppUtils.loadProfilePicFromUrl;


public class DashboardFragment extends Fragment {
    private User user;

    private final String TAG = getClass().getSimpleName();
    private FirebaseFirestore db;
    private View view;
    private ImageView goal,time,idea,imgPhoto;
    private TextView username;
    private ActivityAdapter activityAdapter;
    private RecyclerView rvListActivity;
    private ArrayList<Item> items;
    private ArrayList<Activity> activitytList;
    FirestoreRecyclerAdapter<Note, NoteViewHolder> noteAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        goal = view.findViewById(R.id.goal);
        time = view.findViewById(R.id.time);
        idea = view.findViewById(R.id.idea);
        imgPhoto = view.findViewById(R.id.img_profile);
        username = view.findViewById(R.id.username);
        rvListActivity = view.findViewById(R.id.rv_activity_dashboard);

        items = new ArrayList<>();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        RecyclerView noteList = view.findViewById(R.id.rv_note_dashboard);
        RecyclerView rvListTime = (RecyclerView) view.findViewById(R.id.rv_time_activity);





        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bn_main);


        DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Profile").document("new");

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("username");

                    username.setText(name);

                } else{
                    Toast.makeText(getActivity(), "Dokumen tidak ada", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getActivity(), "Data Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.calendar_menu);
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.time_menu);
            }
        });
//        rvListActivity = (RecyclerView) view.findViewById(R.id.rv_activity_dashboard);
//        rvListActivity.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
//        items = new ArrayList<>();
//        activitytList = new ArrayList<>();
//        items.add(new Item(0, activitytList));
//        rvListActivity.setAdapter(new DashboardAdapter(items));
//        db=FirebaseFirestore.getInstance();
//        query();
//
//        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Activity").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if(error !=null){
//                    Log.w(getClass().getSimpleName(),"failed get update", error);
//                }else if(value !=null){
//                    query();
//                    Log.d(getClass().getSimpleName(), "Changes detected");
//                }
//            }
//        });
        idea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.note_menu);
            }
        });







        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });

        //NOTE DISPLAY

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

        noteList.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
//        noteList.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        noteList.setAdapter(noteAdapter);

        //ACTIVITY DISPLAY

        rvListActivity.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        activitytList = new ArrayList<>();
        activityAdapter = new ActivityAdapter(getActivity(), activitytList, this::DeleteActivity);
        rvListActivity.setAdapter(activityAdapter);

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


        // Inflate the layout for this fragment
        return view;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
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
                                activityAdapter.notifyDataSetChanged();
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

    public void DeleteActivity( Activity activity) {
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Activity").document(activity.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Document Was Delete", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getActivity(), "Error Delete Document", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
    }

}