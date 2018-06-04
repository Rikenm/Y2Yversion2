package com.rikenmaharjan.y2yc.utils;

import java.util.ArrayList;

/**
 * Created by bikenmaharjan on 6/3/18.
 */

public class ActionModel {

    static private ArrayList<SubAction> subAction;
    private String id;
    private int numOfSubAction;
    private String title;
    private Boolean isComplete;
    private Boolean isDrop;

    public ActionModel(){
    }

    public ActionModel(ArrayList<SubAction> subAction, String id, int numOfSubAction, String title, Boolean isComplete, Boolean isDrop) {
        this.subAction = subAction;
        this.id = id;
        this.numOfSubAction = numOfSubAction;
        this.title = title;
        this.isComplete = isComplete;
        this.isDrop = isDrop;
    }

    //getter

    public ArrayList<SubAction> getSubAction() {
        return subAction;
    }

    public String getId() {
        return id;
    }

    public int getNumOfSubAction() {
        return numOfSubAction;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public Boolean getDrop() {
        return isDrop;
    }

    // setter

    public void setSubAction(ArrayList<SubAction> subAction) {
        this.subAction = subAction;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNumOfSubAction(int numOfSubAction) {
        this.numOfSubAction = numOfSubAction;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public void setDrop(Boolean drop) {
        isDrop = drop;
    }
}



