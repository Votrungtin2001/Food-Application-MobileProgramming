package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class AccountSupportHistory extends Fragment {
    Button btnOpenFilter;
    Spinner spinReportStatus;

    public AccountSupportHistory() { }

    public static AccountSupportHistory newInstance() {
        AccountSupportHistory fragment = new AccountSupportHistory();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_support_history, container, false);

        btnOpenFilter = view.findViewById(R.id.btnOpenFilter);
        btnOpenFilter.setOnClickListener(v -> {
            spinReportStatus.performClick();
        });

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("All");
        spinnerArray.add("New");
        spinnerArray.add("Open");
        spinnerArray.add("Processing");
        spinnerArray.add("Resolved");
        spinnerArray.add("Closed");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinReportStatus = view.findViewById(R.id.spinReportStatus);
        spinReportStatus.setAdapter(adapter);

        return view;
    }
}