package com.example.thong.playmusic.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thong on 9/11/15.
 */
public class ChildMusicOnline {

    @SerializedName(value = "streamable")
    private boolean streamAble;

    @SerializedName(value = "downloadable")
    private boolean downloadAble;

    private String title;

    @SerializedName(value = "stream_url")
    private String urlStream;

    @SerializedName(value = "artwork_url")
    private String urlAvatar;

    @SerializedName(value = "download_url")
    private String urlDownLoad;

    private String artist;

    private long duration;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isStreamAble() {
        return streamAble;
    }

    public void setStreamAble(boolean streamAble) {
        this.streamAble = streamAble;
    }

    public boolean isDownloadAble() {
        return downloadAble;
    }

    public void setDownloadAble(boolean downloadAble) {
        this.downloadAble = downloadAble;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlStream() {
        return urlStream;
    }

    public void setUrlStream(String urlStream) {
        this.urlStream = urlStream;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getUrlDownLoad() {
        return urlDownLoad;
    }

    public void setUrlDownLoad(String urlDownLoad) {
        this.urlDownLoad = urlDownLoad;
    }
}
