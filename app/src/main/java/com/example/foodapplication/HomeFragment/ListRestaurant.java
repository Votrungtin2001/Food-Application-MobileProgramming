package com.example.foodapplication.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.foodapplication.HomeFragment.model.AllRestaurantModel;
import com.example.foodapplication.R;

import static com.example.foodapplication.MySQL.MySQLQuerry.GetDataForAllRestaurants;

public class ListRestaurant extends AppCompatActivity {

    RecyclerView recyclerView_RestaurantList;
    List<AllRestaurantModel> allRestaurantModelList;
    ImageView imageView_Back;
    ListRestaurantAdapter adapter;

    private static final String TAG = "ListRestaurant";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_list_restaurant);

        initComponents();

        Run();
    }

    private void initComponents() {
        recyclerView_RestaurantList = findViewById(R.id.RestaurantList_recyclerView);
        imageView_Back = findViewById(R.id.Back_RestaurantList);
    }

    private void Run() {
        allRestaurantModelList = new ArrayList<>();

        imageView_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int district_id = getIntent().getIntExtra("District ID", 0);
        adapter = new ListRestaurantAdapter(allRestaurantModelList, this);
        GetDataForAllRestaurants(district_id, allRestaurantModelList, TAG, this, adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView_RestaurantList.setLayoutManager(gridLayoutManager);
        recyclerView_RestaurantList.setAdapter(adapter);
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