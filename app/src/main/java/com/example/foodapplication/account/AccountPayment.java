package com.example.foodapplication.account;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class AccountPayment extends Fragment {
    private int user_id = -1;
    public static double credit = 0;
    TextView txtAccountPaymentCoins, txtAccountPaymentHistory, txtAccountPaymentTopup;
    private final String TAG = "AccountPayment";

    public AccountPayment() {

    }

    public static AccountPayment newInstance() {
        return new AccountPayment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.customer_id > 0)
            user_id = MainActivity.customer_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_payment, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_payment));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        txtAccountPaymentCoins = view.findViewById(R.id.txtAccountPaymentCoins);
        txtAccountPaymentTopup = view.findViewById(R.id.txtAccountPaymentTopup);
        txtAccountPaymentTopup.setClickable(true);
        txtAccountPaymentTopup.setOnClickListener(runTopupFragment);
        txtAccountPaymentHistory = view.findViewById(R.id.txtAccountPaymentHistory);
        txtAccountPaymentHistory.setClickable(true);
        txtAccountPaymentHistory.setOnClickListener(runHistoryFragment);

        txtAccountPaymentCoins.setText("Tiền trong tài khoản: Đang tải lên...");
        if (user_id != -1) {
            GetCredit(user_id);
        }

        return view;
    }

    View.OnClickListener runTopupFragment = v -> {
        AccountPaymentTopup fragment = new AccountPaymentTopup();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runHistoryFragment = v -> {
        AccountPaymentHistory fragment = new AccountPaymentHistory();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };

    public void GetCredit(int id) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAccountInformation.php";
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

                            credit = object.getDouble("CREDIT");
                            DecimalFormat decimalFormat = new DecimalFormat( "###,###,###" );
                            txtAccountPaymentCoins.setText("Tiền trong tài khoản: " + decimalFormat.format(credit));
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
                params.put("customer_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}