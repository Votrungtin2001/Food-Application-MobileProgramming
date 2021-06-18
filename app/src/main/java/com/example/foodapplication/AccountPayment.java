package com.example.foodapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_payment));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

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
                txtAccountPaymentCoins.setText("Tiền trong tài khoản: " + credits);
            else
                txtAccountPaymentCoins.setText("Tiền trong tài khoản: 0");
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