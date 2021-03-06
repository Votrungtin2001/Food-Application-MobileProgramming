package com.example.foodapplication.account.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;

import java.util.HashMap;
import java.util.Map;

import static com.example.foodapplication.mySQL.MySQLQuerry.GetCustomerEmail;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetCustomerName;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetCustomerPhone;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetMasterEmail;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetMasterName;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetMasterPhone;

public class SingleEditTextUpdateFragment extends Fragment {
    private String type, target;
    private int user_id = -1;

    TextView txtSingleEditTitle;
    EditText txtEditText;
    Button btnConfirmEdit;

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
        txtEditText.addTextChangedListener(textWatcher);

        btnConfirmEdit = view.findViewById(R.id.btnConfirmEdit);

        if (user_id != -1) {
            if (target.equals("Customer")) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Xin vui l??ng ch??? trong gi??y l??t...");
                progressDialog.show();
                switch (type) {
                    case "EditPhone":
                        txtSingleEditTitle.setText("C???p nh???t s??? ??i???n tho???i");
                        btnConfirmEdit.setText("C???p nh???t");
                        txtEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        GetCustomerPhone(user_id, txtEditText, progressDialog, TAG, getActivity());
                        break;
                    case "EditName":
                        txtSingleEditTitle.setText("C???p nh???t t??n");
                        btnConfirmEdit.setText("C???p nh???t");
                        GetCustomerName(user_id, txtEditText, progressDialog, TAG, getActivity());
                        break;
                    case "EditEmail":
                        txtSingleEditTitle.setText("C???p nh???t email");
                        btnConfirmEdit.setText("C???p nh???t");
                        GetCustomerEmail(user_id, txtEditText, progressDialog, TAG, getActivity());
                        break;
                }
            } else {
                if (target.equals("Master")) {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Xin vui l??ng ch??? trong gi??y l??t...");
                    progressDialog.show();
                    switch (type) {
                        case "EditPhone":
                            txtSingleEditTitle.setText("C???p nh???t s??? ??i???n tho???i");
                            btnConfirmEdit.setText("C???p nh???t");
                            txtEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                            GetMasterPhone(user_id, txtEditText, progressDialog, TAG, getActivity());
                            break;
                        case "EditName":
                            txtSingleEditTitle.setText("C???p nh???t t??n");
                            btnConfirmEdit.setText("C???p nh???t");
                            GetMasterName(user_id, txtEditText, progressDialog, TAG, getActivity());
                            break;
                        case "EditEmail":
                            txtSingleEditTitle.setText("C???p nh???t email");
                            btnConfirmEdit.setText("C???p nh???t");
                            GetMasterEmail(user_id, txtEditText, progressDialog, TAG, getActivity());
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
        if ((type.equals("EditEmail")) && (!android.util.Patterns.EMAIL_ADDRESS.matcher(txtEditText.getText().toString()).matches()))
            Toast.makeText(getContext(), "Email kh??ng h???p l???!", Toast.LENGTH_SHORT).show();
        else {
            if (user_id != -1) {
                if (!txtEditText.getText().toString().equals("")) {
                    if (target.equals("Customer")) {
                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Xin vui l??ng ch??? trong gi??y l??t...");
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
                    } else {
                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Xin vui l??ng ch??? trong gi??y l??t...");
                        progressDialog.show();
                        switch (type) {
                            case "EditPhone":
                                UpdateMasterPhone(user_id, txtEditText.getText().toString(), progressDialog);
                                break;
                            case "EditName":
                                UpdateMasterName(user_id, txtEditText.getText().toString(), progressDialog);
                                break;
                            case "EditEmail":
                                UpdateMasterEmail(user_id, txtEditText.getText().toString(), progressDialog);
                                break;
                        }
                    }
                }
            } else
                Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
        }
    };

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().trim().length() == 0) {
                btnConfirmEdit.setEnabled(false);
            } else {
                btnConfirmEdit.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void UpdateCustomerPhone(int customer_id, String phone, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateCustomerPhone.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "C???p nh???t s??? ??i???n tho???i th??nh c??ng!";
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else announcement = "Vi???c c???p nh???t s??? ??i???n tho???i th???t b???i!!!";
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
                    announcement = "C???p nh???t t??n th??nh c??ng!";
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else announcement = "Vi???c c???p nh???t t??n th???t b???i!!!";
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
                    announcement = "C???p nh???t email th??nh c??ng!";
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else announcement = "Vi???c c???p nh???t email th???t b???i!!!";
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

    public void UpdateMasterPhone(int master_id, String phone, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateMasterPhone.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "C???p nh???t s??? ??i???n tho???i th??nh c??ng!";
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else announcement = "Vi???c c???p nh???t s??? ??i???n tho???i th???t b???i!!!";
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
                params.put("master_id", String.valueOf(master_id));
                params.put("phone", String.valueOf(phone));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public void UpdateMasterName(int master_id, String name, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateMasterName.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "C???p nh???t t??n th??nh c??ng!";
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else announcement = "Vi???c c???p nh???t t??n th???t b???i!!!";
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
                params.put("master_id", String.valueOf(master_id));
                params.put("name", String.valueOf(name));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public void UpdateMasterEmail(int master_id, String email, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateMasterEmail.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "C???p nh???t email th??nh c??ng!";
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else announcement = "Vi???c c???p nh???t email th???t b???i!!!";
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
                params.put("master_id", String.valueOf(master_id));
                params.put("email", String.valueOf(email));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}