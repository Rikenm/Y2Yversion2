package com.rikenmaharjan.y2yc.utils;

/**
 * Created by bikenmaharjan on 5/25/18.
 */

public class Events {

    String title;
    String location;
    String time;

    public Events(){

    }

    public Events(String title, String location, String time){

        this.location = location;
        this.time = time;
        this.title = title;


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
}
