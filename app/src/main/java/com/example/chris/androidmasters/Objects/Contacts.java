package com.example.chris.androidmasters.Objects;

/**
 * Created by chris on 13/10/2017.
 */

public class Contacts {

    private String id, name, position, contact;

    public Contacts() {
    }

    public Contacts(String name, String position, String contaact) {

        this.name = name;
        this.position = position;
        this.contact = contaact;

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
