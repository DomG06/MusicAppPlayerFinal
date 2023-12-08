package com.example.musicappplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerActivity extends AppCompatActivity {

    public final String TAG = "CPTR320";
    private final String PLAYER_POSITION_KEY = "CURRENT_POSITION";
    private final String PLAYER_STATE_KEY = "CURRENT_STATE";

    final MediaPlayer mediaPlayer = new MediaPlayer();
    private int currentPosition = 0;
    private boolean currentState = false;
    private boolean prepared = false;
    private String[] currentList, shuffledList, regularList;

    private SeekBar seekBar;
    ImageButton forwardButton, nextButton, pauseButton, playButton, previousButton, rewindButton;

    private Timer timer;
    private TimerTask task;
    private MusicDatabase dbase;

    private String[] array;
    private boolean isLooping, isShuffle;
    private View songBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);
        TextView textView = findViewById(R.id.textView);
        dbase = getIntent().getParcelableExtra(MusicApp.EXTRA_DBASE);
        regularList = dbase.getTitles();
        shuffledList = knuthShuffle(regularList);
        isLooping = getIntent().getBooleanExtra(MusicApp.EXTRA_LOOPING, false);
        isShuffle = getIntent().getBooleanExtra(MusicApp.EXTRA_SHUFFLE, false);
        Song song = dbase.getSelection();
        textView.setText(dbase.getSelection().getTitle());

        seekBar = findViewById(R.id.seekBar);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        forwardButton = findViewById(R.id.forwardButton);
        rewindButton = findViewById(R.id.rewindButton);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        songBanner = findViewById(R.id.imageView);

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        if (isShuffle)
            currentList = shuffledList;
        else
            currentList = regularList;

        setUpSeekBar();
        setUpButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPosition = getPreferences(MODE_PRIVATE).getInt(PLAYER_POSITION_KEY, 0);
        currentState = getPreferences(MODE_PRIVATE).getBoolean(PLAYER_STATE_KEY, false);
        Log.d(TAG, "current position = " + currentPosition
                + " , and current start is " + currentState);
        setUpMediaPlayer();
        setUpTimer();

    }

    private void setUpSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onSeekBar listener...");
                int current = seekBar.getProgress();
                mediaPlayer.seekTo(current);
            }
        });
    }

    private void setUpButtons() {
        forwardButton.setOnClickListener(view -> {
            Log.d(TAG, "Skip 10 seconds");
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
        });


        nextButton.setOnClickListener(view -> {
            Log.d(TAG, "Skip Song");
            String title = dbase.getSelection().getTitle();
            int index = 0;
            for (int i = 0; i < currentList.length; i++) {
                if (title.equals(currentList[i])) {
                    index = i;
                }
            }
            index++;
            if (isLooping) {
                if (index == currentList.length)
                    knuthShuffle(shuffledList);
                index %= currentList.length;
                dbase.setSelection(currentList[index]);
                setUpMediaPlayer();
            }
            if (index == currentList.length){
                    mediaPlayer.stop();
            }
            currentState = true;
        });

        playButton.setOnClickListener(view -> {
            Log.d(TAG, "Media player play requested...");
            if (prepared)
                mediaPlayer.start();
            else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Please wait!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        pauseButton.setOnClickListener(view -> {
            Log.d(TAG, "Pause requested...");
            if (prepared && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                currentState = false;
            }
        });

        previousButton.setOnClickListener(view -> {
            Log.d(TAG, "Go to previous Song");
            String title = dbase.getSelection().getTitle();
            int index = 0;
            for (int i = 0; i < currentList.length; i++) {
                if (title.equals(currentList[i])) {
                    index = i;
                }
            }
            index--;
            if (isLooping) {
                if (index == currentList.length)
                    knuthShuffle(shuffledList);
                index %= currentList.length;
                dbase.setSelection(currentList[index]);
                setUpMediaPlayer();
            }
            if (index == 0){
                mediaPlayer.stop();
            }
            currentState = true;
        });

        rewindButton.setOnClickListener(view -> {
            Log.d(TAG, "Restart Song");
            mediaPlayer.seekTo(0);
        });

    }

    private void setUpMediaPlayer() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Song song = dbase.getSelection();
                String title = song.getTitle();
                int currentIndex = dbase.getSelectionIndex();
                currentIndex++;
                if (isLooping) {
                    currentIndex %= currentList.length;
                    dbase.setSelection(currentList[currentIndex]);
                    setUpMediaPlayer();
                } else {
                    if (currentIndex == currentList.length)
                        mediaPlayer.stop();
                }
            }
        });
        mediaPlayer.setOnPreparedListener(mediaPlayer -> {
            Log.d(TAG, "onPrepared called");
            prepared = true;
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setMin(0);
           // mediaPlayer.seekTo(currentPosition);
            seekBar.setProgress(0);
            if (currentState)
                mediaPlayer.start();

        });
        Log.d(TAG, "In setUpMedia Player");
        Song song = dbase.getSong(dbase.getSelection().getTitle());
        songBanner.setBackground(null);
        songBanner.setBackground(getDrawable(song.getImageID()));
        TextView textView = findViewById(R.id.textView);
        textView.setText(dbase.getSelection().getTitle());
        int resourceID = dbase.getSelection().getResourceID();
        mediaPlayer.reset();
        AssetFileDescriptor afd = getResources().openRawResourceFd(resourceID);
        try {
            mediaPlayer.setDataSource(afd);
            mediaPlayer.prepareAsync();
            afd.close();
        } catch (IOException e) {
            Log.d(TAG, "Exception when setting data source!");
            Log.d(TAG, e.getMessage());
        }
    }

    private void setUpTimer() {
        task = new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 50, 200);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called...");

        getPreferences(MODE_PRIVATE).edit().putInt(PLAYER_POSITION_KEY,
                mediaPlayer.getCurrentPosition()).commit();
        getPreferences(MODE_PRIVATE).edit().putBoolean(PLAYER_STATE_KEY,
                mediaPlayer.isPlaying()).commit();
        prepared = false;
        mediaPlayer.reset();
    }

    private static String[] knuthShuffle(String[] keys) {
        String[] temp = Arrays.copyOf(keys,keys.length);
        Random rand = new Random();
        for (int i = 1; i < keys.length; i++) {
            swap(i, rand.nextInt(i + 1), temp);
        }
        return temp;
    }

    private int indexOf(String[] array, String key){
        for (int i = 0; i < array.length; i++){
            if (key.equals(array[i])){
                return i;
            }
        }
        return 0;
    }

    private static void swap(int i, int j, String[] keys) {
        String tmp = keys[i];
        keys[i] = keys[j];
        keys[j] = tmp;
    }

    private boolean isShuffle() {
        String[] titles = dbase.getTitles();
        knuthShuffle(titles);
        return true;
    }





    public void setShuffle() {
        if (isShuffle()) {

        }

    }
}