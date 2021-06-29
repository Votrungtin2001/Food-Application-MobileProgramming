package com.example.foodapplication.auth;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodapplication.R;
import com.example.foodapplication.databinding.FragmentSignUpBinding;

import java.util.Objects;

import static com.example.foodapplication.mySQL.MySQLQuerry.CreateCustomerAccount;
import static com.example.foodapplication.mySQL.MySQLQuerry.CreateMasterAccount;


public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment";
    private FragmentSignUpBinding binding;
    private LoginFragment loginFragment;

    int role = 0;
    int namefragment = 0;

    public SignUpFragment() {
    }

    public SignUpFragment(int a, int b) {
        this.role = a;
        this.namefragment = b;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData(binding.displayName.getText().toString().trim(),
                        binding.email.getText().toString().trim(),
                        binding.password.getText().toString().trim(),
                        binding.password2.getText().toString().trim());
                CreateAccount();
            }
        });
        binding.loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFragment = new LoginFragment(role, namefragment);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, loginFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textChangeCheck();
    }

    private void textChangeCheck() {
        //region password
        TextWatcher passwordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO: check password==retype password
                String passwordString = Objects.requireNonNull(binding.password.getText()).toString().trim();
                String retypePasswordString = Objects.requireNonNull(binding.password2.getText()).toString().trim();
                if (!passwordString.equals(retypePasswordString) && !retypePasswordString.isEmpty() && !passwordString.isEmpty()) {
                    binding.password2.setError("Mật khẩu không trùng khớp");
                } else {
                    binding.password2.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        binding.password2.addTextChangedListener(passwordTextWatcher);
        binding.password.addTextChangedListener(passwordTextWatcher);
        //endregion

        //region email
        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String emailString = binding.email.getText().toString().trim();
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
                    binding.email.setError("Email không hợp lệ");
                } else {
                    binding.email.setError(null);
                }
            }
        });
    }

    private boolean validateData(String retypePasswordString, String displayNameString, String emailString, String passwordString) {
        if (TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passwordString) || TextUtils.isEmpty(displayNameString)
                || TextUtils.isEmpty(retypePasswordString)) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.email.getError() != null
                || binding.password.getError() != null || binding.password2.getError() != null)
            return false;
        return true;
    }

    private void CreateAccount() {
        String name = binding.displayName.getText().toString().trim();
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Xin vui lòng chờ trong giây lát...");
        progressDialog.show();
        if (role == 2) {
          CreateMasterAccount(name, email, password,
                  binding.displayName, binding.email, binding.password, binding.password2,
                  progressDialog,
                  TAG, getActivity());
        }
        else {
            CreateCustomerAccount(name, email, password,
                    binding.displayName, binding.email, binding.password, binding.password2,
                    progressDialog,
                    TAG, getActivity());
        }
    }

}