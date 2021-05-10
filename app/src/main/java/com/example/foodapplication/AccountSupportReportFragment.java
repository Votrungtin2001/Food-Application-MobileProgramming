package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AccountSupportReportFragment extends Fragment {
    public AccountSupportReportFragment() {
    }

    public static AccountSupportReportFragment newInstance() {
        AccountSupportReportFragment fragment = new AccountSupportReportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_support_report, container, false);
    }
}