package com.example.foodapplication.account;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodapplication.R;

public class AccountPolicyFragment extends Fragment implements View.OnClickListener {
    TextView txtPayment, txtPrivacy, txtRegulation, txtToS, txtDispute;

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
        txtPayment.setOnClickListener(this);

        txtPrivacy = view.findViewById(R.id.txtPrivacyPolicy);
        txtPrivacy.setOnClickListener(this);

        txtRegulation = view.findViewById(R.id.txtRegulation);
        txtRegulation.setOnClickListener(this);

        txtToS = view.findViewById(R.id.txtToS);
        txtToS.setOnClickListener(this);

        txtDispute= view.findViewById(R.id.txtDisputePolicy);
        txtDispute.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        switch (v.getId()) {
            case R.id.txtPaymentGuide:
                args.putString("HTML_PATH", "file:///android_asset/html/payment.html");
                break;
            case R.id.txtPrivacyPolicy:
                args.putString("HTML_PATH", "file:///android_asset/html/privacy.html");
                break;
            case R.id.txtRegulation:
                args.putString("HTML_PATH", "file:///android_asset/html/regulation.html");
                break;
            case R.id.txtToS:
                args.putString("HTML_PATH", "file:///android_asset/html/tos.html");
                break;
            case R.id.txtDisputePolicy:
                args.putString("HTML_PATH", "file:///android_asset/html/claims.html");
                break;
        }
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup) getView().getParent()).getId(), fragment, null)
                .addToBackStack(null)
                .commit();
    }
}