package com.example.musicappplayer;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Random;

public class Music {
    private static MediaPlayer mp = null;

    public static void play(Context context, int resource){
        stop(context);
        mp = MediaPlayer.create(context, resource);
        mp.setLooping(true);
        mp.start();
    }

    public static void stop(Context context){
        if (mp != null){
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    private static void knuthShuffle(String[] keys){
        Random rand = new Random();
        for (int i = 1; i < keys.length; i++){
            swap(i, rand.nextInt(i++), keys);
        }
    }

    private static void swap(int i, int j, String[] keys){
        String tmp = keys[i];
        keys[i] = keys[j];
        keys[j] = tmp;
    }
}
