package com.example.foodapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

            switch (occupation) {
                case "Office State/Professional":
                    txtOccupationOffice.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                    break;
                case "Self-Employed/Freelancer":
                    txtOccupationFree.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                    break;
                case "Student":
                    txtOccupationStudent.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                    break;
                case "At Home":
                    txtOccupationHome.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                    break;
                case "Other":
                    txtOccupationOther.setBackgroundColor(getResources().getColor(R.color.quantum_bluegrey400));
                    break;
            }
        }
        cursor.close();

        return view;
    }

    View.OnClickListener onTextViewClick = v -> {
        if (user_id != -1) {
            TextView view = (TextView) v;
            dbHelper.updUserOccupation(user_id, view.getText().toString());
            Toast.makeText(getContext(), "Occupation updated!", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        }
    };

}