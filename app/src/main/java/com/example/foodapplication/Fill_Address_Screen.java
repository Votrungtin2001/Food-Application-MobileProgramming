package com.example.foodapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.common.api.Status;
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
    }

    private void openMapActivity()
    {
        Intent i = new Intent(Fill_Address_Screen.this, Map.class);
        startActivity(i);
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