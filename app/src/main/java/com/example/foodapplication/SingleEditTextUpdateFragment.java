package com.example.foodapplication;

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
    private int cus_id;

    TextView txtSingleEditTitle;
    EditText txtEditText;
    Button btnConfirmEdit;

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

        Bundle args = getArguments();
        type = args.getString("SINGLE_EDIT_TEXT");
        cus_id = args.getInt("CUSTOMER_ID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_edit_text_update, container, false);

        txtSingleEditTitle = view.findViewById(R.id.txtSingleEditTitle);
        txtEditText = view.findViewById(R.id.txtEditText);
        btnConfirmEdit = view.findViewById(R.id.btnConfirmEdit);

        switch (type) {
            case "EditPhone":
                txtSingleEditTitle.setText("Edit phone number");
                btnConfirmEdit.setText("Update phone number");
                break;
            case "EditName":
                txtSingleEditTitle.setText("Change name");
                btnConfirmEdit.setText("Save name");
                break;
            case "EditEmail":
                txtSingleEditTitle.setText("Update email");
                btnConfirmEdit.setText("Save and update email");
                break;
        }

        btnConfirmEdit.setOnClickListener(onConfirmEditClick);

        return view;
    }

    View.OnClickListener onConfirmEditClick = v -> {
        if (txtEditText.getText().toString() != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            dbHelper.updUserInfoWithKey(cus_id, txtEditText.getText().toString(), type);
            dbHelper.close();
            Toast.makeText(getContext(), "User data updated!", Toast.LENGTH_SHORT).show();
        }
    };
}