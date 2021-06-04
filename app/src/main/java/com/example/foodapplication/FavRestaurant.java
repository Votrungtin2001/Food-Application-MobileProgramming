package com.example.foodapplication;

import java.util.ArrayList;

public class FavRestaurant {
    private String sName;
    private float fDistance, fETA;

    public FavRestaurant(String name) {
        sName = name;
        fDistance = fETA = 0.0f;
    }

    public FavRestaurant(String name, float dist, float ETA) {
        sName = name;
        fDistance = dist;
        fETA = ETA;
    }

    public String getName() { return sName; }
    public float getDistance() { return fDistance; }
    public float getETA() { return fETA; }

    // again, a placeholder function. need to implement a way to obtain the user's personal preferences.
    // perhaps we should implement a secondary, local database?
    public static ArrayList<FavRestaurant> createFavs() {
        return new ArrayList<FavRestaurant>();
    }
}
