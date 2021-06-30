package com.example.foodapplication.home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.foodapplication.R;
import com.example.foodapplication.home.adapter.ViewPagerAdapter;
import com.example.foodapplication.home.fragment.RestaurantInformation_DatDon;
import com.example.foodapplication.home.fragment.RestaurantInformation_ThongTin;
import com.example.foodapplication.orderFragment.Cart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.example.foodapplication.MainActivity.customer_id;
import static com.example.foodapplication.MainActivity.isCustomerHasAddress;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetBranchName;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetRestaurantImage;

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

    Dialog AnnouncementDialog;

    private static final String TAG = "RestaurantInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_restaurant_information);

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

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Xin vui lòng chờ trong giây lát...");
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 2000);

        //Code Minh Thi

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(customer_id > 0) {
                    boolean checkCustomerHasAddress = isCustomerHasAddress;
                    if(checkCustomerHasAddress) {
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
                RestaurantInformation_ThongTin fragment2 = new RestaurantInformation_ThongTin(branch_id, 1);
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

}