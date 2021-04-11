package com.rechit.metime.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rechit.metime.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MeTime);
        setContentView(R.layout.activity_main);
    }
}