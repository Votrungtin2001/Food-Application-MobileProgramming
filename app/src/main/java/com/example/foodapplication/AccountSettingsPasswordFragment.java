package com.example.foodapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountSettingsPasswordFragment extends Fragment {
    EditText txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    Button btnSavePassword;
    int user_id = -1;
    boolean IsUpdatingCustomer;

    public AccountSettingsPasswordFragment() { }

    public static AccountSettingsPasswordFragment newInstance() {
        return new AccountSettingsPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.customer_id > 0) {
            user_id = MainActivity.customer_id;
            IsUpdatingCustomer = true;
        }
        else {
            if (MainActivity.master_id > 0) {
                user_id = MainActivity.master_id;
                IsUpdatingCustomer = false;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings_password, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_settings_account_password_desc));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        txtCurrentPassword = view.findViewById(R.id.txtCurrentPassword);
        txtNewPassword = view.findViewById(R.id.txtNewPassword);
        txtConfirmPassword = view.findViewById(R.id.txtConfirmPassword);
        btnSavePassword = view.findViewById(R.id.btnSavePassword);
        btnSavePassword.setOnClickListener(onPasswordSave);

        return view;
    }

    View.OnClickListener onPasswordSave = v -> {
        if (user_id != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            Cursor cursor;

            if (IsUpdatingCustomer)
                cursor = dbHelper.getCustomerById(user_id);
            else
                cursor = dbHelper.getMasterById(user_id);

            if (cursor.moveToFirst()) {
                String currentPassword;

                if (IsUpdatingCustomer)
                    currentPassword = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_PASSWORD));
                else
                    currentPassword = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CMaster.KEY_PASSWORD));

                if (txtCurrentPassword.getText().toString().equals(currentPassword)) {
                    if (txtConfirmPassword.getText().toString().equals(txtNewPassword.getText().toString())) {
                        if (IsUpdatingCustomer)
                            dbHelper.updUserPassword(user_id, txtNewPassword.getText().toString());
                        else
                            dbHelper.updMasterPassword(user_id, txtNewPassword.getText().toString());

                        Toast.makeText(getContext(), "Đã cập nhật mật khẩu!", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        fragmentManager.popBackStack(null, 0);
                    } else {
                        txtNewPassword.setText("");
                        txtConfirmPassword.setText("");
                        Toast.makeText(getContext(), "Mật khẩu xác nhận không trùng mật khẩu mới!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    txtCurrentPassword.setText("");
                    txtNewPassword.setText("");
                    txtConfirmPassword.setText("");
                    Toast.makeText(getContext(), "Sai mật khẩu hiện tại!", Toast.LENGTH_SHORT).show();
                }
            }
            dbHelper.close();
            cursor.close();
        }
        else
            Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
    };
}