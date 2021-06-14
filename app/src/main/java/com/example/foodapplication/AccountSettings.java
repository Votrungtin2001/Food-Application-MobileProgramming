package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountSettings extends Fragment {
    TextView txtAccountSettingsInfo, txtAccountSettingsPassword;

    public AccountSettings() {

    }

    public static AccountSettings newInstance() {
        return new AccountSettings();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

        txtAccountSettingsInfo = view.findViewById(R.id.txtAccountSettingsInfo);
        txtAccountSettingsInfo.setOnClickListener(openUserInfoFragment);
        txtAccountSettingsPassword = view.findViewById(R.id.txtAccountSettingsPassword);
        txtAccountSettingsPassword.setOnClickListener(openPasswordFragment);

        return view;
    }

    View.OnClickListener openUserInfoFragment = v -> {
        Fragment fragment = new AccountSettingsInfoFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener openPasswordFragment = v -> {
        Fragment fragment = new AccountSettingsPasswordFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };
}