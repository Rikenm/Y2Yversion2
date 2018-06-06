package com.rikenmaharjan.y2yc.utils;

/**
 * Created by bikenmaharjan on 6/5/18.
 */

public class HelperIdModel {

    private String id;
    private int index;

    public HelperIdModel(String id, int index) {
        this.id = id;
        this.index = index;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public String getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }
}
