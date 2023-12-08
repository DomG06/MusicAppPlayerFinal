package com.example.musicappplayer;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

public class MusicDatabase implements Parcelable {

    private String selection;
    private Context context;
    private ArrayList<String> titles = new ArrayList<>();
    private HashMap<String, Song> database = new HashMap<String, Song>();


    public MusicDatabase(Context context){
        String title = context.getString(R.string.title1);
        Song song = new Song(title, R.drawable.bizarre_monkey, R.raw.bizarre);
        database.put(title, song);
        titles.add(title);

        title = context.getString(R.string.title2);
        song = new Song(title, R.drawable.beach, R.raw.beachfrontcelebration);
        database.put(title, song);
        titles.add(title);

        title = context.getString(R.string.title3);
        song = new Song(title, R.drawable.rio_grande, R.raw.delriobravo);
        database.put(title, song);
        titles.add(title);

        title = context.getString(R.string.title4);
        song = new Song(title, R.drawable.verano, R.raw.veranosensual);
        database.put(title, song);
        titles.add(title);

        title = context.getString(R.string.title5);
        song = new Song(title, R.drawable.radio, R.raw.latenightradio);
        database.put(title, song);
        titles.add(title);

        this.context = context;
    }


    public String[] getTitles(){
        return titles.toArray(new String[0]);
    }

    public Context getContext(){
        return context;
    }

    public void setSelection(String title){
        this.selection = title;
    }

    protected MusicDatabase(Parcel in) {
        selection = in.readString();
        titles = in.readArrayList(getClass().getClassLoader());
        database = in.readHashMap(getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(selection);
        dest.writeList(titles);
        dest.writeMap(database);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MusicDatabase> CREATOR = new Creator<MusicDatabase>() {
        @Override
        public MusicDatabase createFromParcel(Parcel in) {
            return new MusicDatabase(in);
        }

        @Override
        public MusicDatabase[] newArray(int size) {
            return new MusicDatabase[size];
        }
    };

    public int getSelectionIndex(){
        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).equals(selection))
                return i;
        }
        return -1;
    }

    public Song getSong(String title) {
        return database.get(title);
    }

    public Song getSelection() {
        return getSong(selection);
    }

}
