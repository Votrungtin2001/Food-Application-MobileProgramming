package com.example.foodapplication.account;

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

import com.example.foodapplication.AccountSettingsInfoMaster;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;

public class AccountSettings extends Fragment {
    TextView txtAccountSettingsInfo, txtAccountSettingsPassword;
    int id;

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

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_settings_account));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        txtAccountSettingsInfo = view.findViewById(R.id.txtAccountSettingsInfo);
        txtAccountSettingsInfo.setOnClickListener(openUserInfoFragment);
        txtAccountSettingsPassword = view.findViewById(R.id.txtAccountSettingsPassword);
        txtAccountSettingsPassword.setOnClickListener(openPasswordFragment);

        return view;
    }

    View.OnClickListener openUserInfoFragment = v -> {
        Fragment fragment;
        if (MainActivity.customer_id > 0) {
            fragment = new AccountSettingsInfoFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), fragment, null)
                    .addToBackStack(null)
                    .commit();
        }
        else {
            if (MainActivity.master_id > 0) {
                fragment = new AccountSettingsInfoMaster();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup) getView().getParent()).getId(), fragment, null)
                        .addToBackStack(null)
                        .commit();
            }
            else
                Toast.makeText(getContext(), "Bạn cần đăng nhập để dùng chức năng này!", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener openPasswordFragment = v -> {
        Fragment fragment = new AccountSettingsPasswordFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };
}