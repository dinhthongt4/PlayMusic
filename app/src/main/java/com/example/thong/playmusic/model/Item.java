package com.example.thong.playmusic.model;

/**
 * Created by thong on 9/7/15.
 */
public class Item {

    private String type;
    private Id id;
    private Snippet snippet;
    private int headerID;
    private int typeItem;
    private int numberList;

    public int getNumberList() {
        return numberList;
    }

    public void setNumberList(int numberList) {
        this.numberList = numberList;
    }

    public int getTypeItem() {
        return typeItem;
    }

    public void setTypeItem(int typeItem) {
        this.typeItem = typeItem;
    }

    public int getHeaderID() {
        return headerID;
    }

    public void setHeaderID(int headerID) {
        this.headerID = headerID;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}



