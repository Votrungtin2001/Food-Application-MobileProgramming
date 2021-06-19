package com.example.foodapplication.HomeFragmentMaster;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.HomeFragment.fragment.RestaurantInformation_ThongTin;
import com.example.foodapplication.HomeFragmentMaster.fragment.HomeFragment_Master_MonAn;
import com.example.foodapplication.R;
import com.example.foodapplication.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class HomeFragment_Master extends Fragment {

    int master_id;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    TextView textView_Annoucement;
    TextView textView_BranchName_HomeFragment_Master;
    TextView textView_Space1_HomeFragment_Master;

    ImageView imageView_No_Restaurant;
    ImageView imageView_ImageRestaurant_HomeFragment_Master;

    TabLayout tabLayout_HomeFragment_Master;

    ViewPager viewPager_HomeFragment_Master;

    ArrayList<String> title_TabLayout = new ArrayList<>();
    ViewPagerAdapter viewPagerAdapter;



    int branch_id = 0;

    public HomeFragment_Master() {
        // Required empty public constructor
    }

    public HomeFragment_Master(int master_id) {
        this.master_id = master_id;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home__master, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        initComponents(view);



        SetUpScreen();

        return view;
    }

    public void initComponents(View view) {
        textView_Annoucement = (TextView) view.findViewById(R.id.TextView_Announcement_Master);
        textView_BranchName_HomeFragment_Master = (TextView) view.findViewById(R.id.BranchName_HomeFragment_Master);
        textView_Space1_HomeFragment_Master = (TextView) view.findViewById(R.id.Space1_HomeFragmentMaster);

        imageView_No_Restaurant = (ImageView) view.findViewById(R.id.ImageView_NoRestaurant_Master);
        imageView_ImageRestaurant_HomeFragment_Master = (ImageView) view.findViewById(R.id.ImageRestaurant_HomeFragment_Master);

        tabLayout_HomeFragment_Master = (TabLayout) view.findViewById(R.id.TabLayout_HomeFragment_Master);

        viewPager_HomeFragment_Master = (ViewPager) view.findViewById(R.id.ViewPager_HomeFragment_Master);

    }


    public boolean CheckMasterHasRestaurant_Is_Right(int id) {
        int count = 0;
        String selectQuery = "SELECT _id FROM BRANCHES WHERE Master='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        count = cursor.getCount();
        cursor.close();

        if(count > 0) return true;
        else return false;
    }


    public void SetUpScreen() {
        boolean checkMasterHasRestaurant = CheckMasterHasRestaurant_Is_Right(master_id);
        if(checkMasterHasRestaurant == true) {
            textView_Annoucement.setVisibility(View.GONE);
            imageView_No_Restaurant.setVisibility(View.GONE);
            textView_Space1_HomeFragment_Master.setVisibility(View.VISIBLE);
            textView_BranchName_HomeFragment_Master.setVisibility(View.VISIBLE);
            imageView_ImageRestaurant_HomeFragment_Master.setVisibility(View.VISIBLE);
            tabLayout_HomeFragment_Master.setVisibility(View.VISIBLE);
            viewPager_HomeFragment_Master.setVisibility(View.VISIBLE);

            Run();

        }
        else {
            textView_Annoucement.setVisibility(View.VISIBLE);
            imageView_No_Restaurant.setVisibility(View.VISIBLE);
            textView_Space1_HomeFragment_Master.setVisibility(View.GONE);
            textView_BranchName_HomeFragment_Master.setVisibility(View.GONE);
            imageView_ImageRestaurant_HomeFragment_Master.setVisibility(View.GONE);
            tabLayout_HomeFragment_Master.setVisibility(View.GONE);
            viewPager_HomeFragment_Master.setVisibility(View.GONE);

        }

    }


    private void prepareViewPagerHomeFragmentMaster(ViewPager viewPager_HomeFragment_Master, ArrayList<String> title_tabLayout) {
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        for(int i = 0; i < title_tabLayout.size(); i++)
        {
            if(i == 0) {
                HomeFragment_Master_MonAn fragment1 = new HomeFragment_Master_MonAn(branch_id);
                viewPagerAdapter.addFragment(fragment1, title_tabLayout.get(i));
            }

            if (i == 1) {
                RestaurantInformation_ThongTin fragment2 = new RestaurantInformation_ThongTin(branch_id);
                viewPagerAdapter.addFragment(fragment2, title_tabLayout.get(i));
            }

        }
        viewPager_HomeFragment_Master.setAdapter(viewPagerAdapter);
    }

    public int getBranchID(int master_id) {
        int id = -1;
        String selectQuery = "SELECT * FROM BRANCHES WHERE Master ='" + master_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex("_id"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return id;
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

    public void Run() {
        branch_id = getBranchID(master_id);

        byte[] image_restaurant = getRestaurantImage(branch_id);
        Bitmap bitmap_restaurant = BitmapFactory.decodeByteArray(image_restaurant, 0, image_restaurant.length);
        imageView_ImageRestaurant_HomeFragment_Master.setImageBitmap(bitmap_restaurant);


        String branch_name = getBranchName(branch_id);
        textView_BranchName_HomeFragment_Master.setText(branch_name);

        title_TabLayout.add("Món ăn");
        title_TabLayout.add("Thông tin");


        tabLayout_HomeFragment_Master.setupWithViewPager(viewPager_HomeFragment_Master);
        prepareViewPagerHomeFragmentMaster(viewPager_HomeFragment_Master, title_TabLayout);
    }


}