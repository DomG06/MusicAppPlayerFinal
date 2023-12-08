package com.example.musicappplayer;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Song implements Parcelable {

    private int resourceID;
    private int imageID;
    private String title;

    public Song(String title, int imageID, int songID) {
        this.title = title;
        this.imageID = imageID;
        this.resourceID = songID;
    }

    protected Song(Parcel in){
        resourceID = in.readInt();
        imageID = in.readInt();
        title = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };


    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags){
        dest.writeInt(resourceID);
        dest.writeInt(imageID);
        dest.writeString(title);
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
