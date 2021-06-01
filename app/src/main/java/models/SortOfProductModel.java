package models;

import android.graphics.Bitmap;

public class SortOfProductModel {
    private int branch_id;
    private Bitmap image_product;
    private String name_product;
    private String name_branch;
    private int price;

    public SortOfProductModel(Bitmap Image, String NameProduct, String NameBranch, int Price, int id) {
        this.image_product = Image;
        this.name_product= NameProduct;
        this.name_branch = NameBranch;
        this.price = Price;
        this.branch_id = id;
    }

    public Bitmap getImage() {
        return image_product;
    }

    public String getNameProduct() {
        return name_product;
    }

    public String getNameBranch() {
        return name_branch;
    }

    public int getPrice() { return price;}

    public int getBranch_id() {return branch_id;}
}
