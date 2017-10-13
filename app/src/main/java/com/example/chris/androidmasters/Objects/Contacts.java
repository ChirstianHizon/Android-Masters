package com.example.chris.androidmasters.Objects;

/**
 * Created by chris on 13/10/2017.
 */

public class Contacts {

    private String id,name,position;

    public Contacts(){}

    public Contacts(String id,String name,String position){


    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
