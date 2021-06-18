package com.example.foodapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragments.FoodFragment_Master;
import fragments.HomeFragment_Master;
import fragments.HomeFragment_Master_DatDon;
import com.example.foodapplication.HomeFragment.fragment.RestaurantInformation_ThongTin;
import fragments.UpdateFragment_Master;

import static com.example.foodapplication.MainActivity.master_id;
import static com.example.foodapplication.MainActivity.addressLine;
import static com.example.foodapplication.MainActivity.nameStreet;
import static com.example.foodapplication.MainActivity.district_id;

public class Master_MainActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    HomeFragment_Master homeFragment;
    UpdateFragment_Master updateFragment;
    FoodFragment_Master foodFragment;

    public static HomeFragment_Master_DatDon fragment1;
    public static RestaurantInformation_ThongTin fragment2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_master__main);

        initComponents();

        Run();
    }

    public void initComponents() {
        navigation = findViewById(R.id.bottom_nav_bar_master);
    }

    public void Run() {
        master_id = getIntent().getIntExtra("Master ID", -1);
        addressLine = getIntent().getStringExtra("AddressLine");
        nameStreet = getIntent().getStringExtra("NameStreet");
        district_id = getIntent().getIntExtra("District ID", -1);

        homeFragment = new HomeFragment_Master(master_id);
        updateFragment = new UpdateFragment_Master(master_id);
        foodFragment = new FoodFragment_Master(master_id);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(homeFragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
            Fragment fragment;
            switch(item.getItemId()) {
                case R.id.nav_home:
                    homeFragment = new HomeFragment_Master(master_id);
                    fragment = homeFragment;
                    loadFragment(fragment);
                    break;

                case R.id.nav_update:
                    fragment = updateFragment;
                    loadFragment(fragment);
                    break;

                case R.id.nav_product:
                    fragment = foodFragment;
                    loadFragment(fragment);
                    break;
            }

            return true;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_master, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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