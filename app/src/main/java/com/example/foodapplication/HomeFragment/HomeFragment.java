package com.example.foodapplication.HomeFragment;

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
import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.HomeFragment.adapter.AllRestaurantAdapter;
import com.example.foodapplication.HomeFragment.adapter.CategoryAdapter;
import com.example.foodapplication.HomeFragment.adapter.CollectionAdapter;
import com.example.foodapplication.HomeFragment.adapter.DiscountComboProductAdapter;
import com.example.foodapplication.HomeFragment.adapter.SearchBarAdapter;
import com.example.foodapplication.HomeFragment.fragment.BestRatingRestaurantFragment;
import com.example.foodapplication.HomeFragment.fragment.BestSellerRestaurantFragment;
import com.example.foodapplication.HomeFragment.fragment.FastestDeliveryRestaurantFragment;
import com.example.foodapplication.HomeFragment.fragment.NearMeRestaurantsFragment;
import com.example.foodapplication.HomeFragment.model.AllRestaurantModel;
import com.example.foodapplication.HomeFragment.model.CollectionModel;
import com.example.foodapplication.HomeFragment.model.SearchBarModel;
import com.example.foodapplication.HomeFragment.model.SortOfProductModel;
import com.example.foodapplication.R;
import com.example.foodapplication.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.foodapplication.MainActivity.addressLine;
import static com.example.foodapplication.MainActivity.district_id;
import static com.example.foodapplication.MainActivity.nameStreet;


public class HomeFragment extends Fragment {

    private ImageView imageView_Location;
    private ImageView imageView_Next;
    private TextView textView_addressLine;
    private EditText editText_search;
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

    //RecyclerView Category
    RecyclerView recyclerView_Category;
    List<String> titles1;
    List<Integer> images;
    CategoryAdapter categoryAdapter;

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

        initComponents(view);
        Run();

