package com.rechit.metime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.rechit.metime.R;
import com.rechit.metime.view.ui.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int waktu_loading = 2000;
        new Handler().postDelayed(() -> {

            //setelah loading maka akan langsung berpindah ke Main activity
            Intent home=new Intent(SplashActivity.this, WelcomeActivity.class);
            startActivity(home);
            finish();

        }, waktu_loading);

    }
}