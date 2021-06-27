package com.example.foodapplication.HomeFragment.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodapplication.R;

import java.util.ArrayList;
import java.util.List;

import com.example.foodapplication.HomeFragment.adapter.KindOfRestaurantAdapter;
import com.example.foodapplication.HomeFragment.model.KindOfRestaurantModel;

import static com.example.foodapplication.MySQL.MySQLQuerry.GetDataForKindOfRestaurantWithType;


public class FastestDeliveryRestaurantFragment extends Fragment {

    Context context;
    List<KindOfRestaurantModel> kindOfRestaurantModelList;
    RecyclerView recyclerView_KindOfRestaurant;
    KindOfRestaurantAdapter kindOfRestaurantAdapter;

    int district_id;

    private static final String TAG = "FastestDelResFrag";

    public FastestDeliveryRestaurantFragment(int id) {
        this.district_id = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fastest_delivery_kindofrestaurant, container, false);

        initComponents(view);

        Run();

        return  view;
    }

    private void initComponents(View view) {
        recyclerView_KindOfRestaurant = view.findViewById(R.id.FastestDeliveryKindOfRestaurant_RecyclerView);
    }

    private void Run() {
        kindOfRestaurantModelList = new ArrayList<>();
        kindOfRestaurantAdapter = new KindOfRestaurantAdapter(getActivity(), kindOfRestaurantModelList);
        GetDataForKindOfRestaurantWithType(district_id, kindOfRestaurantModelList, kindOfRestaurantAdapter, 5, TAG, getActivity());
        LinearLayoutManager linearLayoutManager_Categories = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_KindOfRestaurant.setLayoutManager(linearLayoutManager_Categories);
        recyclerView_KindOfRestaurant.setAdapter(kindOfRestaurantAdapter);
    }

}