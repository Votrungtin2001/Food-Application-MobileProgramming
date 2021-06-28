package com.example.foodapplication.home.model;

public class SortOfProductModel {
    private int branch_id;
    private String image_product;
    private String name_product;
    private String name_branch;
    private double price;

    public SortOfProductModel(String Image, String NameProduct, String NameBranch, double Price, int id) {
        this.image_product = Image;
        this.name_product= NameProduct;
        this.name_branch = NameBranch;
        this.price = Price;
        this.branch_id = id;
    }

    public String getImage() {
        return image_product;
    }

    public String getNameProduct() {
        return name_product;
    }

    public String getNameBranch() {
        return name_branch;
    }

    public double getPrice() { return price;}

    public int getBranch_id() {return branch_id;}
}
