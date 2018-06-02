package com.rikenmaharjan.y2yc.utils;

/**
 * Created by bikenmaharjan on 5/25/18.
 */

public class Events {

    String title;
    String location;
    String time;
    String ID;
    String description;
    Boolean isRSVP;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Events(){

    }

    public Events(String title, String location, String time,String ID,String description, Boolean RSVP,String date){

        this.location = location;
        this.time = time;
        this.title = title;
        this.ID = ID;
        this.description = description;
        this.isRSVP = RSVP;
        this.date = date;


    }


    // getter
    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getID() {
        return ID;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getRSVP() {
        return isRSVP;
    }

    //setter

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRSVP(Boolean RSVP) {
        isRSVP = RSVP;
    }

}
