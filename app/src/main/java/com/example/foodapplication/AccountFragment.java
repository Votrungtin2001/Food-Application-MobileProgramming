package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AccountFragment extends Fragment {
    Button btnVoucher, btnPayment, btnAddress, btnInvite, btnSupport, btnShop, btnPolicy, btnSettings, btnAbout, btnLogout;
    Fragment newFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        btnVoucher = view.findViewById(R.id.btnVoucher);
        btnVoucher.setOnClickListener(runVoucherFragment);

        btnPayment = view.findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(runPaymentFragment);

        btnAddress = view.findViewById(R.id.btnAddress);
        btnAddress.setOnClickListener(runAddressFragment);

        btnSettings = view.findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(runSettingsFragment);

        btnInvite = view.findViewById(R.id.btnInvite);
        btnInvite.setOnClickListener(runInviteFragment);

        btnPolicy = view.findViewById(R.id.btnPolicy);
        btnPolicy.setOnClickListener(runPolicyFragment);

        return view;
    }

    View.OnClickListener runAddressFragment = v -> {
        newFragment = new AccountAddressFragment();
        // source: https://stackoverflow.com/questions/21028786/how-do-i-open-a-new-fragment-from-another-fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runVoucherFragment = v -> {
        newFragment = new AccountVoucherFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runPaymentFragment = v -> {
        newFragment = new AccountPayment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runInviteFragment = v -> {
        newFragment = new AccountInviteFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runSettingsFragment = v -> {
        newFragment = new AccountSettings();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runPolicyFragment = v -> {
        newFragment = new AccountPolicyFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };
}