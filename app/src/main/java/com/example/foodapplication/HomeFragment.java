package com.example.foodapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
    ViewPager viewPager3;

    public HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);

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


        viewPager3 = (ViewPager) view.findViewById(R.id.viewPager3);
        ImageAdapter adapter1 = new ImageAdapter(getActivity());
        viewPager3.setAdapter(adapter1);

        recyclerView_list = view.findViewById(R.id.list_recylcerView);

        titles = new ArrayList<>();
        images = new ArrayList<>();

        titles.add("  Deal Hot \n Hôm Nay");
        titles.add("Giảm 70k");
        titles.add("Cơm");
        titles.add("   Freeship \n   Xtra");
        titles.add("Trà Sữa");
        titles.add("  NowShip - \n Giao Hàng");
        titles.add("Ăn Vặt");
        titles.add("  NowFresh - \n Thực phẩm");
        titles.add("Quán Mới");
        titles.add("  NowTable - \n     Đặt Bàn");
        titles.add("  Ưu Đãi - \n   Đối Tác");
        titles.add("  Ưu Đãi \n   AirPay");
        titles.add("Hoa");
        titles.add("Siêu Thị");
        titles.add("Giặt Ủi");
        titles.add("Thú Cưng");
        titles.add("Thuốc");
        titles.add("Bia");
        titles.add("Làm Đẹp");
        titles.add("     Quán \n Yêu Thích");


        images.add(R.drawable.voucher);
        images.add(R.drawable.hot_deal);
        images.add(R.drawable.rice);
        images.add(R.drawable.xtra);
        images.add(R.drawable.milk_tea);
        images.add(R.drawable.delivery);
        images.add(R.drawable.snack);
        images.add(R.drawable.fish_meat);
        images.add(R.drawable.new_restaurant);
        images.add((R.drawable.dining_table));
        images.add(R.drawable.handshake);
        images.add(R.drawable.airpay);
        images.add(R.drawable.flower);
        images.add(R.drawable.cart);
        images.add(R.drawable.cleaning_tshirt);
        images.add(R.drawable.pet);
        images.add(R.drawable.medicine);
        images.add(R.drawable.beer);
        images.add(R.drawable.salon);
        images.add(R.drawable.like);

        adapter = new Adapter(getActivity(), titles, images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        recyclerView_list.setLayoutManager(gridLayoutManager);
        recyclerView_list.setAdapter(adapter);


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