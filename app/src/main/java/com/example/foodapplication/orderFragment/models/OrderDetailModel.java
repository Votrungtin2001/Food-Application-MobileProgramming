package com.example.foodapplication.orderFragment.models;

import android.graphics.Bitmap;

public class OrderDetailModel {

    private Bitmap ImageProduct;
    public int Price, Quantity;
    public String Name;

    public OrderDetailModel(Bitmap imageProduct, String name, int price, int quantity) {
        ImageProduct = imageProduct;
        Name = name;
        Price = price;
        Quantity = quantity;
    }

    public Bitmap getImageProduct() {
        return ImageProduct;
    }

    public String getName() {
        return Name;
    }



    public int getPrice() {
        return Price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setImageProduct(Bitmap imageProduct) {
        ImageProduct = imageProduct;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void setPrice(int price) {
        Price = price;
    }
}

