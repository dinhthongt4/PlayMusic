package com.example.thong.playmusic.model;

import android.graphics.Bitmap;

/**
 * Created by thong on 04/09/2015.
 */
public class Album {
    private String albumName;
    private int idAlbum;

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getAlbumName() {
        return albumName;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
