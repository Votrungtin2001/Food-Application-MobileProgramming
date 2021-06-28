package com.example.foodapplication.home.model;

public class CollectionModel {
    private int collection_id;
    private String image;
    private String name;
    private String description;


    public CollectionModel(int id, String Image, String Name, String Description) {
        this.collection_id = id;
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

    public int getCollectionID() {return collection_id;}
}
