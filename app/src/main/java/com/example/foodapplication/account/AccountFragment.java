package com.example.foodapplication.account;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.MySQL.DatabaseHelper;
import com.example.foodapplication.MySQL.FoodManagementContract;
import com.example.foodapplication.R;
import com.example.foodapplication.auth.LoginFragment;
import com.example.foodapplication.auth.LoginFragmentMaster;

import java.util.HashMap;
import java.util.Map;

import static com.example.foodapplication.MainActivity.customer_id;
import static com.example.foodapplication.MainActivity.isCustomerHasAddress;
import static com.example.foodapplication.MySQL.MySQLQuerry.LogOutCustomerAccount;
import static com.example.foodapplication.MySQL.MySQLQuerry.SetCustomerAccountInformationInAccountFragment;

public class AccountFragment extends Fragment {
    Button btnPayment, btnAddress, btnPolicy, btnSettings, btnAbout, btnLogout;
    Fragment newFragment;
    TextView txtlogin;
    ImageView imgUser;

    Dialog LoginDialog;

    private final String TAG = "AccountFragment";
    boolean checkCustomerHasAddress = false;

    int choose_role = 0;
    int namefragment = 0;
    public AccountFragment(int role,int name) {
        this.choose_role = role;
        this.namefragment = name;}
    public AccountFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        imgUser = view.findViewById(R.id.imgUser);

        LoginDialog = new Dialog(getActivity());
        LoginDialog.setContentView(R.layout.custom_pop_up_login);

        btnPayment = view.findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(runPaymentFragment);

        btnAddress = view.findViewById(R.id.btnAddress);
        btnAddress.setOnClickListener(runAddressFragment);

        btnSettings = view.findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(runSettingsFragment);

        btnAbout = view.findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(runAboutFragment);

        btnPolicy = view.findViewById(R.id.btnPolicy);
        btnPolicy.setOnClickListener(runPolicyFragment);

        btnLogout = (Button) view.findViewById(R.id.btnLogout);

        txtlogin = view.findViewById(R.id.txtName);
        if (customer_id > 0) {
            CheckCustomerHasAddress(customer_id);
            SetCustomerAccountInformationInAccountFragment(customer_id, txtlogin, imgUser, btnLogout, TAG, getActivity());
        }
        else {
            txtlogin.setText(getResources().getString(R.string.activity_account_login_name));
            imgUser.setImageResource(0);
            btnLogout.setVisibility(View.INVISIBLE);
        }
        txtlogin.setOnClickListener(v -> {
            if (customer_id > 0) {
                Fragment fragment = new AccountSettingsInfoFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                        .addToBackStack(null)
                        .commit();
            }
            else
                ShowPopUpLogin(v);
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                LogOutCustomerAccount(txtlogin, imgUser, btnLogout, progressDialog, TAG, getActivity());
            }
        });

        isCustomerHasAddress = checkCustomerHasAddress;

        return view;
    }

    View.OnClickListener runAddressFragment = v -> {
        if (customer_id > 0) {
            newFragment = new AccountAddressFragment();
            // source: https://stackoverflow.com/questions/21028786/how-do-i-open-a-new-fragment-from-another-fragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), newFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
        else
            Toast.makeText(getContext(), "Bạn không thể dùng chức năng này vì bạn chưa đăng nhập.", Toast.LENGTH_LONG).show();
    };

    View.OnClickListener runPaymentFragment = v -> {
        if (customer_id > 0) {
            newFragment = new AccountPayment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), newFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
        else
            Toast.makeText(getContext(), "Bạn không thể dùng chức năng này vì bạn chưa đăng nhập.", Toast.LENGTH_LONG).show();
    };

    View.OnClickListener runSettingsFragment = v -> {
        if (customer_id > 0) {
            newFragment = new AccountSettings();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), newFragment, null)
                    .addToBackStack(null)
                    .commit();
        } else
            Toast.makeText(getContext(), "Bạn không thể dùng chức năng này vì bạn chưa đăng nhập.", Toast.LENGTH_LONG).show();
    };

    View.OnClickListener runAboutFragment = v -> {
        newFragment = new AccountAbout();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runPolicyFragment = v -> {
        newFragment = new AccountPolicyFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runLoginFragment = v -> {
        newFragment = new LoginFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();

        LoginDialog.dismiss();
    };

    public void ShowPopUpLogin(View v) {
        TextView textView_Close;
        textView_Close = (TextView) LoginDialog.findViewById(R.id.Close_PopUpLogin);
        textView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog.dismiss();
            }
        });

        ImageView imageView_CustomerOption;
        imageView_CustomerOption = (ImageView) LoginDialog.findViewById(R.id.ImageView_Customer_PopUpLogin);
        imageView_CustomerOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_role = 1;
                namefragment = 1;
                newFragment = new LoginFragment(namefragment, choose_role);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                        .addToBackStack(null)
                        .commit();
                LoginDialog.dismiss();
            }
        });

        ImageView imageView_MasterOption;
        imageView_MasterOption = (ImageView) LoginDialog.findViewById(R.id.ImageView_Master_PopUpLogin);
        imageView_MasterOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_role = 2;
                newFragment = new LoginFragmentMaster(choose_role);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                        .addToBackStack(null)
                        .commit();
                LoginDialog.dismiss();
            }
        });

        LoginDialog.show();
    }

    private void CheckCustomerHasAddress(int customer_id) {
        String url = "https://foodapplicationmobile.000webhostapp.com/checkCustomerHasAddress.php\n";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String announcement = "";
                if(response.toString().trim().equals("true")) {
                    checkCustomerHasAddress = true;
                }
                else checkCustomerHasAddress = false;
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