package com.example.foodapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

public class AccountAddressFragment extends Fragment {
    EditText txtAccountAddressInput, txtAccountAddressGate, txtAccountAddressFloor, txtOtherLabelName, txtAccountAddressContactName, txtAccountAddressContactNumber, txtDeliveryNote;
    RadioGroup rbGroup;



    public AccountAddressFragment() {
        // Required empty public constructor
    }
    public static AccountAddressFragment newInstance() {
        AccountAddressFragment fragment = new AccountAddressFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_address, container, false);

        txtAccountAddressInput = view.findViewById(R.id.txtAccountAddressInput);
        txtAccountAddressGate = view.findViewById(R.id.txtAccountAddressGate);
        txtAccountAddressFloor = view.findViewById(R.id.txtAccountAddressFloor);
        txtOtherLabelName = view.findViewById(R.id.txtOtherLabelName);
        txtAccountAddressContactName = view.findViewById(R.id.txtAccountAddressContactName);
        txtAccountAddressContactNumber = view.findViewById(R.id.txtAccountAddressContactNumber);
        txtDeliveryNote = view.findViewById(R.id.txtDeliveryNote);
        rbGroup = view.findViewById(R.id.rbGroup);
        rbGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        return view;
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = (group, checkedId) -> {
        switch(rbGroup.getCheckedRadioButtonId()) {
            case R.id.rbHome:
            case R.id.rbWork:
                txtOtherLabelName.setVisibility(View.INVISIBLE);
                break;
            case R.id.rbOther:
                txtOtherLabelName.setVisibility(View.VISIBLE);
                break;
        }
    };

}