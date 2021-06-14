package com.example.foodapplication;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
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
    Button btnPayment, btnAddress, btnPolicy, btnSettings, btnAbout, btnLogout;
    Fragment newFragment;
    TextView txtlogin;
    ImageView imgUser;

    Dialog LoginDialog;

    int choose_role = 0;
user user = new user();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        imgUser = view.findViewById(R.id.imgUser);

        LoginDialog = new Dialog(getActivity());
        LoginDialog.setContentView(R.layout.custom_pop_up_login);

        btnPayment = view.findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(runPaymentFragment);

        btnAddress = view.findViewById(R.id.btnAddress);
        btnAddress.setOnClickListener(runAddressFragment);

        btnSettings = view.findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(runSettingsFragment);

        btnAbout = view.findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(runAboutFragment);

        btnPolicy = view.findViewById(R.id.btnPolicy);
        btnPolicy.setOnClickListener(runPolicyFragment);

        txtlogin = view.findViewById(R.id.txtName);
        if (MainActivity.customer_id > 0) {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            Cursor cursor = dbHelper.getCustomerById(MainActivity.customer_id);
            if (cursor.moveToFirst()) {
                txtlogin.setText(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_NAME)));
                switch (cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_GENDER))) {
                    case 0:
                        imgUser.setImageResource(R.drawable.avatar_male);
                        break;
                    case 1:
                        imgUser.setImageResource(R.drawable.avatar_female);
                        break;
                    default:
                        imgUser.setImageResource(R.drawable.avatar_none);
                        break;
                }
            }
            cursor.close();
        }
        else
            txtlogin.setText("Đăng nhập/Đăng kí");
        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUpLogin(v);

            }
        });

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> FirebaseAuth.getInstance().signOut());

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

    View.OnClickListener runPaymentFragment = v -> {
        newFragment = new AccountPayment();
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