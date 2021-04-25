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

        btnAddress = view.findViewById(R.id.btnAddress);
        btnAddress.setOnClickListener(runAddressFragment);

        return view;
    }

    View.OnClickListener runAddressFragment = v -> {
        newFragment = new AccountAddressFragment();
        // source: https://stackoverflow.com/questions/21028786/how-do-i-open-a-new-fragment-from-another-fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace( ((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };
}