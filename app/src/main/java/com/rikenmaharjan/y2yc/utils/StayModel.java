package com.rikenmaharjan.y2yc.utils;

import android.media.Image;

/**
 * Created by bikenmaharjan on 5/29/18.
 */

public class StayModel {

    private String title;
    private String subtitle;
    //private Image image;

    public StayModel(){

    }

    public StayModel(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
        //this.image = image;
    }

    // setter

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

//    public void setImage(Image image) {
//        this.image = image;
//    }


    //getter

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

//    public Image getImage() {
//        return image;
//    }
}
