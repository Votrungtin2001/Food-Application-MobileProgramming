package com.example.foodapplication;

// adapter based on this guide: https://guides.codepath.com/android/using-the-recyclerview#defining-a-model

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
    private String sName, sDesc;
    private int iPrice;

    public MenuItem(String name, String desc, int price) {
        sName = name;
        sDesc = desc;
        iPrice = price;
    }

    public String getName() { return sName; }
    public String getDesc() { return sDesc; }
    public int getPrice() { return iPrice; }

    // this is only a placeholder class: requires connection to database to retrieve menu for each establishment
    public static ArrayList<MenuItem> createMenu() {
        return new ArrayList<MenuItem>();
    }
}

