package com.rechit.metime.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.rechit.metime.R;
import com.rechit.metime.Utils.LoadingDialog;
import com.rechit.metime.model.User;
import com.rechit.metime.vievmodel.UserViewModel;
import com.rechit.metime.view.ui.CalenderFragment;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.rechit.metime.Utils.AppUtils.getFixText;
import static com.rechit.metime.Utils.AppUtils.loadProfilePicFromUrl;
import static com.rechit.metime.Utils.AppUtils.showToast;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_PROFILE_IMAGE = 100;

    private LoadingDialog loadingDialog;
    private User user;
    private UserViewModel userViewModel;

    private Button btnSave;
    private ImageView imgPhoto;
    private EditText edtEmail, edtUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new LoadingDialog(this, false);

        Button btnImgUpdate = findViewById(R.id.btn_img_edit);
        btnSave = findViewById(R.id.btn_save_edit);
        btnImgUpdate.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        imgPhoto = findViewById(R.id.img_photo_edit);
        edtUserName = findViewById(R.id.txt_username_edit);
        edtEmail = findViewById(R.id.txt_email_edit);

        Intent intent = getIntent();
        if(intent.hasExtra("EXTRA_PROFILE")){
            user = intent.getParcelableExtra("EXTRA_PROFILE");
            loadProfilePicFromUrl(imgPhoto, user.getImageUrl());
            edtUserName.setText(user.getUsername());
            edtEmail.setText(user.getEmail());
        }

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_img_edit){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Pilih foto profil"), RC_PROFILE_IMAGE);
        } else if(id == R.id.btn_save_edit){
            String name = getFixText(edtUserName);
            String email = getFixText(edtEmail);

            if (name.isEmpty() || email.isEmpty()){
//                if (name.isEmpty()) edtUserName.setError("Masukan Nama");
//                if (email.isEmpty()) edtEmail.setError("Masukan Email");
//                showToast(this, "Pastikan data yang diisi lengkap");
                Toast.makeText(this, R.string.field_cannot_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            user.setUsername(name);
            user.setEmail(email);

            userViewModel.update(user);
            onBackPressed();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PROFILE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) if (data.getData() != null) {
                    loadingDialog.show();

                    Uri uriProfileImage = data.getData();
                    loadProfilePicFromUrl(imgPhoto, uriProfileImage.toString());

                    String fileName = user.getId() + ".jpeg";
                    userViewModel.uploadImage(this, uriProfileImage, "ProfileImg", fileName, imageUrl -> {
                        user.setImageUrl(imageUrl);
                        loadingDialog.dismiss();
                    });
                }
            }
        }
    }
}