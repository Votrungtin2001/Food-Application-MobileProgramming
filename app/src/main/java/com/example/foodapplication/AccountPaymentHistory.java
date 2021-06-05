package com.example.foodapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import adapter.TransactionAdapter;

public class AccountPaymentHistory extends Fragment {
    ArrayList<Transaction> transactions;
    int cus_id = 0; // again, need a way to pass in user id

    public AccountPaymentHistory() { }

    public static AccountPaymentHistory newInstance() {
        AccountPaymentHistory fragment = new AccountPaymentHistory();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_payment_history, container, false);

        RecyclerView rvTransactionHistory = view.findViewById(R.id.rvTransactionHistory);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getTransactions(cus_id);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        while (cursor.moveToNext()) {
            Date date = null;
            try {
                date = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CTransaction.KEY_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int credits = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CTransaction.KEY_CREDITS));
            transactions.add(new Transaction(credits, date));
        }
        cursor.close();

        TransactionAdapter adapter = new TransactionAdapter(transactions);
        rvTransactionHistory.setAdapter(adapter);
        rvTransactionHistory.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}