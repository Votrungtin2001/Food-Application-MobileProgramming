package com.example.foodapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private ImageView imageView_Location;
    private ImageView imageView_Next;
    private TextView textView_addressLine;
    private String addressLine;
    private String nameStreet;

    RecyclerView recyclerView_list;
    List<String> titles1;
    List<String> titles2;
    List<Integer> images;
    ListAdapter listAdapter;

    RecyclerView recyclerView_Collection;
    CollectionAdapter collectionAdapter;

    RecyclerView recyclerView_ViewHistory;
    ViewHistoryAdapter viewHistoryAdapter;

    ViewPager viewPager3;
    ImageSlider imageSlider;
    List<SlideModel> slideModels = new ArrayList<>();

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
        listAdapter = new ListAdapter(getActivity(), titles1, images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        recyclerView_list.setLayoutManager(gridLayoutManager);
        recyclerView_list.setAdapter(listAdapter);



        recyclerView_Collection = view.findViewById(R.id.collection_recyclerView);
        AddDataForCollection();
        collectionAdapter = new CollectionAdapter(getActivity(), titles1, images);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_Collection.setLayoutManager(linearLayoutManager1);
        recyclerView_Collection.setAdapter(collectionAdapter);

        recyclerView_ViewHistory = view.findViewById(R.id.viewHistory_recyclerView);
        AddDataForViewHistory();
        viewHistoryAdapter = new ViewHistoryAdapter(getActivity(), titles1, titles2, images);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_ViewHistory.setLayoutManager(linearLayoutManager2);
        recyclerView_ViewHistory.setAdapter(viewHistoryAdapter);

        imageSlider = view.findViewById(R.id.advertisement_slider);

        slideModels.add(new SlideModel("https://a.ipricegroup.com/media/Lam_La/Dat_NowFood_tren_Shopee.jpg", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://storage.googleapis.com/partnership-shopee-files-live/live/airpay/filer_public_thumbnails/filer_public/11/72/117220a1-274f-44c1-9132-c207f29961b7/apa_banner_nowfood_buangon0d_web-2000x600.jpg__2000x600_q95_crop_subsampling-2.jpg", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://cf.shopee.vn/file/e186e14fca4c4063fe89d47aab53bb78", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://images.foody.vn/delivery/collection/s320x200/image-73b05d50-210416002203.jpeg", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://images.foody.vn/delivery/collection/s480x300/beauty-upload-api-image-200703102303.jpeg", "", ScaleTypes.FIT));
        imageSlider.setImageList(slideModels,  ScaleTypes.FIT);

        return view;
    }

    public void AddDataForList()
    {
        titles1 = new ArrayList<>();
        images = new ArrayList<>();

        titles1.add("  Deal Hot \n Hôm Nay");
        titles1.add("Giảm 70k");
        titles1.add("Cơm");
        titles1.add("   Freeship \n   Xtra");
        titles1.add("Trà Sữa");
        titles1.add("  NowShip - \n Giao Hàng");
        titles1.add("Ăn Vặt");
        titles1.add("  NowFresh - \n Thực phẩm");
        titles1.add("Quán Mới");
        titles1.add("  NowTable - \n     Đặt Bàn");
        titles1.add("  Ưu Đãi - \n   Đối Tác");
        titles1.add("  Ưu Đãi \n   AirPay");
        titles1.add("Hoa");
        titles1.add("Siêu Thị");
        titles1.add("Giặt Ủi");
        titles1.add("Thú Cưng");
        titles1.add("Thuốc");
        titles1.add("Bia");
        titles1.add("Làm Đẹp");
        titles1.add("     Quán \n Yêu Thích");

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
        titles1 = new ArrayList<>();
        images = new ArrayList<>();

        titles1.add("Bánh Mì 0Đ");
        titles1.add("  7 Ngày Review - \n Tiền Triệu Về Túi");
        titles1.add("             Chi 5K - \n Ưu Đãi Freeship 15k");
        titles1.add("Cuối Tuần \n Free Ship");
        titles1.add("Đón Lễ Lớn");
        titles1.add("  Deal Nửa Giá - \n Quán Gần Nhà");
        titles1.add("    Deal Xịn - \n Giảm Tới 70k");
        titles1.add("  Đi Hết Việt Nam - \n        Freeship");
        titles1.add("    Hè Xinh - \n Tiệc Xịn 55k");
        titles1.add("   Lễ To - \n Deal Xịn Xò");
        titles1.add("     Đặt NowFood - \n Nhận Quà Nutriboost");
        titles1.add("Trà Sữa Maycha 0d");
        titles1.add("Muộn Rồi Mà Sao Còn - \n Chưa Nhận Deal 55k");
        titles1.add("Ưu Đãi Kép");
        titles1.add("   Vạn Deal -50% - \n Giảm Giá Siêu Xịn");

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

    public void AddDataForViewHistory()
    {
        titles1 = new ArrayList<>();
        titles2 = new ArrayList<>();
        images = new ArrayList<>();

        titles1.add("The Coffee House - Cao Thắng");
        titles1.add("Cơm Tấm Phúc Lộc Thọ");
        titles1.add("Mì Trộn Tên Lửa");

        titles2.add("Đã xem 2 ngày trước");
        titles2.add("Đã xem 23 giờ trước");
        titles2.add("Đã xem 10 ngày trước");


        images.add(R.drawable.the_coffee_house);
        images.add(R.drawable.com_tam_phucloctho);
        images.add(R.drawable.mi_tron_ten_lua);
    }

    private void runFillAddressActivity()
    {
        Intent intent = new Intent(getActivity(), Fill_Address_Screen.class);
        intent.putExtra("AddressLine", addressLine);
        intent.putExtra("NameStreet", nameStreet);
        startActivity(intent);
    }
}