package com.example.foodapplication.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.foodapplication.MainActivity.customer_id;
import static com.example.foodapplication.MainActivity.nameStreet;
import static com.example.foodapplication.MainActivity.addressLine;
import static com.example.foodapplication.MainActivity.district_id;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetCustomerName;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetCustomerPhone;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetCustomerUsername;


public class ManageAddress extends AppCompatActivity {

    private ImageView back_imageView;
    private TextView textView_nameStreet;
    private TextView textView_addressLine;
    private TextView textView_NameAccount;
    private ImageView map_imageView;
    private EditText editText_AddressBar;

    private ImageView imageView_Home;
    private TextView textView_HomeAddress;

    private  ImageView imageView_BriefCase;
    private TextView textView_CompanyAddress;

    private  TextView textView_FullAddress1;
    private TextView textView_NameContact1;
    private TextView textView_PhoneContact1;
    private  TextView textView_FullAddress2;
    private TextView textView_NameContact2;
    private TextView textView_PhoneContact2;

    private final String TAG = "ManageAddress";

    private Geocoder geocoder;
    private static final String apiKey = "AIzaSyB_RuQCaFrm4wAIamLJA9R-NvSc7RmjlrA";
    double dLatitude;
    double dLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transparent Status and Navigation Bar
        transparentStatusAndNavigation();

        setContentView(R.layout.activity_manage_address);

        initComponents();

        Run();
    }

    public void initComponents() {
        textView_nameStreet = findViewById(R.id.nameStreet_textView);
        textView_addressLine = findViewById(R.id.fullAddress_textView);
        textView_NameAccount = findViewById(R.id.nameAccount_textView);
        textView_HomeAddress = findViewById(R.id.addHomeAddress_textView);
        textView_CompanyAddress = findViewById(R.id.addCompanyAddress_textView);
        textView_FullAddress1 = findViewById(R.id.FullAddress_FillAddress1);
        textView_NameContact1 = findViewById(R.id.NameContact_FillAddress1);
        textView_PhoneContact1 = findViewById(R.id.PhoneContact_FillAddress1);
        textView_FullAddress2 = findViewById(R.id.FullAddress_FillAddress2);
        textView_NameContact2 = findViewById(R.id.NameContact_FillAddress2);
        textView_PhoneContact2 = findViewById(R.id.PhoneContact_FillAddress2);

        back_imageView = findViewById(R.id.Back_imageView);
        map_imageView = findViewById(R.id.Map_imageView);
        imageView_Home = findViewById(R.id.home_imageView);
        imageView_BriefCase = findViewById(R.id.briefCase_imageView);

        editText_AddressBar = findViewById(R.id.searchAddressBar_editText);
    }

    public void Run() {
        textView_nameStreet.setText(nameStreet);

        addressLine = getIntent().getExtras().getString("AddressLine");
        textView_addressLine = findViewById(R.id.fullAddress_textView);
        textView_addressLine.setText(addressLine);

        if (customer_id > 0) {
           GetCustomerUsername(customer_id, textView_NameAccount, TAG, this);
           GetCustomerAddressWithLabel(customer_id, 1, this);
           GetCustomerAddressWithLabel(customer_id, 2, this);

        }

        back_imageView = findViewById(R.id.Back_imageView);
        back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        map_imageView = findViewById(R.id.Map_imageView);
        map_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });

        editText_AddressBar = findViewById(R.id.searchAddressBar_editText);
        Places.initialize(getApplicationContext(), apiKey);
        editText_AddressBar.setFocusable(false);
        editText_AddressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                        , Place.Field.LAT_LNG, Place.Field.NAME);
                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        , fieldList).setCountry("VN").build(ManageAddress.this);
                //Start activity result
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            //When success
            //Initialize place
            Place place = Autocomplete.getPlaceFromIntent(data);
            //Set address on EditText
            editText_AddressBar.setText(place.getAddress());

            LatLng latLng = place.getLatLng();
            double MyLat = latLng.latitude;
            double MyLong = latLng.longitude;
            Geocoder geocoder = new Geocoder(ManageAddress.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
                addressLine = addresses.get(0).getAddressLine(0);
                nameStreet = addresses.get(0).getThoroughfare();
                String stateName = addresses.get(0).getSubAdminArea();
                if(stateName.trim().equals("Thủ Đức") || stateName.trim().equals("Thu Duc") || stateName.trim().equals("Thành Phố Thủ Đức") || stateName.trim().equals("Quận Thủ Đức")) {
                    district_id = 14;
                }
                else {
                    district_id = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }, 1000);

        }
        else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            //When error
            //Initialize status
            Status status = Autocomplete.getStatusFromIntent(data);
            //Display toast
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }

        if(requestCode == 2) {
            if(resultCode == RESULT_OK) {
                dLatitude = data.getDoubleExtra("Latitude", 0);
                dLongitude = data.getDoubleExtra("Longitude", 0);
                String placeAddress = data.getStringExtra("Place Address");
                district_id = data.getIntExtra("District ID", 0);

                try {
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(dLatitude, dLongitude, 1);
                    nameStreet = addresses.get(0).getThoroughfare();
                    addressLine = addresses.get(0).getAddressLine(0);
                    textView_nameStreet.setText(nameStreet);
                    textView_addressLine.setText(addressLine);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    }, 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void openMapActivity()
    {
        Intent intent = new Intent(ManageAddress.this, Map.class);
        startActivityForResult(intent, 2);
    }

    private void transparentStatusAndNavigation()
    {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        //Change status bar text to black when screen is light white
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void GetCustomerAddressWithLabel(int customer_id, int address_label, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAddressWithLabel.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Xin vui lòng chờ trong giây lát...");
                progressDialog.show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String address = object.getString("ADDRESS");
                            int floor = object.getInt("FLOOR");
                            int gate = object.getInt("GATE");
                            String full_address = "";
                            if(!address.trim().equals("")) {
                                if(floor > 0 && gate > 0) {
                                    full_address = "[" + floor + ", " + gate + "] " + address;
                                }
                                else if(floor > 0) full_address = "[" + floor + ", " + 0 + "] " + address;
                                else if(gate > 0) full_address = "[" + 0 + ", " + gate + "] " + address;
                                else full_address = address;
                            }
                            if(address_label == 1) {
                                textView_HomeAddress.setText("Nhà");
                                if (!full_address.trim().equals(""))
                                    textView_FullAddress1.setText(full_address);
                                GetCustomerName(customer_id, textView_NameContact1, TAG, context);
                                GetCustomerPhone(customer_id, textView_PhoneContact1, progressDialog, TAG, context);
                            }
                            else if (address_label == 2) {
                                textView_CompanyAddress.setText("Công ty");
                                if (!full_address.trim().equals(""))
                                    textView_FullAddress2.setText(full_address);
                                GetCustomerName(customer_id, textView_NameContact2, TAG, context);
                                GetCustomerPhone(customer_id, textView_PhoneContact2, progressDialog, TAG, context);
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
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(customer_id));
                params.put("address_label", String.valueOf(address_label));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}