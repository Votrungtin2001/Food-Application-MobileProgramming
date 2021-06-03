package com.example.foodapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    ArrayList<FavRestaurant> favs;
    int customer_id = 0; //placeholder value, need to get user id from a passed bundle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        RecyclerView rvFavorites = (RecyclerView) rootView.findViewById(R.id.rvFavorites);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getFavorites(customer_id);
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CFavorites.KEY_RESTAURANT));
            Cursor resCursor = dbHelper.getRestaurant(id);
            favs.add(new FavRestaurant(resCursor.getString(resCursor.getColumnIndexOrThrow(FoodManagementContract.CRestaurant.KEY_NAME))));
            resCursor.close();
        }
        cursor.close();

        FavoritesAdapter favAdapter = new FavoritesAdapter(favs);
        rvFavorites.setAdapter(favAdapter);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
