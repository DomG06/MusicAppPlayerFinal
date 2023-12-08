package com.example.musicappplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.os.Bundle;

public class PreferenceActivity extends AppCompatActivity {

    private static final String OPT_MUSIC = "music";
    private static final boolean OPT_MUSIC_DEF = true;
    private static final String OPT_LOOP = "loop";
    private static final boolean OPT_LOOP_DEF = true;
    private static final String OPT_SHUFFLE = "shuffle";
    private static final boolean OPT_SHUFFLE_DEF = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        if (findViewById(R.id.settings_container) != null){
            if (savedInstanceState != null){
                return;
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.settings_container, new PreferenceFragment()).commit();
        }
    }

    public static boolean getMusic(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(OPT_MUSIC, OPT_MUSIC_DEF);
    }

    public static boolean getLoop(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(OPT_LOOP, OPT_LOOP_DEF);
    }

    public static boolean getShuffle(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(OPT_SHUFFLE, OPT_SHUFFLE_DEF);
    }
   
}