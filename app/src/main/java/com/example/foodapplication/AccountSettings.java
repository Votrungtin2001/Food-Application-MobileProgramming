package com.example.foodapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountSettings extends Fragment {
    TextView txtAccountSettingsInfo, txtAccountSettingsPassword, txtAccountAppLanguage, txtAccountAppNotif;
    int user_id;
    Bundle importArgs;

    public AccountSettings() {

    }

    public static AccountSettings newInstance(String param1, String param2) {
        AccountSettings fragment = new AccountSettings();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        user_id = args.getInt("CUSTOMER_ID");
    }

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

        importArgs = new Bundle();
        importArgs.putInt("CUSTOMER_ID", user_id);

        return view;
    }

    View.OnClickListener openUserInfoFragment = v -> {
        Fragment fragment = new AccountSettingsInfoFragment();
        fragment.setArguments(importArgs);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener openPasswordFragment = v -> {
        Fragment fragment = new AccountSettingsPasswordFragment();
        fragment.setArguments(importArgs);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
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