package com.example.foodapplication.account;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;

import java.util.Calendar;
import java.util.Date;


public class AccountPaymentTopup extends Fragment {
    Button btnAdd50k, btnAdd100k, btnAdd200k, btnAdd300k, btnAdd500k, btnAdd1M, btnAdd2M, btnAdd5M, btnAdd10M, btnDeposit;
    EditText txtTopupAmount;
    int user_id = -1;

    public AccountPaymentTopup() {

    }

    public static AccountPaymentTopup newInstance() {
        return new AccountPaymentTopup();
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
        View view = inflater.inflate(R.layout.fragment_account_payment_topup, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_payment_topup));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

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
        if (!txtTopupAmount.getText().toString().equals("")) {
            if (user_id != -1) {
                int topup = Integer.parseInt(txtTopupAmount.getText().toString());
                Date date = Calendar.getInstance().getTime();

                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                dbHelper.addTransaction(user_id, date, topup);
                int credits = dbHelper.getCredits(user_id);
                credits += topup;
                dbHelper.updCredits(user_id, credits);
                Toast.makeText(getContext(), "Nạp tiền thành công!", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack(null, 0);
            } else
                Toast.makeText(getContext(), "User not found. Did you forget to log in?", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getContext(), "Xin hãy nhập lượng tiền cần nạp!", Toast.LENGTH_LONG).show();
    };
}