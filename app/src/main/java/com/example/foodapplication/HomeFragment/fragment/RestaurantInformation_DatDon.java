package com.example.foodapplication.HomeFragment.fragment;

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

import com.example.foodapplication.HomeFragment.adapter.MenuAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.foodapplication.HomeFragment.model.ProductModel;

import static com.example.foodapplication.MySQL.MySQLQuerry.GetProducts;


public class RestaurantInformation_DatDon extends Fragment {

    private RecyclerView recyclerView_Menu;
    private MenuAdapter menuAdapter;
    private List<ProductModel> productModelList = new ArrayList<>();
    private int branch_id;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    private static final String TAG = "RI_DatDon";

    public RestaurantInformation_DatDon() {
    }
    public RestaurantInformation_DatDon(int id) {
        branch_id = id;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_information__dat_don, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        initComponents(view);
        Run();

        return  view;
    }

    public void initComponents(View view) {
        recyclerView_Menu = view.findViewById(R.id.Menu_RecyclerView);
    }

    public void Run() {
        menuAdapter = new MenuAdapter(getActivity(), productModelList);
        GetProducts(branch_id, productModelList, menuAdapter, TAG, getActivity());
        LinearLayoutManager linearLayoutManager_Menu = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_Menu.setLayoutManager(linearLayoutManager_Menu);
        recyclerView_Menu.setAdapter(menuAdapter);
    }


}