package com.example.chris.androidmasters.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 13/10/2017.
 */

public class Details {

    private String display_image,display_video,full_description,organization,short_description,title,logo;
    private List<String>images,objectives;

    public Details(){}

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

    public List<String> getImages() {
        return images;
    }

    public String getSelectedImages(int pos) {
        if(pos >= images.size()){
            return null;
        }else {
            return images.get(pos);
        }
    }
    public int getImagesSize(){
        return images.size();
    }

    public void setImages(List<String> images) {
        this.images = new ArrayList<String>();
        this.images = images;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<String> getObjectives() {

        if(objectives == null){
            return  null;
        } else if(objectives.isEmpty()){
            return null;
        }else{
            return objectives;
        }
    }

    public void setObjectives(List<String> objectives) {

        this.objectives = new ArrayList<String>();
        this.objectives = objectives;
    }
}
