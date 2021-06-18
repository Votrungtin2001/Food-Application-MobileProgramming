package com.example.foodapplication.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.example.foodapplication.HomeFragment.adapter.ListRestaurantAdapter;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.HomeFragment.model.AllRestaurantModel;
import com.example.foodapplication.R;

public class ListRestaurant extends AppCompatActivity {

    RecyclerView recyclerView_RestaurantList;
    List<AllRestaurantModel> allRestaurantModelList;
    ImageView imageView_Back;
    RecyclerView.Adapter adapter;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_list_restaurant);

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
            adapter = new ListRestaurantAdapter(allRestaurantModelList, this);
            GetDataForAllRestaurants(district_id);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            recyclerView_RestaurantList.setLayoutManager(gridLayoutManager);
            recyclerView_RestaurantList.setAdapter(adapter);
        }

    }

    private void GetDataForAllRestaurants(int id) {
        String selectQuery = "SELECT B._id, R.Image, B.NAME, A.Address FROM (RESTAURANT R JOIN BRANCHES B ON R._id = B.Restaurant) JOIN ADDRESS A ON B.Address = A._id WHERE A.District = '" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
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

    private void transparentStatusAndNavigation()
    {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        //Change status bar text to black when screen is light white
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}