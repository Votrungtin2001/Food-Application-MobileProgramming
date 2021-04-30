package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AccountSettingsPasswordFragment extends Fragment {
    EditText txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    Button btnSavePassword;

    public AccountSettingsPasswordFragment() { }

    public static AccountSettingsPasswordFragment newInstance(String param1, String param2) {
        AccountSettingsPasswordFragment fragment = new AccountSettingsPasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings_password, container, false);

        txtCurrentPassword = view.findViewById(R.id.txtCurrentPassword);
        txtNewPassword = view.findViewById(R.id.txtNewPassword);
        txtConfirmPassword = view.findViewById(R.id.txtConfirmPassword);
        btnSavePassword = view.findViewById(R.id.btnSavePassword);

        return view;
    }
}