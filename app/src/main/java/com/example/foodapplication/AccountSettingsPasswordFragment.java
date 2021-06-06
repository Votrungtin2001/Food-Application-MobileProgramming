package com.example.foodapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountSettingsPasswordFragment extends Fragment {
    EditText txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    Button btnSavePassword;
    int cus_id = 0;//pass this in

    public AccountSettingsPasswordFragment() { }

    public static AccountSettingsPasswordFragment newInstance() {
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
        btnSavePassword.setOnClickListener(onPasswordSave);

        return view;
    }

    View.OnClickListener onPasswordSave = v -> {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getCustomerById(cus_id);
        String currentPassword = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_PASSWORD));
        cursor.close();

        if (txtCurrentPassword.getText().toString().equals(currentPassword)) {
            if (txtConfirmPassword.getText().toString().equals(txtNewPassword.getText().toString())) {
                dbHelper.updUserPassword(cus_id, txtNewPassword.getText().toString());
                Toast.makeText(getContext(), "Password updated!!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getContext(), "Password fields do not match!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();

        dbHelper.close();
    };
}