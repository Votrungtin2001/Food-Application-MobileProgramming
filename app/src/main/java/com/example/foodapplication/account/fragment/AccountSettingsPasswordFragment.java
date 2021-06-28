package com.example.foodapplication.account.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import java.util.HashMap;
import java.util.Map;

import static com.example.foodapplication.mySQL.MySQLQuerry.UpdateCustomerPassword;
import static com.example.foodapplication.mySQL.MySQLQuerry.UpdateMasterPassword;

public class AccountSettingsPasswordFragment extends Fragment {
    EditText txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    Button btnSavePassword;
    int user_id = -1;
    boolean IsUpdatingCustomer;

    String currentPassword = "";
    private final String TAG = "AccountSettingPF";

    public AccountSettingsPasswordFragment() { }

    public static AccountSettingsPasswordFragment newInstance() {
        return new AccountSettingsPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.customer_id > 0) {
            user_id = MainActivity.customer_id;
            IsUpdatingCustomer = true;
        }
        else {
            if (MainActivity.master_id > 0) {
                user_id = MainActivity.master_id;
                IsUpdatingCustomer = false;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings_password, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_settings_account_password_desc));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        txtCurrentPassword = view.findViewById(R.id.txtCurrentPassword);
        txtNewPassword = view.findViewById(R.id.txtNewPassword);
        txtConfirmPassword = view.findViewById(R.id.txtConfirmPassword);
        btnSavePassword = view.findViewById(R.id.btnSavePassword);
        btnSavePassword.setOnClickListener(onPasswordSave);

        return view;
    }

    View.OnClickListener onPasswordSave = v -> {
        if (user_id != -1) {
            if (IsUpdatingCustomer)
                UpdatingCustomerPassword(user_id);
            else UpdatingMasterPassword(user_id);

        }
        else
            Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
    };

    private void UpdatingCustomerPassword(int id) {
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

                            currentPassword = object.getString("PASSWORD");
                            if (txtCurrentPassword.getText().toString().equals(currentPassword)) {
                                if (txtConfirmPassword.getText().toString().equals(txtNewPassword.getText().toString())) {
                                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();
                                    UpdateCustomerPassword(id, txtNewPassword.getText().toString(), progressDialog, TAG, getActivity());
                                    FragmentManager fragmentManager = getParentFragmentManager();
                                    fragmentManager.popBackStack(null, 0);
                                } else {
                                    txtNewPassword.setText("");
                                    txtConfirmPassword.setText("");
                                    Toast.makeText(getContext(), "Mật khẩu xác nhận không trùng mật khẩu mới!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                txtCurrentPassword.setText("");
                                txtNewPassword.setText("");
                                txtConfirmPassword.setText("");
                                Toast.makeText(getContext(), "Sai mật khẩu hiện tại!", Toast.LENGTH_SHORT).show();
                            }
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

    private void UpdatingMasterPassword(int id) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getMasterAccountInformation.php";
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

                            currentPassword = object.getString("PASSWORD");
                            if (txtCurrentPassword.getText().toString().equals(currentPassword)) {
                                if (txtConfirmPassword.getText().toString().equals(txtNewPassword.getText().toString())) {
                                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();
                                    UpdateMasterPassword(id, txtNewPassword.getText().toString(), progressDialog, TAG, getActivity());
                                    FragmentManager fragmentManager = getParentFragmentManager();
                                    fragmentManager.popBackStack(null, 0);
                                } else {
                                    txtNewPassword.setText("");
                                    txtConfirmPassword.setText("");
                                    Toast.makeText(getContext(), "Mật khẩu xác nhận không trùng mật khẩu mới!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                txtCurrentPassword.setText("");
                                txtNewPassword.setText("");
                                txtConfirmPassword.setText("");
                                Toast.makeText(getContext(), "Sai mật khẩu hiện tại!", Toast.LENGTH_SHORT).show();
                            }
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
                params.put("master_id", String.valueOf(id));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}