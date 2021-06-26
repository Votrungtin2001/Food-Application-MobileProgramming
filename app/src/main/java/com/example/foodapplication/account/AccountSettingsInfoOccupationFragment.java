package com.example.foodapplication.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.foodapplication.MySQL.MySQLQuerry.UpdateCustomerOccupation;

public class AccountSettingsInfoOccupationFragment extends Fragment {
    TextView txtOccupationOffice, txtOccupationFree, txtOccupationStudent, txtOccupationHome, txtOccupationOther;
    int user_id = -1;

    String occupation = "";
    private final String TAG = "AccountSettingIOF";

    public AccountSettingsInfoOccupationFragment() {

    }

    public static AccountSettingsInfoOccupationFragment newInstance() {
        return new AccountSettingsInfoOccupationFragment();
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
        View view = inflater.inflate(R.layout.fragment_account_settings_info_occupation, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_settings_account_info_occupation));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        txtOccupationOffice = view.findViewById(R.id.txtOccupationOffice);
        txtOccupationOffice.setOnClickListener(onTextViewClick);
        txtOccupationFree = view.findViewById(R.id.txtOccupationFree);
        txtOccupationFree.setOnClickListener(onTextViewClick);
        txtOccupationStudent = view.findViewById(R.id.txtOccupationStudent);
        txtOccupationStudent.setOnClickListener(onTextViewClick);
        txtOccupationHome = view.findViewById(R.id.txtOccupationHome);
        txtOccupationHome.setOnClickListener(onTextViewClick);
        txtOccupationOther= view.findViewById(R.id.txtOccupationOther);
        txtOccupationOther.setOnClickListener(onTextViewClick);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        GetCustomerOccupation(user_id, progressDialog);
        return view;
    }

    View.OnClickListener onTextViewClick = v -> {
        if (user_id != -1) {
            TextView view = (TextView) v;
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            UpdateCustomerOccupation(user_id, view.getText().toString(), progressDialog, TAG, getActivity());
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        }
    };

    private void GetCustomerOccupation(int id, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAccountInformation.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            occupation = object.getString("OCCUPATION");
                            if(!occupation.trim().equals("null") && !occupation.trim().equals("")) {
                                switch (occupation) {
                                    case "Văn phòng":
                                        txtOccupationOffice.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                                        break;
                                    case "Tự kinh doanh/Tự do":
                                        txtOccupationFree.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                                        break;
                                    case "Sinh viên":
                                        txtOccupationStudent.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                                        break;
                                    case "Ở nhà":
                                        txtOccupationHome.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                                        break;
                                    case "Khác":
                                        txtOccupationOther.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                                        break;
                                }
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
                progressDialog.dismiss();
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