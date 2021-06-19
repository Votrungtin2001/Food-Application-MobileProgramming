package com.example.foodapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragments.AccountFragment_Master;
import fragments.FoodFragment_Master;
import fragments.HomeFragment_Master;
import fragments.HomeFragment_Master_DatDon;
import fragments.RestaurantInformation_ThongTin;
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
    AccountFragment_Master accountFragment;

    public static HomeFragment_Master_DatDon fragment1;
    public static RestaurantInformation_ThongTin fragment2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master__main);

        master_id = getIntent().getIntExtra("Master ID", -1);
        addressLine = getIntent().getStringExtra("AddressLine");
        nameStreet = getIntent().getStringExtra("NameStreet");
        district_id = getIntent().getIntExtra("District ID", -1);

        homeFragment = new HomeFragment_Master(master_id);
        updateFragment = new UpdateFragment_Master(master_id);
        foodFragment = new FoodFragment_Master(master_id);
        accountFragment = new AccountFragment_Master();

        navigation = findViewById(R.id.bottom_nav_bar_master);
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

                case R.id.nav_account:
                    fragment = accountFragment;
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
}