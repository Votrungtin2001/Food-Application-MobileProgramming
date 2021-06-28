package com.example.foodapplication.orderFragment.models;

import android.graphics.Bitmap;

public class OrderDetailModel {

    private String ImageProduct;
    public int Quantity, id;
    public double Price;
    public String Name;

    public OrderDetailModel(int id, String imageProduct, String name, double price, int quantity) {
        this.id = id;
        this.ImageProduct = imageProduct;
        this.Name = name;
        this.Price = price;
        this.Quantity = quantity;
    }

    public String getImageProduct() {
        return ImageProduct;
    }

    public String getName() {
        return Name;
    }

    public int getID() {return id;}

    public double getPrice() {
        return Price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setImageProduct(String imageProduct) {
        ImageProduct = imageProduct;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void setPrice(double price) {
        Price = price;
    }
}

