package com.example.foodapplication.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.foodapplication.home.fragment.BestRatingRestaurantFragment;
import com.example.foodapplication.home.fragment.BestSellerRestaurantFragment;
import com.example.foodapplication.home.fragment.FastestDeliveryRestaurantFragment;
import com.example.foodapplication.home.model.ImageSliderModel;
import com.example.foodapplication.home.adapter.AllRestaurantAdapter;
import com.example.foodapplication.home.adapter.CategoryAdapter;
import com.example.foodapplication.home.adapter.CollectionAdapter;
import com.example.foodapplication.home.adapter.DiscountComboProductAdapter;
import com.example.foodapplication.home.adapter.SearchBarAdapter;
import com.example.foodapplication.home.fragment.NearMeRestaurantsFragment;
import com.example.foodapplication.home.model.AllRestaurantModel;
import com.example.foodapplication.home.model.CollectionModel;
import com.example.foodapplication.home.model.SearchBarModel;
import com.example.foodapplication.home.model.SortOfProductModel;
import com.example.foodapplication.R;
import com.example.foodapplication.home.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodapplication.MainActivity.addressLine;
import static com.example.foodapplication.MainActivity.district_id;
import static com.example.foodapplication.MainActivity.nameStreet;

import static com.example.foodapplication.mySQL.MySQLQuerry.GetDataForAllRestaurants;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetDataForAllImageSliders;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetDataForAllCollections;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetDataForDiscountComboProductAndCheapestProduct;

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
    List<AllRestaurantModel> list = new ArrayList<>();
    LinearLayoutManager linearLayoutManager_SearchBar;

    //RecyclerView Category
    RecyclerView recyclerView_Category;
    List<String> titles1;
    List<Integer> images;
    CategoryAdapter categoryAdapter;

    //RecyclerView Collection
    RecyclerView recyclerView_Collection;
    List<CollectionModel> collectionModelList = new ArrayList<>();
    CollectionAdapter colectionAdapter;
    TextView textView_CollectionTitle;
    TextView textView_MoreCollection;

    //RecyclerView AllRestaurant
    RecyclerView recyclerView_AllRestaurants;
    AllRestaurantAdapter allRestaurantAdapter;
    List<AllRestaurantModel> allRestaurantModelList = new ArrayList<>();
    TextView AllRestaurant_more;
    TextView AllRestaurant_Title;

    //ImageSlider Promo and Advertisement
    List<ImageSliderModel> imageSliderModelList = new ArrayList<>();
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

    private static final String TAG = "HomeFragment";

    public HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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

    private void filter(String toString, List<AllRestaurantModel> list) {
        searchBarModels = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            String name_branch = list.get(i).getNameBranch();
            if(name_branch.toLowerCase().contains(toString.toLowerCase())) {
                String image = list.get(i).getImage();
                int branch_id = list.get(i).getBranchID();
                SearchBarModel searchBarModel = new SearchBarModel(image, name_branch, branch_id);
                searchBarModels.add(searchBarModel);
            }
        }
        searchBarAdapter.filterList(searchBarModels);
        recyclerView_SearchBar.setAdapter(searchBarAdapter);
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
        for(int i = 0; i < 5; i++) {
            String url = imageSliderModelList.get(i).getImage();
            slideModels.add(new SlideModel(url, "", ScaleTypes.FIT));
            imageSlider_promo.setImageList(slideModels, ScaleTypes.FIT);
        }
    }

    public void ClickPromoItemImageSlider(int i)
    {
        Intent intent = new Intent(getActivity(), Details.class);
        String image = "";
        String sName = "";
        String sDescription = "";
        switch (i) {
            case 0:
                image = imageSliderModelList.get(0).getImage();
                sName = imageSliderModelList.get(0).getName();
                sDescription = imageSliderModelList.get(0).getDescription();
                break;

            case 1:
                image = imageSliderModelList.get(1).getImage();
                sName = imageSliderModelList.get(1).getName();
                sDescription = imageSliderModelList.get(1).getDescription();
                break;

            case 2:
                image = imageSliderModelList.get(2).getImage();
                sName = imageSliderModelList.get(2).getName();
                sDescription = imageSliderModelList.get(2).getDescription();
                break;

            case 3:
                image = imageSliderModelList.get(3).getImage();
                sName = imageSliderModelList.get(3).getName();
                sDescription = imageSliderModelList.get(3).getDescription();
                break;

            default:
                image = imageSliderModelList.get(4).getImage();
                sName = imageSliderModelList.get(4).getName();
                sDescription = imageSliderModelList.get(4).getDescription();
                break;
        }
        intent.putExtra("image", image);
        intent.putExtra("name", sName);
        intent.putExtra("description", sDescription);
        startActivity(intent);
    }

    public void AddDataForAdvertisementImageSlider()
    {
        slideModels = new ArrayList<>();
        for(int i = 5; i < imageSliderModelList.size(); i++) {
            String url = imageSliderModelList.get(i).getImage();
            slideModels.add(new SlideModel(url, "", ScaleTypes.FIT));
            imageSlider_advertisement.setImageList(slideModels, ScaleTypes.FIT);
        }
    }

    public void ClickAdvertisementItemImageSlider(int i)
    {
        Intent intent = new Intent(getActivity(), Details.class);
        String image = "";
        String sName = "";
        String sDescription = "";
        switch (i) {
            case 0:
                image = imageSliderModelList.get(5).getImage();
                sName = imageSliderModelList.get(5).getName();
                sDescription = imageSliderModelList.get(5).getDescription();
                break;

            case 1:
                image = imageSliderModelList.get(6).getImage();
                sName = imageSliderModelList.get(6).getName();
                sDescription = imageSliderModelList.get(6).getDescription();
                break;

            case 2:
                image = imageSliderModelList.get(7).getImage();
                sName = imageSliderModelList.get(7).getName();
                sDescription = imageSliderModelList.get(7).getDescription();
                break;

            case 3:
                image = imageSliderModelList.get(8).getImage();
                sName = imageSliderModelList.get(8).getName();
                sDescription = imageSliderModelList.get(8).getDescription();
                break;

            default:
                image = imageSliderModelList.get(9).getImage();
                sName = imageSliderModelList.get(9).getName();
                sDescription = imageSliderModelList.get(9).getDescription();
                break;
        }
        intent.putExtra("image", image);
        intent.putExtra("name", sName);
        intent.putExtra("description", sDescription);
        startActivity(intent);
    }

    public void SetAllData(int a) {
        district_isAvailable = false;
        if(a > 0) district_isAvailable = true;

        //Search Bar Event
        GetDataForAllRestaurants(district_id, list, TAG, getActivity());
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
                        filter(s.toString(), list);
                    }
                }
            }
        });

        if(district_isAvailable == true) {
            GetDataForAllImageSliders(imageSliderModelList, TAG, getActivity());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Promo ImageSlider Event
                    AddDataForPromoImageSlider();
                    imageSlider_promo.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemSelected(int i) {
                            ClickPromoItemImageSlider(i);
                        }
                    });

                    //Advertisement ImageSlider Event
                    AddDataForAdvertisementImageSlider();
                    imageSlider_advertisement.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemSelected(int i) {
                            ClickAdvertisementItemImageSlider(i);
                        }
                    });
                }
            }, 6000);

            //RecyclerView Category
            AddDataForCategory();
            categoryAdapter = new CategoryAdapter(getActivity(), titles1, images, district_id, district_isAvailable);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
            recyclerView_Category.setLayoutManager(gridLayoutManager);
            recyclerView_Category.setAdapter(categoryAdapter);

            //RecyclerView Collection
            colectionAdapter = new CollectionAdapter(collectionModelList, getActivity());
            GetDataForAllCollections(collectionModelList, colectionAdapter, TAG, getActivity());
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView_Collection.setLayoutManager(linearLayoutManager1);
            recyclerView_Collection.setAdapter(colectionAdapter);
            textView_MoreCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListCollection.class);
                    startActivity(intent);
                }
            });

           // RecyclerView AllRestaurant
            allRestaurantAdapter = new AllRestaurantAdapter(getActivity(), allRestaurantModelList);
            GetDataForAllRestaurants(district_id, allRestaurantModelList, TAG, getActivity(), allRestaurantAdapter);
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView_AllRestaurants.setLayoutManager(linearLayoutManager2);
            recyclerView_AllRestaurants.setAdapter(allRestaurantAdapter);
            AllRestaurant_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListRestaurant.class);
                    intent.putExtra("District ID", district_id);
                    startActivity(intent);
                }
            });

            // RecyclerView Discount Combo Product and RecyclerView Cheapest Product
            discountComboProductAdapter = new DiscountComboProductAdapter(getActivity(), sortOfProductModelList1);
            cheapestproductAdapter = new DiscountComboProductAdapter(getActivity(), sortOfProductModelList2);
            GetDataForDiscountComboProductAndCheapestProduct(district_id, sortOfProductModelList1, sortOfProductModelList2,
                    discountComboProductAdapter, cheapestproductAdapter, TAG, getActivity());

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
