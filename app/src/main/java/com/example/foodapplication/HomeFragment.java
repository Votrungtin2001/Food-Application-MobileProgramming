package com.example.foodapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import adapter.AllRestaurantAdapter;
import adapter.CollectionAdapter;
import adapter.DiscountComboProductAdapter;
import adapter.ListAdapter;
import adapter.SearchBarAdapter;
import fragments.BestRatingRestaurantFragment;
import fragments.BestSellerRestaurantFragment;
import fragments.NearMeRestaurantsFragment;
import models.AllRestaurantModel;
import models.CollectionModel;
import models.SearchBarModel;
import models.SortOfProductModel;


public class HomeFragment extends Fragment {

    private ImageView imageView_Location;
    private ImageView imageView_Next;
    private TextView textView_addressLine;
    private EditText editText_search;
    private String addressLine;
    private String nameStreet;
    private TextView textView_DistrictIsUnavailable;
    private TextView textView_space1;
    private TextView textView_space2;
    private TextView textView_space3;
    private TextView textView_space4;
    private TextView textView_space5;

    //RecyclerView SearchBar
    RecyclerView recyclerView_SearchBar;
    SearchBarAdapter searchBarAdapter;
    List<SearchBarModel> searchBarModels;
    LinearLayoutManager linearLayoutManager_SearchBar;

    //RecyclerView List
    RecyclerView recyclerView_list;
    List<String> titles1;
    List<Integer> images;
    ListAdapter listAdapter;


    //RecyclerView Collection
    RecyclerView recyclerView_Collection;
    List<CollectionModel> collectionModels;
    RecyclerView.Adapter adapter;
    TextView textView_CollectionTitle;
    TextView textView_MoreCollection;

    //RecyclerView AllRestaurant
    RecyclerView recyclerView_AllRestaurants;
    AllRestaurantAdapter allRestaurantAdapter;
    List<AllRestaurantModel> allRestaurantModelList = new ArrayList<>();
    TextView AllRestaurant_more;
    TextView AllRestaurant_Title;

    //ImageSlider Promo and Advertisement
    ImageSlider imageSlider_advertisement;
    ImageSlider imageSlider_promo;
    List<SlideModel> slideModels = new ArrayList<>();

    //RecyclerView Discount Combo Product
    RecyclerView recyclerView_DiscountComboProduct;
    DiscountComboProductAdapter discountComboProductAdapter;
    List<SortOfProductModel> sortOfProductModelList1 = new ArrayList<>();
    TextView DiscountComboProduct_Title;
    TextView DiscountComboProduct_more;

    //RecyclerView CheapestProduct
    RecyclerView recyclerView_CheapestProduct;
    TextView CheapestProduct_Title;
    TextView CheapestProduct_more;
    DiscountComboProductAdapter cheapestproductAdapter;
    List<SortOfProductModel> sortOfProductModelList2 = new ArrayList<>();

    //TabLayout & ViewPager
    TabLayout tabLayout_KindOfRestaurant;
    ViewPager viewPager_KindOfRestaurant;
    ViewPagerAdapter viewPagerAdapter;

    ArrayList<String> title_KindOfRestaurant = new ArrayList<>();

    private int district_id;
    boolean district_isAvailable = false;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    byte[] img1;
    int img;
    Resources resources;
    Drawable drawable;
    Bitmap bitmap;
    ByteArrayOutputStream stream;
    byte[] bitmapData;

