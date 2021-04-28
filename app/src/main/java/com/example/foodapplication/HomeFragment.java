package com.example.foodapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private ImageView imageView_Location;
    private ImageView imageView_Next;
    private TextView textView_addressLine;

    private String addressLine;
    private String nameStreet;

    RecyclerView recyclerView_list;
    List<String> titles;
    List<Integer> images;
    Adapter adapter;



    public HomeFragment(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView_list = view.findViewById(R.id.list_recylcerView);

        titles = new ArrayList<>();
        images = new ArrayList<>();

        titles.add("Cơm");
        titles.add("Giảm 70k");
        titles.add("Trà Sữa");
        titles.add("Freeship Xtra");
        titles.add("Ăn Vặt");
        titles.add("NowShip - Giao Hàng");
        titles.add("NowFresh - Thực phẩm");
        titles.add("Siêu Thị");
        titles.add("Quán Yêu Thích");
        titles.add("Hoa");

        images.add(R.drawable.rice);
        images.add(R.drawable.hot_deal);
        images.add(R.drawable.milk_tea);
        images.add(R.drawable.xtra);
        images.add(R.drawable.snack);
        images.add(R.drawable.delivery);
        images.add(R.drawable.fish_meat);
        images.add(R.drawable.cart);
        images.add(R.drawable.like);
        images.add(R.drawable.flower);

        adapter = new Adapter(getActivity(), titles, images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        recyclerView_list.setLayoutManager(gridLayoutManager);
        recyclerView_list.setAdapter(adapter);

        imageView_Location = (ImageView) view.findViewById(R.id.location_imageView);
        imageView_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runFillAddressActivity();
            }
        });

        imageView_Next = (ImageView) view.findViewById(R.id.next_imageView);
        imageView_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { runFillAddressActivity(); }
        });

        textView_addressLine = (TextView) view.findViewById(R.id.address_Txt);
        textView_addressLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { runFillAddressActivity(); }
        });

        textView_addressLine = (TextView) view.findViewById(R.id.address_Txt);
        Bundle b = getArguments();
        addressLine = b.getString("AddressLine");
        nameStreet = b.getString("NameStreet");
        textView_addressLine.setText(addressLine);


        return view;
    }

    private void runFillAddressActivity()
    {
        Intent intent = new Intent(getActivity(), Fill_Address_Screen.class);
        intent.putExtra("AddressLine", addressLine);
        intent.putExtra("NameStreet", nameStreet);
        startActivity(intent);
    }

}