package com.example.foodapplication;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.HomeFragment.HomeFragment;
import com.example.foodapplication.orderFragment.OrderFragment;
import com.example.foodapplication.account.fragment.AccountFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static String addressLine;
    public static String nameStreet;
    public static int district_id;
    public static int customer_id;
    public static int master_id;
    public static String stateName;

    public static boolean isCustomerHasAddress = false;
    private final String TAG = "MainActivity";
    public static int addressid_Home = 0;
    public static int addressid_Work = 0;

    Bundle importArgs;

    BottomNavigationView navigation;

    FragmentManager fragmentManager = getSupportFragmentManager();
    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    HomeFragment homeFragment = new HomeFragment();
    AccountFragment accFragment = new AccountFragment();
    OrderFragment orderFragment = new OrderFragment();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transparent Status and Navigation Bar
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_main);

        initComponents();

        Run();
    }

    public void initComponents() {
        navigation = findViewById(R.id.bottom_nav_bar);
    }

    public void Run() {
        addressLine = getIntent().getExtras().getString("AddressLine");
        nameStreet = getIntent().getExtras().getString("NameStreet");
        district_id = getIntent().getExtras().getInt("District ID");
        customer_id = getIntent().getExtras().getInt("Customer ID");

        CheckCustomerHasAddress(customer_id);
        GetCustomerAddressIDWithLabel(customer_id, 1);
        GetCustomerAddressIDWithLabel(customer_id, 2);

        fragmentTransaction.add(R.id.frame_container, homeFragment);
        fragmentTransaction.commit();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Default fragment
        loadFragment(homeFragment);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
            switch(item.getItemId()) {
                case R.id.nav_home:
                    //homeFragment.setArguments(importArgs);
                    loadFragment(homeFragment);
                    break;
                case R.id.nav_account:
                    loadFragment(accFragment);
                    break;
                case R.id.nav_order:
                    loadFragment(orderFragment);
                    break;
                default:
                    return false;
            }
            return true;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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

    private void CheckCustomerHasAddress(int customer_id) {
        String url = "https://foodapplicationmobile.000webhostapp.com/checkCustomerHasAddress.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String announcement = "";
                if(response.toString().trim().equals("true")) {
                    isCustomerHasAddress = true;
                }
                else isCustomerHasAddress = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(customer_id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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