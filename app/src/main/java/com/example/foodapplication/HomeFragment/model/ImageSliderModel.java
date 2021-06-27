package com.example.foodapplication.HomeFragment.model;

public class ImageSliderModel {
    private int imageslider_id;
    private String image;
    private String name;
    private String description;


    public ImageSliderModel(int id, String Image, String Name, String Description) {
        this.imageslider_id = id;
        this.image = Image;
        this.name= Name;
        this.description = Description;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageSliderID() {return imageslider_id;}
}
