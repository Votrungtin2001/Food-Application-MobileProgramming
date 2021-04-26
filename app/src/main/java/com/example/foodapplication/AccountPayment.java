package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountPayment extends Fragment {
    private int credits;
    TextView txtAccountPaymentCoins, txtAccountPaymentHistory, txtAccountPaymentTopup;

    public AccountPayment() {

    }

    public static AccountPayment newInstance() {
        AccountPayment fragment = new AccountPayment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account_payment, container, false);

        txtAccountPaymentCoins = view.findViewById(R.id.txtAccountPaymentCoins);
        txtAccountPaymentTopup = view.findViewById(R.id.txtAccountPaymentTopup);
        txtAccountPaymentTopup.setClickable(true);
        txtAccountPaymentTopup.setOnClickListener(runTopupFragment);
        txtAccountPaymentHistory = view.findViewById(R.id.txtAccountPaymentHistory);
        txtAccountPaymentHistory.setClickable(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        txtAccountPaymentCoins.setText("Your Credits: " + Integer.toString(credits) + "");
    }

    View.OnClickListener runTopupFragment = v -> {
        AccountPaymentTopup fragment = new AccountPaymentTopup();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };
}