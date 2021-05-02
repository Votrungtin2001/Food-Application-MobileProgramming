package com.example.foodapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class OrderFragment extends Fragment {

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);


        ViewPager vp = view.findViewById(R.id.viewPager_order);
        OrderAdapter orderAdapter = new OrderAdapter(getFragmentManager());
        vp.setAdapter(orderAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabBar);
        tabLayout.setupWithViewPager(vp);

        return view;
    }



}