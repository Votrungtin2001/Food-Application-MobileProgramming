package com.example.foodapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapplication.auth.LoginFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.example.foodapplication.MainActivity.customer_id;


public class Fill_Address_Screen extends AppCompatActivity {

    private ImageView back_imageView;
    private TextView textView_nameStreet;
    private TextView textView_addressLine;
    private ImageView map_imageView;
    private EditText editText_AddressBar;

    private String nameStreet;
    private String addressLine;

    private ImageView imageView_Home;
    private TextView textView_HomeAddress;
    private ImageView imageView_HomeNext;

    private  ImageView imageView_BriefCase;
    private TextView textView_CompanyAddress;
    private ImageView imageView_CompanyNext;

    private  TextView textView_FullAddress1;
    private TextView textView_NameContact1;
    private TextView textView_PhoneContact1;
    private TextView textView_Note1;
    private  TextView textView_FullAddress2;
    private TextView textView_NameContact2;
    private TextView textView_PhoneContact2;
    private TextView textView_Note2;

    private Button button_NewAddress;

    private Location location;
    private Geocoder geocoder;
    private static final String apiKey = "AIzaSyB_RuQCaFrm4wAIamLJA9R-NvSc7RmjlrA";
    double dLatitude;
    double dLongitude;

    private int district_id;

    Dialog AnnouncementDialog;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transparent Status and Navigation Bar
        transparentStatusAndNavigation();

        setContentView(R.layout.activity_fill__address__screen);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

        initComponents();