        return view;
    }

    public void initComponents(View view) {
        //EditText Search
        editText_search = view.findViewById(R.id.editText_SearchBar);

        //RecyclerView SearchBar
        recyclerView_SearchBar = view.findViewById(R.id.recyclerView_SearchBar);

        //ImageView Location
        imageView_Location = (ImageView) view.findViewById(R.id.location_imageView);

        //TextView AddressLine
        textView_addressLine = view.findViewById(R.id.address_Txt);

        //ImageView Next
        imageView_Next = (ImageView) view.findViewById(R.id.next_imageView);

        //TextView Announcement When District Doesn't Have Any Restaurants
        textView_DistrictIsUnavailable = view.findViewById(R.id.textView_DistrictIsUnavailable);

        //Promo ImageSlider
        imageSlider_promo = view.findViewById(R.id.promo_slider);

        //RecyclerView List
        recyclerView_Category = view.findViewById(R.id.list_recyclerView);

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
    }

    public void Run() {
        //Open Mange Address Activity
        imageView_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { runFillAddressActivity(); }
        });

        textView_addressLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { runFillAddressActivity(); }
        });

        imageView_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { runFillAddressActivity(); }
        });

        //TextView AddressLine
        textView_addressLine.setText(addressLine);

        // Set Screen
        district_id = 14;
        if(district_id > 0) district_isAvailable = true;
        SetAllData(district_id);
        setUpSreen(district_isAvailable);
    }


    private void setUpSreen(boolean sign) {
        if (sign == true) {
            imageSlider_promo.setVisibility(View.VISIBLE);
            textView_DistrictIsUnavailable.setVisibility(View.GONE);
            recyclerView_Category.setVisibility(View.VISIBLE);
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
            recyclerView_Category.setVisibility(View.GONE);
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
        if (cursor.getCount() > 0) {
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

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                if (count < 8) {
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

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                if (count < 8) {
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
        int count = 0;
        sortOfProductModelList2 = new ArrayList<>();
        String selectQuery = "SELECT B._id, P.Image, P.Name, B.NAME, M.Price " +
                "FROM (((PRODUCTS P JOIN MENU M ON P._id = M.Product) " +
                "JOIN RESTAURANT R ON M.Restaurant = R._id) " +
                "JOIN BRANCHES B ON R._id = B.Restaurant) " +
                "JOIN ADDRESS A ON B.Address = A._id  " +
                "WHERE M.Price < 20000 AND M.Price >= 15000 AND P.Category != 4 AND P.Category != 12 AND A.District ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                if (count < 8) {
                    count++;
                    String name_product = cursor.getString(cursor.getColumnIndex("Name"));
                    byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                    int branch_id = cursor.getInt(cursor.getColumnIndex("_id"));
                    Bitmap img_bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                    String name_branch = cursor.getString(cursor.getColumnIndex("NAME"));
                    int price = cursor.getInt(cursor.getColumnIndex("Price"));
                    SortOfProductModel sortOfProductModel = new SortOfProductModel(img_bitmap, name_product, name_branch, price, branch_id);
                    sortOfProductModelList2.add(sortOfProductModel);
                }
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

    public void AddDataForCategory()
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
        Intent intent = new Intent(getActivity(), ManageAddress.class);
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
                textView_addressLine.setText(addressLine);
                boolean sign = false;
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
            slideModels.add(new SlideModel("https://scontent-xsp1-1.xx.fbcdn.net/v/t1.6435-0/p526x296/202106671_2982052245446055_8297844154551318828_n.jpg?_nc_cat=103&ccb=1-3&_nc_sid=730e14&_nc_ohc=AwNiJP_i6UkAX8a14Kx&_nc_ht=scontent-xsp1-1.xx&tp=6&oh=0fa7676533e0164e7e3fd3698241f401&oe=60D737F3", "", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://scontent.fsgn5-7.fna.fbcdn.net/v/t1.6435-9/186486539_2962000987451181_5574183893428808477_n.jpg?_nc_cat=104&ccb=1-3&_nc_sid=730e14&_nc_ohc=1E2IX-q2UHsAX-HXkAo&_nc_ht=scontent.fsgn5-7.fna&oh=ad4cea16142bf5a7a9c96a88ffada497&oe=60D8FE8C", "", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://scontent-xsp1-1.xx.fbcdn.net/v/t1.6435-9/201754266_2981137335537546_9104974445809144925_n.jpg?_nc_cat=108&ccb=1-3&_nc_sid=730e14&_nc_ohc=iQZ4vVqgTvUAX9zl2rw&_nc_ht=scontent-xsp1-1.xx&oh=89308a3312f7d3bdf5c5991670d44d59&oe=60D70E59", "", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://scontent-xsp1-2.xx.fbcdn.net/v/t1.6435-9/201337288_2980520765599203_302196789186671101_n.jpg?_nc_cat=101&ccb=1-3&_nc_sid=730e14&_nc_ohc=HL5iygGiqMsAX8KSxqP&tn=oGKLtx5wpmgDHrLD&_nc_ht=scontent-xsp1-2.xx&oh=5e5f304a9feacecd455df6dc79131684&oe=60D693EB", "", ScaleTypes.FIT));
            slideModels.add(new SlideModel("https://scontent-xsp1-2.xx.fbcdn.net/v/t1.6435-9/202051231_2980530008931612_5677314270312715322_n.jpg?_nc_cat=101&ccb=1-3&_nc_sid=730e14&_nc_ohc=dV30_QEjXcwAX_NH1zY&_nc_ht=scontent-xsp1-2.xx&oh=857f279c7361ebaa6fc216fb7d419d97&oe=60D69CCC", "", ScaleTypes.FIT));
            imageSlider_promo.setImageList(slideModels, ScaleTypes.FIT);
    }

    public void ClickPromoItemImageSlider(int i)
    {
        String sDescription = "";
        Intent intent = new Intent(getActivity(), Details.class);
        int iImage = 0;
        String sName = "";
        switch (i) {
            case 0:
                iImage = R.drawable.ghepdoithanhcong;
                sName = "Ghép đôi thành công \n Nhận ngay code xịn";
                sDescription = "[MINIGAME] GHÉP ĐÔI THÀNH CÔNG - NHẬN NGAY CODE XỊN\n" +
                        "Fan cứng Starbucks thuộc làu làu menu đâu rồi nhỉ, test thử xem bạn đã đạt đến trình độ chỉ cần nhìn ngoại hình mỗi ly là biết ngay tên món chưa nha. Hãy cho Now thấy những đáp án đúng của các bạn nào, nhanh tay comment giật ngay code xịn nè!";
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
                iImage = R.drawable.shiphangthaga;
                sName = "Ship hàng thả ga \n Giảm 50%";
                sDescription = "Cuối tháng đánh bay phí vận chuyển cùng ưu đãi GIAO HÀNG NỬA GIÁ\n" +
                        "\n" +
                        "\uD83D\uDCE6Những ngày cuối tháng là bộn bề với một đống hợp đồng, hàng hóa, đồ ăn.... cần phải giao nhanh cho khách. Chỉ cần đặt ngay NowShip và sử dụng dịch vụ giao hàng siêu tốc, giao nhanh cấp tốc chỉ 30 phút là nhận được hàng rồi nè!\n" +
                        "\uD83D\uDD14Đừng quên nhập NOWSHIP5 để giảm ngay 50% (tối đa 5k) khi sử dụng dịch vụ giao hàng siêu tốc\n" +
                        "*Để bảo vệ sức khỏe cho mọi người trong mùa dịch, nên mọi người sử dụng hình thức thanh toán Shopee Pay khi giao hàng nhé!*";
                break;
            case 3:
                iImage = R.drawable.onhamotiec;
                sName = "Ở nhà mở tiệc \n Voucher cực nhiệt";
                sDescription = "MINIGAME [Ở NHÀ MỞ TIỆC - VOUCHER CỰC NHIỆT]\n" +
                        "\n" +
                        "Thành phố vẫn còn giãn cách nhưng game Now thì vẫn luôn \"rộn rã\" nhé mọi người ơi! Đón một tuần mới với \"chiếc\" game đơn giản, dễ như ăn bánh nhưng ẵm voucher thiệt nào.";
                break;
            default:
                iImage = R.drawable.dealxindanloi;
                sName = "Deal Xịn Dẫn Lối \n Món mới mỗi ngày";
                sDescription = "[DEAL XỊN DẪN LỐI - MÓN MỚI MỖI NGÀY - NOW NGAY KẺO HẾT!]\n" +
                        "\n" +
                        "Không cần phải lo nghĩ nhức đầu là hôm nay sẽ ăn gì đây, vì Now tặng bạn mã Deal Xịn với loạt món mới được ưu đãi cực hấp dẫn mỗi ngày đến từ các thương hiệu xịn xò: Starbucks, The Alley, KFC, Yifang, .... Tha hồ ăn ngập mặt, uống tràn bờ đê không lo ngán nhé! Nhanh tay đặt món nhập mã giảm kẻo hết nè!";
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
        slideModels.add(new SlideModel("https://scontent-xsp1-1.xx.fbcdn.net/v/t1.6435-9/201776033_2979765912341355_37434459354534839_n.jpg?_nc_cat=105&ccb=1-3&_nc_sid=730e14&_nc_ohc=Mzn-Asqy0z8AX8UlAWr&_nc_ht=scontent-xsp1-1.xx&oh=590ce6e2fc965816c01b165297653eb9&oe=60D6B3AB", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent-xsp1-3.xx.fbcdn.net/v/t1.6435-9/185061072_2955623628088917_8236040101871481045_n.jpg?_nc_cat=107&ccb=1-3&_nc_sid=730e14&_nc_ohc=l1HyBy6ZrLoAX9icslA&_nc_ht=scontent-xsp1-3.xx&oh=62da3033eb8a0fac09aff2750e84ed1a&oe=60D20C0C", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent.fsgn5-5.fna.fbcdn.net/v/t1.6435-9/188460271_2955282381456375_7822490260664823430_n.jpg?_nc_cat=102&ccb=1-3&_nc_sid=730e14&_nc_ohc=3kjXuEqXHQAAX-cvzwt&_nc_ht=scontent.fsgn5-5.fna&oh=fcb070d238afbb7b3e35a56739c93421&oe=60D25137", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent-xsp1-2.xx.fbcdn.net/v/t1.6435-9/202436419_2979733865677893_6155688824291389949_n.jpg?_nc_cat=102&ccb=1-3&_nc_sid=730e14&_nc_ohc=DP1m6ORc2QAAX9yDE_B&_nc_oc=AQlA3yBiv7bRCNDoQq0GJu_8tEYJJx3M0144rpLLn-ooIIAp8uPKlzbRePV8GgMJ3dxVfQXf7w_5b2OoR1ujRXOh&_nc_ht=scontent-xsp1-2.xx&oh=c0a3f44d8ac0afdd610aef541c9e5713&oe=60D7D36D", "", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent-xsp1-1.xx.fbcdn.net/v/t1.6435-9/201029239_2979545829030030_6039762444511418176_n.jpg?_nc_cat=103&ccb=1-3&_nc_sid=730e14&_nc_ohc=Qj8BFWJSTqAAX_9gbBV&_nc_ht=scontent-xsp1-1.xx&oh=da9c1f50804289564039a2303ff8b0fc&oe=60D63BCD", "", ScaleTypes.FIT));
        imageSlider_advertisement.setImageList(slideModels, ScaleTypes.FIT);
    }

    public void ClickAdvertisementItemImageSlider(int i)
    {
        String sDescription = "";
        Intent intent = new Intent(getActivity(), Details.class);
        int iImage = 0;
        String sName = "";
        switch (i) {
            case 0:
                iImage = R.drawable.doantuoitienboi;
                sName = "Đoán tuổi tiền bối \n Rinh voucher đỉnh";
                sDescription = "[MINIGAME] ĐOÁN TUỔI TIỀN BỐI - RINH VOUCHER ĐỈNH\n" +
                        "\n" +
                        "Bạn ơi đừng để đánh rơi tài năng toán học mà vô tình tụt mất voucher 200k nhá. Bột Chiên Đạt Thành có hơn bao nhiêu năm tuổi, cùng đuổi theo những con số gợi ý tìm ra đoán án đúng nhất ha.";
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
                iImage = R.drawable.ngaycuacha;
                sName = "Ngày của cha \n Bữa ngon tại gia";
                sDescription = "[NOW KHAO MÓN NGON MỪNG NGÀY CỦA CHA - TIỆC TẠI GIA CHỈ 116K]\n" +
                        "\n" +
                        "Đã bao lâu bạn chưa bày tỏ yêu thương đến cha? Nhân dịp Ngày của Cha, chi bằng đặt ngay vài món ngon để khao cả nhà và bày tỏ tình \"củm\" đến \"người hùng thầm lặng\" của cả gia đình nào:\n" +
                        "✅Nhập mã CHAYEU giảm 30k cho đơn từ 146k\n" +
                        "✅Áp dụng cho các quán chọn lọc. Số lượng có hạn, nhanh tay Now ngay!";
                break;
            default:
                iImage = R.drawable.monchanaigiainhiet;
                sName = "Món chân ái giải nhiệt";
                sDescription = "Bình chọn Món Xịn Mùa Hè: đâu mới là chân ái vào mùa hè của bạn??\n" +
                        "\n" +
                        "\uD83C\uDFCD Dù là món gì thì Deal xịn cũng chiều được bạn hết nhen";
                break;


        }
        intent.putExtra("image", iImage);
        intent.putExtra("name", sName);
        intent.putExtra("description", sDescription);
        startActivity(intent);
    }

    public void SetAllData(int a) {
        district_isAvailable = false;
        if(a > 0) district_isAvailable = true;

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

        if(district_isAvailable == true) {
            //Promo Image Slider Event
            AddDataForPromoImageSlider();
            imageSlider_promo.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemSelected(int i) {
                    ClickPromoItemImageSlider(i);
                }
            });

            //RecyclerView Category
            AddDataForCategory();
            categoryAdapter = new CategoryAdapter(getActivity(), titles1, images, district_id, district_isAvailable);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
            recyclerView_Category.setLayoutManager(gridLayoutManager);
            recyclerView_Category.setAdapter(categoryAdapter);

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
                    Intent intent = new Intent(getActivity(), ListCollection.class);
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
                    Intent intent = new Intent(getActivity(), ListRestaurant.class);
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
}
