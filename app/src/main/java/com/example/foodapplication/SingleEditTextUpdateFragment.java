package com.example.foodapplication;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.MySQL.DatabaseHelper;
import com.example.foodapplication.MySQL.FoodManagementContract;

import java.util.HashMap;
import java.util.Map;

import static com.example.foodapplication.MySQL.MySQLQuerry.GetCustomerEmail;
import static com.example.foodapplication.MySQL.MySQLQuerry.GetCustomerName;
import static com.example.foodapplication.MySQL.MySQLQuerry.GetCustomerPhone;

public class SingleEditTextUpdateFragment extends Fragment {
    private String type, target;
    private int user_id = -1;

    TextView txtSingleEditTitle;
    EditText txtEditText;
    Button btnConfirmEdit;

    DatabaseHelper dbHelper;

    private final String TAG = "SingleEditTextUF";

    public SingleEditTextUpdateFragment() { }

    public static SingleEditTextUpdateFragment newInstance() {
        return new SingleEditTextUpdateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getString("SINGLE_EDIT_TEXT");
            target = getArguments().getString("EDIT_TARGET");

            if (target.equals("Customer")) {
                if (MainActivity.customer_id > 0)
                    user_id = MainActivity.customer_id;
            } else {
                if (target.equals("Master")) {
                    if (MainActivity.master_id > 0)
                        user_id = MainActivity.master_id;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_edit_text_update, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        txtSingleEditTitle = view.findViewById(R.id.txtSingleEditTitle);
        txtEditText = view.findViewById(R.id.txtEditText);
        btnConfirmEdit = view.findViewById(R.id.btnConfirmEdit);

        if (user_id != -1) {
            dbHelper = new DatabaseHelper(getContext());
            if (target.equals("Customer")) {
                Cursor cursor = dbHelper.getCustomerById(user_id);
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                switch (type) {
                    case "EditPhone":
                        txtSingleEditTitle.setText("Cập nhật số điện thoại");
                        btnConfirmEdit.setText("Cập nhật");
                        txtEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                        GetCustomerPhone(user_id, txtEditText, progressDialog, TAG, getActivity());
                        break;
                    case "EditName":
                        txtSingleEditTitle.setText("Cập nhật tên");
                        btnConfirmEdit.setText("Cập nhật");
                        GetCustomerName(user_id, txtEditText, progressDialog, TAG, getActivity());
                        break;
                    case "EditEmail":
                        txtSingleEditTitle.setText("Cập nhật email");
                        btnConfirmEdit.setText("Cập nhật");
                        GetCustomerEmail(user_id, txtEditText, progressDialog, TAG, getActivity());
                        break;
                }
                cursor.close();
            } else {
                if (target.equals("Master")) {
                    Cursor cursor = dbHelper.getMasterById(user_id);

                    switch (type) {
                        case "EditPhone":
                            txtSingleEditTitle.setText("Cập nhật số điện thoại");
                            btnConfirmEdit.setText("Cập nhật");
                            if (cursor.moveToFirst())
                                txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CMaster.KEY_PHONE)));
                            break;
                        case "EditName":
                            txtSingleEditTitle.setText("Cập nhật tên");
                            btnConfirmEdit.setText("Cập nhật");
                            if (cursor.moveToFirst())
                                txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CMaster.KEY_NAME)));
                            break;
                        case "EditEmail":
                            txtSingleEditTitle.setText("Cập nhật email");
                            btnConfirmEdit.setText("Cập nhật");
                            if (cursor.moveToFirst())
                                txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CMaster.KEY_EMAIL)));
                            break;
                    }
                }
            }
        }

        btnConfirmEdit.setOnClickListener(onConfirmEditClick);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(txtSingleEditTitle.getText().toString());

        return view;
    }

    View.OnClickListener onConfirmEditClick = v -> {
        if (user_id != -1) {
            if (!txtEditText.getText().toString().equals("")) {
                if (target.equals("Customer")) {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    switch (type) {
                        case "EditPhone":
                            UpdateCustomerPhone(user_id, txtEditText.getText().toString(), progressDialog);
                            break;
                        case "EditName":
                            UpdateCustomerName(user_id, txtEditText.getText().toString(), progressDialog);
                            break;
                        case "EditEmail":
                            UpdateCustomerEmail(user_id, txtEditText.getText().toString(), progressDialog);
                            break;
                    }
                }
                else
                    dbHelper.updMasterInfoWithKey(user_id, txtEditText.getText().toString(), type);

                dbHelper.close();
            }
        }
        else
            Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
    };

    public void UpdateCustomerPhone(int customer_id, String phone, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateCustomerPhone.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "Cập nhật số điện thoại thành công!";
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else announcement = "Việc cập nhật số điện thoại thất bại!!!";
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
                params.put("customer_id", String.valueOf(customer_id));
                params.put("phone", String.valueOf(phone));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public void UpdateCustomerName(int customer_id, String name, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateCustomerName.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "Cập nhật tên thành công!";
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else announcement = "Việc cập nhật tên thất bại!!!";
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
                params.put("customer_id", String.valueOf(customer_id));
                params.put("name", String.valueOf(name));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public void UpdateCustomerEmail(int customer_id, String email, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateCustomerEmail.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "Cập nhật email thành công!";
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else announcement = "Việc cập nhật email thất bại!!!";
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
                params.put("customer_id", String.valueOf(customer_id));
                params.put("email", String.valueOf(email));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}