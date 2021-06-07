package com.example.foodapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodapplication.Notification.Noti;
import com.example.foodapplication.Notification.NotiSettingFragment;
import com.example.foodapplication.Order.OrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements CommunicationInterface{

    NotiSettingFragment NotiSettingFrag;

    private String addressLine;
    private String nameStreet;
    private int district_id, user_id;
    Bundle importArgs;

    FragmentManager fragmentManager = getSupportFragmentManager();
    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    final HomeFragment homeFragment = new HomeFragment();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        user_id = intent.getIntExtra("CUSTOMER_ID", 0);

        //Transparent Status and Navigation Bar
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_main);

        addressLine = getIntent().getExtras().getString("AddressLine");
        nameStreet = getIntent().getExtras().getString("NameStreet");
        district_id = getIntent().getExtras().getInt("District ID");
        Bundle b = new Bundle();
        b.putString("AddressLine", addressLine);
        b.putString("NameStreet", nameStreet);
        b.putInt("District ID", district_id);
        b.putInt("CUSTOMER_ID", user_id);
        homeFragment.setArguments(b);
        fragmentTransaction.add(R.id.frame_container, homeFragment);
        fragmentTransaction.commit();

        importArgs = new Bundle();
        importArgs.putInt("CUSTOMER_ID", user_id);

        BottomNavigationView navigation = findViewById(R.id.bottom_nav_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Default fragment
        loadFragment(homeFragment);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.foodapplication",                  //Insert your own package name.
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
            Fragment fragment;
            switch(item.getItemId()) {
                case R.id.nav_home:
                    fragment = homeFragment;
                    fragment.setArguments(importArgs);
                    loadFragment(fragment);
                    break;

                case R.id.nav_favorites:
                    fragment = new FavoritesFragment();
                    fragment.setArguments(importArgs);
                    loadFragment(fragment);
                    break;
                case R.id.nav_account:
                    fragment = new AccountFragment();
                    fragment.setArguments(importArgs);
                    loadFragment(fragment);
                    break;
                case R.id.nav_notif:
                    fragment = new Noti();
                    fragment.setArguments(importArgs);
                    loadFragment(fragment);
                    break;

                case R.id.nav_order:
                    fragment=new OrderFragment();
                    fragment.setArguments(importArgs);
                    loadFragment(fragment);
                    break;
                default:
                    return false;
            }

            return true;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openActivity1()
    {
        Intent intent = new Intent(this, Fill_Address_Screen.class);
        intent.putExtra("NameStreet", nameStreet);
        intent.putExtra("AddressLine", addressLine);
        startActivity(intent);

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

    @Override
    public void onClickTopFragment(String str) {
        @SuppressLint("ResourceType") NotiSettingFragment fragBot = (NotiSettingFragment) getSupportFragmentManager().findFragmentById(R.layout.fragment_noti_setting);
        if ("add".equalsIgnoreCase(String.valueOf(fragBot))) { // kiểm tra Fragment cần truyền data đến có thực sự tồn tại và đang hiện.
            fragBot.updateFragment(str);
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        }
        else
            {
            Toast.makeText(this, "Khong tim thay, hoac fragment khong hien", Toast.LENGTH_SHORT).show();
        }
    }
}