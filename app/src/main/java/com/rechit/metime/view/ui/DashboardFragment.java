package com.rechit.metime.view.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.rechit.metime.activity.ProfileActivity;
import com.rechit.metime.model.Activity;
import com.rechit.metime.model.User;
import com.rechit.metime.vievmodel.UserViewModel;

import org.jetbrains.annotations.NotNull;

import static com.rechit.metime.Utils.AppUtils.loadProfilePicFromUrl;


public class DashboardFragment extends Fragment {
    private User user;

    private View view;
    private ImageView goal,time,idea,imgPhoto;
    private TextView username;

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

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bn_main);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
            loadProfilePicFromUrl(imgPhoto, user.getImageUrl());
            username.setText(user.getUsername());

        });

        userViewModel.query(firebaseUser.getUid());
        userViewModel.addSnapshotListener(firebaseUser.getUid());

//        DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Profile").document("new");
//
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists()){
//                    String name = documentSnapshot.getString("username");
//
//                    username.setText(name);
//
//                } else{
//                    Toast.makeText(getActivity(), "Dokumen tidak ada", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//                Toast.makeText(getActivity(), "Data Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

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

        // Inflate the layout for this fragment
        return view;
    }
}