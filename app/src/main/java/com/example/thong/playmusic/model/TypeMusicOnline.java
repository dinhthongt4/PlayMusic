
package com.example.thong.playmusic.model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class TypeMusicOnline implements Serializable{

    private ArrayList<Tracks> tracks;
    private String permalink;

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public ArrayList<Tracks> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Tracks> tracks) {
        this.tracks = tracks;
    }
}


