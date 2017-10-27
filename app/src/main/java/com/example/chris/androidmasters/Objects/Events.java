package com.example.chris.androidmasters.Objects;

import java.util.Date;

/**
 * Created by chris on 25/10/2017.
 */

public class Events {

    private String id, name, location, description, project;
    private Date date_added, date_event;
    private Boolean volunteers;

    public Events() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Date getDate_added() {
        return date_added;
    }

    public void setDate_added(Date date_added) {
        this.date_added = date_added;
    }

    public Date getDate_event() {
        return date_event;
    }

    public void setDate_event(Date date_event) {
        this.date_event = date_event;
    }

    public Boolean getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(Boolean volunteers) {
        this.volunteers = volunteers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
