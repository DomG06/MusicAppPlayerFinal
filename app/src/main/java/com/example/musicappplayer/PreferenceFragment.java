package com.example.musicappplayer;

import androidx.preference.PreferenceFragmentCompat;

import android.os.Bundle;

public class PreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings, rootKey);
    }
}