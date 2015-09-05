package com.example.thong.playmusic.model;

import android.graphics.Bitmap;

/**
 * Created by thong on 04/09/2015.
 */
public class Album {
    private String albumName;
    private Bitmap imgPathAvatar;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Bitmap getImgPathAvatar() {
        return imgPathAvatar;
    }

    public void setImgPathAvatar(Bitmap imgPathAvatar) {
        this.imgPathAvatar = imgPathAvatar;
    }
}
