package com.example.chris.androidmasters.Objects;

import java.util.ArrayList;

/**
 * Created by chris on 13/10/2017.
 */

public class Details {

    private String display_image,display_video,full_description,organization,short_description,title;
    private ArrayList<String> image;

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public String getDisplay_image() {
        return display_image;
    }

    public void setDisplay_image(String display_image) {
        this.display_image = display_image;
    }

    public String getDisplay_video() {
        return display_video;
    }

    public void setDisplay_video(String display_video) {
        this.display_video = display_video;
    }

    public String getFull_description() {
        return full_description;
    }

    public void setFull_description(String full_description) {
        this.full_description = full_description;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
