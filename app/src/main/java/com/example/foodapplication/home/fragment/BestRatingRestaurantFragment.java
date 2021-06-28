package com.example.foodapplication.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.R;

import java.util.ArrayList;
import java.util.List;

import com.example.foodapplication.home.adapter.KindOfRestaurantAdapter;
import com.example.foodapplication.home.model.KindOfRestaurantModel;

import static com.example.foodapplication.mySQL.MySQLQuerry.GetDataForKindOfRestaurantWithType;


public class BestRatingRestaurantFragment extends Fragment {

    Context context;
    List<KindOfRestaurantModel> kindOfRestaurantModelList;
    RecyclerView recyclerView_KindOfRestaurant;
    KindOfRestaurantAdapter kindOfRestaurantAdapter;

    int district_id;

    private static final String TAG = "BestRatingRestaurantFra";

    public BestRatingRestaurantFragment() {
    }
    public BestRatingRestaurantFragment(int id) {
        this.district_id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best_rating_kindofrestaurant, container, false);

        initComponents(view);

        Run();

        return  view;
    }

    private void initComponents(View view) {
        recyclerView_KindOfRestaurant = view.findViewById(R.id.BestRatingKindOfRestaurant_RecyclerView);
    }

    private void Run() {
        kindOfRestaurantModelList = new ArrayList<>();
        kindOfRestaurantAdapter = new KindOfRestaurantAdapter(getActivity(), kindOfRestaurantModelList);
        GetDataForKindOfRestaurantWithType(district_id, kindOfRestaurantModelList, kindOfRestaurantAdapter, 4, TAG, getActivity());
        LinearLayoutManager linearLayoutManager_Categories = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_KindOfRestaurant.setLayoutManager(linearLayoutManager_Categories);
        recyclerView_KindOfRestaurant.setAdapter(kindOfRestaurantAdapter);
    }


}