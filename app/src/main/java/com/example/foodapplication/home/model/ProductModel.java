package com.example.foodapplication.home.model;

public class ProductModel {
    private String image_product;
    private String name_product;
    private String description_product;
    private String valueOfSell_product;
    private double price;
    private int menu_id;

    public int quantity;
    private int product_id;

    public ProductModel( String Name, int Quantity, double Price, int product_id, int Menu_id) {
        this.name_product = Name;
        this.quantity = Quantity;
        this.price = Price;
        this.product_id = product_id;
        this.menu_id = Menu_id;
    }

    public ProductModel(String Image, String Name, String Description_Product, String Value, double Price, int Product_ID, int Menu_id) {
        this.image_product = Image;
        this.name_product= Name;
        this.description_product = Description_Product;
        this.valueOfSell_product = Value;
        this.price = Price;
        this.product_id = Product_ID;
        this.menu_id = Menu_id;
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

    public String getValueOfSell() {return valueOfSell_product;}

    public double getPrice() { return price;}

    public int getProduct_id() {return product_id;}

    public int getMenu_id() {return menu_id;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int Quantity) {this.quantity = Quantity;}

}
