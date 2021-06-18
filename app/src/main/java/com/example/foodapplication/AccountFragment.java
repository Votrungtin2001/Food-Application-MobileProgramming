package com.example.foodapplication;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.foodapplication.auth.LoginFragment;
import com.example.foodapplication.auth.user;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.foodapplication.MainActivity.customer_id;
import static com.example.foodapplication.MainActivity.master_id;

public class AccountFragment extends Fragment {
    Button btnPayment, btnAddress, btnPolicy, btnSettings, btnAbout, btnLogout;
    Fragment newFragment;
    TextView txtlogin;
    ImageView imgUser;

    Dialog LoginDialog;

    int choose_role = 0;
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
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

        btnLogout = (Button) view.findViewById(R.id.btnLogout);

        databaseHelper = new DatabaseHelper(getContext());

        txtlogin = view.findViewById(R.id.txtName);
        if (customer_id > 0) {
            Cursor cursor = databaseHelper.getCustomerById(customer_id);
            if (cursor.moveToFirst()) {
                imgUser.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.VISIBLE);
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
        else {
            txtlogin.setText(getResources().getString(R.string.activity_account_login_name));
            imgUser.setImageResource(0);
            btnLogout.setVisibility(View.INVISIBLE);
        }
        txtlogin.setOnClickListener(v -> {
            if (customer_id > 0) {
                Fragment fragment = new AccountSettingsInfoFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), fragment, null)
                        .addToBackStack(null)
                        .commit();
            }
            else
                ShowPopUpLogin(v);
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.updAllAcountLogOutStatus();
                imgUser.setImageResource(0);
                txtlogin.setText("Đăng nhập/Đăng ký");
                btnLogout.setVisibility(View.INVISIBLE);
                customer_id = 0;
                master_id = 0;
            }
        });

        return view;
    }

    View.OnClickListener runAddressFragment = v -> {
        if (customer_id > 0) {
            newFragment = new AccountAddressFragment();
            // source: https://stackoverflow.com/questions/21028786/how-do-i-open-a-new-fragment-from-another-fragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), newFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
        else
            Toast.makeText(getContext(), "Bạn không thể dùng chức năng này vì bạn chưa đăng nhập.", Toast.LENGTH_LONG).show();
    };

    View.OnClickListener runPaymentFragment = v -> {
        if (customer_id > 0) {
            newFragment = new AccountPayment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), newFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
        else
            Toast.makeText(getContext(), "Bạn không thể dùng chức năng này vì bạn chưa đăng nhập.", Toast.LENGTH_LONG).show();
    };

    View.OnClickListener runSettingsFragment = v -> {
        if (customer_id > 0) {
            newFragment = new AccountSettings();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), newFragment, null)
                    .addToBackStack(null)
                    .commit();
        } else
            Toast.makeText(getContext(), "Bạn không thể dùng chức năng này vì bạn chưa đăng nhập.", Toast.LENGTH_LONG).show();
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