package com.example.foodapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {
    ArrayList<FavRestaurant> favs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FoodManagementContract.CCategory.KEY_NAME
        };

        Spinner spinCategory = (Spinner) findViewById(R.id.spinCategory);
        ArrayList<String> result = new ArrayList<>();

        Cursor cursor = db.query(FoodManagementContract.CCategory.KEY_NAME, projection, null, null, null, null, null, null);
        while(cursor.moveToNext()) {
            String category = cursor.getString(
                    cursor.getColumnIndexOrThrow(FoodManagementContract.CCategory.KEY_NAME));
            result.add(category);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, result);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapter);

        RecyclerView rvFavorites = (RecyclerView) findViewById(R.id.rvFavorites);
        FavoritesAdapter favAdapter = new FavoritesAdapter(favs);
        rvFavorites.setAdapter(favAdapter);
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
    }
}
