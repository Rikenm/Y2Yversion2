package com.rikenmaharjan.y2yc.utils;

/**
 * Created by bikenmaharjan on 6/3/18.
 */

/////
public class SubAction {

    private String title;
    private String id;
    private Boolean isComplete;


    public SubAction(String title, String id, Boolean isComplete) {
        this.title = title;
        this.id = id;
        this.isComplete = isComplete;
    }

    // Deep Copy
    public SubAction(SubAction other){

        this.title = other.getTitle();
        this.id = other.getId();
        this.isComplete = other.getComplete();

    }


    // getter

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    // setter

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }
}
/////
