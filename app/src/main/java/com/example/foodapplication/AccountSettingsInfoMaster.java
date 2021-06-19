package com.example.foodapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountSettingsInfoMaster extends Fragment {
    TextView txtAccountSettingsInfoPhoneMaster, txtAccountSettingsInfoNameMaster, txtAccountSettingsInfoEmailMaster, txtAccountSettingsPasswordMaster;

    public AccountSettingsInfoMaster() {}

    public static AccountSettingsInfoMaster newInstance(String param1, String param2) {
        return new AccountSettingsInfoMaster();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings_info_master, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_settings_account_info));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        txtAccountSettingsInfoNameMaster = view.findViewById(R.id.txtAccountSettingsInfoNameMaster);
        txtAccountSettingsInfoNameMaster.setOnClickListener(openSingleEditFragment);
        txtAccountSettingsInfoEmailMaster = view.findViewById(R.id.txtAccountSettingsInfoEmailMaster);
        txtAccountSettingsInfoEmailMaster.setOnClickListener(openSingleEditFragment);
        txtAccountSettingsInfoPhoneMaster = view.findViewById(R.id.txtAccountSettingsInfoPhoneMaster);
        txtAccountSettingsInfoPhoneMaster.setOnClickListener(openSingleEditFragment);
        txtAccountSettingsPasswordMaster = view.findViewById(R.id.txtAccountSettingsPasswordMaster);
        txtAccountSettingsPasswordMaster.setOnClickListener(openPasswordFragment);

        return view;
    }

    View.OnClickListener openSingleEditFragment = v -> {
        SingleEditTextUpdateFragment fragment = new SingleEditTextUpdateFragment();

        Bundle args = new Bundle();
        args.putString("EDIT_TARGET", "Master");
        switch (v.getId()) {
            case R.id.txtAccountSettingsInfoPhoneMaster:
                args.putString("SINGLE_EDIT_TEXT", "EditPhone");
                break;
            case R.id.txtAccountSettingsInfoNameMaster:
                args.putString("SINGLE_EDIT_TEXT", "EditName");
                break;
            case R.id.txtAccountSettingsInfoEmailMaster:
                args.putString("SINGLE_EDIT_TEXT", "EditEmail");
                break;
        }

        fragment.setArguments(args);
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