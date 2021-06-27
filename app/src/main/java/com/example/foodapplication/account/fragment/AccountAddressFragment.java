package com.example.foodapplication.account.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.example.foodapplication.account.model.IdWithNameListItem;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.foodapplication.MySQL.MySQLQuerry.GetCities;
import static com.example.foodapplication.MySQL.MySQLQuerry.GetDistrictsWithCity;

public class AccountAddressFragment extends Fragment {
    Spinner spinDistrictInput, spinCityInput;
    EditText txtAccountAddressInput, txtAccountAddressGate, txtAccountAddressFloor;
    RadioGroup rbGroup;
    Button btnAccountAddressSave;
    ArrayList<IdWithNameListItem> citySpinner, districtSpinner;

    private final String TAG = "AccountAddressFragment";

    int user_id = -1, address_id = 0;


    public AccountAddressFragment() {
        // Required empty public constructor
    }
    public static AccountAddressFragment newInstance() {
        return new AccountAddressFragment();
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
        View view = inflater.inflate(R.layout.fragment_account_address, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_address));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });


        spinCityInput = view.findViewById(R.id.spinCityInput);
        citySpinner = new ArrayList<>();
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, citySpinner);
        GetCities(citySpinner, spinnerAdapter, TAG, getActivity());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCityInput.setAdapter(spinnerAdapter);
        spinCityInput.setOnItemSelectedListener(onCityChosen);

        spinDistrictInput = view.findViewById(R.id.spinDistrictInput);

        txtAccountAddressInput = view.findViewById(R.id.txtAccountAddressInput);

        txtAccountAddressGate = view.findViewById(R.id.txtAccountAddressGate);
        txtAccountAddressGate.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        txtAccountAddressFloor = view.findViewById(R.id.txtAccountAddressFloor);
        txtAccountAddressFloor.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        rbGroup = view.findViewById(R.id.rbGroup);
        rbGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        btnAccountAddressSave = view.findViewById(R.id.btnAccountAddressSave);
        btnAccountAddressSave.setOnClickListener(onAddressSave);

        return view;
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = (group, checkedId) -> {
        switch(rbGroup.getCheckedRadioButtonId()) {
            case R.id.rbHome:
                setUpHints(true);
                break;
            case R.id.rbWork:
                setUpHints(false);
                break;
            case R.id.rbOther:
                btnAccountAddressSave.setText(getResources().getText(R.string.activity_account_address_save));
                break;
        }
    };

    AdapterView.OnItemSelectedListener onCityChosen = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            IdWithNameListItem spinnerItem = (IdWithNameListItem) spinCityInput.getSelectedItem();
            int city_id = spinnerItem.getId();
            districtSpinner = new ArrayList<>();
            ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, districtSpinner);
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            GetDistrictsWithCity(city_id, districtSpinner, spinnerAdapter, progressDialog, TAG, getActivity());
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinDistrictInput.setAdapter(spinnerAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener onAddressSave = v -> {
        if(!txtAccountAddressInput.getText().toString().trim().equals("")) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            if (user_id != -1) {
                IdWithNameListItem selectedCity = (IdWithNameListItem) spinCityInput.getSelectedItem();
                IdWithNameListItem selectedDistrict = (IdWithNameListItem) spinDistrictInput.getSelectedItem();

                int address_label = 0;
                switch (rbGroup.getCheckedRadioButtonId()) {
                    case R.id.rbHome:
                        address_label = 1;
                        break;
                    case R.id.rbWork:
                        address_label = 2;
                        break;
                    default:
                        address_label = 3;
                        break;
                }

                int floor = 0, gate = 0;

                if (!txtAccountAddressFloor.getText().toString().equals(""))
                    floor = Integer.parseInt(txtAccountAddressFloor.getText().toString());
                if (!txtAccountAddressGate.getText().toString().equals(""))
                    gate = Integer.parseInt(txtAccountAddressGate.getText().toString());

                if (btnAccountAddressSave.getText().toString().equals("Lưu địa chỉ")) {
                    String address = txtAccountAddressInput.getText().toString() + ", " + selectedDistrict.getName() + ", " + selectedCity.getName();
                    CreateCustomerAddress(user_id, address, selectedCity.getId(),
                            selectedDistrict.getId(), floor, gate,
                            address_label, progressDialog);

                } else {
                    String address = txtAccountAddressInput.getText().toString() + ", " + selectedDistrict.getName() + ", " + selectedCity.getName();
                    UpdateCustomerAddress(address_id, address, selectedDistrict.getId(), selectedCity.getId(),
                            floor, gate, address_label, progressDialog);
                }
            } else
                Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
        } else Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin địa chỉ!!!", Toast.LENGTH_LONG).show();
    };

    public void setUpHints(boolean IsHomeAddress) {
        address_id = 0;
        if (IsHomeAddress)
            GetCustomerAddressIDWithLabel(user_id, 1);
        else
            GetCustomerAddressIDWithLabel(user_id, 2);
    }

    private void CreateCustomerAddress(int customer_id, String address, int city_id, int district_id,
                                                                int floor, int gate, int address_label,
                                                                ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/createCustomerAddress.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "Lưu địa chỉ thành công!";
                    Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else {
                    announcement = "Việc lưu địa chỉ xảy ra lỗi!!!";
                    Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
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
                params.put("customer_id", String.valueOf(customer_id));
                params.put("address", String.valueOf(address));
                params.put("city_id", String.valueOf(city_id));
                params.put("district_id", String.valueOf(district_id));
                params.put("floor", String.valueOf(floor));
                params.put("gate", String.valueOf(gate));
                params.put("address_label", String.valueOf(address_label));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void GetCustomerAddressIDWithLabel(int customer_id, int address_label) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAddressIDWithCustomer.php";
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

                            address_id = object.getInt("_ID");
                        }
                        if(address_id > 0) {
                            btnAccountAddressSave.setText(getResources().getText(R.string.activity_account_address_update));
                            GetAddress(address_id);
                        }
                        else {
                            btnAccountAddressSave.setText(getResources().getString(R.string.activity_account_address_save));
                            address_id = 0;
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
        })  {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(customer_id));
                params.put("address_label", String.valueOf(address_label));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void GetAddress(int address_id) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getAddress.php";
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

                            String address = object.getString("ADDRESS");
                            int district_id = object.getInt("DISTRICT");
                            int city_id = object.getInt("CITY");
                            int floor = object.getInt("FLOOR");
                            int gate = object.getInt("GATE");

                            txtAccountAddressInput.setText(address);
                            txtAccountAddressFloor.setText(Integer.toString(floor));
                            txtAccountAddressGate.setText(Integer.toString(gate));
                            for (int k = 0; k < citySpinner.size(); k++) {
                                if (city_id == citySpinner.get(k).getId()) {
                                    spinCityInput.setSelection(k);
                                    break;
                                }
                            }

                            for (int j = 0; j < districtSpinner.size(); j++) {
                                if (district_id == districtSpinner.get(j).getId()) {
                                    spinDistrictInput.setSelection(j);
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
                Log.e(TAG, error.toString());
            }
        })  {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("address_id", String.valueOf(address_id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public void UpdateCustomerAddress(int address_id, String address, int district_id, int city_id, int floor, int gate, int address_label, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateCustomerAddress.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "Cập nhật địa chỉ thành công!";
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                }
                else announcement = "Việc cập nhật địa chỉ thất bại!!!";
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
                params.put("address_id", String.valueOf(address_id));
                params.put("address", String.valueOf(address));
                params.put("district_id", String.valueOf(district_id));
                params.put("city_id", String.valueOf(city_id));
                params.put("floor", String.valueOf(floor));
                params.put("gate", String.valueOf(gate));
                params.put("address_label", String.valueOf(address_label));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}