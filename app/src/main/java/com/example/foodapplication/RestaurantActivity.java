package com.example.foodapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity {
    ArrayList<MenuItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        RecyclerView rvMenu = (RecyclerView) findViewById(R.id.rvMenu);

        // again, this is a placeholder; need to add a check to create menu if it doesn't exist, or update if it does
        items = MenuItem.createMenu();
        MenuItemAdapter adapter = new MenuItemAdapter(items);
        rvMenu.setAdapter(adapter);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
    }
}
