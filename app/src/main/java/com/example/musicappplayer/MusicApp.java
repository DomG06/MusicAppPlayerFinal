package com.example.musicappplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MusicApp extends AppCompatActivity {

    private final String TAG = "CPTR320";
    public final static String EXTRA_DBASE = "DBASE";
    public final static String EXTRA_LOOPING = "LOOPING";
    public final static String EXTRA_SHUFFLE = "SHUFFLE";

    private View currentSelection = null;
    private MusicDatabase dbase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.play_list);
        dbase = new MusicDatabase(MusicApp.this);

        PlayList playList = new PlayList(this, android.R.layout.simple_list_item_1,
                dbase.getTitles());
        listView.setAdapter(playList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        String title = (String) parent.getItemAtPosition(position);
                        currentSelection = view;

                        dbase.setSelection(title);
                        Log.d(TAG, "Index clicked is " + position + " set to " + title);
                        Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                        intent.putExtra(EXTRA_DBASE, dbase);
                        intent.putExtra(EXTRA_LOOPING, PreferenceActivity.getLoop(MusicApp.this));
                        intent.putExtra(EXTRA_SHUFFLE, PreferenceActivity.getShuffle(MusicApp.this));

                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent i = new Intent(this, PreferenceActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.about) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentSelection != null) {
            currentSelection.setAlpha(1.0f);
        }
    }



}