    public HomeFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);


        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

       /* databaseHelper.delBranch(43);
        databaseHelper.delAddress(43);
        databaseHelper.delRestaurant(43);
        databaseHelper.delProduct(582);
        databaseHelper.delMenu(591);
        databaseHelper.delProduct(583);
        databaseHelper.delMenu(592);

       databaseHelper.delCustomerAddress(1);
        databaseHelper.delCustomerAddress(2);

        databaseHelper.delAddress(43);
        databaseHelper.delAddress(44);*/


        //EditText Search
        editText_search = view.findViewById(R.id.editText_SearchBar);

        //RecyclerView SearchBar
        recyclerView_SearchBar = view.findViewById(R.id.recyclerView_SearchBar);

        //ImageView Location
        imageView_Location = (ImageView) view.findViewById(R.id.location_imageView);
        imageView_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runFillAddressActivity();
            }
        });

        //Open Fill Address Activity
        textView_addressLine = view.findViewById(R.id.address_Txt);
        textView_addressLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { runFillAddressActivity(); }
        });

        //ImageView Next
        imageView_Next = (ImageView) view.findViewById(R.id.next_imageView);
        //Open Fill Address Activity
        imageView_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { runFillAddressActivity(); }
        });


        //TextView AddressLine
        //Set Address Data
        textView_addressLine.setText(addressLine);

        //TextView Announcement When District Doesn't Have Any Restaurants
        textView_DistrictIsUnavailable = view.findViewById(R.id.textView_DistrictIsUnavailable);

        //Promo ImageSlider
        imageSlider_promo = view.findViewById(R.id.promo_slider);

        //RecyclerView List
        recyclerView_list = view.findViewById(R.id.list_recyclerView);

        textView_space1 = view.findViewById(R.id.textView7);

        //RecyclerView Collection
        textView_CollectionTitle = view.findViewById(R.id.collection_textView);
        recyclerView_Collection = view.findViewById(R.id.collection_recyclerView);
        textView_MoreCollection = view.findViewById(R.id.collection_more);

        textView_space2 = view.findViewById(R.id.textView17);

        //RecyclerView AllRestaurant
        recyclerView_AllRestaurants = view.findViewById(R.id.AllRestaurants_recyclerView);
        AllRestaurant_Title = view.findViewById(R.id.AllRestaurants_textView);
        AllRestaurant_more = view.findViewById(R.id.AllRestaurants_more);

        textView_space3 = view.findViewById(R.id.txtView);

        //ImageSlider Advertisement
        imageSlider_advertisement = view.findViewById(R.id.advertisement_slider);

        //RecyclerView Discount Combo Product
        recyclerView_DiscountComboProduct = view.findViewById(R.id.DiscountComboProduct_RecyclerView);
        DiscountComboProduct_Title = view.findViewById(R.id.DiscountComboProduct_Title);
        DiscountComboProduct_more = view.findViewById(R.id.DiscountComboProduct_more);

        textView_space4 = view.findViewById(R.id.txtView1);

        //RecyclerView CheapestProduct
        recyclerView_CheapestProduct = view.findViewById(R.id.CheapestProduct_RecyclerView);
        CheapestProduct_Title = view.findViewById(R.id.CheapestProduct_Title);
        CheapestProduct_more = view.findViewById(R.id.CheapestProduct_more);

        textView_space5 = view.findViewById(R.id.txtView2);

        //Tab Layout and ViewPager
        tabLayout_KindOfRestaurant = (TabLayout) view.findViewById(R.id.KindOfRestaurant_TabLayout);
        viewPager_KindOfRestaurant = (ViewPager) view.findViewById(R.id.KindOfRestaurant_ViewPager);

        district_id = 14;
        if(district_id >= 0) district_isAvailable = true;
        SetAllData(district_id);
        setUpSreen(district_isAvailable);

        return view;
    }


    private void setUpSreen(boolean sign) {
        if (sign == true) {
            imageSlider_promo.setVisibility(View.VISIBLE);
            textView_DistrictIsUnavailable.setVisibility(View.GONE);
            recyclerView_list.setVisibility(View.VISIBLE);
            textView_space1.setVisibility(View.VISIBLE);
            textView_CollectionTitle.setVisibility(View.VISIBLE);
            textView_MoreCollection.setVisibility(View.VISIBLE);
            recyclerView_Collection.setVisibility(View.VISIBLE);
            textView_space2.setVisibility(View.VISIBLE);
            AllRestaurant_Title.setVisibility(View.VISIBLE);
            AllRestaurant_more.setVisibility(View.VISIBLE);
            recyclerView_AllRestaurants.setVisibility(View.VISIBLE);
            imageSlider_advertisement.setVisibility(View.VISIBLE);
            textView_space3.setVisibility(View.VISIBLE);
            DiscountComboProduct_Title.setVisibility(View.VISIBLE);
            DiscountComboProduct_more.setVisibility(View.VISIBLE);
            recyclerView_DiscountComboProduct.setVisibility(View.VISIBLE);
            textView_space4.setVisibility(View.VISIBLE);
            CheapestProduct_Title.setVisibility(View.VISIBLE);
            CheapestProduct_more.setVisibility(View.VISIBLE);
            recyclerView_CheapestProduct.setVisibility(View.VISIBLE);
            textView_space5.setVisibility(View.VISIBLE);
            tabLayout_KindOfRestaurant.setVisibility(View.VISIBLE);
            viewPager_KindOfRestaurant.setVisibility(View.VISIBLE);
        }
        else {
            imageSlider_promo.setVisibility(View.GONE);
            textView_DistrictIsUnavailable.setVisibility(View.VISIBLE);
            recyclerView_list.setVisibility(View.GONE);
            textView_space1.setVisibility(View.GONE);
            textView_CollectionTitle.setVisibility(View.GONE);
            textView_MoreCollection.setVisibility(View.GONE);
            recyclerView_Collection.setVisibility(View.GONE);
            textView_space2.setVisibility(View.GONE);
            AllRestaurant_Title.setVisibility(View.GONE);
            AllRestaurant_more.setVisibility(View.GONE);
            recyclerView_AllRestaurants.setVisibility(View.GONE);
            imageSlider_advertisement.setVisibility(View.GONE);
            textView_space3.setVisibility(View.GONE);
            DiscountComboProduct_Title.setVisibility(View.GONE);
            DiscountComboProduct_more.setVisibility(View.GONE);
            recyclerView_DiscountComboProduct.setVisibility(View.GONE);
            textView_space4.setVisibility(View.GONE);
            CheapestProduct_Title.setVisibility(View.GONE);
            CheapestProduct_more.setVisibility(View.GONE);
            recyclerView_CheapestProduct.setVisibility(View.GONE);
            textView_space5.setVisibility(View.GONE);
            tabLayout_KindOfRestaurant.setVisibility(View.GONE);
            viewPager_KindOfRestaurant.setVisibility(View.GONE);
        }
    }

    private void filter(String toString, int id) {
        searchBarModels = new ArrayList<>();
        String selectQuery = "SELECT B._id, B.NAME, R.Image FROM (RESTAURANT R JOIN BRANCHES B ON R._id = B.Restaurant) JOIN ADDRESS A ON B.Address = A._id WHERE A.District ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String name_branch = cursor.getString(cursor.getColumnIndex("NAME"));
                if(name_branch.toLowerCase().contains(toString.toLowerCase())) {
                    byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                    int branch_id = cursor.getInt(cursor.getColumnIndex("_id"));
                    Bitmap img_bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                    SearchBarModel searchBarModel = new SearchBarModel(img_bitmap, name_branch, branch_id);
                    searchBarModels.add(searchBarModel);
                }
            } while (cursor.moveToNext());
            searchBarAdapter.filterList(searchBarModels);
            recyclerView_SearchBar.setAdapter(searchBarAdapter);

        }
        cursor.close();


    }

    private void GetDataForAllRestaurants(int id) {
        int count = 0;
        allRestaurantModelList = new ArrayList<>();
            String selectQuery = "SELECT B._id, R.Image, B.NAME, A.Address FROM (RESTAURANT R JOIN BRANCHES B ON R._id = B.Restaurant) JOIN ADDRESS A ON B.Address = A._id WHERE A.District = '" + id + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                do {
                    if (count <= 15) {
                        count++;
                        int branch_id = cursor.getInt(cursor.getColumnIndex("_id"));
                        byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                        String name_branch = cursor.getString(cursor.getColumnIndex("NAME"));
                        String address_branch = cursor.getString(cursor.getColumnIndex("Address"));
                        AllRestaurantModel allRestaurantModel = new AllRestaurantModel(bitmap, name_branch, address_branch, branch_id);
                        allRestaurantModelList.add(allRestaurantModel);
                    }
                } while (cursor.moveToNext());

            }
            cursor.close();
    }

    private void AddDataForDiscountComboProduct(int id) {
        int count = 0;
        sortOfProductModelList1 = new ArrayList<>();
        String selectQuery = "SELECT B._id, P.Image, P.Name, B.NAME, M.Price " +
                "FROM (((PRODUCTS P JOIN MENU M ON P._id = M.Product) " +
                "JOIN RESTAURANT R ON M.Restaurant = R._id) " +
                "JOIN BRANCHES B ON R._id = B.Restaurant) " +
                "JOIN ADDRESS A ON B.Address = A._id " +
                "WHERE A.District ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                if (count <= 15) {
                    String key = "Combo";
                    String name_product = cursor.getString(cursor.getColumnIndex("Name"));
                    if (name_product.toLowerCase().contains(key.toLowerCase())) {
                        count++;
                        byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                        int branch_id = cursor.getInt(cursor.getColumnIndex("_id"));
                        Bitmap img_bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                        String name_branch = cursor.getString(cursor.getColumnIndex("NAME"));
                        int price = cursor.getInt(cursor.getColumnIndex("Price"));
                        SortOfProductModel sortOfProductModel = new SortOfProductModel(img_bitmap, name_product, name_branch, price, branch_id);
                        sortOfProductModelList1.add(sortOfProductModel);
                    }
                }
            } while (cursor.moveToNext());

        }
        cursor.close();
    }

    public void AddDataForCheapestProduct(int id) {
        sortOfProductModelList2 = new ArrayList<>();
        String selectQuery = "SELECT B._id, P.Image, P.Name, B.NAME, M.Price " +
                "FROM (((PRODUCTS P JOIN MENU M ON P._id = M.Product) " +
                "JOIN RESTAURANT R ON M.Restaurant = R._id) " +
                "JOIN BRANCHES B ON R._id = B.Restaurant) " +
                "JOIN ADDRESS A ON B.Address = A._id  " +
                "WHERE M.Price < 20000 AND M.Price >= 15000 AND P.Category != 4 AND P.Category != 12 AND A.District ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String name_product = cursor.getString(cursor.getColumnIndex("Name"));
                byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                int branch_id = cursor.getInt(cursor.getColumnIndex("_id"));
                Bitmap img_bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                String name_branch = cursor.getString(cursor.getColumnIndex("NAME"));
                int price = cursor.getInt(cursor.getColumnIndex("Price"));
                SortOfProductModel sortOfProductModel = new SortOfProductModel(img_bitmap, name_product, name_branch, price, branch_id);
                sortOfProductModelList2.add(sortOfProductModel);
            } while (cursor.moveToNext());

        }
        cursor.close();
    }


    private void prepareViewPagerCategories(ViewPager viewPager, ArrayList<String> arrayList)
    {
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        for(int i = 0; i < arrayList.size(); i++)
        {
            if (i == 0)
            {
                NearMeRestaurantsFragment nearMeRestaurantsFragment = new NearMeRestaurantsFragment(district_id);
                viewPagerAdapter.addFragment(nearMeRestaurantsFragment, arrayList.get(i));
            }
            if (i == 1)
            {
                BestSellerRestaurantFragment bestSellerRestaurantFragment = new BestSellerRestaurantFragment(district_id);
                viewPagerAdapter.addFragment(bestSellerRestaurantFragment, arrayList.get(i));
            }
            if (i == 2)
            {
                BestRatingRestaurantFragment bestRatingRestaurantFragment = new BestRatingRestaurantFragment(district_id);
                viewPagerAdapter.addFragment(bestRatingRestaurantFragment, arrayList.get(i));
            }
            if (i == 3)
            {
                FastestDeliveryRestaurantFragment fastestDeliveryRestaurantFragment = new FastestDeliveryRestaurantFragment(district_id);
                viewPagerAdapter.addFragment(fastestDeliveryRestaurantFragment, arrayList.get(i));
            }


        }
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void AddDataForList()
    {
            titles1 = new ArrayList<>();
            images = new ArrayList<>();

            titles1.add("Cơm");
            titles1.add("Trà Sữa");
            titles1.add("Mì");
            titles1.add("Smoothie");
            titles1.add("Gà");
            titles1.add("Coffee");
            titles1.add("Pizza");
            titles1.add("Tea");
            titles1.add("Hamburger");
            titles1.add("Juice");
            titles1.add("Bánh Mì");
            titles1.add("Chè");
            titles1.add("Salad");
            titles1.add("Cake");
            titles1.add("Sushi");
            titles1.add("Xôi");


            images.add(R.drawable.rice);
            images.add(R.drawable.milk_tea);
            images.add(R.drawable.noodle_icon);
            images.add(R.drawable.smoothie_icon);
            images.add(R.drawable.chicken_icon);
            images.add(R.drawable.coffee_icon);
            images.add(R.drawable.pizza_icon);
            images.add(R.drawable.tea_icon);
            images.add(R.drawable.hamburger_icon);
            images.add(R.drawable.juice_icon);
            images.add(R.drawable.banhmi_icon);
            images.add(R.drawable.sweetsoup_icon);
            images.add(R.drawable.salad_icon);
            images.add(R.drawable.cake_icon);
            images.add(R.drawable.sushi_icon);
            images.add(R.drawable.xoi_icon);

    }

    public void AddDataForCollection()
    {
            collectionModels = new ArrayList<>();
            collectionModels.add(new CollectionModel(R.drawable.banh_mi_0d, "Bánh Mì 0Đ"));
            collectionModels.add(new CollectionModel(R.drawable.bay_ngay_tien_trieu_ve_tui, "7 Ngày Review - \nTiền Triệu Về Túi"));
            collectionModels.add(new CollectionModel(R.drawable.chi_5k, "Chi 5K - \nƯu Đãi Freeship 15k"));
            collectionModels.add(new CollectionModel(R.drawable.cuoi_tuan_freeship, "Cuối Tuần \nFree Ship"));
            collectionModels.add(new CollectionModel(R.drawable.day_don_ngay_le, "Đón Lễ Lớn"));
            collectionModels.add(new CollectionModel(R.drawable.deal_nua_gia_quan_gan_nha, "Deal Nửa Giá - \nQuán Gần Nhà"));
            collectionModels.add(new CollectionModel(R.drawable.deal_xin, "Deal Xịn - \n Giảm Tới 70k"));
            collectionModels.add(new CollectionModel(R.drawable.di_het_viet_nam, "Đi Hết Việt Nam - \nFreeship"));
            collectionModels.add(new CollectionModel(R.drawable.he_xinh_tiec_xin, "Hè Xinh - \nTiệc Xịn 55k"));
            collectionModels.add(new CollectionModel(R.drawable.le_to_deal_xin_xo, "Lễ To - \nDeal Xịn Xò"));


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
                boolean sign = false;
                addressLine = data.getStringExtra("Address Line");
                district_id = data.getIntExtra("District ID", 0);
                textView_addressLine.setText(addressLine);
                nameStreet = data.getStringExtra("Name Street");

                if(district_id >= 0) sign = true;
                SetAllData(district_id);

                setUpSreen(district_isAvailable);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();

            }
        }
    }

    public void AddDataForPromoImageSlider()
    {
            slideModels = new ArrayList<>();
            slideModels.add(new SlideModel("https://scontent-xsp1-1.xx.fbcdn.net/v/t1.6435-9/185300233_2957674807883799_868667496622488565_n.png?_nc_cat=110&ccb=1-3&_nc_sid=730e14&_nc_ohc=8hcDqTC2dI0AX8e9G-x&_nc_ht=scontent-xsp1-1.xx&oh=0144670a64d17d8f9ad030ab3c632fb8&oe=60D12B74", "", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://scontent.fsgn5-7.fna.fbcdn.net/v/t1.6435-9/186486539_2962000987451181_5574183893428808477_n.jpg?_nc_cat=104&ccb=1-3&_nc_sid=730e14&_nc_ohc=1E2IX-q2UHsAX-HXkAo&_nc_ht=scontent.fsgn5-7.fna&oh=ad4cea16142bf5a7a9c96a88ffada497&oe=60D8FE8C", "", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://scontent-xsp1-3.xx.fbcdn.net/v/t1.6435-9/189654320_2957909371193676_2363560169176149542_n.jpg?_nc_cat=109&ccb=1-3&_nc_sid=e3f864&_nc_ohc=M4DNMZxSCvMAX-VPTjq&_nc_ht=scontent-xsp1-3.xx&oh=560db1b6c656d8482f40eeca00a6f027&oe=60D0A310", "", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://scontent.fsgn5-5.fna.fbcdn.net/v/t1.6435-9/186629918_2956213878029892_1702674951943422020_n.jpg?_nc_cat=100&ccb=1-3&_nc_sid=730e14&_nc_ohc=-D1lKSojBkgAX-6lvQU&_nc_ht=scontent.fsgn5-5.fna&oh=c721ae5fece413a70dbcd87c4a9b25ad&oe=60D0A1B3", "", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://scontent-xsp1-3.xx.fbcdn.net/v/t1.6435-9/187515711_2956744857976794_8796511153691441813_n.jpg?_nc_cat=107&ccb=1-3&_nc_sid=730e14&_nc_ohc=dZ7DvN9Ij9MAX-WBKSZ&_nc_ht=scontent-xsp1-3.xx&oh=aa23cf7817739bab7b053c64a8828f87&oe=60D12A2C", "", ScaleTypes.FIT));
            imageSlider_promo.setImageList(slideModels, ScaleTypes.FIT);
    }

    public void ClickPromoItemImageSlider(int i)
    {
        String sDescription = "";
        Intent intent = new Intent(getActivity(), Item_Collection.class);
        int iImage = 0;
        String sName = "";
        switch (i) {
            case 0:
                iImage = R.drawable.van_deal_50_percent;
                sName = "Vạn Deal \n Giảm 50%";
                sDescription = "[DUY NHẤT 24.5 - LÊN ĐƠN \"VỢT\" GẤP 7749 CHIẾC DEAL 50% BẠN ƠI!]\n" +
                        "Mùa dịch này không lên đường được thì mình ở nhà lên đơn thôi chứ còn làm gì nữa nè. Bao la deal đỉnh giảm 50% đang đợi bạn nè: Burger King, Popeyes, Phúc Long, Starbucks, Koi Thé, ... Nhanh tay kẻo hết nhé!\n" +
                        "✅Số lượng mã có hạn\n";
                break;
            case 1:
                iImage = R.drawable.oder_lien_tay;
                sName = "Order Liền Tay \n Nhận Ngay Iphone 12 Pro Max";
                sDescription = "                            [CONTEST]\n" +
                        "ORDER LIỀN TAY - NHẬN NGAY IPHONE 12 PRO MAX\n" +
                        "\n" +
                        "Mùa này không thể ra đường thì thôi mình cứ ở nhà đặt món cho khỏe, sẵn tiện tham gia đua đơn cùng Now biết đâu rinh luôn cái Iphone 12 Pro Max 128GB nè! Không chỉ thế, chỉ cần 01 ĐƠN thôi là bạn cũng có cơ hội trúng ngay các voucher NowFood xịn xò dành cho các bạn may mắn tham gia đường đua kì này. Chờ gì nữa mà không xắn tay áo, bật điện thoại nhanh nhanh order.";
                break;
            case 2:
                iImage = R.drawable.sale_nua_gia;
                sName = "Sale Giữa Năm \n Giảm Nửa Giá";
                sDescription = "\uD83D\uDD256.6 NOWFOOD SALE GIỮA NĂM - GIẢM NỬA GIÁ\uD83D\uDD25\n" +
                        "⚡️Sale tháng 6 chỉ 6k\n" +
                        "⚡️Tiệc nửa giá 66k bao la món đỉnh\n" +
                        "⚡️Deal đếm ngược 1Đ\n" +
                        "\uD83D\uDCA5Bắt trọn voucher khủng mỗi khung giờ vàng\n" +
                        "Cùng NowFood đại náo mùa hè!!!";
                break;
            case 3:
                iImage = R.drawable.chi_tu_0d;
                sName = "Hot Deal \n Chi Tu 0đ";
                sDescription = "[DEAL HOT CHỈ TỪ 0 ĐỒNG - MỘT LÒNG Ở NHÀ LÊN NOW CHỐT ĐƠN]\n" +
                        "Bây giờ ra đường chi nữa khi mà lên Now có quá trời món ngon hấp dẫn, giá nghe hết hồn nè! Săn deal lẹ lẹ kẻo hết nha cả nhà:\n" +
                        ". Sữa Chua Trân Châu Hạ Long Ngon\n" +
                        "- Sữa chua trân châu Thanh Nghi\n" +
                        "- Bún Đậu Gánh Hà Thành\n" +
                        "- Rau Má Plus\n" +
                        "- Bánh Mì Bami Bread\n" +
                        "- Twitter Beans Coffee\n" +
                        "- Bánh Mì Dân Tổ Hà Nội\n" +
                        "- Cơm Gà Bento\n" +
                        "- Sữa Chua Trân Châu Hoàng Gia\n" +
                        "- Tiger Sugar - Đường Nâu Sữa Đài Loan\n" +
                        "✅ Số lượng có hạn";
                break;
            default:
                iImage = R.drawable.o_nha_dat_now;
                sName = "Xa Mặt Cách 2 Mét \n Quán Nhỏ Now Vẫn Giao";
                sDescription = "[ĐỪNG LO LẮNG, QUÁN NHỎ BẠN THÍCH VẪN GIAO TRÊN NOW]\n" +
                        "Bạn ơi đừng lo lắng, quán nhỏ quen thuộc không thể phục vụ tại chỗ thì lên Now đặt là có ngay. Cứ yên tâm ở nhà hủ tíu, bột chiên, xiên que, gỏi cuốn...nóng hổi sẽ được giao tận cửa. Để đảm bảo an toàn trong mùa dịch, Now khuyến khích bạn thanh toán qua Airpay và lựa chọn không tiếp xúc nhé.\n" +
                        "✅ Nhập mã ANTOAN để được giảm ngay 15K phí ship cho đơn từ 0Đ.";
                break;


        }
        intent.putExtra("image", iImage);
        intent.putExtra("name", sName);
        intent.putExtra("description", sDescription);
        startActivity(intent);
    }

    public void AddDataForAdvertisementImageSlider()
    {
        slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://scontent-xsp1-1.xx.fbcdn.net/v/t1.6435-9/188671556_2956750824642864_7898612267439071608_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=e3f864&_nc_ohc=5T1by_CuRnsAX_GYF2T&_nc_oc=AQnSFjs4Cp3BCVQoajaWL2wNwlzn5MkY2JSf_fyMekJgXS1eUX570GYxahi_AOGNFDqgvAj7A_18cJ3GsYmq4AOi&_nc_ht=scontent-xsp1-1.xx&oh=b49ea9fd0dd3fa4375729ca0991d649d&oe=60D1E0DA", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent-xsp1-3.xx.fbcdn.net/v/t1.6435-9/185061072_2955623628088917_8236040101871481045_n.jpg?_nc_cat=107&ccb=1-3&_nc_sid=730e14&_nc_ohc=l1HyBy6ZrLoAX9icslA&_nc_ht=scontent-xsp1-3.xx&oh=62da3033eb8a0fac09aff2750e84ed1a&oe=60D20C0C", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent.fsgn5-5.fna.fbcdn.net/v/t1.6435-9/188460271_2955282381456375_7822490260664823430_n.jpg?_nc_cat=102&ccb=1-3&_nc_sid=730e14&_nc_ohc=3kjXuEqXHQAAX-cvzwt&_nc_ht=scontent.fsgn5-5.fna&oh=fcb070d238afbb7b3e35a56739c93421&oe=60D25137", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent.fsgn5-5.fna.fbcdn.net/v/t1.6435-9/182918562_2950431178608162_2329511474755934128_n.jpg?_nc_cat=100&ccb=1-3&_nc_sid=730e14&_nc_ohc=Jgq6I7C60f0AX-2AN7W&_nc_ht=scontent.fsgn5-5.fna&oh=a57d61aef409cdf117feb5ce0d9c096f&oe=60D19B3E", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent.fsgn5-5.fna.fbcdn.net/v/t1.6435-9/183957113_2952210498430230_5664319190676897654_n.jpg?_nc_cat=100&ccb=1-3&_nc_sid=730e14&_nc_ohc=za8BW5IJPXkAX9pLiwy&_nc_ht=scontent.fsgn5-5.fna&oh=9703325e27053340c449dc54a4b119d4&oe=60CFE4AE", "", ScaleTypes.FIT));
        imageSlider_advertisement.setImageList(slideModels, ScaleTypes.FIT);

    }

    public void ClickAdvertisementItemImageSlider(int i)
    {
        String sDescription = "";
        Intent intent = new Intent(getActivity(), Item_Collection.class);
        int iImage = 0;
        String sName = "";
        switch (i) {
            case 0:
                iImage = R.drawable.quan_van_mo_now_van_giao;
                sName = "Quán Vẫn Mở \n Now Vẫn Giao";
                sDescription = "[QUÁN QUEN YÊU THÍCH VẪN MỞ TRÊN NOW]\n" +
                        "Bạn ơi đừng lo lắng, quán nhỏ quen thuộc không thể phục vụ tại chỗ thì lên Now đặt là có ngay. Hủ tíu, bột chiên, xiên que, gỏi cuốn... ở nhà vẫn ăn ngon. Để đảm bảo an toàn trong mùa dịch, Now khuyến khích bạn thanh toán qua Airpay và lựa chọn không tiếp xúc nhé.\n" +
                        "✅ Nhập mã ANTOAN để được giảm ngay 15K phí ship cho đơn từ 0Đ.";
                break;
            case 1:
                iImage = R.drawable.xe_san_sale;
                sName = "Xế Săn Sale \n Giảm 50%";
                sDescription = "[XẾ SĂN SALE GIẢM 50% - MĂM MĂM QUÀ VẶT DEAL GIẢM NGẬP MẶT]\n" +
                        "Ding dong! Lại tới giờ linh của chị em mình rồi, cả nhà đã có đồ ăn vặt cả chưa, chưa thì lên Now săn ngay deal 50% các món siêu tốn \"enzym\" nè: cóc non sốt muối thái 15k, xôi chiên 6k, bánh tráng 10k, sữa chua kem socola 15k, bắp nếp xào xúc xích 17,5k,... và nhiều nhiều món ngon hấp dẫn khác. Lẹ tay chị em ơi!\n" +
                        "✅Số lượng có hạn";
                break;
            case 2:
                iImage = R.drawable.mon_hau_hoan_vu;
                sName = "Món Hậu Hoàn Vũ";
                sDescription = "Cùng dự đoán \"món hậu\" nào sẽ lên ngôi trong mùa giải năm nay nào cả nhà ơi!\n" +
                        "Link bình chọn: https://nowfood.onelink.me/dBJB/4b6fec7a";
                break;
            case 3:
                iImage = R.drawable.he_ron_ra;
                sName = "Hè Rộn Rã \n Săn Deal Giờ Vàng";
                sDescription = "[HÈ RỘN RÃ - ĐI CHỢ SĂN DEAL GIỜ VÀNG]\n" +
                        "Săn khung giờ vàng với nghìn ưu đãi đến từ NowFresh nha mọi người ơi. Các thương hiệu đã có giảm giá sẵn lên đến 50% từ thực phẩm tươi, thịt, cá, rau củ quả, sữa rồi, giờ chỉ cần áp đúng mã code để được giảm thêm nữa nhé.";
                break;
            default:
                iImage = R.drawable.announcement_now;
                sName = "Thông Báo";
                sDescription = "\uD83D\uDCE3 THÔNG BÁO NOW TẠM NGƯNG TẤT CẢ DỊCH VỤ TẠI ĐÀ NẴNG \uD83D\uDCE3\n" +
                        "\n" +
                        "Với nỗ lực chung tay phòng chống dịch cùng cộng đồng và thực hiện nghiêm túc Công văn số 2938/UBND-KGVX của UBND Thành phố Đà Nẵng, Now thông báo sẽ tạm ngưng tất cả dịch vụ tại Đà Nẵng từ 0h ngày 17.05.2021 & sẽ mở lại dịch vụ khi có thông báo chính thức từ Chính phủ.\n" +
                        "\n" +
                        "Việc tạm ngưng dịch vụ Now có thể sẽ đem đến những bất tiện nhất định, nhưng Now tin rằng quyết định này sẽ góp phần chung tay cùng cộng đồng đẩy lùi dịch bệnh.\n" +
                        "Rất mong Quý Khách hàng thông cảm, đồng thời chủ động bảo vệ sức khoẻ của bản thân, cộng đồng và thực hiện đầy đủ các khuyến cáo từ Bộ Y tế.\n" +
                        "\n" +
                        "Đà Nẵng ơi, cố lên!\n" +
                        "\n" +
                        "Trân trọng,\n" +
                        "Đội ngũ Now.";
                break;


        }
        intent.putExtra("image", iImage);
        intent.putExtra("name", sName);
        intent.putExtra("description", sDescription);
        startActivity(intent);
    }

    public void SetAllData(int a) {
        district_isAvailable = false;
        if(a >= 0) district_isAvailable = true;

        //Search Bar Event
        searchBarAdapter = new SearchBarAdapter(getActivity(), searchBarModels);
        linearLayoutManager_SearchBar = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_SearchBar.setLayoutManager(linearLayoutManager_SearchBar);
        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key_search = editText_search.getText().toString();
                if(district_isAvailable == true) {
                    if (key_search.trim().equals("")) {
                        recyclerView_SearchBar.setVisibility(View.GONE);

                    } else {
                        recyclerView_SearchBar.setVisibility(View.VISIBLE);
                        filter(s.toString(), district_id);

                    }
                }
            }
        });

        //Open Fill Address Activity
        imageView_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runFillAddressActivity();
            }
        });

        //Open Fill Address Activity
        imageView_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { runFillAddressActivity(); }
        });

        //Set Address Data
        textView_addressLine.setText(addressLine);

        //Open Fill Address Activity
        textView_addressLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { runFillAddressActivity(); }
        });

        if(district_isAvailable == true) {
            //Promo Image Slider Event
            AddDataForPromoImageSlider();
            imageSlider_promo.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemSelected(int i) {
                    ClickPromoItemImageSlider(i);
                }
            });

            //RecyclerView List
            AddDataForList();
            listAdapter = new ListAdapter(getActivity(), titles1, images, district_id, district_isAvailable);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
            recyclerView_list.setLayoutManager(gridLayoutManager);
            recyclerView_list.setAdapter(listAdapter);

            //RecyclerView Collection
            collectionModels = new ArrayList<>();
            AddDataForCollection();
            adapter = new CollectionAdapter(collectionModels, getActivity());
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView_Collection.setLayoutManager(linearLayoutManager1);
            recyclerView_Collection.setAdapter(adapter);
            textView_MoreCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ItemList_Collection.class);
                    startActivity(intent);
                }
            });

            //RecyclerView AllRestaurant
            GetDataForAllRestaurants(district_id);
            allRestaurantAdapter = new AllRestaurantAdapter(getActivity(), allRestaurantModelList);
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView_AllRestaurants.setLayoutManager(linearLayoutManager2);
            recyclerView_AllRestaurants.setAdapter(allRestaurantAdapter);
            AllRestaurant_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RestaurantList.class);
                    intent.putExtra("Name Activity", "All Restaurants");
                    intent.putExtra("District ID", district_id);
                    startActivity(intent);
                }
            });

            // ImageSlider Advertisement
            AddDataForAdvertisementImageSlider();
            imageSlider_advertisement.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemSelected(int i) {
                    ClickAdvertisementItemImageSlider(i);
                }
            });

            // RecyclerView Discount Combo Product
            AddDataForDiscountComboProduct(district_id);
            discountComboProductAdapter = new DiscountComboProductAdapter(getActivity(), sortOfProductModelList1);
            LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView_DiscountComboProduct.setLayoutManager(linearLayoutManager3);
            recyclerView_DiscountComboProduct.setAdapter(discountComboProductAdapter);
            DiscountComboProduct_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SortOfProductList.class);
                    intent.putExtra("District ID", district_id);
                    intent.putExtra("Name Activity", "Discount Combo Product");
                    startActivity(intent);
                }
            });

            //RecyclerView Cheapest Product
            AddDataForCheapestProduct(district_id);
            cheapestproductAdapter = new DiscountComboProductAdapter(getActivity(), sortOfProductModelList2);
            LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView_CheapestProduct.setLayoutManager(linearLayoutManager4);
            recyclerView_CheapestProduct.setAdapter(cheapestproductAdapter);
            CheapestProduct_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SortOfProductList.class);
                    intent.putExtra("District ID", district_id);
                    intent.putExtra("Name Activity", "Cheapest Product");
                    startActivity(intent);
                }
            });

            //Tab Layout & ViewPager
            title_KindOfRestaurant.add("Gần tôi");
            title_KindOfRestaurant.add("Bán chạy");
            title_KindOfRestaurant.add("Đánh giá");
            title_KindOfRestaurant.add("Giao nhanh");
            //Set tab layout
            tabLayout_KindOfRestaurant.setupWithViewPager(viewPager_KindOfRestaurant);

            //Prepare view pager
            prepareViewPagerCategories(viewPager_KindOfRestaurant, title_KindOfRestaurant);
        }
    }

    public void setKeyValue(String AddressLine, String NameStreet, int District_ID) {
        this.addressLine = AddressLine;
        this.nameStreet = NameStreet;
        this.district_id = District_ID;
    }

}