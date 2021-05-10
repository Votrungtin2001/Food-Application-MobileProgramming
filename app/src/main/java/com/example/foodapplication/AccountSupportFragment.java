package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AccountSupportFragment extends Fragment {
    Button btnHistory;
    TextView txtSupportReport;

    public AccountSupportFragment() { }

    public static AccountSupportFragment newInstance() {
        AccountSupportFragment fragment = new AccountSupportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_support, container, false);

        btnHistory = view.findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(openHistoryFragment);

        txtSupportReport = view.findViewById(R.id.txtSupportReport);
        txtSupportReport.setOnClickListener(openReportFragment);

        return view;
    }

    View.OnClickListener openReportFragment = v -> {
        Fragment fragment = new AccountSupportReportFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener openHistoryFragment = v -> {
        Fragment fragment = new AccountSupportHistory();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    };
}