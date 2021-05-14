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


public class BestSellerCategoriesFragment extends Fragment {

    List<String> name_Restaurant;
    List<String> value_Rating;
    List<String> value_Time;
    List<String> value_Distance;
    List<String> name_Voucher;

    List<Integer> images;

    RecyclerView recyclerView_Categories;
    CategoriesAdapter categoriesAdapter;


    public BestSellerCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_best_seller_categories, container, false);

        recyclerView_Categories = view.findViewById(R.id.BestSellerCategories_RecyclerView);

        AddDataForBestSellerCategories();
        categoriesAdapter = new CategoriesAdapter(getActivity(), name_Restaurant, value_Rating, value_Time, value_Distance, name_Voucher, images);
        LinearLayoutManager linearLayoutManager7 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_Categories.setLayoutManager(linearLayoutManager7);
        recyclerView_Categories.setAdapter(categoriesAdapter);
        return  view;
    }

    private void AddDataForBestSellerCategories()
    {
        name_Restaurant = new ArrayList<>();
        value_Rating = new ArrayList<>();
        value_Time = new ArrayList<>();
        value_Distance = new ArrayList<>();
        name_Voucher = new ArrayList<>();
        images = new ArrayList<>();

        name_Restaurant.add("The Coffee House - Cao Thắng");
        name_Restaurant.add("Uncle Tea - Trà Đài Loan");
        name_Restaurant.add("Sushi Cô Chủ Nhỏ");

        value_Rating.add("4.5");
        value_Rating.add("4.2");
        value_Rating.add("4.3");

        value_Time.add("28 min");
        value_Time.add("30 min");
        value_Time.add("25 min");

        value_Distance.add(("2.0 km"));
        value_Distance.add(("2.3 km"));
        value_Distance.add(("1.8 km"));

        name_Voucher.add("Giảm 10%");
        name_Voucher.add("Giảm 30% - Tối đa 30k");
        name_Voucher.add("Mua 2 tặng 1 vào Chủ Nhật");

        images.add(R.drawable.the_coffee_house);
        images.add(R.drawable.uncle_tea);
        images.add(R.drawable.sushi_cochunho);

    }
}