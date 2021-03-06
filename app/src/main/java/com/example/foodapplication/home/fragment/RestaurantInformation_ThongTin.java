package com.example.foodapplication.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.foodapplication.R;

import static com.example.foodapplication.MainActivity.master_id;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetBranchAddress;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetBranchAddressAndOpeningTimeWithMaster;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetOpeningTime;


public class RestaurantInformation_ThongTin extends Fragment {

    TextView textView_address;
    TextView textView_openingtime;

    int branch_id;
    String branch_address = "";
    String restaurant_openingtime = "";
    int namefragment = 0;

    private static final String TAG = "RI_ThongTin";

    public RestaurantInformation_ThongTin(int id, int namefragment) {
        this.namefragment = namefragment;
        this.branch_id = id;

    }

    public RestaurantInformation_ThongTin() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_information__thong_tin, container, false);

        initComponents(view);
        Run();

        return  view;
    }

    public void initComponents(View view) {
        textView_address = view.findViewById(R.id.RestaurantInformation_Address);
        textView_openingtime = view.findViewById(R.id.RestaurantInformation_OpeningTime);
    }

    public void Run() {
        if(namefragment == 1) {
            textView_address.setText(branch_address);
            GetBranchAddress(branch_id, textView_address, TAG, getActivity());

            textView_openingtime.setText(restaurant_openingtime);
            GetOpeningTime(branch_id, textView_openingtime, TAG, getActivity());
        }
        if(namefragment == 2) {
            textView_address.setText(branch_address);
            textView_openingtime.setText(restaurant_openingtime);
            GetBranchAddressAndOpeningTimeWithMaster(master_id, textView_address, textView_openingtime, TAG, getActivity());
        }
    }


}