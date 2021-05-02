package com.example.foodapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    ListAdapter listAdapter;

    RecyclerView recyclerView_Collection;
    CollectionAdapter collectionAdapter;

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
        AddDataForList();
        listAdapter = new ListAdapter(getActivity(), titles, images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        recyclerView_list.setLayoutManager(gridLayoutManager);
        recyclerView_list.setAdapter(listAdapter);



        recyclerView_Collection = view.findViewById(R.id.collection_recyclerView);
        AddDataForCollection();
        collectionAdapter = new CollectionAdapter(getActivity(), titles, images);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_Collection.setLayoutManager(linearLayoutManager);
        recyclerView_Collection.setAdapter(collectionAdapter);


        return view;
    }

    public void AddDataForList()
    {
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
    }

    public void AddDataForCollection()
    {
        titles = new ArrayList<>();
        images = new ArrayList<>();

        titles.add("Bánh Mì 0Đ");
        titles.add("7 Ngày Review - \n Tiền Triệu Về Túi");
        titles.add("Chi 5K - \n Ưu Đãi Freeship 15k");
        titles.add("Cuối Tuần \n Free Ship");
        titles.add("Đón Lễ Lớn");
        titles.add("Deal Nửa Giá - \n Quán Gần Nhà");
        titles.add("Deal Xịn - \n Giảm Tới 70k");
        titles.add("Đi Hết Việt Nam - \n Freeship");
        titles.add("Hè Xinh - \n Tiệc Xịn 55k");
        titles.add("Lễ To - \n Deal Xịn Xò");
        titles.add("Đặt NowFood - \n Nhận Quà Nutriboost");
        titles.add("Trà Sữa Maycha 0d");
        titles.add("Muộn Rồi Mà Sao Còn - \n Chưa Nhận Deal 55k");
        titles.add("Ưu Đãi Kép");
        titles.add("Vạn Deal -50% - \n Giảm Giá Siêu Xịn");

        images.add(R.drawable.banh_mi_0d);
        images.add(R.drawable.bay_ngay_tien_trieu_ve_tui);
        images.add(R.drawable.chi_5k);
        images.add(R.drawable.cuoi_tuan_freeship);
        images.add(R.drawable.day_don_ngay_le);
        images.add(R.drawable.deal_nua_gia_quan_gan_nha);
        images.add(R.drawable.deal_xin);
        images.add(R.drawable.di_het_viet_nam);
        images.add(R.drawable.he_xinh_tiec_xin);
        images.add(R.drawable.le_to_deal_xin_xo);
        images.add(R.drawable.nhan_qua_nutriboost);
        images.add(R.drawable.tra_sua_maycha_0d);
        images.add(R.drawable.tiec_55k);
        images.add(R.drawable.uu_dai_kep);
        images.add(R.drawable.van_deal_50percent);
    }

    private void runFillAddressActivity()
    {
        Intent intent = new Intent(getActivity(), Fill_Address_Screen.class);
        intent.putExtra("AddressLine", addressLine);
        intent.putExtra("NameStreet", nameStreet);
        startActivity(intent);
    }
}