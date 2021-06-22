package com.example.foodapplication.account;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.databaseHelper.FoodManagementContract;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;
import com.example.foodapplication.Transaction;

import java.util.ArrayList;

import adapter.TransactionAdapter;

public class AccountPaymentHistory extends Fragment {
    ArrayList<Transaction> transactions;
    int user_id = -1;

    public AccountPaymentHistory() { }

    public static AccountPaymentHistory newInstance() {
        return new AccountPaymentHistory();
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
        View view = inflater.inflate(R.layout.fragment_account_payment_history, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_payment_history));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        RecyclerView rvTransactionHistory = view.findViewById(R.id.rvTransactionHistory);
        transactions = new ArrayList<>();

        if (user_id != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            Cursor cursor = dbHelper.getTransactions(user_id);
            while (cursor.moveToNext()) {
                int credits = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CTransaction.KEY_CREDITS));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CTransaction.KEY_DATE));
                transactions.add(new Transaction(credits, date));
            }
            cursor.close();

            TransactionAdapter adapter = new TransactionAdapter(transactions);
            rvTransactionHistory.setAdapter(adapter);
            rvTransactionHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        return view;
    }
}