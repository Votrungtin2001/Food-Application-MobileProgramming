package com.example.foodapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import fragments.RestaurantInformation_DatDon;
import fragments.RestaurantInformation_ThongTin;
import models.SearchBarModel;

public class RestaurantInformation extends AppCompatActivity {

    ImageView imageView_RestaurantInformation;
    TextView textView_BranchName;

    TabLayout tabLayout_RestaurantInformation;
    ArrayList<String> title_TabLayout = new ArrayList<>();
    ViewPager viewPager_RestaurantInformation;
    ViewPagerAdapter viewPagerAdapter;

    int branch_id;
    byte[] image_restaurant;
    String branch_name;
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    Bitmap bitmap_restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_information);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

        imageView_RestaurantInformation = findViewById(R.id.imageView_RestaurantInformation);
        branch_id = getIntent().getIntExtra("Branch ID", 0);
        image_restaurant = getRestaurantImage(branch_id);
        bitmap_restaurant = BitmapFactory.decodeByteArray(image_restaurant, 0, image_restaurant.length);
        imageView_RestaurantInformation.setImageBitmap(bitmap_restaurant);

        textView_BranchName = findViewById(R.id.BranchName_RestaurantInformation);
        branch_name = getBranchName(branch_id);
        textView_BranchName.setText(branch_name);

        tabLayout_RestaurantInformation = (TabLayout) findViewById(R.id.RestaurantInformation_TabLayout);
        viewPager_RestaurantInformation = (ViewPager) findViewById(R.id.RestaurantInformation_ViewPager);
        title_TabLayout.add("Đặt đơn");
        title_TabLayout.add("Thông tin");

        //Set tablayout
        tabLayout_RestaurantInformation.setupWithViewPager(viewPager_RestaurantInformation);
        //Prepare viewpager
        prepareViewPagerRestaurantInformation(viewPager_RestaurantInformation, title_TabLayout);

    }

    private void prepareViewPagerRestaurantInformation(ViewPager viewPager_restaurantInformation, ArrayList<String> title_tabLayout) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for(int i = 0; i < title_TabLayout.size(); i++)
        {
            if(i == 0) {
                RestaurantInformation_DatDon fragment1 = new RestaurantInformation_DatDon(branch_id);
                viewPagerAdapter.addFragment(fragment1, title_TabLayout.get(i));
            }

            if (i == 1) {
                RestaurantInformation_ThongTin fragment2 = new RestaurantInformation_ThongTin(branch_id);
                viewPagerAdapter.addFragment(fragment2, title_TabLayout.get(i));
            }

        }
        viewPager_RestaurantInformation.setAdapter(viewPagerAdapter);
    }

    public byte[] getRestaurantImage(int id) {
        byte[] img = null;
        String selectQuery = "SELECT R.Image FROM Restaurant R JOIN BRANCHES B ON R._id = B.Restaurant WHERE B._id ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                img = cursor.getBlob(cursor.getColumnIndex("Image"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return img;
    }

    public String getBranchName(int id) {
        String name = "";
        String selectQuery = "SELECT NAME FROM BRANCHES WHERE _id ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                name = cursor.getString(cursor.getColumnIndex("NAME"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return name;
    }
}