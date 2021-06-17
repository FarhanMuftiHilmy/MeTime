package com.rechit.metime.view.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rechit.metime.R;
import com.rechit.metime.activity.AddNoteActivity;
import com.rechit.metime.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseUser fuser;

    ProgressBar progressBar;
    TextView sign_in;
    EditText username, email, password;
    Button btn_register;

    FirebaseAuth auth;
    FirebaseFirestore fstore;

    String userId;
//    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        sign_in = findViewById(R.id.sign_in);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressBar);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = Objects.requireNonNull(username.getText()).toString();
                String txt_email = Objects.requireNonNull(email.getText()).toString().trim();
                String txt_password = Objects.requireNonNull(password.getText()).toString().trim();

                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if(txt_password.length() < 6){
                    Toast.makeText(SignUpActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else{
                    register(txt_username, txt_email, txt_password);
                }


            }
        });
    }

    private void register(String username, String email, String password){
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getBaseContext(), "Register Successfully...", Toast.LENGTH_LONG).show();
                    userId  = auth.getCurrentUser().getUid();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    DocumentReference documentReference = fstore.collection("User").document(userId).collection("Profile").document("new");
                    User user = new User();
                    user.setId(firebaseUser.getUid());
                    user.setUsername(username);
                    user.setEmail(firebaseUser.getEmail());
                    user.setImageUrl(getPhotoUrl(firebaseUser));
                    //                    Map<String, Object> userProfile = new HashMap<>();
//                    userProfile.put("username", username);
//                    userProfile.put("email", email);
//                    userProfile.put("imageUrl",  FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null? FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl(): "");
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.w("TAG", "Error adding document", e);
                        }
                    });
                } else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getBaseContext(), "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String getPhotoUrl(FirebaseUser firebaseUser){
        if (firebaseUser.getPhotoUrl() != null) return firebaseUser.getPhotoUrl().toString();
        else return "default";
    }



//    private void register(final String username, String email, String password){
//        auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//
//                            FirebaseUser firebaseUser = auth.getCurrentUser();
//                            assert firebaseUser != null;
//                            String userid = firebaseUser.getUid();
//
//                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
//
//                            HashMap<String, String> hashMap = new HashMap<>();
//                            hashMap.put("id", userid);
//                            hashMap.put("username", username);
//                            hashMap.put("imageURL", "default");
//
//
//                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        progressBar.setVisibility(View.INVISIBLE);
//                                        Toast.makeText(getBaseContext(), "Register Successfully...", Toast.LENGTH_LONG).show();
//                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                }
//                            });
//                        } else {
//                            Toast.makeText(SignUpActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
//                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
//                            Toast.makeText(SignUpActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    }
//                });
//
//
//
//    }
}