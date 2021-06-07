package com.example.foodapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class AccountSettingsInfoFragment extends Fragment {
    TextView txtAccountSettingsInfoAvatar, txtAccountSettingsInfoPhone, txtAccountSettingsInfoName, txtAccountSettingsInfoEmail, txtAccountSettingsInfoGender, txtAccountSettingsInfoDoB, txtAccountSettingsInfoOccupation;
    int cus_id;

    public AccountSettingsInfoFragment() {}

    public static AccountSettingsInfoFragment newInstance() {
        AccountSettingsInfoFragment fragment = new AccountSettingsInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_account_settings_info, container, false);

        txtAccountSettingsInfoAvatar = view.findViewById(R.id.txtAccountSettingsInfoAvatar);
        txtAccountSettingsInfoPhone = view.findViewById(R.id.txtAccountSettingsInfoPhone);
        txtAccountSettingsInfoPhone.setOnClickListener(openSingleEditFragment);
        txtAccountSettingsInfoName = view.findViewById(R.id.txtAccountSettingsInfoName);
        txtAccountSettingsInfoName.setOnClickListener(openSingleEditFragment);
        txtAccountSettingsInfoEmail = view.findViewById(R.id.txtAccountSettingsInfoEmail);
        txtAccountSettingsInfoEmail.setOnClickListener(openSingleEditFragment);
        txtAccountSettingsInfoGender = view.findViewById(R.id.txtAccountSettingsInfoGender);
        txtAccountSettingsInfoGender.setOnClickListener(openGenderDialogBox);
        txtAccountSettingsInfoDoB = view.findViewById(R.id.txtAccountSettingsInfoDoB);
        txtAccountSettingsInfoDoB.setOnClickListener(openDateFragment);
        txtAccountSettingsInfoOccupation = view.findViewById(R.id.txtAccountSettingsInfoOccupation);
        txtAccountSettingsInfoOccupation.setOnClickListener(openOccupationFragment);

        return view;
    }

    View.OnClickListener openSingleEditFragment = v -> {
        SingleEditTextUpdateFragment fragment = new SingleEditTextUpdateFragment();

        Bundle args = new Bundle();
        args.putInt("CUSTOMER_ID", cus_id);
        switch (v.getId()) {
            case R.id.txtAccountSettingsInfoPhone:
                args.putString("SINGLE_EDIT_TEXT", "EditPhone");
                break;
            case R.id.txtAccountSettingsInfoName:
                args.putString("SINGLE_EDIT_TEXT", "EditName");
                break;
            case R.id.txtAccountSettingsInfoEmail:
                args.putString("SINGLE_EDIT_TEXT", "EditEmail");
                break;
        }

        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener openGenderDialogBox = v -> {
        String[] gender = { "Male", "Female", "None" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(gender, (dialog, which) -> {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            switch (gender[which]) {
                case "Male":
                    dbHelper.updUserGender(cus_id, 0);
                    break;
                case "Female":
                    dbHelper.updUserGender(cus_id, 1);
                    break;
                case "None":
                    dbHelper.updUserGender(cus_id, 2);
                    break;
            }
            dbHelper.close();
            Toast.makeText(getContext(), "Gender updated!", Toast.LENGTH_SHORT).show();
        });
        builder.show();
    };

    View.OnClickListener openDateFragment = v -> {
        DialogFragment dateFragment = new DateFragment();

        Bundle args = new Bundle();
        args.putInt("CUSTOMER_ID", cus_id);
        dateFragment.setArguments(args);

        dateFragment.show(getChildFragmentManager(), "DatePicker");
    };

    View.OnClickListener openOccupationFragment = v-> {
        AccountSettingsInfoOccupationFragment newFragment = new AccountSettingsInfoOccupationFragment();
        Bundle args = new Bundle();
        args.putInt("CUSTOMER_ID", cus_id);
        newFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };
}