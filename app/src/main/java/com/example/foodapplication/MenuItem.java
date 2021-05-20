package com.example.foodapplication;

// adapter based on this guide: https://guides.codepath.com/android/using-the-recyclerview#defining-a-model

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
    private String sName, sDesc;
    private int iPrice, id;

    public MenuItem(int id, String name, String desc, int price) {
        this.id = id;
        sName = name;
        sDesc = desc;
        iPrice = price;
    }

    public String getName() { return sName; }
    public String getDesc() { return sDesc; }
    public int getPrice() { return iPrice; }
    public int getId() { return id; }

    public static ArrayList<MenuItem> createMenu(int restaurant_id, Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Cursor cursor = dbHelper.getMenu(restaurant_id);

        ArrayList<MenuItem> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CMenu.KEY_PRODUCT)),
                    desc = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CMenu.KEY_DESC));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CMenu.KEY_PRICE));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CMenu._ID));

            list.add(new MenuItem(id, name, desc, price));
        }

        return list;
    }
}

