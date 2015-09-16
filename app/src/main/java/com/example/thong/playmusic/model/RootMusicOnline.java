package com.example.thong.playmusic.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by thong on 9/11/15.
 */
public class RootMusicOnline {

    @SerializedName(value = "collection")
    ArrayList<ChildMusicOnline> childMusicOnlines;

    public ArrayList<ChildMusicOnline> getChildMusicOnlines() {
        return childMusicOnlines;
    }

    public void setChildMusicOnlines(ArrayList<ChildMusicOnline> childMusicOnlines) {
        this.childMusicOnlines = childMusicOnlines;
    }
}
