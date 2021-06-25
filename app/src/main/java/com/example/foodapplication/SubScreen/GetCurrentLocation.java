package com.example.foodapplication.SubScreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.foodapplication.MySQL.DatabaseHelper;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.Master_MainActivity;
import com.example.foodapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetCurrentLocation extends AppCompatActivity {

    private TextView textView;
    private String addressLine;
    private String nameStreet;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    FusedLocationProviderClient fusedLocationProviderClient;
    boolean permission = false;
    int district_id = 0;
    int master_id = 0;
    int customer_id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transparent Status and Navigation Bar
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_get_current_location);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();
        initComponents();

        Run();
    }

    public void initComponents() {
        textView = findViewById(R.id.address_TextView);
    }

    public void Run() {
        //get current location - Address Line
        getLocation();

        customer_id = GetCustomerIDLoginBefore();
        master_id = GetMasterIDLoginBefore();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(master_id > 0) {
                    Intent i = new Intent(GetCurrentLocation.this, Master_MainActivity.class);
                    addressLine = textView.getText().toString();
                    i.putExtra("AddressLine", addressLine);
                    i.putExtra("NameStreet", nameStreet);
                    i.putExtra("District ID", district_id);
                    i.putExtra("Master ID", master_id);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
                else {
                    Intent i = new Intent(GetCurrentLocation.this, MainActivity.class);
                    addressLine = textView.getText().toString();
                    i.putExtra("AddressLine", addressLine);
                    i.putExtra("NameStreet", nameStreet);
                    i.putExtra("District ID", district_id);
                    i.putExtra("Customer ID", customer_id);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
        }, 5000);
    }

    public void getLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(GetCurrentLocation.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
           permission = true;
        } else {
            //when permisson denied
            ActivityCompat.requestPermissions(GetCurrentLocation.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        if (permission) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(GetCurrentLocation.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1);

                            addressLine = addresses.get(0).getAddressLine(0);
                            nameStreet = addresses.get(0).getThoroughfare();
                            textView.setText(addressLine);
                            String disctrict = addresses.get(0).getSubAdminArea().toString();
                            if(disctrict.trim().equals("Thủ Đức") || disctrict.trim().equals("Thu Duc") || disctrict.trim().equals("Thành Phố Thủ Đức") || disctrict.trim().equals("Quận Thủ Đức")) {
                                district_id = 14;
                            }
                            else district_id = 0;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void transparentStatusAndNavigation() {
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

    public int GetCustomerIDLoginBefore() {
        int cus_id = 0;
        String selectQuery = "Select _id from CUSTOMER where Status ='" + 1 + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                cus_id = cursor.getInt(cursor.getColumnIndex("_id"));
            } while (cursor.moveToNext());

        }
        cursor.close();
        return cus_id;
    }

    public int GetMasterIDLoginBefore() {
        int master_id = 0;
        String selectQuery = "Select _id from MASTER where Status ='" + 1 + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                master_id = cursor.getInt(cursor.getColumnIndex("_id"));
            } while (cursor.moveToNext());

        }
        cursor.close();
        return master_id;
    }


}