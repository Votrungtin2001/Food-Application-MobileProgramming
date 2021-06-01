package models;

import android.graphics.Bitmap;

public class ProductModel {
    private Bitmap image_product;
    private String name_product;
    private String description_product;
    private String valueOfSell_product;
    private int price;

    public ProductModel(Bitmap Image, String Name, String Description_Product, String Value, int Price) {
        this.image_product = Image;
        this.name_product= Name;
        this.description_product = Description_Product;
        this.valueOfSell_product = Value;
        this.price = Price;
    }

    public Bitmap getImage() {
        return image_product;
    }

    public String getNameProduct() {
        return name_product;
    }

    public String getDescriptionProduct() {
        return description_product;
    }

    public String getValueOfSell() {return valueOfSell_product;}

    public int getPrice() { return price;}
}
