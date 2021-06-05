package com.example.foodapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

public class AccountAddressFragment extends Fragment {
    Spinner spinDistrictInput, spinCityInput;
    EditText txtAccountAddressInput, txtAccountAddressGate, txtAccountAddressFloor, txtOtherLabelName;
    RadioGroup rbGroup;
    Button btnAccountAddressSave;

    DatabaseHelper dbHelper;
    int cus_id;

    public AccountAddressFragment() {
        // Required empty public constructor
    }
    public static AccountAddressFragment newInstance() {
        AccountAddressFragment fragment = new AccountAddressFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_address, container, false);

        dbHelper = new DatabaseHelper(getContext());

        spinCityInput = view.findViewById(R.id.spinCityInput);
        ArrayList<String> spinnerArray = new ArrayList<>();
        Cursor cursor = dbHelper.getCities();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CCity._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCity.KEY_NAME));
            spinnerArray.add(id + " - " + name);
        }
        cursor.close();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCityInput.setAdapter(spinnerAdapter);
        spinCityInput.setOnItemSelectedListener(onCityChosen);

        spinDistrictInput = view.findViewById(R.id.spinDistrictInput);

        txtAccountAddressInput = view.findViewById(R.id.txtAccountAddressInput);
        txtAccountAddressGate = view.findViewById(R.id.txtAccountAddressGate);
        txtAccountAddressFloor = view.findViewById(R.id.txtAccountAddressFloor);
        txtOtherLabelName = view.findViewById(R.id.txtOtherLabelName);
        rbGroup = view.findViewById(R.id.rbGroup);
        rbGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        btnAccountAddressSave = view.findViewById(R.id.btnAccountAddressSave);
        btnAccountAddressSave.setOnClickListener(onAddressSave);

        return view;
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = (group, checkedId) -> {
        switch(rbGroup.getCheckedRadioButtonId()) {
            case R.id.rbHome:
            case R.id.rbWork:
                txtOtherLabelName.setVisibility(View.INVISIBLE);
                break;
            case R.id.rbOther:
                txtOtherLabelName.setVisibility(View.VISIBLE);
                break;
        }
    };

    AdapterView.OnItemSelectedListener onCityChosen = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String spinnerItem = spinCityInput.getSelectedItem().toString();
            String string = null;
            for (int i = 0; i < spinnerItem.length(); i++) {
                if (spinnerItem.charAt(i) == ' ')
                    break;
                string.concat(String.valueOf(spinnerItem.charAt(i)));
            }

            assert string != null;
            Cursor cursor = dbHelper.getDistricts(Integer.parseInt(string));
            ArrayList<String> spinnerArray = new ArrayList<>();
            while (cursor.moveToNext()) {
                int dist_id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CDistrict._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CDistrict.KEY_NAME));
                spinnerArray.add(id + " - " + name);
            }
            cursor.close();
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinDistrictInput.setAdapter(spinnerAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener onAddressSave = v -> {
        String city = null;
        for (int i = 0; i < spinCityInput.getSelectedItem().toString().length(); i++) {
            if (spinCityInput.getSelectedItem().toString().charAt(i) == ' ')
                break;
            city.concat(String.valueOf(spinCityInput.getSelectedItem().toString().charAt(i)));
        }
        int city_id = Integer.parseInt(city);

        String district = null;
        for (int i = 0; i < spinDistrictInput.getSelectedItem().toString().length(); i++) {
            if (spinDistrictInput.getSelectedItem().toString().charAt(i) == ' ')
                break;
            district.concat(String.valueOf(spinDistrictInput.getSelectedItem().toString().charAt(i)));
        }
        int dist_id = Integer.parseInt(district);

        long address_label = 0L;
        switch (rbGroup.getCheckedRadioButtonId()) {
            case R.id.rbOther:
                address_label = dbHelper.addAddressLabel(txtOtherLabelName.getText().toString());
                break;
            case R.id.rbHome:
                address_label = 0L;
                break;
            case R.id.rbWork:
                address_label = 1L;
                break;
        }

        dbHelper.addAddress(txtAccountAddressInput.getText().toString(),
                dist_id,
                city_id,
                Integer.parseInt(txtAccountAddressFloor.getText().toString()),
                Integer.parseInt(txtAccountAddressGate.getText().toString()),
                (int) address_label);
    };
}