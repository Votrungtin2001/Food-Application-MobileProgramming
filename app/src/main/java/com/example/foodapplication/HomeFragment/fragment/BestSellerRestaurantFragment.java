package com.example.foodapplication.HomeFragment.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.MySQL.DatabaseHelper;
import com.example.foodapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.foodapplication.HomeFragment.adapter.KindOfRestaurantAdapter;
import com.example.foodapplication.HomeFragment.model.KindOfRestaurantModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.foodapplication.MySQL.MySQLQuerry.GetDataForKindOfRestaurantWithType;


public class BestSellerRestaurantFragment extends Fragment {
    List<KindOfRestaurantModel> kindOfRestaurantModelList;
    RecyclerView recyclerView_KindOfRestaurant;
    KindOfRestaurantAdapter kindOfRestaurantAdapter;

    int district_id;

    private static final String TAG = "BestSellerRestaurantFrag";

    public BestSellerRestaurantFragment() {

    }

    public BestSellerRestaurantFragment(int id) {
        this.district_id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_best_seller_kindofrestaurant, container, false);

        initComponents(view);

        Run();

        return view;
    }

    private void initComponents(View view) {
        recyclerView_KindOfRestaurant = view.findViewById(R.id.BestSellerKindOfRestaurant_RecyclerView);
    }

    private void Run() {
        kindOfRestaurantModelList = new ArrayList<>();
        kindOfRestaurantAdapter = new KindOfRestaurantAdapter(getActivity(), kindOfRestaurantModelList);
        GetDataForKindOfRestaurantWithType(district_id, kindOfRestaurantModelList, kindOfRestaurantAdapter, 3, TAG, getActivity());
        LinearLayoutManager linearLayoutManager_Categories = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_KindOfRestaurant.setLayoutManager(linearLayoutManager_Categories);
        recyclerView_KindOfRestaurant.setAdapter(kindOfRestaurantAdapter);
    }


}