package com.example.foodapplication.HomeFragment.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.R;


public class RestaurantInformation_ThongTin extends Fragment {

    String address;
    String opening_time;

    TextView textView_address;
    TextView textView_openingtime;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    int branch_id;

    public RestaurantInformation_ThongTin(int id) {
        this.branch_id = id;

    }

    public RestaurantInformation_ThongTin() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_information__thong_tin, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        initComponents(view);
        Run();

        return  view;
    }

    public void initComponents(View view) {
        textView_address = view.findViewById(R.id.RestaurantInformation_Address);
        textView_openingtime = view.findViewById(R.id.RestaurantInformation_OpeningTime);
    }

    public void Run() {
        address = getAddress(branch_id);
        textView_address.setText(address);


        opening_time = getOpeningTime(branch_id);
        textView_openingtime.setText("Giờ mở cửa \t" + opening_time);
    }


    public String getAddress(int id) {
        String branch_address = "";
        String selectQuery = "SELECT A.Address FROM BRANCHES B JOIN ADDRESS A ON B.Address = A._id WHERE B._id ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                branch_address = cursor.getString(cursor.getColumnIndex("Address"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return branch_address;
    }

    public String getOpeningTime(int id) {
        String branch_openingtime = "";
        String selectQuery = "SELECT R.Opening_Times FROM BRANCHES B JOIN RESTAURANT R ON B.Restaurant = R._id WHERE B._id ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                branch_openingtime = cursor.getString(cursor.getColumnIndex("Opening_Times"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return branch_openingtime;
    }
}