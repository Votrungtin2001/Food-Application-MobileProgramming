package com.example.foodapplication.HomeFragment;

import android.app.Dialog;
import android.content.Context;
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
import com.example.foodapplication.Transaction;
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
import static com.example.foodapplication.MainActivity.isCustomerHasAddress;
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
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    public static int addressid_Home = 0;
    public static int addressid_Work = 0;

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

        GetCustomerAddressIDWithLabel(customer_id, 1);
        GetCustomerAddressIDWithLabel(customer_id, 2);
        //Code Minh Thi

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(customer_id > 0) {
                    boolean checkCustomerHasAddress = isCustomerHasAddress;
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

    public void GetCustomerAddressIDWithLabel(int customer_id, int address_label) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAddressIDWithLabel.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int address_id = object.getInt("_ID");
                            if(address_label == 1) {
                                addressid_Home = address_id;
                            }
                            else addressid_Work = address_id;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(customer_id));
                params.put("address_label", String.valueOf(address_label));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}