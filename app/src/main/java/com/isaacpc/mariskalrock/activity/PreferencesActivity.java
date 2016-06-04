package com.isaacpc.mariskalrock.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.isaacpc.mariskalrock.R;

public class PreferencesActivity extends PreferenceActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {

                super.onCreate(savedInstanceState);

                // Load the preferences from an XML resource
                addPreferencesFromResource(R.xml.preferences);

                PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
                final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
                p.getString("key", "value_default");
        }





}
