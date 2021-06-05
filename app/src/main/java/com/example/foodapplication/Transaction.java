package com.example.foodapplication;

import java.util.Date;

public class Transaction {
    private int credits;
    private Date date;

    public Transaction(int credits, Date date) {
        this.credits = credits;
        this.date = date;
    }

    public int getCredits() { return credits;}
    public Date getDate() { return date;}
}
