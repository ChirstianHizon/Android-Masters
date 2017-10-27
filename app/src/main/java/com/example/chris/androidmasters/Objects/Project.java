package com.example.chris.androidmasters.Objects;

import android.net.Uri;

import java.util.Date;

/**
 * Created by chris on 08/10/2017.
 */

public class Project {

    private String id, name, description, organization, image, logo, current, goal;
    private Date completion_date, insert_date;
    private Uri uriimage;

    public Project() {

    }

    public Project(String name, String desc, String org, Date date, String goal, Uri img) {
        this.name = name;
        this.description = desc;
        this.organization = org;
        this.completion_date = date;
        this.goal = goal;
        this.uriimage = img;
    }

    public Project(String id, String name, String desc, String date, String organization, String image, String logo, String current, String goal) {
        this.id = id;
        this.name = name;
        this.description = desc;
        this.organization = organization;
        this.image = image;
        this.logo = logo;
        this.current = current;
        this.logo = logo;
        this.goal = goal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Date getCompletion_date() {
        return completion_date;
    }

    public void setCompletion_date(Date completion_date) {
        this.completion_date = completion_date;
    }

    public Date getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(Date insert_date) {
        this.insert_date = insert_date;
    }

    public Uri getUriimage() {
        return uriimage;
    }

    public void setUriimage(Uri uriimage) {
        this.uriimage = uriimage;
    }

}
