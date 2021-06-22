package com.example.foodapplication.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodapplication.account.AccountFragment;
import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.Master_MainActivity;
import com.example.foodapplication.databinding.FragmentLoginMasterBinding;

import fragments.AccountFragment_Master;

import static com.example.foodapplication.MainActivity.master_id;


public class LoginFragmentMaster extends Fragment {

    @NonNull FragmentLoginMasterBinding binding;
    DatabaseHelper databaseHelper;
    MainActivity mainActivity = new MainActivity();
    AccountFragment accountFragment = new AccountFragment();
    AccountFragment_Master accountFragment_master = new AccountFragment_Master();

    int role = 0;
    int getRole = 0;

    public LoginFragmentMaster() { }

    public LoginFragmentMaster(int choose_role) {
        this.role = choose_role;
    }
    public void LoginFragment(int choose_role) {
        this.getRole = choose_role;
    }
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        databaseHelper = new DatabaseHelper(getActivity());

        binding = FragmentLoginMasterBinding.inflate(inflater, container, false);

        binding.signinMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (role == 2) {
                    boolean isExist = databaseHelper.checkMaster(binding.emailMaster.getText().toString().trim(),binding.passwordMaster.getText().toString().trim());

                    if(isExist){
                        master_id = databaseHelper.getIdMasterByUsername(binding.emailMaster.getText().toString().trim());
                        databaseHelper.updAllAcountLogOutStatus();
                        databaseHelper.updMasterLoginStatus(master_id);

                        Intent intent = new Intent(getActivity(), Master_MainActivity.class);
                        intent.putExtra("Master ID",master_id);
                        startActivity(intent);
                    } else {
                        binding.passwordMaster.setText(null);
                        Toast.makeText(getActivity(), "Đăng nhập không thành công. Vui lòng điền email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                }


//                else {
//                    boolean isExist = databaseHelper.checkUser(binding.emailMaster.getText().toString().trim(),binding.passwordMaster.getText().toString().trim());
//
//                    if(isExist){
//                        customer_id = databaseHelper.getIdByUsername(binding.emailMaster.getText().toString().trim());
//                        databaseHelper.updAllAcountLogOutStatus();
//                        databaseHelper.updCustomerLoginStatus(customer_id);
//                        Toast.makeText(getActivity(), "Login id: " + customer_id, Toast.LENGTH_SHORT).show();
//
////                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
////                        transaction.replace(R.id.frame_container, accountFragment);
////                        transaction.addToBackStack(null);
////                        transaction.commit();
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        intent.putExtra("_id",customer_id);
//                        startActivity(intent);
//                    } else {
//                        binding.passwordMaster.setText(null);
//                        Toast.makeText(getActivity(), "Đăng nhập không thành công. Vui lòng điền email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });

        binding.signup.setOnClickListener(runSignUpFragment);

        return binding.getRoot();
    }

    View.OnClickListener runSignUpFragment = v -> {
        SignUpFragment newFragment = new SignUpFragment(role);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };
}