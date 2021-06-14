package com.example.foodapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.foodapplication.auth.LoginFragment;
import com.example.foodapplication.auth.user;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {
    Button btnPayment, btnAddress, btnInvite, btnSupport, btnShop, btnPolicy, btnSettings, btnAbout, btnLogout;
    Fragment newFragment;
    Button txtlogin;

    int user_id = -1;

    Bundle importArgs;

    Dialog LoginDialog;

    int choose_role = 0;
user user = new user();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getArguments() != null) && (getArguments().containsKey("CUSTOMER_ID")))
            user_id = getArguments().getInt("CUSTOMER_ID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        LoginDialog = new Dialog(getActivity());
        LoginDialog.setContentView(R.layout.custom_pop_up_login);

        btnPayment = view.findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(runPaymentFragment);

        btnAddress = view.findViewById(R.id.btnAddress);
        btnAddress.setOnClickListener(runAddressFragment);

        btnSettings = view.findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(runSettingsFragment);

        btnSupport = view.findViewById(R.id.btnSupport);
        btnSupport.setOnClickListener(runSupportFragment);

        btnAbout = view.findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(runAboutFragment);

        btnInvite = view.findViewById(R.id.btnInvite);
        btnInvite.setOnClickListener(runInviteFragment);

        btnPolicy = view.findViewById(R.id.btnPolicy);
        btnPolicy.setOnClickListener(runPolicyFragment);

        txtlogin = view.findViewById(R.id.txtName);
        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUpLogin(v);

            }
        });

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> FirebaseAuth.getInstance().signOut());

        importArgs = new Bundle();
        importArgs.putInt("CUSTOMER_ID", user_id);

        return view;
    }

    View.OnClickListener runAddressFragment = v -> {
        newFragment = new AccountAddressFragment();
        newFragment.setArguments(importArgs);
        // source: https://stackoverflow.com/questions/21028786/how-do-i-open-a-new-fragment-from-another-fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runSupportFragment = v -> {
        newFragment = new AccountSupportFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runPaymentFragment = v -> {
        newFragment = new AccountPayment();
        newFragment.setArguments(importArgs);
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
        newFragment.setArguments(importArgs);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    View.OnClickListener runAboutFragment = v -> {
        newFragment = new AccountAbout();
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

    View.OnClickListener runLoginFragment = v -> {
        newFragment = new LoginFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();

        LoginDialog.dismiss();
    };

    public void ShowPopUpLogin(View v) {
        TextView textView_Close;
        textView_Close = (TextView) LoginDialog.findViewById(R.id.Close_PopUpLogin);
        textView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog.dismiss();
            }
        });

        ImageView imageView_CustomerOption;
        imageView_CustomerOption = (ImageView) LoginDialog.findViewById(R.id.ImageView_Customer_PopUpLogin);
        imageView_CustomerOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_role = 1;
                newFragment = new LoginFragment(choose_role);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                        .addToBackStack(null)
                        .commit();
                LoginDialog.dismiss();

            }
        });

        ImageView imageView_MasterOption;
        imageView_MasterOption = (ImageView) LoginDialog.findViewById(R.id.ImageView_Master_PopUpLogin);
        imageView_MasterOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_role = 2;
                newFragment = new LoginFragment(choose_role);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                        .addToBackStack(null)
                        .commit();
                LoginDialog.dismiss();
            }
        });

        LoginDialog.show();

    }


}