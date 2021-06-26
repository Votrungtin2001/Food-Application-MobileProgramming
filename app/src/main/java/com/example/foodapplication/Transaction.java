package com.example.foodapplication;

import java.util.Date;

public class Transaction {
    private double credits;
    private String date;

    public Transaction(double credits, String date) {
        this.credits = credits;
        this.date = date;
    }

    public double getCredits() { return credits;}
    public String getDate() { return date; }
}
