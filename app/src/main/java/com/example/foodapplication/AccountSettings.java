package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountSettings extends Fragment {
    TextView txtAccountSettingsInfo, txtAccountSettingsPassword, txtAccountAppLanguage, txtAccountAppNotif;

    public AccountSettings() {

    }

    public static AccountSettings newInstance(String param1, String param2) {
        AccountSettings fragment = new AccountSettings();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

        txtAccountSettingsInfo = view.findViewById(R.id.txtAccountSettingsInfo);
        txtAccountSettingsInfo.setOnClickListener(openUserInfoFragment);
        txtAccountSettingsPassword = view.findViewById(R.id.txtAccountSettingsPassword);
        txtAccountSettingsPassword.setOnClickListener(openPasswordFragment);
        txtAccountAppLanguage = view.findViewById(R.id.txtAccountAppLanguage);
        txtAccountAppNotif= view.findViewById(R.id.txtAccountAppNotif);
        txtAccountAppNotif.setOnClickListener(openNotifFragment);

        return view;
    }

    View.OnClickListener openUserInfoFragment = v -> {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), new AccountSettingsInfoFragment(), null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener openPasswordFragment = v -> {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), new AccountSettingsPasswordFragment(), null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener openNotifFragment = v -> {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), new AccountSettingsNotifFragment(), null)
                .addToBackStack(null)
                .commit();
    };
}