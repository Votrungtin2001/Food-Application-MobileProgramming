package com.example.foodapplication.HomeFragment.model;

import android.graphics.Bitmap;

public class ProductCategoryModel {
    private String image_product;
    private String name_product;
    private String description_product;
    private String name_branch;

    private int branch_id;

    public ProductCategoryModel(String Image, String Name, String Description_Product, String Name_Branch, int Branch_ID){
        this.image_product = Image;
        this.name_product= Name;
        this.description_product = Description_Product;
        this.name_branch = Name_Branch;
        this.branch_id = Branch_ID;
    }

    public String getImage() {
        return image_product;
    }

    public String getNameProduct() {
        return name_product;
    }

    public String getDescriptionProduct() {
        return description_product;
    }

    public String getNameBranch() {return name_branch;}

    public int getBranchId() {return branch_id;}
}
