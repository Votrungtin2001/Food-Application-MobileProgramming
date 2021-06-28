package com.example.foodapplication.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.Master_MainActivity;
import com.example.foodapplication.mySQL.DatabaseHelper;
import com.example.foodapplication.R;
import com.example.foodapplication.account.fragment.AccountFragment_Master;
import com.example.foodapplication.databinding.FragmentLoginMasterBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.foodapplication.MainActivity.addressLine;
import static com.example.foodapplication.MainActivity.district_id;
import static com.example.foodapplication.MainActivity.master_id;
import static com.example.foodapplication.MainActivity.nameStreet;


public class LoginFragmentMaster extends Fragment {

    @NonNull FragmentLoginMasterBinding binding;
    AccountFragment_Master accountFragment_master = new AccountFragment_Master();
    private final String TAG = "LoginFragmentMaster";

    int role = 0;
    int namefragment_before = 0;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    public LoginFragmentMaster() { }

    public LoginFragmentMaster(int name, int choose_role) {
        this.namefragment_before = name;
        this.role = choose_role;
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginMasterBinding.inflate(inflater, container, false);

        databaseHelper = new DatabaseHelper(getActivity());

        binding.signinMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                if (namefragment_before == 1 && role == 2) {
                    NormalLoginForMasterAndOpenMasterMainActivity(binding.emailMaster.getText().toString().trim(), binding.passwordMaster.getText().toString().trim(),
                            progressDialog, binding.passwordMaster);
                }
                else if(namefragment_before == 2 && role == 2) {
                    NormalLoginForMasterAndBackMasterAccountFragment(binding.emailMaster.getText().toString().trim(), binding.passwordMaster.getText().toString().trim(),
                            progressDialog, binding.passwordMaster);
                }
            }
        });

        binding.signup.setOnClickListener(runSignUpFragment);

        return binding.getRoot();
    }

    View.OnClickListener runSignUpFragment = v -> {
        SignUpFragment newFragment = new SignUpFragment(role, namefragment_before);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    private void NormalLoginForMasterAndOpenMasterMainActivity(String username, String password,
                                                                ProgressDialog progressDialog, EditText editText) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getNormalLoginForMaster.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Failed to log in")) {
                    editText.setText(null);
                    announcement = "Đăng nhập thất bại! Tài khoản hoặc mật khẩu không chính xác!";
                    Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int mas_id = Integer.parseInt(object.getString("_ID"));
                            boolean checkMasterIDIsExist = CheckMasterIDIsExist(mas_id);
                            if(checkMasterIDIsExist) {
                                int id = getIDInMasterTable(mas_id);
                                databaseHelper.updAllAcountLogOutStatus();
                                databaseHelper.updMasterLoginStatus(id);
                                master_id = getMasterIDLogin();
                            }
                            else {
                                databaseHelper.addMaster(mas_id);
                                int id = getIDInMasterTable(mas_id);
                                databaseHelper.updAllAcountLogOutStatus();
                                databaseHelper.updMasterLoginStatus(id);
                                master_id = getMasterIDLogin();
                            }

                            Intent intent = new Intent(getActivity(), Master_MainActivity.class);
                            intent.putExtra("Master ID",master_id);
                            intent.putExtra("AddressLine", addressLine);
                            intent.putExtra("NameStreet", nameStreet);
                            intent.putExtra("District ID", district_id);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
                params.put("username", String.valueOf(username));
                params.put("password", String.valueOf(password));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void NormalLoginForMasterAndBackMasterAccountFragment(String username, String password,
                                                               ProgressDialog progressDialog, EditText editText) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getNormalLoginForMaster.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Failed to log in")) {
                    editText.setText(null);
                    announcement = "Đăng nhập thất bại! Tài khoản hoặc mật khẩu không chính xác!";
                    Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int mas_id = Integer.parseInt(object.getString("_ID"));
                            boolean checkMasterIDIsExist = CheckMasterIDIsExist(mas_id);
                            if(checkMasterIDIsExist) {
                                int id = getIDInMasterTable(mas_id);
                                databaseHelper.updAllAcountLogOutStatus();
                                databaseHelper.updMasterLoginStatus(id);
                                master_id = getMasterIDLogin();
                            }
                            else {
                                databaseHelper.addMaster(mas_id);
                                int id = getIDInMasterTable(mas_id);
                                databaseHelper.updAllAcountLogOutStatus();
                                databaseHelper.updMasterLoginStatus(id);
                                master_id = getMasterIDLogin();
                            }
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_container_master, accountFragment_master);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
                params.put("username", String.valueOf(username));
                params.put("password", String.valueOf(password));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public boolean CheckMasterIDIsExist(int mas_id) {
        db = databaseHelper.getReadableDatabase();
        int count = 0;
        String selectQuery =  "SELECT * FROM MASTER WHERE Master_ID = '" + mas_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

            } while (cursor.moveToNext());

        }
        count = cursor.getCount();
        cursor.close();
        if(count > 0) return true;
        else return false;
    }

    public int getIDInMasterTable(int mas_id) {
        db = databaseHelper.getReadableDatabase();
        int id = 0;
        String selectQuery =  "SELECT * FROM MASTER WHERE Master_ID = '" + mas_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex("_id"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return  id;
    }

    public int getMasterIDLogin() {
        db = databaseHelper.getReadableDatabase();
        int id = 0;
        String selectQuery =  "SELECT * FROM MASTER WHERE STATUS = '" + 1 + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex("Master_ID"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return  id;
    }
}