package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountPayment extends Fragment {
    private int credits = 0, user_id = -1;
    TextView txtAccountPaymentCoins, txtAccountPaymentHistory, txtAccountPaymentTopup;

    public AccountPayment() {

    }

    public static AccountPayment newInstance() {
        return new AccountPayment();
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
        View view = inflater.inflate(R.layout.fragment_account_payment, container, false);

        txtAccountPaymentCoins = view.findViewById(R.id.txtAccountPaymentCoins);
        txtAccountPaymentTopup = view.findViewById(R.id.txtAccountPaymentTopup);
        txtAccountPaymentTopup.setClickable(true);
        txtAccountPaymentTopup.setOnClickListener(runTopupFragment);
        txtAccountPaymentHistory = view.findViewById(R.id.txtAccountPaymentHistory);
        txtAccountPaymentHistory.setClickable(true);
        txtAccountPaymentHistory.setOnClickListener(runHistoryFragment);

        if (user_id != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            credits = dbHelper.getCredits(user_id);
            if (credits > 0)
                txtAccountPaymentCoins.setText("Your Credits: " + credits);
            else
                txtAccountPaymentCoins.setText("Your Credits: 0");
        }

        return view;
    }

    View.OnClickListener runTopupFragment = v -> {
        AccountPaymentTopup fragment = new AccountPaymentTopup();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runHistoryFragment = v -> {
        AccountPaymentHistory fragment = new AccountPaymentHistory();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };
}