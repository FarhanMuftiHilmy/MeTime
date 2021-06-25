package com.rechit.metime.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rechit.metime.R;
import com.rechit.metime.model.User;
import com.rechit.metime.vievmodel.UserViewModel;
import com.rechit.metime.view.ui.SignInActivity;

import static com.rechit.metime.Utils.AppUtils.loadProfilePicFromUrl;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Log.d(getClass().getSimpleName(), "userId: " + firebaseUser.getUid());

        Button btnLogOut = findViewById(R.id.btn_logout);
        Button btnEdtProfile = findViewById(R.id.btn_edt_profile);
        TextView selectLanguage = findViewById(R.id.select_language);
        btnLogOut.setOnClickListener(this);
        btnEdtProfile.setOnClickListener(this);
        selectLanguage.setOnClickListener(this);

        ImageView imgPhoto = findViewById(R.id.img_profile);
        TextView tvUserName = findViewById(R.id.tv_nama_user);
        TextView tvUserEmail = findViewById(R.id.tv_email_user);

        UserViewModel userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserViewModel.class);
        userViewModel.getUserLiveData().observe(this, user ->  {
            this.user = user;
            loadProfilePicFromUrl(imgPhoto, user.getImageUrl());
            tvUserName.setText(user.getUsername());
            tvUserEmail.setText(user.getEmail());
        });

        userViewModel.query(firebaseUser.getUid());
        userViewModel.addSnapshotListener(firebaseUser.getUid());

        ExpansionLayout expansionLayoutAbout = findViewById(R.id.expansionLayoutAbout);
        ExpansionLayout expansionLayoutOurTeam = findViewById(R.id.expansionLayoutOurTeam);
        ExpansionLayout expansionLayoutHelpCenter = findViewById(R.id.expansionLayoutHelpCenter);
        ExpansionLayout expansionLayoutLanguage = findViewById(R.id.expansionLayoutLanguage);
        expansionLayoutAbout.addListener(new ExpansionLayout.Listener() {
            @Override
            public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {

            }
        });
        expansionLayoutOurTeam.addListener(new ExpansionLayout.Listener() {
            @Override
            public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {

            }
        });
        expansionLayoutHelpCenter.addListener(new ExpansionLayout.Listener() {
            @Override
            public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {

            }
        });
        expansionLayoutLanguage.addListener(new ExpansionLayout.Listener() {
            @Override
            public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_edt_profile) {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("EXTRA_PROFILE", user);
            startActivity(intent);
        }else if (id == R.id.btn_logout) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_warning_delete)
                    .setMessage(R.string.do_you_want_log_out_from_yout_account)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            finish();
                        }
                    }).show();
        }else if (id == R.id.select_language){
            Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(languageIntent);
        }
    }
}