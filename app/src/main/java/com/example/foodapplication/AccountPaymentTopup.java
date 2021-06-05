package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class AccountPaymentTopup extends Fragment {
    Button btnAdd50k, btnAdd100k, btnAdd200k, btnAdd300k, btnAdd500k, btnAdd1M, btnAdd2M, btnAdd5M, btnAdd10M, btnDeposit;
    EditText txtTopupAmount;
    int cus_id = 0;

    public AccountPaymentTopup() {

    }

    public static AccountPaymentTopup newInstance() {
        AccountPaymentTopup fragment = new AccountPaymentTopup();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_payment_topup, container, false);

        btnAdd50k = view.findViewById(R.id.btnAdd50k);
        btnAdd50k.setOnClickListener(onAmountClickListener);
        btnAdd100k = view.findViewById(R.id.btnAdd100k);
        btnAdd100k.setOnClickListener(onAmountClickListener);
        btnAdd200k = view.findViewById(R.id.btnAdd200k);
        btnAdd200k.setOnClickListener(onAmountClickListener);
        btnAdd300k = view.findViewById(R.id.btnAdd300k);
        btnAdd300k.setOnClickListener(onAmountClickListener);
        btnAdd500k = view.findViewById(R.id.btnAdd500k);
        btnAdd500k.setOnClickListener(onAmountClickListener);
        btnAdd1M = view.findViewById(R.id.btnAdd1M);
        btnAdd1M.setOnClickListener(onAmountClickListener);
        btnAdd2M = view.findViewById(R.id.btnAdd2M);
        btnAdd2M.setOnClickListener(onAmountClickListener);
        btnAdd5M = view.findViewById(R.id.btnAdd5M);
        btnAdd5M.setOnClickListener(onAmountClickListener);
        btnAdd10M = view.findViewById(R.id.btnAdd10M);
        btnAdd10M.setOnClickListener(onAmountClickListener);
        btnDeposit = view.findViewById(R.id.btnDeposit);
        btnDeposit.setOnClickListener(onDepositClick);

        txtTopupAmount = view.findViewById(R.id.txtTopupAmount);

        return view;
    }

    View.OnClickListener onAmountClickListener = v -> {
        Button btn = (Button) v;
        txtTopupAmount.setText(btn.getText());
    };

    View.OnClickListener onDepositClick = v -> {
        if ((Integer.getInteger(txtTopupAmount.getText().toString()) != 0) && (txtTopupAmount.getText().toString() != null)) {
            int topup = Integer.parseInt(txtTopupAmount.getText().toString());

            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            dbHelper.addTransaction(cus_id, topup);
            int credits = dbHelper.getCredits(cus_id);
            credits += topup;
            dbHelper.updCredits(cus_id, credits);
        }
    };
}