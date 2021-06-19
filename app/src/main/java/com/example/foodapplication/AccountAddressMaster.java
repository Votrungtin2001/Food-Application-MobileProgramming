package com.example.foodapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import java.util.ArrayList;


public class AccountAddressMaster extends Fragment {
    Spinner spinDistrictInput, spinCityInput;
    EditText txtBranchNameMaster, txtAccountAddressInput, txtAccountAddressGate, txtAccountAddressFloor;
    Button btnAccountAddressSave;
    ArrayList<IdWithNameListItem> citySpinner, districtSpinner;

    DatabaseHelper dbHelper;
    int user_id = -1, address_id = 0, branch_id = 0;

    public AccountAddressMaster() { }

    public static AccountAddressMaster newInstance() {
        return new AccountAddressMaster();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dbHelper = new DatabaseHelper(getContext());

        if (MainActivity.master_id > 0) {
            user_id = MainActivity.master_id;
            address_id = dbHelper.getBranchAddressByMaster(user_id);
            branch_id = dbHelper.getBranchId(address_id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_address_master, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_address));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        spinCityInput = view.findViewById(R.id.spinCityInputMaster);
        citySpinner = new ArrayList<>();
        Cursor cursor = dbHelper.getCities();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CCity._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCity.KEY_NAME));
            citySpinner.add(new IdWithNameListItem(id, name));
        }
        cursor.close();
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, citySpinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCityInput.setAdapter(spinnerAdapter);
        spinCityInput.setOnItemSelectedListener(onCityChosen);

        spinDistrictInput = view.findViewById(R.id.spinDistrictInputMaster);
        districtSpinner = new ArrayList<>();

        txtBranchNameMaster = view.findViewById(R.id.txtBranchNameMaster);
        txtBranchNameMaster.setText(dbHelper.getBranchName(branch_id));
        txtAccountAddressInput = view.findViewById(R.id.txtAccountAddressInputMaster);
        txtAccountAddressFloor = view.findViewById(R.id.txtAccountAddressFloorMaster);
        txtAccountAddressGate = view.findViewById(R.id.txtAccountAddressGateMaster);

        Cursor cAddress = dbHelper.getAddress(address_id);
        if (cAddress.moveToFirst()) {
            int city_id = cAddress.getInt(cAddress.getColumnIndexOrThrow(FoodManagementContract.CAddress.KEY_CITY)),
                    district_id = cAddress.getInt(cAddress.getColumnIndexOrThrow(FoodManagementContract.CAddress.KEY_DISTRICT)),
                    floor = cAddress.getInt(cAddress.getColumnIndexOrThrow(FoodManagementContract.CAddress.KEY_FLOOR)),
                    gate = cAddress.getInt(cAddress.getColumnIndexOrThrow(FoodManagementContract.CAddress.KEY_GATE));
            String address = cAddress.getString(cAddress.getColumnIndexOrThrow(FoodManagementContract.CAddress.KEY_ADDRESS));

            for (int i = 0; i < citySpinner.size(); i++) {
                if (city_id == citySpinner.get(i).getId()) {
                    spinCityInput.setSelection(i);
                    break;
                }
            }

            for (int i = 0; i < districtSpinner.size(); i++) {
                if (district_id == districtSpinner.get(i).getId()) {
                    spinDistrictInput.setSelection(i);
                    break;
                }
            }

            txtAccountAddressFloor.setText(Integer.toString(floor));
            txtAccountAddressGate.setText(Integer.toString(gate));
            txtAccountAddressInput.setText(address);
        }
        cAddress.close();

        btnAccountAddressSave = view.findViewById(R.id.btnAccountAddressSaveMaster);
        btnAccountAddressSave.setOnClickListener(onAddressUpdate);

        return view;
    }

    AdapterView.OnItemSelectedListener onCityChosen = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            IdWithNameListItem spinnerItem = (IdWithNameListItem) spinCityInput.getSelectedItem();
            int city_id = spinnerItem.getId();
            Cursor cursor = dbHelper.getDistricts(city_id);
            districtSpinner = new ArrayList<>();
            while (cursor.moveToNext()) {
                int dist_id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CDistrict._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CDistrict.KEY_NAME));
                districtSpinner.add(new IdWithNameListItem(dist_id, name));
            }
            cursor.close();
            ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, districtSpinner);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinDistrictInput.setAdapter(spinnerAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener onAddressUpdate = v -> {
        if (user_id != -1) {
            dbHelper.updBranchName(branch_id, txtBranchNameMaster.getText().toString());

            IdWithNameListItem selectedCity = (IdWithNameListItem) spinCityInput.getSelectedItem();
            IdWithNameListItem selectedDistrict = (IdWithNameListItem) spinDistrictInput.getSelectedItem();

            int floor = 0, gate = 0;

            if (!txtAccountAddressFloor.getText().toString().equals(""))
                floor = Integer.parseInt(txtAccountAddressFloor.getText().toString());
            if (!txtAccountAddressGate.getText().toString().equals(""))
                gate = Integer.parseInt(txtAccountAddressGate.getText().toString());

            dbHelper.updAddress(address_id,
                    txtAccountAddressInput.getText().toString(),
                    selectedDistrict.getId(),
                    selectedCity.getId(),
                    floor,
                    gate,
                    4);
            Toast.makeText(getContext(), "Cập nhật địa chỉ thành công!", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        }
        else
            Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
    };
}