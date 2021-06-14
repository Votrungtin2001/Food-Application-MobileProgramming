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
    TextView txtAccountSettingsInfoPhone, txtAccountSettingsInfoName, txtAccountSettingsInfoEmail, txtAccountSettingsInfoGender, txtAccountSettingsInfoDoB, txtAccountSettingsInfoOccupation;
    int user_id = -1;

    public AccountSettingsInfoFragment() {}

    public static AccountSettingsInfoFragment newInstance() {
        return new AccountSettingsInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_account_settings_info, container, false);

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
        if (user_id != -1) {
            String[] gender = {"Nam", "Nữ", "Chưa rõ"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setItems(gender, (dialog, which) -> {
                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                switch (gender[which]) {
                    case "Nam":
                        dbHelper.updUserGender(user_id, 0);
                        break;
                    case "Nữ":
                        dbHelper.updUserGender(user_id, 1);
                        break;
                    case "Chưa rõ":
                        dbHelper.updUserGender(user_id, 2);
                        break;
                }
                dbHelper.close();
                Toast.makeText(getContext(), "Đã cập nhật giới tính!", Toast.LENGTH_SHORT).show();
            });
            builder.show();
        }
        else
            Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
    };

    View.OnClickListener openDateFragment = v -> {
        DialogFragment dateFragment = new DateFragment();
        dateFragment.show(getChildFragmentManager(), "DatePicker");
    };

    View.OnClickListener openOccupationFragment = v-> {
        AccountSettingsInfoOccupationFragment newFragment = new AccountSettingsInfoOccupationFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };
}