package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountSettingsInfoOccupationFragment extends Fragment {
    TextView txtOccupationOffice, txtOccupationFree, txtOccupationStudent, txtOccupationHome, txtOccupationOther;

    public AccountSettingsInfoOccupationFragment() {

    }

    public static AccountSettingsInfoOccupationFragment newInstance() {
        AccountSettingsInfoOccupationFragment fragment = new AccountSettingsInfoOccupationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings_info_occupation, container, false);

        txtOccupationOffice = view.findViewById(R.id.txtOccupationOffice);
        txtOccupationFree = view.findViewById(R.id.txtOccupationFree);
        txtOccupationStudent = view.findViewById(R.id.txtOccupationStudent);
        txtOccupationHome = view.findViewById(R.id.txtOccupationHome);
        txtOccupationOther= view.findViewById(R.id.txtOccupationOther);

        return view;
    }
}