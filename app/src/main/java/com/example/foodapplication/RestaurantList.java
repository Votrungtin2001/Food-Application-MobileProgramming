package com.example.foodapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.ItemListCollectionAdapter;
import adapter.RestaurantListAdapter;
import models.AllRestaurantModel;
import models.CollectionModel;

public class RestaurantList extends AppCompatActivity {

    RecyclerView recyclerView_RestaurantList;
    List<AllRestaurantModel> allRestaurantModelList;
    ImageView imageView_Back;
    RecyclerView.Adapter adapter;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

        recyclerView_RestaurantList = findViewById(R.id.RestaurantList_recyclerView);
        allRestaurantModelList = new ArrayList<>();

        imageView_Back = findViewById(R.id.Back_RestaurantList);
        imageView_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int district_id = getIntent().getIntExtra("District ID", 0);
        String name_activity = getIntent().getStringExtra("Name Activity");

        if (name_activity.trim().equals("All Restaurants")) {
            adapter = new RestaurantListAdapter(allRestaurantModelList, this);
            GetDataForAllRestaurants(district_id);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            recyclerView_RestaurantList.setLayoutManager(gridLayoutManager);
            recyclerView_RestaurantList.setAdapter(adapter);
        }

    }

    private void GetDataForAllRestaurants(int id) {
        String selectQuery = "SELECT B._id, R.Image, B.NAME, A.Address FROM (RESTAURANT R JOIN BRANCHES B ON R._id = B.Restaurant) JOIN ADDRESS A ON B.Address = A._id WHERE A.District = '" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                int branch_id = cursor.getInt(cursor.getColumnIndex("_id"));
                byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                String name_branch = cursor.getString(cursor.getColumnIndex("NAME"));
                String address_branch = cursor.getString(cursor.getColumnIndex("Address"));
                AllRestaurantModel allRestaurantModel = new AllRestaurantModel(bitmap,name_branch,address_branch, branch_id);
                allRestaurantModelList.add(allRestaurantModel);
            } while (cursor.moveToNext());

        }
        cursor.close();
    }
}