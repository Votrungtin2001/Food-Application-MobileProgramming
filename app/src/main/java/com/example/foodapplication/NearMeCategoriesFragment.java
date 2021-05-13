package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class NearMeCategoriesFragment extends Fragment {

    List<String> name_Restaurant;
    List<String> value_Rating;
    List<String> value_Time;
    List<String> value_Distance;
    List<String> name_Voucher;

    List<Integer> images;

    RecyclerView recyclerView_Categories;
    CategoriesAdapter categoriesAdapter;


    public NearMeCategoriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearme_categories, container, false);

        recyclerView_Categories = view.findViewById(R.id.NearMeCategories_RecyclerView);

        AddDataForNearMeCategories();
        categoriesAdapter = new CategoriesAdapter(getActivity(), name_Restaurant, value_Rating, value_Time, value_Distance, name_Voucher, images);
        LinearLayoutManager linearLayoutManager_Categories = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_Categories.setLayoutManager(linearLayoutManager_Categories);
        recyclerView_Categories.setAdapter(categoriesAdapter);

        return  view;

    }

    private void AddDataForNearMeCategories()
    {
        name_Restaurant = new ArrayList<>();
        value_Rating = new ArrayList<>();
        value_Time = new ArrayList<>();
        value_Distance = new ArrayList<>();
        name_Voucher = new ArrayList<>();
        images = new ArrayList<>();

        name_Restaurant.add("Rau Má Mix - Nguyễn Tri Phương");
        name_Restaurant.add("Lotteria");
        name_Restaurant.add("Thái Tuk Tuk - Ẩm Thực Thái");

        value_Rating.add("4.3");
        value_Rating.add("4.5");
        value_Rating.add("4.2");

        value_Time.add("15 min");
        value_Time.add("18 min");
        value_Time.add("15 min");

        value_Distance.add(("0.8 km"));
        value_Distance.add(("1.1km"));
        value_Distance.add(("0.8km"));

        name_Voucher.add("Giảm 15%");
        name_Voucher.add("Mua 2 tặng 1");
        name_Voucher.add("Giảm 10%");

        images.add(R.drawable.rau_ma_mix);
        images.add(R.drawable.lotteria);
        images.add(R.drawable.thai_tuk_tuk);

    }
}