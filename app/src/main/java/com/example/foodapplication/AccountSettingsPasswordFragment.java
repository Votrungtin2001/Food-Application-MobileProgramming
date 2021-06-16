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
    int user_id = -1;

    public AccountSettingsPasswordFragment() { }

    public static AccountSettingsPasswordFragment newInstance() {
        return new AccountSettingsPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_account_settings_password, container, false);

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
            Cursor cursor = dbHelper.getCustomerById(user_id);
            if (cursor.moveToFirst()) {
                String currentPassword = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_PASSWORD));

                if (txtCurrentPassword.getText().toString().equals(currentPassword)) {
                    if (txtConfirmPassword.getText().toString().equals(txtNewPassword.getText().toString())) {
                        dbHelper.updUserPassword(user_id, txtNewPassword.getText().toString());
                        Toast.makeText(getContext(), "Đã cập nhật mật khẩu!", Toast.LENGTH_SHORT).show();
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