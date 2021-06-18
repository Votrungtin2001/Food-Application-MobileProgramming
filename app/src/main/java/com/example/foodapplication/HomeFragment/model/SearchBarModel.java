package com.example.foodapplication.HomeFragment.model;
import android.graphics.Bitmap;

public class SearchBarModel {
    private Bitmap image;
    private String text;
    private int branch_id;

    public SearchBarModel(Bitmap Image, String Text, int id) {
        this.image = Image;
        this.text = Text;
        this.branch_id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public int getBranch_id() {return branch_id;}

}
