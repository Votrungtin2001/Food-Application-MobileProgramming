package com.example.foodapplication.orderFragment.cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.example.foodapplication.MySQL.DatabaseHelper;
import com.example.foodapplication.orderFragment.adapter.CartAdapter;
import com.example.foodapplication.orderFragment.model.OrderModel;
import com.example.foodapplication.R;
import com.example.foodapplication.auth.user;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.foodapplication.HomeFragment.model.ProductModel;

import adapter.TransactionAdapter;
import models.Request;

import static com.example.foodapplication.HomeFragment.RestaurantInformation.addressid_Home;
import static com.example.foodapplication.HomeFragment.RestaurantInformation.addressid_Work;
import static com.example.foodapplication.HomeFragment.adapter.MenuAdapter.productModelList;
import static com.example.foodapplication.MainActivity.customer_id;


public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseHelper databaseHelper;
    public static TextView txtTotalPrice;
    Button btnPlaceOrder;
    OrderModel orderModel;
    CartAdapter cartAdapter;
    private SQLiteDatabase db;
    List<ProductModel> listCart = productModelList;
    private int branch_id;
    ProductModel productModel;
    user user;
    private static final String TAG = "Cart";
    int address_id;
    public static int result = 0;

    Context context = this;
    public static double dTotal = 0;

    public Cart(){ }

    public Cart(int id) { branch_id = id; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        user = new user();

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        databaseHelper = new DatabaseHelper(getBaseContext());
        db = databaseHelper.getReadableDatabase();
        txtTotalPrice = findViewById(R.id.total);

        loadListFood();

        if(addressid_Home > 0) address_id = addressid_Home;
        else address_id = addressid_Work;

        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddOrder();
            }
        });
    }

    public void AddOrder() {
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String sDate = simpleDateFormat.format(currentTime);
            if(listCart.size() > 0){
                Request req = new Request(sDate,
                        customer_id,
                        address_id,
                        dTotal,
                        0);

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                AddOrder(req.getDateTime(), req.getCustomerId(), req.getAddressId(),
                        req.getTotal(), this);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < listCart.size(); i++)
                        {
                            AddOrderDetails(listCart.get(i).getMenu_id(), listCart.get(i).getQuantity(), listCart.get(i).getPrice());
                        }
                    }
                }, 1500);

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        listCart.clear();
                        if (result == 1) Toast.makeText(context, "Đã thêm đơn hàng thành công", Toast.LENGTH_SHORT).show();
                        else if(result == 0) Toast.makeText(context, "Thêm đơn hàng không thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 3500);

            }
            else
                Toast.makeText(getApplicationContext(),"Chưa thêm món ăn nào trong giỏ hàng!",Toast.LENGTH_LONG).show();

    }

    private void loadListFood() {
        cartAdapter = new CartAdapter(listCart, getApplicationContext());
        LinearLayoutManager linearLayoutManager_Menu = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager_Menu);
        recyclerView.setAdapter(cartAdapter);

        int total = 0;
        DecimalFormat decimalFormat = new DecimalFormat( "###,###,###");
        for(int i = 0; i < listCart.size(); i++)
            total += ((listCart.get(i).getPrice()*(listCart.get(i).getQuantity())));

        txtTotalPrice.setText(decimalFormat.format(total));
        dTotal = total;

    }

    private void AddOrder(String datetime, int customer_id, int address_id, double total, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/addOrder.php";
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("Successfully")) {

                }
                else Toast.makeText(context, "Thêm đơn hàng không thành công", Toast.LENGTH_SHORT);

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
                params.put("datetime", String.valueOf(datetime));
                params.put("customer_id", String.valueOf(customer_id));
                params.put("address_id", String.valueOf(address_id));
                params.put("total", String.valueOf(total));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void AddOrderDetails(int menu, int quantity, double price) {
        String url = "https://foodapplicationmobile.000webhostapp.com/addCustomerOrderDetails.php";
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
                if(response.toString().trim().equals("Successfully")) {
                    result = 1;
                }
                else result = 0;
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
                params.put("menu", String.valueOf(menu));
                params.put("quantity", String.valueOf(quantity));
                params.put("price", String.valueOf(price));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

}