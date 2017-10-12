package com.example.chris.androidmasters.Objects;

/**
 * Created by chris on 08/10/2017.
 */

public class Project {

    private String id,name,description,date,organization,image,logo,current,goal;

    public Project(){

    }
    public Project(String id,String name,String desc,String date,String organization,String image,String logo,String current,String goal){
        this.id = id;
        this.name = name;
        this.description = desc;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
