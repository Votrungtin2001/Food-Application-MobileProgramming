package com.example.foodapplication.account.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;

import static com.example.foodapplication.MySQL.MySQLQuerry.UpdateCustomerGender;

public class AccountSettingsInfoFragment extends Fragment {
    TextView txtAccountSettingsInfoPhone, txtAccountSettingsInfoName, txtAccountSettingsInfoEmail, txtAccountSettingsInfoGender, txtAccountSettingsInfoDoB, txtAccountSettingsInfoOccupation;
    int user_id = -1;

    private final String TAG = "AccountSettingIF";

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

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_settings_account_info));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

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
        args.putString("EDIT_TARGET", "Customer");
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
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                switch (gender[which]) {
                    case "Nam":
                        UpdateCustomerGender(user_id, 0, progressDialog, TAG, getActivity());
                        break;
                    case "Nữ":
                        UpdateCustomerGender(user_id, 1, progressDialog, TAG, getActivity());
                        break;
                    case "Chưa rõ":
                        UpdateCustomerGender(user_id, 2, progressDialog, TAG, getActivity());
                        break;
                }
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