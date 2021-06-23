package com.example.foodapplication.account;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.databaseHelper.FoodManagementContract;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;

public class AccountSettingsInfoOccupationFragment extends Fragment {
    TextView txtOccupationOffice, txtOccupationFree, txtOccupationStudent, txtOccupationHome, txtOccupationOther;
    int user_id = -1;

    DatabaseHelper dbHelper;

    public AccountSettingsInfoOccupationFragment() {

    }

    public static AccountSettingsInfoOccupationFragment newInstance() {
        return new AccountSettingsInfoOccupationFragment();
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
        View view = inflater.inflate(R.layout.fragment_account_settings_info_occupation, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_settings_account_info_occupation));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        txtOccupationOffice = view.findViewById(R.id.txtOccupationOffice);
        txtOccupationOffice.setOnClickListener(onTextViewClick);
        txtOccupationFree = view.findViewById(R.id.txtOccupationFree);
        txtOccupationFree.setOnClickListener(onTextViewClick);
        txtOccupationStudent = view.findViewById(R.id.txtOccupationStudent);
        txtOccupationStudent.setOnClickListener(onTextViewClick);
        txtOccupationHome = view.findViewById(R.id.txtOccupationHome);
        txtOccupationHome.setOnClickListener(onTextViewClick);
        txtOccupationOther= view.findViewById(R.id.txtOccupationOther);
        txtOccupationOther.setOnClickListener(onTextViewClick);

        dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getCustomerById(user_id);
        if (cursor.moveToFirst()) {
            String occupation = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_OCCUPATION));

            if (occupation != null) {
                switch (occupation) {
                    case "Văn phòng":
                        txtOccupationOffice.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                        break;
                    case "Tự kinh doanh/Tự do":
                        txtOccupationFree.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                        break;
                    case "Sinh viên":
                        txtOccupationStudent.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                        break;
                    case "Ở nhà":
                        txtOccupationHome.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                        break;
                    case "Khác":
                        txtOccupationOther.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                        break;
                }
            }
        }
        cursor.close();

        return view;
    }

    View.OnClickListener onTextViewClick = v -> {
        if (user_id != -1) {
            TextView view = (TextView) v;
            dbHelper.updUserOccupation(user_id, view.getText().toString());
            Toast.makeText(getContext(), "Đã cập nhật việc làm!", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        }
    };

}