package com.example.foodapplication.account.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.foodapplication.account.fragment.AccountPayment.credit;


public class AccountPaymentTopup extends Fragment {
    Button btnAdd50k, btnAdd100k, btnAdd200k, btnAdd300k, btnAdd500k, btnAdd1M, btnAdd2M, btnAdd5M, btnAdd10M, btnDeposit;
    EditText txtTopupAmount;
    int user_id = -1;

    private final String TAG = "AccountPaymentTopUp";

    public AccountPaymentTopup() {

    }

    public static AccountPaymentTopup newInstance() {
        return new AccountPaymentTopup();
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
        View view = inflater.inflate(R.layout.fragment_account_payment_topup, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_payment_topup));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        btnAdd50k = view.findViewById(R.id.btnAdd50k);
        btnAdd50k.setOnClickListener(onAmountClickListener);
        btnAdd100k = view.findViewById(R.id.btnAdd100k);
        btnAdd100k.setOnClickListener(onAmountClickListener);
        btnAdd200k = view.findViewById(R.id.btnAdd200k);
        btnAdd200k.setOnClickListener(onAmountClickListener);
        btnAdd300k = view.findViewById(R.id.btnAdd300k);
        btnAdd300k.setOnClickListener(onAmountClickListener);
        btnAdd500k = view.findViewById(R.id.btnAdd500k);
        btnAdd500k.setOnClickListener(onAmountClickListener);
        btnAdd1M = view.findViewById(R.id.btnAdd1M);
        btnAdd1M.setOnClickListener(onAmountClickListener);
        btnAdd2M = view.findViewById(R.id.btnAdd2M);
        btnAdd2M.setOnClickListener(onAmountClickListener);
        btnAdd5M = view.findViewById(R.id.btnAdd5M);
        btnAdd5M.setOnClickListener(onAmountClickListener);
        btnAdd10M = view.findViewById(R.id.btnAdd10M);
        btnAdd10M.setOnClickListener(onAmountClickListener);
        btnDeposit = view.findViewById(R.id.btnDeposit);
        btnDeposit.setOnClickListener(onDepositClick);

        txtTopupAmount = view.findViewById(R.id.txtTopupAmount);

        return view;
    }

    View.OnClickListener onAmountClickListener = v -> {
        Button btn = (Button) v;
        txtTopupAmount.setText(btn.getText());
    };

    View.OnClickListener onDepositClick = v -> {
        if (!txtTopupAmount.getText().toString().equals("")) {
            if (user_id != -1) {
                double topup = Double.parseDouble(txtTopupAmount.getText().toString());
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                String sDate = simpleDateFormat.format(date);

                double customer_credit = credit;
                customer_credit += topup;

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Xin vui lòng chờ trong giây lát...");
                progressDialog.show();
                UpdateCreditAndAddTransaction(user_id, customer_credit, topup, sDate, progressDialog);

            } else
                Toast.makeText(getContext(), "User not found. Did you forget to log in?", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getContext(), "Xin hãy nhập lượng tiền cần nạp!", Toast.LENGTH_LONG).show();
    };

    public void UpdateCreditAndAddTransaction(int user_id, double customer_credit, double transaction_credit, String date, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateCreditAndAddTransaction.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "Nạp tiền thành công!";
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else announcement = "Lỗi kết nối mạng!!!";
                Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(user_id));
                params.put("customer_credit", String.valueOf(customer_credit));
                params.put("transaction_credit", String.valueOf(transaction_credit));
                params.put("date", String.valueOf(date));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}