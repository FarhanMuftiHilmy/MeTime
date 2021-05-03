package com.rechit.metime.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rechit.metime.view.ui.CalendarFragment;
import com.rechit.metime.view.ui.DashboardFragment;
import com.rechit.metime.view.ui.NoteFragment;
import com.rechit.metime.view.ui.TimeFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new DashboardFragment();
                break;
            case 1:
                fragment = new CalendarFragment();
                break;
            case 2:
                fragment = new TimeFragment();
                break;
            case 3:
                fragment = new NoteFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}

