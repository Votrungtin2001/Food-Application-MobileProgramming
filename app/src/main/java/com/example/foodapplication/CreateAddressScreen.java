package com.example.foodapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateAddressScreen extends AppCompatActivity {

    private TextView textView_Home;
    private TextView textView_Company;
    private  TextView textView_Others;

    private ImageView imageView_Back;

    private EditText editText_FullAddress;
    private EditText editText_HouseAddress;
    private EditText editText_NumberOfGate;

    private EditText editText_NameContact;
    private EditText editText_PhoneContact;
    private EditText editText_Note;

    private Button button_SaveAddress;
    private String fullAddress;
    private String houseAddress;
    private String numberOfGateAddress;
    private String nameContact;
    private String phoneContact;
    private String note;

    private int choose = -1;
    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_address_screen);

        imageView_Back = findViewById(R.id.Back_CreateAddress);
        imageView_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView_Home = findViewById(R.id.HomeOption_CreateAddress);
        textView_Company = findViewById(R.id.CompanyOption_CreateAddress);
        textView_Others = findViewById(R.id.OthersOption_CreateAddress);

        TurnOnOption();
        textView_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnOnHomeOption();
            }
        });
        textView_Company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnOnCompanyOption();
            }
        });
        textView_Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnOnOthersOption();
            }
        });

        editText_FullAddress = findViewById(R.id.FullAddressEditText_CreateAddress);

        editText_HouseAddress = findViewById(R.id.HouseAddressEditText_CreateAddress);

        editText_NumberOfGate = findViewById(R.id.NumberOfGateEditText_CreateAddress);

        editText_NameContact = findViewById(R.id.NameContactEditText_CreateAddress);

        editText_PhoneContact = findViewById(R.id.PhoneContactEditText_CreateAddress);

        editText_Note = findViewById(R.id.NoteEditText_CreateAddress);

        button_SaveAddress = findViewById(R.id.SaveAddressButton_CreateAddress);

        editText_FullAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!editText_FullAddress.getText().toString().trim().equals(""))
                {
                    state = 1;
                    button_SaveAddress.setTextColor(getResources().getColor(R.color.white));
                    button_SaveAddress.setBackground(getResources().getDrawable(R.drawable.button_turn_on));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_SaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 1)
                {
                    SaveButtonEvent();
                }
            }
        });



    }

    private void SaveButtonEvent()
    {
        fullAddress = editText_FullAddress.getText().toString();
        Intent resultIntent = new Intent();

        if(!editText_HouseAddress.getText().toString().trim().equals(""))
        {
            houseAddress = editText_HouseAddress.getText().toString();
            resultIntent.putExtra("House Address", houseAddress);
        }
        else resultIntent.putExtra("House Address", "");

        if(!editText_NumberOfGate.getText().toString().trim().equals(""))
        {
            numberOfGateAddress = editText_NumberOfGate.getText().toString();
            resultIntent.putExtra("Number Of Gate", numberOfGateAddress);
        }

        else resultIntent.putExtra("Number Of Gate", " ");

        if(!editText_NameContact.getText().toString().trim().equals(""))
        {
            nameContact = editText_NameContact.getText().toString();
            resultIntent.putExtra("Name Contact", nameContact);
        }
        else resultIntent.putExtra("Name Contact", "");

        if(!editText_PhoneContact.getText().toString().trim().equals(""))
        {
            phoneContact = editText_PhoneContact.getText().toString();
            resultIntent.putExtra("Phone Contact", phoneContact);
        }
        else resultIntent.putExtra("Phone Contact", "");

        if(!editText_Note.getText().toString().trim().equals(""))
        {
            note = editText_Note.getText().toString();
            resultIntent.putExtra("Note", note);
        }
        else resultIntent.putExtra("Note", "");

        if(choose == 1)
        {
            resultIntent.putExtra("Number Of Choice", 1);
        }
        else if(choose == 2)
        {
            resultIntent.putExtra("Number Of Choice", 2);
        }
        else resultIntent.putExtra("Number Of Choice", 3);

        resultIntent.putExtra("Full Address", fullAddress);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void TurnOnOption(){
        if(getIntent().getStringExtra("HomeOption").equals("Enable")){
            choose = 1;
            textView_Home.setTextColor(getResources().getColor(R.color.colorAccent));
            textView_Home.setBackground(getResources().getDrawable(R.drawable.address_option_turn_on));
            textView_Company.setTextColor(getResources().getColor(R.color.black));
            textView_Company.setBackground(getResources().getDrawable(R.drawable.searchbg));
            textView_Others.setTextColor(getResources().getColor(R.color.black));
            textView_Others.setBackground(getResources().getDrawable(R.drawable.searchbg));
        }
        else {
            choose = 2;
            textView_Company.setTextColor(getResources().getColor(R.color.colorAccent));
            textView_Company.setBackground(getResources().getDrawable(R.drawable.address_option_turn_on));
            textView_Home.setTextColor(getResources().getColor(R.color.black));
            textView_Home.setBackground(getResources().getDrawable(R.drawable.searchbg));
            textView_Others.setTextColor(getResources().getColor(R.color.black));
            textView_Others.setBackground(getResources().getDrawable(R.drawable.searchbg));
        }
    }

    private  void TurnOnHomeOption()
    {
        choose = 1;
        textView_Home.setTextColor(getResources().getColor(R.color.colorAccent));
        textView_Home.setBackground(getResources().getDrawable(R.drawable.address_option_turn_on));
        textView_Company.setTextColor(getResources().getColor(R.color.black));
        textView_Company.setBackground(getResources().getDrawable(R.drawable.searchbg));
        textView_Others.setTextColor(getResources().getColor(R.color.black));
        textView_Others.setBackground(getResources().getDrawable(R.drawable.searchbg));
    }

    private void TurnOnCompanyOption()
    {
        choose = 2;
        textView_Company.setTextColor(getResources().getColor(R.color.colorAccent));
        textView_Company.setBackground(getResources().getDrawable(R.drawable.address_option_turn_on));
        textView_Home.setTextColor(getResources().getColor(R.color.black));
        textView_Home.setBackground(getResources().getDrawable(R.drawable.searchbg));
        textView_Others.setTextColor(getResources().getColor(R.color.black));
        textView_Others.setBackground(getResources().getDrawable(R.drawable.searchbg));
    }

    private  void TurnOnOthersOption()
    {
        choose = 3;
        textView_Others.setTextColor(getResources().getColor(R.color.colorAccent));
        textView_Others.setBackground(getResources().getDrawable(R.drawable.address_option_turn_on));
        textView_Home.setTextColor(getResources().getColor(R.color.black));
        textView_Home.setBackground(getResources().getDrawable(R.drawable.searchbg));
        textView_Company.setTextColor(getResources().getColor(R.color.black));
        textView_Company.setBackground(getResources().getDrawable(R.drawable.searchbg));
    }
}