package com.example.foodapplication;

import android.database.Cursor;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AccountAddressFragment extends Fragment {
    Spinner spinDistrictInput, spinCityInput;
    EditText txtAccountAddressInput, txtAccountAddressGate, txtAccountAddressFloor, txtOtherLabelName;
    RadioGroup rbGroup;
    Button btnAccountAddressSave;

    DatabaseHelper dbHelper;
    int user_id = -1;

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

        dbHelper = new DatabaseHelper(getContext());

        spinCityInput = view.findViewById(R.id.spinCityInput);
        ArrayList<IdWithNameListItem> spinnerArray = new ArrayList<>();
        Cursor cursor = dbHelper.getCities();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CCity._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCity.KEY_NAME));
            spinnerArray.add(new IdWithNameListItem(id, name));
        }
        cursor.close();
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
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
            IdWithNameListItem spinnerItem = (IdWithNameListItem) spinCityInput.getSelectedItem();
            int city_id = spinnerItem.getId();
            Cursor cursor = dbHelper.getDistricts(city_id);
            ArrayList<IdWithNameListItem> spinnerArray = new ArrayList<>();
            while (cursor.moveToNext()) {
                int dist_id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CDistrict._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CDistrict.KEY_NAME));
                spinnerArray.add(new IdWithNameListItem(dist_id, name));
            }
            cursor.close();
            ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinDistrictInput.setAdapter(spinnerAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener onAddressSave = v -> {
        if (user_id != -1) {
            IdWithNameListItem selectedCity = (IdWithNameListItem) spinCityInput.getSelectedItem();
            IdWithNameListItem selectedDistrict = (IdWithNameListItem) spinDistrictInput.getSelectedItem();

            long address_label = 0L;
            switch (rbGroup.getCheckedRadioButtonId()) {
                case R.id.rbOther:
                    if (txtOtherLabelName.getText().toString().equals(""))
                        address_label = 3L;
                    else {
                        if (dbHelper.getAddressLabelId(txtOtherLabelName.getText().toString().trim()) != 0L)
                            address_label = dbHelper.getAddressLabelId(txtOtherLabelName.getText().toString().trim());
                        else
                            address_label = dbHelper.addAddressLabel(txtOtherLabelName.getText().toString());
                    }
                    break;
                case R.id.rbHome:
                    address_label = 1L;
                    break;
                case R.id.rbWork:
                    address_label = 2L;
                    break;
            }

            int floor = 0, gate = 0;

            if (!txtAccountAddressFloor.getText().toString().equals(""))
                floor = Integer.parseInt(txtAccountAddressFloor.getText().toString());
            if (!txtAccountAddressGate.getText().toString().equals(""))
                gate = Integer.parseInt(txtAccountAddressGate.getText().toString());


            long address_id = dbHelper.addAddress(txtAccountAddressInput.getText().toString(),
                    selectedDistrict.getId(),
                    selectedCity.getId(),
                    floor,
                    gate,
                    (int) address_label);
            dbHelper.addCustomerAddress(user_id, (int) address_id);
        }
        else
            Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
    };
}