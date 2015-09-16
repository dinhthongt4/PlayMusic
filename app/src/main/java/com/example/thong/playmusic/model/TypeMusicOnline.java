package com.example.thong.playmusic.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by thong on 9/11/15.
 */
public class TypeMusicOnline implements ParentObject {


    private List<Object> childrenList;

    @SerializedName(value = "tracks")
    private List<ChildMusicOnline> childMusicOnlines;

    @SerializedName(value = "permalink")
    private String typeMusicOnline;

    private boolean isExpanded;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    @Override
    public List<Object> getChildObjectList() {
        return childrenList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public void setChildObjectList(List<Object> list) {
        childrenList = list;
    }

    public String getTypeMusicOnline() {
        return typeMusicOnline;
    }

    public void setTypeMusicOnline(String typeMusicOnline) {
        this.typeMusicOnline = typeMusicOnline;
    }

    public List<ChildMusicOnline> getChildMusicOnlines() {
        return childMusicOnlines;
    }

    public void setChildMusicOnlines(List<ChildMusicOnline> childMusicOnlines) {
        this.childMusicOnlines = childMusicOnlines;
    }
}
