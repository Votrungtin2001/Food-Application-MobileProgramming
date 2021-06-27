package com.example.foodapplication.orderFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.R;
import com.example.foodapplication.orderFragment.adapter.OrderViewHolder;
import com.example.foodapplication.orderFragment.models.OrderModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.foodapplication.MainActivity.addressid_Home;
import static com.example.foodapplication.MainActivity.addressid_Work;
import static com.example.foodapplication.MainActivity.customer_id;
import static com.example.foodapplication.MySQL.MySQLQuerry.GetOrders;


public class OrderFragment extends Fragment {

    public RecyclerView recyclerView;
    List<OrderModel> orderModelList = new ArrayList<>();
    OrderViewHolder orderViewHolder;
    LinearLayout linearLayout;
    LinearLayoutManager linearLayoutManager_Menu;

    boolean order_isAvailable = false;
    private final String TAG = "OrderFragment";

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.listOrders);
        linearLayout = view.findViewById(R.id.none);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        CheckCustomerOrderWithCustomerID(customer_id);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(order_isAvailable) {
                    progressDialog.show();
                    SetAllData(progressDialog);
                }
               setUpSreen(order_isAvailable);

            }
        }, 1000);

        return view;
    }

    public void SetAllData(ProgressDialog progressDialog) {
        orderModelList = new ArrayList<>();
        orderViewHolder = new OrderViewHolder(getActivity(), orderModelList);
        if(addressid_Home > 0) {
            GetOrders(customer_id, addressid_Home, orderModelList, orderViewHolder,progressDialog, TAG, getActivity());
        }
        else  GetOrders(customer_id, addressid_Work, orderModelList, orderViewHolder,progressDialog, TAG, getActivity());
        linearLayoutManager_Menu = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager_Menu);
        recyclerView.setAdapter(orderViewHolder);
    }

    private void setUpSreen(boolean sign) {
        if (sign == true) {
            recyclerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void CheckCustomerOrderWithCustomerID(int customer_id) {
        String url = "https://foodapplicationmobile.000webhostapp.com/checkCustomerOrderWithCustomerID.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String announcement = "";
                if(response.toString().trim().equals("true")) {
                    order_isAvailable = true;
                }
                else order_isAvailable = false;
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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

}