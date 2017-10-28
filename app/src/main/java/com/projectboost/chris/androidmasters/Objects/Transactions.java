package com.projectboost.chris.androidmasters.Objects;

/**
 * Created by chris on 12/10/2017.
 */

public class Transactions {

    private String id, date, amount;

    public Transactions() {
    }

    public Transactions(String id, String date, String amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
