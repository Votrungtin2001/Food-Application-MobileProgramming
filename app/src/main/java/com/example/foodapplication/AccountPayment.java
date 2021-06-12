package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountPayment extends Fragment {
    private int credits, user_id = -1;
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

        if ((getArguments() != null) && (getArguments().containsKey("CUSTOMER_ID")))
            user_id = getArguments().getInt("CUSTOMER_ID");
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
        txtAccountPaymentHistory.setOnClickListener(runHistoryFragment);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        credits = dbHelper.getCredits(user_id);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        txtAccountPaymentCoins.setText("Your Credits: " + Integer.toString(credits) + "");
    }

    View.OnClickListener runTopupFragment = v -> {
        AccountPaymentTopup fragment = new AccountPaymentTopup();
        Bundle args = new Bundle();
        args.putInt("CUSTOMER_ID", user_id);
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runHistoryFragment = v -> {
        AccountPaymentHistory fragment = new AccountPaymentHistory();
        Bundle args = new Bundle();
        args.putInt("CUSTOMER_ID", user_id);
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };
}