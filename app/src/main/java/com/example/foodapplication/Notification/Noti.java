package com.example.foodapplication.Notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.foodapplication.R;

public class Noti extends Fragment {
    private ImageView nextSale;
    private  ImageView nextNews;
    private  ImageView setting;
    Fragment newF;

    public Noti() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_noti, container, false);

        nextSale = view.findViewById(R.id.nextSale);
        nextSale.setOnClickListener(NotiSaleFragment);

        nextNews = view.findViewById(R.id.nextNews);
        nextNews.setOnClickListener(NotiNewsFragment);

        setting = view.findViewById(R.id.iWsetting);
        setting.setOnClickListener(NotiSettingFragment);

        return view;
    }

    View.OnClickListener NotiSaleFragment = v -> {
        newF = new NotiSaleFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newF, null)
                .addToBackStack(null)
                .commit();
    };
    View.OnClickListener NotiNewsFragment = v -> {
        newF = new NotiNewsFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newF, null)
                .addToBackStack(null)
                .commit();
    };
    View.OnClickListener NotiSettingFragment = v -> {
        newF = new NotiSettingFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newF, null)
                .addToBackStack(null)
                .commit();
    };
}