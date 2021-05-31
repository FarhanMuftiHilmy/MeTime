package com.rechit.metime.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME = "introslider";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    @SuppressLint("CommitPrefEdits")
    PrefManager(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
