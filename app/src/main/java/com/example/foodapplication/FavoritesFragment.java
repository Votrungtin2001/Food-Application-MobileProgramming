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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FoodManagementContract.CCategory.KEY_NAME
        };

        Spinner spinCategory = (Spinner) rootView.findViewById(R.id.spinCategory);
        ArrayList<String> result = new ArrayList<>();

        db.beginTransaction();
        Cursor cursor = db.query(FoodManagementContract.CCategory.KEY_NAME, projection, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String category = cursor.getString(
                    cursor.getColumnIndexOrThrow(FoodManagementContract.CCategory.KEY_NAME));
            result.add(category);
        }
        cursor.close();
        db.endTransaction();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, result);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapter);

        RecyclerView rvFavorites = (RecyclerView) rootView.findViewById(R.id.rvFavorites);
        FavoritesAdapter favAdapter = new FavoritesAdapter(favs);
        rvFavorites.setAdapter(favAdapter);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
