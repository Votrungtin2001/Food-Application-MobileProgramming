package com.example.foodapplication.HomeFragment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.orderFragment.cart.Cart;
import com.example.foodapplication.MySQL.DatabaseHelper;
import com.example.foodapplication.R;
import com.example.foodapplication.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.foodapplication.HomeFragment.fragment.RestaurantInformation_DatDon;
import com.example.foodapplication.HomeFragment.fragment.RestaurantInformation_ThongTin;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.foodapplication.MainActivity.customer_id;
import static com.example.foodapplication.MySQL.MySQLQuerry.GetBranchName;
import static com.example.foodapplication.MySQL.MySQLQuerry.GetRestaurantImage;

public class RestaurantInformation extends AppCompatActivity {

    ImageView imageView_RestaurantInformation;
    TextView textView_BranchName;
    ImageView imageView_Back;

    FloatingActionButton cart;

    TabLayout tabLayout_RestaurantInformation;
    ArrayList<String> title_TabLayout = new ArrayList<>();
    ViewPager viewPager_RestaurantInformation;
    ViewPagerAdapter viewPagerAdapter;

    int branch_id;
    String image_restaurant = "";
    String branch_name = "";
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    Bitmap bitmap_restaurant;

    Dialog AnnouncementDialog;

    private static final String TAG = "RestaurantInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_restaurant_information);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

        initComponents();

        Run();
    }

    public void initComponents() {
        textView_BranchName = findViewById(R.id.BranchName_RestaurantInformation);

        imageView_Back = findViewById(R.id.Back_RestaurantInformation);
        imageView_RestaurantInformation = findViewById(R.id.imageView_RestaurantInformation);

        tabLayout_RestaurantInformation = (TabLayout) findViewById(R.id.RestaurantInformation_TabLayout);

        viewPager_RestaurantInformation = (ViewPager) findViewById(R.id.RestaurantInformation_ViewPager);

        cart = findViewById(R.id.btnCart);

        AnnouncementDialog = new Dialog(this);
        AnnouncementDialog.setContentView(R.layout.custom_popup_require_login);
    }

    public void Run() {
        imageView_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        branch_id = getIntent().getIntExtra("Branch ID", 0);

        textView_BranchName.setText("");
        GetBranchName(branch_id, textView_BranchName, TAG, this);

        imageView_RestaurantInformation.setImageResource(R.drawable.noimage_restaurant);
        GetRestaurantImage(branch_id, imageView_RestaurantInformation, TAG, this);

        title_TabLayout.add("Đặt đơn");
        title_TabLayout.add("Thông tin");

        //Set tablayout
        tabLayout_RestaurantInformation.setupWithViewPager(viewPager_RestaurantInformation);
        //Prepare viewpager
        prepareViewPagerRestaurantInformation(viewPager_RestaurantInformation, title_TabLayout);

        //Code Minh Thi

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(customer_id > 0) {
                    boolean checkCustomerHasAddress = CheckCustomerHasAddress(customer_id);
                    if(checkCustomerHasAddress == true) {
                        Intent intent = new Intent(getApplication(), Cart.class);
                        startActivity(intent);
                         }
                  else ShowPopUpRequireAddress();
                }
            else ShowPopUpRequireLogin();
           }
        });
    }

    private void prepareViewPagerRestaurantInformation(ViewPager viewPager_restaurantInformation, ArrayList<String> title_tabLayout) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for(int i = 0; i < title_TabLayout.size(); i++)
        {
            if(i == 0) {
                RestaurantInformation_DatDon fragment1 = new RestaurantInformation_DatDon(branch_id);
                viewPagerAdapter.addFragment(fragment1, title_TabLayout.get(i));
            }

            if (i == 1) {
                RestaurantInformation_ThongTin fragment2 = new RestaurantInformation_ThongTin(branch_id);
                viewPagerAdapter.addFragment(fragment2, title_TabLayout.get(i));
            }

        }
        viewPager_RestaurantInformation.setAdapter(viewPagerAdapter);
    }

    private void transparentStatusAndNavigation()
    {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        //Change status bar text to black when screen is light white
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void ShowPopUpRequireLogin() {
        TextView textView_Close;
        textView_Close = (TextView) AnnouncementDialog.findViewById(R.id.Close_PopUpLogin);
        textView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnnouncementDialog.dismiss();
            }
        });

        TextView textView_Text;
        textView_Text = (TextView) AnnouncementDialog.findViewById(R.id.TextView_PopUp_RequireLogin);
        textView_Text.setText("Vui lòng đăng nhập với vai trò là khách hàng!!!");

        AnnouncementDialog.show();
    }

    public void ShowPopUpRequireAddress() {
        TextView textView_Close;
        textView_Close = (TextView) AnnouncementDialog.findViewById(R.id.Close_PopUpLogin);
        textView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnnouncementDialog.dismiss();
            }
        });

        TextView textView_Text;
        textView_Text = (TextView) AnnouncementDialog.findViewById(R.id.TextView_PopUp_RequireLogin);
        textView_Text.setText("Vui lòng thêm địa chỉ giao hàng!!!    ");

        AnnouncementDialog.show();
    }

    public boolean CheckCustomerHasAddress(int customer_id) {
        int count = 0;

        String selectQuery = "SELECT * FROM CUSTOMER_ADDRESS WHERE Customer='" + customer_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

            } while (cursor.moveToNext());

        }
        count = cursor.getCount();
        cursor.close();
        if(count > 0) return true;
        else return false;

    }
}