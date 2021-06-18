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

public class AccountPolicyFragment extends Fragment {
    TextView txtPayment, txtPaymentAnswer, txtPrivacy, txtPrivacyAnswer, txtRegulation, txtRegulationAnswer, txtToS, txtToSAnswer, txtDispute, txtDisputeAnswer;

    public AccountPolicyFragment() { }

    public static AccountPolicyFragment newInstance() {
        return new AccountPolicyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_policy, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_policy));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        txtPayment = view.findViewById(R.id.txtPaymentGuide);
        txtPayment.setOnClickListener(onPaymentClick);
        txtPaymentAnswer = view.findViewById(R.id.txtPaymentGuideAnswer);

        txtPrivacy = view.findViewById(R.id.txtPrivacyPolicy);
        txtPrivacy.setOnClickListener(onPrivacyClick);
        txtPrivacyAnswer = view.findViewById(R.id.txtPrivacyPolicyAnswer);

        txtRegulation = view.findViewById(R.id.txtRegulation);
        txtRegulation.setOnClickListener(onRegulationClick);
        txtRegulationAnswer= view.findViewById(R.id.txtRegulationAnswer);

        txtToS = view.findViewById(R.id.txtToS);
        txtToS.setOnClickListener(onToSClick);
        txtToSAnswer = view.findViewById(R.id.txtToSAnswer);

        txtDispute= view.findViewById(R.id.txtDisputePolicy);
        txtDispute.setOnClickListener(onDisputeClick);
        txtDisputeAnswer = view.findViewById(R.id.txtDisputePolicyAnswer);

        return view;
    }

    View.OnClickListener onPaymentClick = v -> {
        if (txtPaymentAnswer.getVisibility() == View.GONE)
            txtPaymentAnswer.setVisibility(View.VISIBLE);
        else
            txtPaymentAnswer.setVisibility(View.GONE);
    };

    View.OnClickListener onPrivacyClick = v -> {
        if (txtPrivacyAnswer.getVisibility() == View.GONE)
            txtPrivacyAnswer.setVisibility(View.VISIBLE);
        else
            txtPrivacyAnswer.setVisibility(View.GONE);
    };

    View.OnClickListener onRegulationClick = v -> {
        if (txtRegulationAnswer.getVisibility() == View.GONE)
            txtRegulationAnswer.setVisibility(View.VISIBLE);
        else
            txtRegulationAnswer.setVisibility(View.GONE);
    };

    View.OnClickListener onToSClick = v -> {
        if (txtToSAnswer.getVisibility() == View.GONE)
            txtToSAnswer.setVisibility(View.VISIBLE);
        else
            txtToSAnswer.setVisibility(View.GONE);
    };

    View.OnClickListener onDisputeClick = v -> {
        if (txtDisputeAnswer.getVisibility() == View.GONE)
            txtDisputeAnswer.setVisibility(View.VISIBLE);
        else
            txtDisputeAnswer.setVisibility(View.GONE);
    };
}