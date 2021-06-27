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


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rechit.metime.R;
import com.rechit.metime.model.User;

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    int RC_SIGN_IN = 1;

    EditText email, password;

    TextView sign_up;

    Button btn_login, register_google;

    FirebaseUser firebaseUser;
    FirebaseFirestore fstore;

    String userId;


    ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // check if user is null
        if (firebaseUser != null) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        sign_up = findViewById(R.id.sign_up);
        register_google = findViewById(R.id.register_google);

        progressBar = findViewById(R.id.progressBar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignInActivity.this, R.string.field_cannot_empty, Toast.LENGTH_SHORT).show();

                } else{
                    mAuth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else{
                                        Toast.makeText(SignInActivity.this, "", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                }

            }
        });
        register_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Configure Google Sign In
                signIn();
            }
        });


    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getBaseContext(), "Login Successfully...", Toast.LENGTH_LONG).show();
                            userId  = mAuth.getCurrentUser().getUid();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            DocumentReference documentReference = fstore.collection("User").document(userId).collection("Profile").document("new");
                            User user = new User();
                            user.setId(firebaseUser.getUid());
                            user.setUsername(firebaseUser.getDisplayName());
                            user.setEmail(firebaseUser.getEmail());
                            user.setImageUrl(String.valueOf(firebaseUser.getPhotoUrl()));
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
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
                        } else {
                            Toast.makeText(SignInActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                        }
                    }
                });
    }
}

