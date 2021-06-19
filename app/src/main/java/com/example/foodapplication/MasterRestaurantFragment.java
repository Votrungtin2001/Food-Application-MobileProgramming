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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MasterRestaurantFragment extends Fragment {
    Button btnConfirmMasterRestaurant;
    Spinner spinMasterRestaurant;
    DatabaseHelper dbHelper;

    public MasterRestaurantFragment() { }

    public static MasterRestaurantFragment newInstance(String param1, String param2) {
        return new MasterRestaurantFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_restaurant, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_address));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        spinMasterRestaurant = view.findViewById(R.id.spinMasterRestaurant);
        ArrayList<IdWithNameListItem> list = new ArrayList<>();
        dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getRestaurant();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CRestaurant._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CRestaurant.KEY_NAME));
            list.add(new IdWithNameListItem(id, name));
        }
        cursor.close();
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMasterRestaurant.setAdapter(spinnerAdapter);
        spinMasterRestaurant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                btnConfirmMasterRestaurant.setClickable(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnConfirmMasterRestaurant = view.findViewById(R.id.btnConfirmMasterRestaurant);
        btnConfirmMasterRestaurant.setClickable(false);
        btnConfirmMasterRestaurant.setOnClickListener((View.OnClickListener) v -> {
            if (MainActivity.master_id > 0) {
                IdWithNameListItem item = (IdWithNameListItem) spinMasterRestaurant.getSelectedItem();
                int res_id = item.getId();

                long address_id = dbHelper.addAddress("Địa chỉ", 1, 3, 0, 0, 4);
                dbHelper.addBranch(item.getName().concat(" - Chi nhánh mới"), res_id, (int) address_id, MainActivity.master_id);
                Toast.makeText(getContext(), "Đăng ký chi nhánh thành công!", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack(null, 0);
            }
        });

        return view;
    }
}