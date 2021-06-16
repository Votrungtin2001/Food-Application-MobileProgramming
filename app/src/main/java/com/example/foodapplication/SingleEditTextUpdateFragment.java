package com.example.foodapplication;

import android.database.Cursor;
import android.os.Bundle;

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
        return new SingleEditTextUpdateFragment();
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
                Toast.makeText(getContext(), "Cập nhật dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack(null, 0);
            }
        }
        else
            Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
    };
}