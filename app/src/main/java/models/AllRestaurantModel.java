package models;

import android.graphics.Bitmap;

public class AllRestaurantModel {
    private Bitmap image_restaurant;
    private String name_branch;
    private String address_branch;
    private int branch_id;

    public AllRestaurantModel(Bitmap Image, String Name, String Address, int id) {
        this.image_restaurant = Image;
        this.name_branch= Name;
        this.address_branch = Address;
        this.branch_id = id;
    }

    public Bitmap getImage() {
        return image_restaurant;
    }

    public String getNameBranch() {
        return name_branch;
    }

    public String getAddressBranch() {
        return address_branch;
    }

    public int getBranchID() {return  branch_id;}
}
