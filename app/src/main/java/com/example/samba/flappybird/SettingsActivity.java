package com.example.samba.flappybird;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by samue on 10/03/2018.
 */

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}