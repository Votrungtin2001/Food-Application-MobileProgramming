package com.example.foodapplication.HomeFragment.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RestaurantInformation_ThongTin extends Fragment {

    TextView textView_address;
    TextView textView_openingtime;

    int branch_id;
    String branch_address = "";
    String restaurant_openingtime = "";

    private static final String TAG = "RI_ThongTin";

    public RestaurantInformation_ThongTin(int id) {
        this.branch_id = id;

    }

    public RestaurantInformation_ThongTin() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_information__thong_tin, container, false);

        initComponents(view);
        Run();

        return  view;
    }

    public void initComponents(View view) {
        textView_address = view.findViewById(R.id.RestaurantInformation_Address);
        textView_openingtime = view.findViewById(R.id.RestaurantInformation_OpeningTime);
    }

    public void Run() {
        textView_address.setText(branch_address);
        getBranchAddress(branch_id);

        textView_openingtime.setText(restaurant_openingtime);
        getOpeningTime(branch_id);
    }


    public void getBranchAddress(int id) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getBranchAddress.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            branch_address = object.getString("ADDRESS");
                            if(!branch_address.trim().equals("")) textView_address.setText(branch_address);

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
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public void getOpeningTime(int id) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getRestaurantOpeningTime.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            restaurant_openingtime = object.getString("OPENING_TIMES");
                            if(!restaurant_openingtime.trim().equals("")) textView_openingtime.setText("Giờ mở cửa \t" + restaurant_openingtime);

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
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}