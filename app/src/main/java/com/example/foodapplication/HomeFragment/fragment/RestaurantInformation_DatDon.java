package com.example.foodapplication.HomeFragment.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.foodapplication.HomeFragment.model.AllRestaurantModel;
import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.foodapplication.HomeFragment.adapter.MenuAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import models.ProductModel;


public class RestaurantInformation_DatDon extends Fragment {

    private RecyclerView recyclerView_Menu;
    private MenuAdapter menuAdapter;
    private List<ProductModel> productModelList = new ArrayList<>();
    private int branch_id;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    private static final String TAG = "RI_DatDon";

    public RestaurantInformation_DatDon() {
    }
    public RestaurantInformation_DatDon(int id) {
        branch_id = id;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_information__dat_don, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        initComponents(view);
        Run();

        return  view;
    }

    public void initComponents(View view) {
        recyclerView_Menu = view.findViewById(R.id.Menu_RecyclerView);
    }

    public void Run() {
        getProducts(branch_id);
        menuAdapter = new MenuAdapter(getActivity(), productModelList);
        LinearLayoutManager linearLayoutManager_Menu = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_Menu.setLayoutManager(linearLayoutManager_Menu);
        recyclerView_Menu.setAdapter(menuAdapter);
    }

    public void getProducts(int id) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getProducts.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                productModelList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String name = object.getString("NAME");
                            String product_description = object.getString("PDESCRIPTION");
                            String menu_description = object.getString("MDESCRIPTION");
                            double price = object.getDouble("PRICE");

                            ProductModel productModel = new ProductModel(image, name, product_description, menu_description, price, id);
                            productModelList.add(productModel);
                            menuAdapter.notifyDataSetChanged();

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
                params.put("branch_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }


}