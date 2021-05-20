package com.example.foodapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.tabs.TabLayout;

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

    List<String> prices;
    List<String> valueOfLike;
    List<Integer> images;
    ListAdapter listAdapter;

    RecyclerView recyclerView_Collection;
    CollectionAdapter collectionAdapter;

    RecyclerView recyclerView_ViewHistory;
    ViewHistoryAdapter viewHistoryAdapter;

    ViewPager viewPager3;
    ImageSlider imageSlider;
    List<SlideModel> slideModels = new ArrayList<>();

    RecyclerView recyclerView_RecentlyEaten;
    RecentlyEatenAdapter recentlyEatenAdapter;

    RecyclerView recyclerView_MayBeYouLike;
    MaybeYouLikeAdapter maybeYouLikeAdapter;

    RecyclerView recyclerView_FreeshipXtra;
    FreeshipXtraAdapter freeshipXtraAdapter;

    RecyclerView recyclerView_TypesOfFood;
    TypesOfFoodAdapter typesOfFoodAdapter;

    TabLayout tabLayout_Categories;
    ViewPager viewPager_Categories;
    ViewPagerAdapter viewPagerAdapter;

    ArrayList<String> title_Categories = new ArrayList<>();




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

        recyclerView_list = view.findViewById(R.id.list_recyclerView);
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

        recyclerView_RecentlyEaten = view.findViewById(R.id.recentlyEaten_RecyclerView);
        AddDataForRecentlyEaten();
        recentlyEatenAdapter = new RecentlyEatenAdapter(getActivity(), titles1, titles2, images);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_RecentlyEaten.setLayoutManager(linearLayoutManager3);
        recyclerView_RecentlyEaten.setAdapter(recentlyEatenAdapter);

        recyclerView_MayBeYouLike = view.findViewById(R.id.maybeYouLike_RecyclerView);
        AddDataForMayBeYouLike();
        maybeYouLikeAdapter = new MaybeYouLikeAdapter(getActivity(), titles1, titles2, prices, valueOfLike, images);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_MayBeYouLike.setLayoutManager(linearLayoutManager4);
        recyclerView_MayBeYouLike.setAdapter(maybeYouLikeAdapter);

        recyclerView_FreeshipXtra = view.findViewById(R.id.FreeshipXtra_RecyclerView);
        AddDataForFreeshipXtra();
        freeshipXtraAdapter = new FreeshipXtraAdapter(getActivity(), titles1, titles2, images);
        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_FreeshipXtra.setLayoutManager(linearLayoutManager5);
        recyclerView_FreeshipXtra.setAdapter(freeshipXtraAdapter);

        recyclerView_TypesOfFood = view.findViewById(R.id.TypesOfFood_RecyclerView);
        AddDataForTypesOfFood();
        typesOfFoodAdapter = new TypesOfFoodAdapter(getActivity(), titles1, images);
        LinearLayoutManager linearLayoutManager6 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_TypesOfFood.setLayoutManager(linearLayoutManager6);
        recyclerView_TypesOfFood.setAdapter(typesOfFoodAdapter);

        tabLayout_Categories = (TabLayout) view.findViewById(R.id.Categories_TabLayout);
        viewPager_Categories = (ViewPager) view.findViewById(R.id.Categories_ViewPager);

        title_Categories.add("Gần tôi");
        title_Categories.add("Bán chạy");
        title_Categories.add("Đánh giá");
        title_Categories.add("Giao nhanh");
        //Set tab layout
        tabLayout_Categories.setupWithViewPager(viewPager_Categories);

        //Prepare view pager
        prepareViewPagerCategories(viewPager_Categories, title_Categories);




        return view;
    }

    private void prepareViewPagerCategories(ViewPager viewPager, ArrayList<String> arrayList)
    {
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        for(int i = 0; i < title_Categories.size(); i++)
        {
            if (i == 0)
            {
                NearMeCategoriesFragment nearMeCategoriesFragment = new NearMeCategoriesFragment();
                viewPagerAdapter.addFragment(nearMeCategoriesFragment, title_Categories.get(i));
            }
            if (i == 1)
            {
                BestSellerCategoriesFragment bestSellerCategoriesFragment = new BestSellerCategoriesFragment();
                viewPagerAdapter.addFragment(bestSellerCategoriesFragment, title_Categories.get(i));
            }
            if (i == 2)
            {
                BestRatingCategoriesFragment bestRatingCategoriesFragment = new BestRatingCategoriesFragment();
                viewPagerAdapter.addFragment(bestRatingCategoriesFragment, title_Categories.get(i));
            }
            if (i == 3) {
                FastestDeliveryCategoriesFragment fastestDeliveryCategoriesFragment = new FastestDeliveryCategoriesFragment();
                viewPagerAdapter.addFragment(fastestDeliveryCategoriesFragment, title_Categories.get(i));
            }
        }
        viewPager.setAdapter(viewPagerAdapter);
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

    public void AddDataForRecentlyEaten()
    {
        titles1 = new ArrayList<>();
        titles2 = new ArrayList<>();
        images = new ArrayList<>();

        titles1.add("Rau Má Mix");
        titles1.add("Lotteria");
        titles1.add("The Alley");
        titles1.add("Jolibee");
        titles1.add("Mì Cay Sasin");

        titles2.add("FREESHIP");
        titles2.add("Discount 15%");
        titles2.add("Mua 2 tặng 1");
        titles2.add("Free Nước");
        titles2.add("Đi 4 tính tiền 3");

        images.add(R.drawable.rau_ma_mix);
        images.add(R.drawable.lotteria);
        images.add(R.drawable.the_alley);
        images.add(R.drawable.jolibee);
        images.add(R.drawable.mi_cay_sasin);

    }

    private void AddDataForMayBeYouLike()
    {
        titles1 = new ArrayList<>();
        titles2 = new ArrayList<>();
        prices = new ArrayList<>();
        valueOfLike = new ArrayList<>();
        images = new ArrayList<>();

        titles1.add("Mì cay hải sản");
        titles1.add("Gà sốt cay Sài Gòn");
        titles1.add("Hamburger Big Mac");
        titles1.add("Pizza cá hồi xông khói");
        titles1.add("Trà ô lông dâu");

        titles2.add("Mì Cay Sasin - Đại Học Khoa Học Tự Nhiên");
        titles2.add("Jolibee Sư Vạn Thạnh - Quận 5");
        titles2.add("Mc Donald - Aeon Bình Dương");
        titles2.add("Pizza Hut - Vincom Thủ Đức");
        titles2.add("Phúc Long - Võ Văn Ngân");

        prices.add("40,000đ");
        prices.add("33,000đ");
        prices.add("69,000đ");
        prices.add("79,000đ");
        prices.add("45,000đ");

        valueOfLike.add("10+ lượt thích");
        valueOfLike.add("100+ lượt thích");
        valueOfLike.add("200+ lượt thích");
        valueOfLike.add("50+ lượt thích");
        valueOfLike.add("500+ lượt thích");

        images.add(R.drawable.mi_cay_hai_san_sasin);
        images.add(R.drawable.ga_sot_cay_jolibee);
        images.add(R.drawable.hamburger_mcdonald);
        images.add(R.drawable.pizza_ca_hoi_xong_khoi_pizzahut);
        images.add(R.drawable.tra_o_long_dau_pl);
    }

    public void AddDataForFreeshipXtra()
    {
        titles1 = new ArrayList<>();
        titles2 = new ArrayList<>();
        images = new ArrayList<>();

        titles1.add("HP Cơm Tấm - Nguyễn Tri Phương");
        titles1.add("Thái Tuk Tuk - Ẩm Thực Thái");
        titles1.add("Uncle Tea - Trà Đài Loan");
        titles1.add("Sushi Cô Chủ Nhỏ");
        titles1.add("Salad Poki Katuri");

        titles2.add("FREESHIP");
        titles2.add("Giảm 50%");
        titles2.add("Giảm món");
        titles2.add("Giảm 50%");
        titles2.add("Giảm món");


        images.add(R.drawable.hp_com_tam);
        images.add(R.drawable.thai_tuk_tuk);
        images.add(R.drawable.uncle_tea);
        images.add(R.drawable.sushi_cochunho);
        images.add(R.drawable.salad_poki_katuri);

    }

    public void AddDataForTypesOfFood()
    {
        titles1 = new ArrayList<>();
        images = new ArrayList<>();

        titles1.add("Tất cả");
        titles1.add("Đồ ăn");
        titles1.add("Đồ uống");
        titles1.add("Đồ chay");
        titles1.add("Bánh kem");
        titles1.add("Tráng miệng");
        titles1.add("Homemade");
        titles1.add("Vỉa hè");
        titles1.add("Pizza/Burger");
        titles1.add("Món gà");
        titles1.add("Món lẩu");
        titles1.add("Sushi");
        titles1.add("Mì phở");
        titles1.add("Cơm hộp");

        images.add(R.drawable.all_types);
        images.add(R.drawable.general_food);
        images.add(R.drawable.general_drink);
        images.add(R.drawable.vegetarian_food);
        images.add(R.drawable.sweet_cake);
        images.add(R.drawable.dessert_food);
        images.add(R.drawable.sweet_soup);
        images.add(R.drawable.street_food);
        images.add(R.drawable.pizza);
        images.add(R.drawable.chicken_food);
        images.add(R.drawable.hot_pot);
        images.add(R.drawable.sushi_food);
        images.add(R.drawable.noodle_food);
        images.add(R.drawable.bento);

    }

    private void runFillAddressActivity()
    {
        Intent intent = new Intent(getActivity(), Fill_Address_Screen.class);
        intent.putExtra("AddressLine", addressLine);
        intent.putExtra("NameStreet", nameStreet);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == getActivity().RESULT_OK)
            {
                addressLine = data.getStringExtra("Address Line");
                textView_addressLine.setText(addressLine);
                nameStreet = data.getStringExtra("Name Street");
            }
        }
    }
}