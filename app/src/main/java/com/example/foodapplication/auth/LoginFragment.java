package com.example.foodapplication.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;
import com.example.foodapplication.account.AccountFragment;
import com.example.foodapplication.MySQL.DatabaseHelper;
import com.example.foodapplication.databinding.FragmentLoginBinding;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.foodapplication.MainActivity.customer_id;


// DataPasser reference: https://stackoverflow.com/questions/9343241/passing-data-between-a-fragment-and-its-container-activity

public class LoginFragment extends Fragment  {

    private final String TAG = "LoginFragment";
    AccountFragment accountFragment = new AccountFragment();
    private static final int RC_SIGN_IN = 9001 ;
    private AccessTokenTracker accessTokenTracker;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private GoogleSignInClient mGoogleSignInClient;

    private GoogleApiClient mGoogleApiClient; // for google sign in
    private CallbackManager mFacebookCallbackManager; // for facebook log in
    LoginButton mFacebookLoginButton;
    private DatabaseHelper databaseHelper;
    private user user;
    private FragmentLoginBinding binding;

    String userId;
    MainActivity mainActivity = new MainActivity();

    int role = 0;
    int namefragment_before = 0;

    public LoginFragment() { }

    public LoginFragment(int name, int choose_role) {
        this.namefragment_before = name;
        this.role = choose_role;
    }

    public LoginFragment(int choose_role) {
        this.role = choose_role;
    }


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textChangeCheck();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        databaseHelper = new DatabaseHelper(getActivity());

        mFirebaseAuth = FirebaseAuth.getInstance();

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.facebookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.facebookView){
                    binding.signinFb.performClick();
                }
            }
        });

        createRequest();
        binding.signinGg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        binding.signinUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                if (namefragment_before == 1 && role == 1) {
                    NormalLoginForCustomerAndBackToAccountFragment(binding.email.getText().toString().trim(), binding.password.getText().toString().trim(),
                            progressDialog, binding.password);

                }
                /*else if(namefragment_before == 2 && role == 1) {
                    NormalLoginForCustomerAndBackToAccountFragment(binding.email.getText().toString().trim(), binding.password.getText().toString().trim(),
                            progressDialog, binding.password);
                }*/
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

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        facebookLogin();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    private void emptyInputEditText() {
        binding.email.setText(null);
        binding.password.setText(null);
    }
    private void textChangeCheck() {

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
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException exception){
                Log.w(TAG,"Fail",exception);
            }
        }
         mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    // [START auth_with_google]
    public void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void signInWithGoogle() {
        Intent signIn = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIn, RC_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser User = mFirebaseAuth.getCurrentUser();
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNew){
                                Toast.makeText(getActivity(), "Email chưa được đăng ký. Vui lòng đăng ký!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                if (GoogleSignIn.getLastSignedInAccount(getActivity()) != null) {
                                    mGoogleSignInClient.silentSignIn().addOnCompleteListener(getActivity(), new OnCompleteListener<GoogleSignInAccount>() {
                                        @Override
                                        public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                            if (task.isSuccessful()) {
                                                // The signed in account is stored in the task's result.
                                                GoogleSignInAccount signedInAccount = task.getResult();
                                                //   user.setAccessToken(signedInAccount.getIdToken());
                                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                                progressDialog.setMessage("Please wait...");
                                                progressDialog.show();
                                                GoogleLoginForCustomerAndBackToAccountFragment(User.getEmail().toString().trim(), progressDialog);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "Đăng nhập không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // [END auth_with_google]

    // [START auth_with_facebook]

    private void facebookLogin() {
        binding.signinFb.setReadPermissions(Arrays.asList("email"));
        binding.signinFb.setFragment(this);
        mFacebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());
                                        try {
                                            String email = object.getString("email");
                                            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                            progressDialog.setMessage("Please wait...");
                                            progressDialog.show();
                                            FacebookLoginForCustomerAndBackToAccountFragment(email, progressDialog);
                                        } catch ( JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG,"Canceled !");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG,"ERROR : " + exception.getMessage());
                    }
                });

         accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null && getActivity() != null) {
                    getActivity().recreate();
                }
            }
        };

    }

    private void NormalLoginForCustomerAndBackToAccountFragment(String username, String password,
                                                                ProgressDialog progressDialog, EditText editText) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getNormalLoginForCustomer.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Failed to log in")) {
                    editText.setText(null);
                    announcement = "Đăng nhập thất bại! Tài khoản hoặc mật khẩu không chính xác!";
                    Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            customer_id = Integer.parseInt(object.getString("_ID"));
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_container, accountFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", String.valueOf(username));
                params.put("password", String.valueOf(password));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void NormalLoginForCustomerAndOpenMainActivity(String username, String password,
                                                                ProgressDialog progressDialog, EditText editText) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getNormalLoginForCustomer.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Logged in successfully")) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("Customer ID", customer_id);
                    startActivity(intent);
                }
                else {
                    editText.setText(null);
                    announcement = "Đăng nhập thất bại! Tài khoản hoặc mật khẩu không chính xác!";
                    Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", String.valueOf(username));
                params.put("password", String.valueOf(password));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void GoogleLoginForCustomerAndBackToAccountFragment(String email, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getGoogleLoginForCustomer.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Failed to log in")) {
                    announcement = "Email Google chưa được đăng ký. Vui lòng đăng ký!";
                    Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            customer_id = Integer.parseInt(object.getString("_ID"));
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_container, accountFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", String.valueOf(email));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void FacebookLoginForCustomerAndBackToAccountFragment(String email, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getFacebookLoginForCustomer.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Failed to log in")) {
                    announcement = "Email Facebook chưa được đăng ký. Vui lòng đăng ký!";
                    Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            customer_id = Integer.parseInt(object.getString("_ID"));
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_container, accountFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", String.valueOf(email));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}