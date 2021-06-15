package com.rechit.metime.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rechit.metime.R;
import com.rechit.metime.view.adapter.MainPagerAdapter;

public class MainActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.vp_main);
        bottomNavigationView = findViewById(R.id.bn_main);
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.dashboard_menu);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.calendar_menu);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.time_menu);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.note_menu);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.dashboard_menu:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.calendar_menu:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.time_menu:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.note_menu:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });



    }

}

