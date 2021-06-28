package com.example.foodapplication.home.model;

public class SearchBarModel {
    private String image;
    private String text;
    private int branch_id;

    public SearchBarModel(String Image, String Text, int id) {
        this.image = Image;
        this.text = Text;
        this.branch_id = id;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public int getBranch_id() {return branch_id;}

}
