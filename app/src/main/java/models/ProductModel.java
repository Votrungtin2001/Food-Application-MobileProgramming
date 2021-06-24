package models;

import android.graphics.Bitmap;

public class ProductModel {
    private String image_product;
    private String name_product;
    private String description_product;
    private String valueOfSell_product;
    private double price;

    public int quantity = 1;
    private int product_id;

    public ProductModel( String Name, int Quantity, double Price, int product_id) {
        this.name_product = Name;
        this.quantity = Quantity;
        this.price = Price;
        this.product_id = product_id;
    }

    public ProductModel(String Image, String Name, String Description_Product, String Value, double Price, int Product_ID) {
        this.image_product = Image;
        this.name_product= Name;
        this.description_product = Description_Product;
        this.valueOfSell_product = Value;
        this.price = Price;
        this.product_id = Product_ID;
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

    public int getQuantity() {return quantity;}

}
