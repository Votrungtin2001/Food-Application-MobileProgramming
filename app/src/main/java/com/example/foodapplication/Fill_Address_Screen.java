package com.example.foodapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;


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

    private int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transparent Status and Navigation Bar
        transparentStatusAndNavigation();

        setContentView(R.layout.activity_fill__address__screen);

        nameStreet = getIntent().getExtras().getString("NameStreet");
        textView_nameStreet = findViewById(R.id.nameStreet_textView);
        textView_nameStreet.setText(nameStreet);

        addressLine = getIntent().getExtras().getString("AddressLine");
        textView_addressLine = findViewById(R.id.fullAddress_textView);
        textView_addressLine.setText(addressLine);


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
        Places.initialize(getApplicationContext(), "AIzaSyBTVnRocNYtZdZa359_zfrlELKGOkFlXYg");
        editText_AddressBar.setFocusable(false);
        editText_AddressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                    , Place.Field.LAT_LNG, Place.Field.NAME);
                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        , fieldList).build(Fill_Address_Screen.this);
                //Start activity result
                startActivityForResult(intent, 100);
            }
        });

        imageView_Home = findViewById(R.id.home_imageView);
        imageView_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithHomeOption();
            }
        });

        textView_HomeAddress = findViewById(R.id.addHomeAddress_textView);
        textView_HomeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithHomeOption();
            }
        });

        imageView_HomeNext = findViewById(R.id.next1_imageView);
        imageView_HomeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithHomeOption();
            }
        });

        imageView_BriefCase = findViewById(R.id.briefCase_imageView);
        imageView_BriefCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithCompanyOption();
            }
        });

        textView_CompanyAddress = findViewById(R.id.addCompanyAddress_textView);
        textView_CompanyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithCompanyOption();
            }
        });

        imageView_CompanyNext = findViewById(R.id.next2_imageView);
        imageView_CompanyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAddressActivityWithCompanyOption();
            }
        });

        textView_FullAddress1 = findViewById(R.id.FullAddress_FillAddress1);
        textView_NameContact1 = findViewById(R.id.NameContact_FillAddress1);
        textView_PhoneContact1 = findViewById(R.id.PhoneContact_FillAddress1);
        textView_Note1 = findViewById(R.id.Note_FillAddress1);

        textView_FullAddress2 = findViewById(R.id.FullAddress_FillAddress2);
        textView_NameContact2 = findViewById(R.id.NameContact_FillAddress2);
        textView_PhoneContact2 = findViewById(R.id.PhoneContact_FillAddress2);
        textView_Note2 = findViewById(R.id.Note_FillAddress2);
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

    }

    private void openCreateAddressActivityWithHomeOption()
    {
        Intent intent = new Intent(Fill_Address_Screen.this, CreateAddressScreen.class);
        intent.putExtra("HomeOption", "Enable");
        startActivityForResult(intent, 1);
    }

    private void openCreateAddressActivityWithCompanyOption()
    {
        Intent intent = new Intent(Fill_Address_Screen.this, CreateAddressScreen.class);
        intent.putExtra("HomeOption", "Not Enable");
        startActivityForResult(intent, 1);
    }


    private void openMapActivity()
    {
        Intent intent = new Intent(Fill_Address_Screen.this, Map.class);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
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
}