package com.rikenmaharjan.y2yc.utils;

import java.util.ArrayList;

/**
 * Created by bikenmaharjan on 6/3/18.
 */

public class ActionModel {

    private SubAction [] subAction;
    private String id;
    private int numOfSubAction;
    private String title;
    private Boolean isComplete;
    private Boolean isDrop;



    public ActionModel(SubAction [] subAction, String id, int numOfSubAction, String title, Boolean isComplete, Boolean isDrop) {

        this.subAction = subAction;
        this.id = id;
        this.numOfSubAction = numOfSubAction;
        this.title = title;
        this.isComplete = isComplete;
        this.isDrop = isDrop;
    }


    public ActionModel(ActionModel otherAction){

        this.subAction = otherAction.getSubAction();
        this.id = otherAction.getId();
        this.numOfSubAction = otherAction.getNumOfSubAction();
        this.title = otherAction.getTitle();
        this.isComplete = otherAction.getComplete();
        this.isDrop = otherAction.getDrop();


    }

    //getter

    public SubAction [] getSubAction() {
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

    public void setSubAction(SubAction []  subAction) {
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



