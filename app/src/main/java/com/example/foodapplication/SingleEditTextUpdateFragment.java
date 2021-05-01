package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SingleEditTextUpdateFragment extends Fragment {
    private String type;

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

        return view;
    }
}