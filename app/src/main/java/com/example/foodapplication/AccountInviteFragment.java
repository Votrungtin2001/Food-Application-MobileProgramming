package com.example.foodapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountInviteFragment extends Fragment {
    public AccountInviteFragment() { }

    public static AccountInviteFragment newInstance() {
        AccountInviteFragment fragment = new AccountInviteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_invite, container, false);

        TextView txtSendSMS = view.findViewById(R.id.txtSendSMS);
        txtSendSMS.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setType("vnd.android-dir/mms-sms");
            startActivity(intent);
        });

        TextView txtSendEmail = view.findViewById(R.id.txtSendEmail);
        txtSendEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            startActivity(intent);
        });

        return view;
    }
}