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
import androidx.fragment.app.FragmentTransaction;

import com.example.foodapplication.R;
import com.example.foodapplication.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class SignUpFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;
    private static final String TAG = SignUpFragment.class.getName();
    private FragmentSignUpBinding binding;

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
                createUserWithEmailAndPassword();
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

    private void createUserWithEmailAndPassword() {
        String email = "oemilk@naver.com"; // email address format
        String password = "123456"; // at least 6 characters
        LoginFragment loginFragment = new LoginFragment();
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, loginFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
}