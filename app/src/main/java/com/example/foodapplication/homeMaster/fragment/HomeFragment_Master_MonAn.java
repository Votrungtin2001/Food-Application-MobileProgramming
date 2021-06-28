package com.example.foodapplication.homeMaster.fragment;

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

import com.example.foodapplication.homeMaster.adapter.MenuAdapter_HomeFragment_Master_DatDon;
import com.example.foodapplication.home.model.ProductModel;

import static com.example.foodapplication.MainActivity.master_id;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetProductsWithMaster;


public class HomeFragment_Master_MonAn extends Fragment {


    private List<ProductModel> productModelList;
    private int branch_id;

    RecyclerView recyclerView_HomeFragment_Master_DatDon;
    MenuAdapter_HomeFragment_Master_DatDon adapter;

    private final String TAG = "MasterHFMonAn";


    public HomeFragment_Master_MonAn() {
        // Required empty public constructor
    }

    public HomeFragment_Master_MonAn(int id) {
        branch_id = id;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home__master__mon_an, container, false);

        initComponents(view);

        SetRecyclerView();

        return view;
    }

    public void initComponents(View view) {
        recyclerView_HomeFragment_Master_DatDon = (RecyclerView) view.findViewById(R.id.Menu_RecyclerView_HomeFragment_Master_DatDon);
    }

    public void SetRecyclerView()
    {
        productModelList = new ArrayList<>();
        adapter = new MenuAdapter_HomeFragment_Master_DatDon(getActivity(), productModelList);
        GetProductsWithMaster(master_id, productModelList, adapter, TAG, getActivity());
        LinearLayoutManager linearLayoutManager_Menu = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_HomeFragment_Master_DatDon.setLayoutManager(linearLayoutManager_Menu);
        recyclerView_HomeFragment_Master_DatDon.setAdapter(adapter);

    }
}