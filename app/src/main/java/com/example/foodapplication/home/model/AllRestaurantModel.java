package com.example.foodapplication.home.model;

public class AllRestaurantModel {
    private int branch_id;
    private String image_restaurant;
    private String name_branch;
    private String address_branch;


    public AllRestaurantModel(int id, String Image, String Name, String Address) {
        this.branch_id = id;
        this.image_restaurant = Image;
        this.name_branch= Name;
        this.address_branch = Address;
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

    public int getBranchID() {return  branch_id;}
}
