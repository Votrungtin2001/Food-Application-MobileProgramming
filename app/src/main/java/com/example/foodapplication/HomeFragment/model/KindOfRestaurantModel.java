package com.example.foodapplication.HomeFragment.model;

import android.graphics.Bitmap;

public class KindOfRestaurantModel {
    private String image_restaurant;
    private String name_branch;
    private String address_branch;
    private String openingtime_restaurant;
    private int branch_id;

    public KindOfRestaurantModel(String Image, String Name, String Address, String OpeningTime, int id) {
        this.image_restaurant = Image;
        this.name_branch= Name;
        this.address_branch = Address;
        this.openingtime_restaurant = OpeningTime;
        this.branch_id = id;
    }

    public String getImage() {
        return image_restaurant;
    }

    public String getNameBranch() {
        return name_branch;
    }

    public String getAddressBranch() {
        return address_branch;
    }

    public String getOpeningTime() {return openingtime_restaurant;}

    public int getBranchID() {return  branch_id;}
}
