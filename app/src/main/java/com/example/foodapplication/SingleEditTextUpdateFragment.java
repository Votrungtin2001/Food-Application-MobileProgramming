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

        type = getArguments().getString("SINGLE_EDIT_TEXT");
        if (MainActivity.customer_id > 0)
            user_id = MainActivity.customer_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_edit_text_update, container, false);

        txtSingleEditTitle = view.findViewById(R.id.txtSingleEditTitle);
        txtEditText = view.findViewById(R.id.txtEditText);
        btnConfirmEdit = view.findViewById(R.id.btnConfirmEdit);

        if (user_id != -1) {
            dbHelper = new DatabaseHelper(getContext());
            Cursor cursor = dbHelper.getCustomerById(user_id);

            switch (type) {
                case "EditPhone":
                    txtSingleEditTitle.setText("Edit phone number");
                    btnConfirmEdit.setText("Update phone number");
                    if (cursor.moveToFirst())
                        txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_PHONE)));
                    break;
                case "EditName":
                    txtSingleEditTitle.setText("Change name");
                    btnConfirmEdit.setText("Save name");
                    if (cursor.moveToFirst())
                        txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_NAME)));
                    break;
                case "EditEmail":
                    txtSingleEditTitle.setText("Update email");
                    btnConfirmEdit.setText("Save and update email");
                    if (cursor.moveToFirst())
                        txtEditText.setHint(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_EMAIL)));
                    break;
            }
            cursor.close();
        }

        btnConfirmEdit.setOnClickListener(onConfirmEditClick);
        return view;
    }

    View.OnClickListener onConfirmEditClick = v -> {
        if (user_id != -1) {
            if (!txtEditText.getText().toString().equals("")) {
                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                dbHelper.updUserInfoWithKey(user_id, txtEditText.getText().toString(), type);
                dbHelper.close();
                Toast.makeText(getContext(), "User data updated!", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
    };
}