package com.example.foodapplication.auth;

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

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.databinding.FragmentSignUpBinding;

import java.util.Objects;


public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment";
        private FragmentSignUpBinding binding;
    user user = new user();
    private DatabaseHelper databaseHelper ;
    private LoginFragment loginFragment;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataToSQLite();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textChangeCheck();
        initObjects();
    }
    private void initObjects() {
        // inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(getActivity());
        user = new user();
    }
    private void textChangeCheck() {
        //region password
        //kiểm tra 2 password có giống nhau không
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
                    // or
                    binding.email.setError("Email không hợp lệ");
                } else {
                    binding.email.setError(null);

                }
            }
        });
        //endregion
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

    private void postDataToSQLite() {
       if (!databaseHelper.checkUser(binding.email.getText().toString().trim())) {
            user.setName(binding.displayName.getText().toString().trim());
            user.setEmail(binding.email.getText().toString().trim());
            user.setPassword(binding.password.getText().toString().trim());
            user.setUsername(binding.email.getText().toString().trim());

            databaseHelper.addCustomer(user);
            // Snack Bar to show success message that record saved successfully
//           FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//           transaction.replace(R.id.frame_container, loginFragment);
//           transaction.addToBackStack(null);
//           transaction.commit();
            Toast.makeText(getActivity(),"Đăng kí thành công", Toast.LENGTH_LONG).show();
            emptyInputEditText();
        } else {
           Toast.makeText(getActivity(),"Lỗi đăng kí, vui lòng kiểm tra email ", Toast.LENGTH_LONG).show();
       }
    }
    private void emptyInputEditText() {
        binding.displayName.setText(null);
        binding.email.setText(null);
        binding.password.setText(null);
        binding.password2.setText(null);
    }
}