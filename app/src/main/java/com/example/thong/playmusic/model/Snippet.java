package com.example.thong.playmusic.model;

/**
 * Created by thong on 9/8/15.
 */
public class Snippet {
    private String publishAt;
    private String channelID;
    private String title;
    private Thumbnail thumbnails;
    private String channelTitle;

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(String publishAt) {
        this.publishAt = publishAt;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Thumbnail getThumbnail() {
        return thumbnails;
    }

    public void setThumbnail(Thumbnail thumbnails) {
        this.thumbnails = thumbnails;
    }
}
