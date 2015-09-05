package com.example.thong.playmusic.model;

import android.graphics.Bitmap;

/**
 * Created by thong on 28/08/2015.
 */

// save information file media
public class MediaInfo {
    private int id;
    private String path;
    private String name;
    private String idType;
    private String artist;
    private String singer;
    private String duration;
    private String imagePath;
    private String description;
    private String album;
    private String copyRight;
    private Bitmap bmMediaPlayer;

    public Bitmap getBmMediaPlayer() {
        return bmMediaPlayer;
    }

    public void setBmMediaPlayer(Bitmap bmMediaPlayer) {
        this.bmMediaPlayer = bmMediaPlayer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }
}
