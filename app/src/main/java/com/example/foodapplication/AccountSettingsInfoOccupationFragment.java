package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountSettingsInfoOccupationFragment extends Fragment {
    TextView txtOccupationOffice, txtOccupationFree, txtOccupationStudent, txtOccupationHome, txtOccupationOther;
    int cus_id; //pass customer id

    public AccountSettingsInfoOccupationFragment() {

    }

    public static AccountSettingsInfoOccupationFragment newInstance() {
        AccountSettingsInfoOccupationFragment fragment = new AccountSettingsInfoOccupationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        cus_id = args.getInt("CUSTOMER_ID");
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

        return view;
    }

    View.OnClickListener onTextViewClick = v -> {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        TextView view = (TextView) v;
        dbHelper.updUserOccupation(cus_id, view.getText().toString());
        dbHelper.close();
    };

}