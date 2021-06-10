package com.example.foodapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SingleEditTextUpdateFragment extends Fragment {
    private String type;
    private int user_id = -1;

    TextView txtSingleEditTitle;
    EditText txtEditText;
    Button btnConfirmEdit;

    DatabaseHelper dbHelper;

    public SingleEditTextUpdateFragment() {
        // Required empty public constructor
    }

    public static SingleEditTextUpdateFragment newInstance() {
        SingleEditTextUpdateFragment fragment = new SingleEditTextUpdateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getArguments() != null) && (getArguments().containsKey("CUSTOMER_ID")) && (getArguments().containsKey("SINGLE_EDIT_TEXT"))) {
            user_id = getArguments().getInt("CUSTOMER_ID");
            type = getArguments().getString("SINGLE_EDIT_TEXT");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_edit_text_update, container, false);

        dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getCustomerById(user_id);

        txtSingleEditTitle = view.findViewById(R.id.txtSingleEditTitle);
        txtEditText = view.findViewById(R.id.txtEditText);
        btnConfirmEdit = view.findViewById(R.id.btnConfirmEdit);

        switch (type) {
            case "EditPhone":
                txtSingleEditTitle.setText("Edit phone number");
                txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_PHONE)));
                btnConfirmEdit.setText("Update phone number");
                break;
            case "EditName":
                txtSingleEditTitle.setText("Change name");
                txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_USERNAME)));
                btnConfirmEdit.setText("Save name");
                break;
            case "EditEmail":
                txtSingleEditTitle.setText("Update email");
                txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_EMAIL)));
                btnConfirmEdit.setText("Save and update email");
                break;
        }
        cursor.close();

        btnConfirmEdit.setOnClickListener(onConfirmEditClick);

        return view;
    }

    View.OnClickListener onConfirmEditClick = v -> {
        if (txtEditText.getText().toString() != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            dbHelper.updUserInfoWithKey(user_id, txtEditText.getText().toString(), type);
            dbHelper.close();
            Toast.makeText(getContext(), "User data updated!", Toast.LENGTH_SHORT).show();
        }
    };
}