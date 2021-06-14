package com.example.foodapplication;

import java.util.Date;

public class Transaction {
    private int credits;
    private String date;

    public Transaction(int credits, String date) {
        this.credits = credits;
        this.date = date;
    }

    public int getCredits() { return credits;}
    public String getDate() { return date; }
}