        Run();


    }

    public void initComponents() {
        textView_nameStreet = findViewById(R.id.nameStreet_textView);
        textView_addressLine = findViewById(R.id.fullAddress_textView);
        textView_HomeAddress = findViewById(R.id.addHomeAddress_textView);
        textView_CompanyAddress = findViewById(R.id.addCompanyAddress_textView);
        textView_FullAddress1 = findViewById(R.id.FullAddress_FillAddress1);
        textView_NameContact1 = findViewById(R.id.NameContact_FillAddress1);
        textView_PhoneContact1 = findViewById(R.id.PhoneContact_FillAddress1);
        textView_Note1 = findViewById(R.id.Note_FillAddress1);
        textView_FullAddress2 = findViewById(R.id.FullAddress_FillAddress2);
        textView_NameContact2 = findViewById(R.id.NameContact_FillAddress2);
        textView_PhoneContact2 = findViewById(R.id.PhoneContact_FillAddress2);
        textView_Note2 = findViewById(R.id.Note_FillAddress2);

        back_imageView = findViewById(R.id.Back_imageView);
        map_imageView = findViewById(R.id.Map_imageView);
        imageView_Home = findViewById(R.id.home_imageView);
        imageView_HomeNext = findViewById(R.id.next1_imageView);
        imageView_BriefCase = findViewById(R.id.briefCase_imageView);
        imageView_CompanyNext = findViewById(R.id.next2_imageView);

        editText_AddressBar = findViewById(R.id.searchAddressBar_editText);

        button_NewAddress = findViewById(R.id.addNewAddress_button);

        AnnouncementDialog = new Dialog(this);
        AnnouncementDialog.setContentView(R.layout.custom_popup_require_login);
    }

    public void Run() {
        nameStreet = getIntent().getExtras().getString("NameStreet");
        textView_nameStreet.setText(nameStreet);

        addressLine = getIntent().getExtras().getString("AddressLine");
        textView_addressLine.setText(addressLine);

        back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        map_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });

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
                        , fieldList).setCountry("VN").build(Fill_Address_Screen.this);
                //Start activity result
                startActivityForResult(intent, 100);
            }
        });

        imageView_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithHomeOption();
            }
        });

        textView_HomeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithHomeOption();
            }
        });

        imageView_HomeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithHomeOption();
            }
        });

        imageView_BriefCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithCompanyOption();
            }
        });

        textView_CompanyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithCompanyOption();
            }
        });

        imageView_CompanyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithCompanyOption();
            }
        });

        button_NewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithNewAddressOption();
            }
        });

        boolean checkCustomerHasAddress = CheckCustomerHasAddress(customer_id);
        if(checkCustomerHasAddress == true) {
            String phone = getCustomerPhone(customer_id);
            String name = getCustomerName(customer_id);
            boolean checkCustomerHasHomeAddress = CheckCustomerHasRightAddressLabel(customer_id, 1);
            if (checkCustomerHasHomeAddress == true) {
                String full_address_HOME = getFullAddress(customer_id, 1);
                if (!full_address_HOME.trim().equals("")) {
                    textView_HomeAddress.setText("Nhà");
                    textView_FullAddress1.setText(full_address_HOME);
                }
                if(phone != null) textView_PhoneContact1.setText(phone);
                if(name != null) textView_NameContact1.setText(name);
            }

            boolean checkCustomerHasWorkAddress = CheckCustomerHasRightAddressLabel(customer_id, 2);
            if(checkCustomerHasWorkAddress == true) {
                String full_address_WORK = getFullAddress(customer_id, 2);
                if (!full_address_WORK.trim().equals("")) {
                    textView_CompanyAddress.setText("Công ty");
                    textView_FullAddress2.setText(full_address_WORK);
                }
                if(phone != null) textView_PhoneContact2.setText(phone);
                if(name != null) textView_NameContact2.setText(name);
            }

        }
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
            textView_addressLine.setText(place.getAddress());
            textView_nameStreet.setText(place.getName());

            LatLng latLng = place.getLatLng();
            double MyLat = latLng.latitude;
            double MyLong = latLng.longitude;
            Geocoder geocoder = new Geocoder(Fill_Address_Screen.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
                String stateName = addresses.get(0).getSubAdminArea();
                if(stateName.trim().equals("Thủ Đức") || stateName.trim().equals("Thu Duc") || stateName.trim().equals("Thành Phố Thủ Đức") || stateName.trim().equals("Quận Thủ Đức")) {
                    district_id = 14;
                }
                else if(stateName.trim().equals("Quận 5") || stateName.trim().equals("Quan 5")) {
                    district_id = 5;
                }
                else {
                    district_id = -1;
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent resultIntent = new Intent();
                    addressLine = textView_addressLine.getText().toString();
                    nameStreet = textView_nameStreet.getText().toString();
                    resultIntent.putExtra("Name Street", nameStreet);
                    resultIntent.putExtra("Address Line", addressLine);
                    resultIntent.putExtra("District ID", district_id);
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
        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK){
                String fullAddress = data.getStringExtra("Full Address");
                String houseAddress = data.getStringExtra("House Address");
                String numberOfGateAddress = data.getStringExtra("Number Of Gate");
                String nameContact = data.getStringExtra("Name Contact");
                String phoneContact = data.getStringExtra("Phone Contact");
                String note = data.getStringExtra("Note");
                int choose = data.getIntExtra("Number Of Choice", 1);
                if(choose == 1) {
                    textView_HomeAddress.setText("Nhà");
                    if(!houseAddress.trim().equals("") && !numberOfGateAddress.trim().equals("")) {
                        textView_FullAddress1.setText(" [" + houseAddress + ", " + numberOfGateAddress + "] \t " + fullAddress);
                    }
                    else if (!houseAddress.trim().equals("")){
                        textView_FullAddress1.setText(" " + houseAddress + ", " + fullAddress);
                    }
                    else if (!numberOfGateAddress.trim().equals("")){
                        textView_FullAddress1.setText("Cong: " + numberOfGateAddress + ", " + fullAddress);
                    }
                    else  textView_FullAddress1.setText(" " + fullAddress);
                    if(!nameContact.trim().equals("")) textView_NameContact1.setText("Tên liên lạc: " + nameContact);
                    else textView_NameContact1.setText("Tên liên lạc: Không có.");
                    if(!phoneContact.trim().equals("")) textView_PhoneContact1.setText("Điện thoại: " + phoneContact);
                    else textView_PhoneContact1.setText("Điện thoại: Không có.");
                    if(!note.trim().equals("")) textView_Note1.setText("Ghi chú: " + note);
                    else textView_Note1.setText("Ghi chú: Không có.");

                }
                else if (choose == 2) {
                    textView_CompanyAddress.setText("Công ty");
                    if(!houseAddress.trim().equals("") && !numberOfGateAddress.trim().equals("")) {
                        textView_FullAddress2.setText(" [" + houseAddress + ", " + numberOfGateAddress + "] \t " + fullAddress);
                    }
                    else if (!houseAddress.trim().equals("")){
                        textView_FullAddress2.setText(" " + houseAddress + ", " + fullAddress);
                    }
                    else if (!numberOfGateAddress.trim().equals("")){
                        textView_FullAddress2.setText("Cong: " + numberOfGateAddress + ", " + fullAddress);
                    }
                    else  textView_FullAddress2.setText(" " + fullAddress);
                    if(!nameContact.trim().equals("")) textView_NameContact2.setText("Tên liên lạc: " + nameContact);
                    else textView_NameContact2.setText("Tên liên lạc: Không có.");
                    if(!phoneContact.trim().equals("")) textView_PhoneContact2.setText("Điện thoại: " + phoneContact);
                    else textView_PhoneContact2.setText("Điện thoại: Không có.");
                    if(!note.trim().equals("")) textView_Note2.setText("Ghi chú: " + note);
                    else textView_Note2.setText("Ghi chú: Không có.");
                }
                else {
                    textView_CompanyAddress.setText("Nơi khác");
                    if(!houseAddress.trim().equals("") && !numberOfGateAddress.trim().equals("")) {
                        textView_FullAddress2.setText(" [" + houseAddress + ", " + numberOfGateAddress + "] \t " + fullAddress);
                    }
                    else if (!houseAddress.trim().equals("")){
                        textView_FullAddress2.setText(" " + houseAddress + ", " + fullAddress);
                    }
                    else if (!numberOfGateAddress.trim().equals("")){
                        textView_FullAddress2.setText("Cong: " + numberOfGateAddress + ", " + fullAddress);
                    }
                    else  textView_FullAddress2.setText(" " + fullAddress);
                    if(!nameContact.trim().equals("")) textView_NameContact2.setText("Tên liên lạc: " + nameContact);
                    else textView_NameContact2.setText("Tên liên lạc: Không có.");
                    if(!phoneContact.trim().equals("")) textView_PhoneContact2.setText("Điện thoại: " + phoneContact);
                    else textView_PhoneContact2.setText("Điện thoại: Không có.");
                    if(!note.trim().equals("")) textView_Note2.setText("Ghi chú: " + note);
                    else textView_Note2.setText("Ghi chú: Không có.");
                }
            }
        }
        if(requestCode == 2) {
            if(resultCode == RESULT_OK) {
                dLatitude = data.getDoubleExtra("Latitude", 0);
                dLongitude = data.getDoubleExtra("Longitude", 0);
                String placeName = data.getStringExtra("Place Name");
                String placeAddress = data.getStringExtra("Place Address");
                district_id = data.getIntExtra("District ID", 0);

                try {
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(dLatitude, dLongitude, 1);
                    textView_nameStreet.setText(placeName);
                    textView_addressLine.setText(placeAddress);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent resultIntent = new Intent();
                            nameStreet = textView_nameStreet.getText().toString();
                            resultIntent.putExtra("Name Street", nameStreet);
                            addressLine = textView_addressLine.getText().toString();
                            resultIntent.putExtra("Address Line", addressLine);
                            resultIntent.putExtra("District ID", district_id);
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

    private void openCreateAddressActivityWithHomeOption()
    {
        if(customer_id > 0 ) {
            Intent intent = new Intent(Fill_Address_Screen.this, CreateAddressScreen.class);
            intent.putExtra("Option", "Home");
            startActivityForResult(intent, 1);
        }
        else ShowPopUpRequireLogin();
    }

    private void openCreateAddressActivityWithCompanyOption()
    {
        if(customer_id > 0) {
            Intent intent = new Intent(Fill_Address_Screen.this, CreateAddressScreen.class);
            intent.putExtra("Option", "Company");
            startActivityForResult(intent, 1);
        }
        else ShowPopUpRequireLogin();
    }

    private void openCreateAddressActivityWithNewAddressOption()
    {
        if(customer_id > 0) {
            Intent intent = new Intent(Fill_Address_Screen.this, CreateAddressScreen.class);
            intent.putExtra("Option", "New Address");
            startActivityForResult(intent, 1);
        }
        else ShowPopUpRequireLogin();
    }


    private void openMapActivity()
    {
        Intent intent = new Intent(Fill_Address_Screen.this, Map.class);
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

    public void ShowPopUpRequireLogin() {
        TextView textView_Close;
        textView_Close = (TextView) AnnouncementDialog.findViewById(R.id.Close_PopUpLogin);
        textView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnnouncementDialog.dismiss();
            }
        });

        TextView textView_Text;
        textView_Text = (TextView) AnnouncementDialog.findViewById(R.id.TextView_PopUp_RequireLogin);
        textView_Text.setText("Vui lòng đăng nhập với vai trò là khách hàng!!!");

        AnnouncementDialog.show();
    }

    public boolean CheckCustomerHasAddress(int customer_id) {
        int count = 0;

        String selectQuery = "SELECT * FROM CUSTOMER_ADDRESS WHERE Customer='" + customer_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {

            } while (cursor.moveToNext());

        }
        count = cursor.getCount();
        cursor.close();
        if(count > 0) return true;
        else return false;

    }

    public String getFullAddress(int customer_id, int label) {
        String full_address = "";
        String address_name = "";
        int floor = 0;
        int gate = 0;
        String selectQuery = "SELECT A.ADDRESS, A.Floor, A.Gate FROM (CUSTOMER_ADDRESS C JOIN ADDRESS A ON C.Address = A._id) " +
                "JOIN ADDRESS_LABEL AL ON A.Label = AL._id " +
                "WHERE C.Customer ='" + customer_id + "' AND A.Label =' " + label + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                address_name = cursor.getString(cursor.getColumnIndex("Address"));
                floor = cursor.getInt(cursor.getColumnIndex("Floor"));
                gate = cursor.getInt(cursor.getColumnIndex("Gate"));
            } while (cursor.moveToNext());

        }
        cursor.close();

        if(!address_name.trim().equals("")) {
            if(floor > 0 && gate > 0) {
                full_address = "[" + floor + ", " + gate + "] " + address_name;
            }
            else if(floor > 0) full_address = "[" + floor + ", " + 0 + "] " + address_name;
            else if(gate > 0) full_address = "[" + 0 + ", " + gate + "] " + address_name;
            else full_address = address_name;
        }
        return full_address;
    }

    public boolean CheckCustomerHasRightAddressLabel(int customer_id, int label) {
        int count = 0;
        String selectQuery =  "SELECT A.ADDRESS, A.Floor, A.Gate FROM (CUSTOMER_ADDRESS C JOIN ADDRESS A ON C.Address = A._id) " +
                "JOIN ADDRESS_LABEL AL ON A.Label = AL._id " +
                "WHERE C.Customer ='" + customer_id + "' AND A.Label =' " + label + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {

            } while (cursor.moveToNext());

        }
        count = cursor.getCount();
        cursor.close();
        if(count > 0) return true;
        else return false;
    }

    public String getCustomerPhone(int customer_id) {
        String phone = "";

        String selectQuery = "SELECT Phone FROM CUSTOMER WHERE _id='" + customer_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
               phone = cursor.getString(cursor.getColumnIndex("Phone"));
            } while (cursor.moveToNext());

        }
        cursor.close();

       return phone;
    }

    public String getCustomerName(int customer_id) {
        String name = "";

        String selectQuery = "SELECT Name FROM CUSTOMER WHERE _id='" + customer_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                name = cursor.getString(cursor.getColumnIndex("Name"));
            } while (cursor.moveToNext());

        }
        cursor.close();

        return name;
    }
}