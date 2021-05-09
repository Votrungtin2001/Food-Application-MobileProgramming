package com.example.foodapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// where to open this fragment?
public class RestaurantFragment extends Fragment {
    ArrayList<MenuItem> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        RecyclerView rvMenu = view.findViewById(R.id.rvMenu);

        // again, this is a placeholder; need to add a check to create menu if it doesn't exist, or update if it does
        items = MenuItem.createMenu();
        MenuItemAdapter adapter = new MenuItemAdapter(items);
        rvMenu.setAdapter(adapter);
        rvMenu.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}
