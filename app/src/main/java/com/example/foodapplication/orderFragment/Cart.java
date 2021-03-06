package com.example.foodapplication.orderFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.foodapplication.R;
import com.example.foodapplication.home.model.ProductModel;
import com.example.foodapplication.orderFragment.adapter.CartAdapter;
import com.example.foodapplication.orderFragment.models.Request;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.foodapplication.MainActivity.addressid_Home;
import static com.example.foodapplication.MainActivity.addressid_Work;
import static com.example.foodapplication.MainActivity.customer_id;
import static com.example.foodapplication.MainActivity.isCustomerHasPhone;
import static com.example.foodapplication.home.adapter.MenuAdapter.productModelList;

public class Cart extends AppCompatActivity {

    private static final String TAG = "Cart";
    RecyclerView recyclerView;
    public static TextView txtTotalPrice;
    Button btnPlaceOrder;
    ImageView imageViewBack;
    CartAdapter cartAdapter;
    Dialog AnnouncementDialog;
    List<ProductModel> listCart = productModelList;
    int address_id;
    public static int result = 0;

    Context context = this;
    public static double dTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        txtTotalPrice = findViewById(R.id.total);
        imageViewBack = findViewById(R.id.back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AnnouncementDialog = new Dialog(this);
        AnnouncementDialog.setContentView(R.layout.custom_popup_require_login);

        loadListFood();

        if(addressid_Home > 0) address_id = addressid_Home;        else address_id = addressid_Work;

        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkCustomerHasPhone = isCustomerHasPhone;
                if(checkCustomerHasPhone) {
                    AddOrder();
                }
                else {
                    ShowPopUpRequirePhone();
                }
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
                progressDialog.setMessage("Xin vui l??ng ch??? trong gi??y l??t...");
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
                        if (result == 1) Toast.makeText(context, "???? th??m ????n h??ng th??nh c??ng!", Toast.LENGTH_SHORT).show();
                        else if(result == 0) Toast.makeText(context, "Th??m ????n h??ng kh??ng th??nh c??ng!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 5000);

            }
            else
                Toast.makeText(getApplicationContext(),"Vui l??ng th??m m??n ??n tr?????c khi ?????t h??ng!",Toast.LENGTH_LONG).show();

    }

    private void loadListFood() {
        cartAdapter = new CartAdapter(listCart, getApplicationContext());
        LinearLayoutManager linearLayoutManager_Menu = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager_Menu);
        recyclerView.setAdapter(cartAdapter);

        int total = 0;
        DecimalFormat decimalFormat = new DecimalFormat( "###,###,###");
        for(int i = 0; i < listCart.size(); i++) {
            total += ((listCart.get(i).getPrice()*(listCart.get(i).getQuantity())));
        }

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
                else Toast.makeText(context, "Th??m ????n h??ng kh??ng th??nh c??ng", Toast.LENGTH_SHORT);

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

    public void ShowPopUpRequirePhone() {
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
        textView_Text.setText("Vui l??ng th??m s??? ??i???n tho???i!!!   ");

        AnnouncementDialog.show();
    }

}