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
import android.widget.TextView;
import android.widget.Toast;

public class SingleEditTextUpdateFragment extends Fragment {
    private String type, target;
    private int user_id = -1;

    TextView txtSingleEditTitle;
    EditText txtEditText;
    Button btnConfirmEdit;

    DatabaseHelper dbHelper;

    public SingleEditTextUpdateFragment() { }

    public static SingleEditTextUpdateFragment newInstance() {
        return new SingleEditTextUpdateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getString("SINGLE_EDIT_TEXT");
            target = getArguments().getString("EDIT_TARGET");

            if (target.equals("Customer")) {
                if (MainActivity.customer_id > 0)
                    user_id = MainActivity.customer_id;
            } else {
                if (target.equals("Master")) {
                    if (MainActivity.master_id > 0)
                        user_id = MainActivity.master_id;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_edit_text_update, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        txtSingleEditTitle = view.findViewById(R.id.txtSingleEditTitle);
        txtEditText = view.findViewById(R.id.txtEditText);
        btnConfirmEdit = view.findViewById(R.id.btnConfirmEdit);

        if (user_id != -1) {
            dbHelper = new DatabaseHelper(getContext());
            if (target.equals("Customer")) {
                Cursor cursor = dbHelper.getCustomerById(user_id);

                switch (type) {
                    case "EditPhone":
                        txtSingleEditTitle.setText("Cập nhật số điện thoại");
                        btnConfirmEdit.setText("Cập nhật");
                        if (cursor.moveToFirst())
                            txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_PHONE)));
                        break;
                    case "EditName":
                        txtSingleEditTitle.setText("Cập nhật tên");
                        btnConfirmEdit.setText("Cập nhật");
                        if (cursor.moveToFirst())
                            txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_NAME)));
                        break;
                    case "EditEmail":
                        txtSingleEditTitle.setText("Cập nhật email");
                        btnConfirmEdit.setText("Cập nhật");
                        if (cursor.moveToFirst())
                            txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_EMAIL)));
                        break;
                }
                cursor.close();
            } else {
                if (target.equals("Master")) {
                    Cursor cursor = dbHelper.getMasterById(user_id);

                    switch (type) {
                        case "EditPhone":
                            txtSingleEditTitle.setText("Cập nhật số điện thoại");
                            btnConfirmEdit.setText("Cập nhật");
                            if (cursor.moveToFirst())
                                txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CMaster.KEY_PHONE)));
                            break;
                        case "EditName":
                            txtSingleEditTitle.setText("Cập nhật tên");
                            btnConfirmEdit.setText("Cập nhật");
                            if (cursor.moveToFirst())
                                txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CMaster.KEY_NAME)));
                            break;
                        case "EditEmail":
                            txtSingleEditTitle.setText("Cập nhật email");
                            btnConfirmEdit.setText("Cập nhật");
                            if (cursor.moveToFirst())
                                txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CMaster.KEY_EMAIL)));
                            break;
                    }
                }
            }
        }

        btnConfirmEdit.setOnClickListener(onConfirmEditClick);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(txtSingleEditTitle.getText().toString());

        return view;
    }

    View.OnClickListener onConfirmEditClick = v -> {
        if (user_id != -1) {
            if (!txtEditText.getText().toString().equals("")) {
                if (target.equals("Customer"))
                    dbHelper.updUserInfoWithKey(user_id, txtEditText.getText().toString(), type);
                else
                    dbHelper.updMasterInfoWithKey(user_id, txtEditText.getText().toString(), type);

                dbHelper.close();
                Toast.makeText(getContext(), "Cập nhật dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack(null, 0);
            }
        }
        else
            Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
    };
}