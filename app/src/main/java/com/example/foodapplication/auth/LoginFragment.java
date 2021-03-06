package com.example.foodapplication.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.foodapplication.mySQL.DatabaseHelper;
import com.example.foodapplication.R;
import com.example.foodapplication.account.fragment.AccountFragment;
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

import static com.example.foodapplication.MainActivity.addressLine;
import static com.example.foodapplication.MainActivity.customer_id;
import static com.example.foodapplication.MainActivity.district_id;
import static com.example.foodapplication.MainActivity.nameStreet;


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
    private FragmentLoginBinding binding;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    int role = 0;
    int namefragment_before = 0;

    public LoginFragment() { }

    public LoginFragment(int name, int choose_role) {
        this.namefragment_before = name;
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

        mFirebaseAuth = FirebaseAuth.getInstance();

        databaseHelper = new DatabaseHelper(getActivity());

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
                progressDialog.setMessage("Xin vui l??ng ch??? trong gi??y l??t...");
                progressDialog.show();

                if (namefragment_before == 1 && role == 1) {
                    NormalLoginForCustomerAndBackToCustomerAccountFragment(binding.email.getText().toString().trim(), binding.password.getText().toString().trim(),
                            progressDialog, binding.password);

                }
                else if(namefragment_before == 2 && role == 1) {
                    NormalLoginForCustomerAndOpenMainActivity(binding.email.getText().toString().trim(), binding.password.getText().toString().trim(),
                            progressDialog, binding.password);
                }
            }
        });

        binding.signup.setOnClickListener(runSignUpFragment);

        return binding.getRoot();
    }

    View.OnClickListener runSignUpFragment = v -> {
        SignUpFragment newFragment = new SignUpFragment(role, namefragment_before);
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
                    binding.email.setError("Email kh??ng h???p l???");
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
                                Toast.makeText(getActivity(), "Email ch??a ???????c ????ng k??. Vui l??ng ????ng k??!", Toast.LENGTH_LONG).show();
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
                                                progressDialog.setMessage("Xin vui l??ng ch??? trong gi??y l??t...");
                                                progressDialog.show();
                                                GoogleLoginForCustomerAndBackToAccountFragment(User.getEmail().toString().trim(), progressDialog);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "????ng nh???p kh??ng th??nh c??ng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

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
                                            progressDialog.setMessage("Xin vui l??ng ch??? trong gi??y l??t...");
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

    private void NormalLoginForCustomerAndBackToCustomerAccountFragment(String username, String password,
                                                                ProgressDialog progressDialog, EditText editText) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getNormalLoginForCustomer.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Failed to log in")) {
                    editText.setText(null);
                    announcement = "????ng nh???p th???t b???i! T??i kho???n ho???c m???t kh???u kh??ng ch??nh x??c!";
                    Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int cus_id = Integer.parseInt(object.getString("_ID"));
                            boolean checkCustomerIDIsExist = CheckCustomerIDIsExist(cus_id);
                            if(checkCustomerIDIsExist) {
                                int id = getIDInCustomerTable(cus_id);
                                databaseHelper.updAllAcountLogOutStatus();
                                databaseHelper.updCustomerLoginStatus(id);
                                customer_id = getCustomerIDLogin();
                            }
                            else {
                                databaseHelper.addCustomer(cus_id);
                                int id = getIDInCustomerTable(cus_id);
                                databaseHelper.updAllAcountLogOutStatus();
                                databaseHelper.updCustomerLoginStatus(id);
                                customer_id = getCustomerIDLogin();
                            }

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
                if(response.toString().trim().equals("Failed to log in")) {
                    editText.setText(null);
                    announcement = "????ng nh???p th???t b???i! T??i kho???n ho???c m???t kh???u kh??ng ch??nh x??c!";
                    Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int cus_id = Integer.parseInt(object.getString("_ID"));
                            boolean checkCustomerIDIsExist = CheckCustomerIDIsExist(cus_id);
                            if(checkCustomerIDIsExist) {
                                int id = getIDInCustomerTable(cus_id);
                                databaseHelper.updAllAcountLogOutStatus();
                                databaseHelper.updCustomerLoginStatus(id);
                                customer_id = getCustomerIDLogin();
                            }
                            else {
                                databaseHelper.addCustomer(cus_id);
                                int id = getIDInCustomerTable(cus_id);
                                databaseHelper.updAllAcountLogOutStatus();
                                databaseHelper.updCustomerLoginStatus(id);
                                customer_id = getCustomerIDLogin();
                            }

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("Customer ID", customer_id);
                            intent.putExtra("AddressLine", addressLine);
                            intent.putExtra("NameStreet", nameStreet);
                            intent.putExtra("District ID", district_id);
                            startActivity(intent);
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

    private void GoogleLoginForCustomerAndBackToAccountFragment(String email, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getGoogleLoginForCustomer.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Failed to log in")) {
                    announcement = "Email Google ch??a ???????c ????ng k??. Vui l??ng ????ng k??!";
                    Toast.makeText(getActivity(), announcement, Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int cus_id = Integer.parseInt(object.getString("_ID"));
                            boolean checkCustomerIDIsExist = CheckCustomerIDIsExist(cus_id);
                            if(checkCustomerIDIsExist) {
                                int id = getIDInCustomerTable(cus_id);
                                databaseHelper.updAllAcountLogOutStatus();
                                databaseHelper.updCustomerLoginStatus(id);
                                customer_id = getCustomerIDLogin();
                            }
                            else {
                                databaseHelper.addCustomer(cus_id);
                                int id = getIDInCustomerTable(cus_id);
                                databaseHelper.updAllAcountLogOutStatus();
                                databaseHelper.updCustomerLoginStatus(id);
                                customer_id = getCustomerIDLogin();
                            }
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
                    announcement = "Email Facebook ch??a ???????c ????ng k??. Vui l??ng ????ng k??!";
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

    public boolean CheckCustomerIDIsExist(int cus_id) {
        db = databaseHelper.getReadableDatabase();
        int count = 0;
        String selectQuery =  "SELECT * FROM CUSTOMER WHERE Customer_ID = '" + customer_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

            } while (cursor.moveToNext());

        }
        count = cursor.getCount();
        cursor.close();
        if(count > 0) return true;
        else return false;
    }

    public int getIDInCustomerTable(int cus_id) {
        db = databaseHelper.getReadableDatabase();
        int id = 0;
        String selectQuery =  "SELECT * FROM CUSTOMER WHERE Customer_ID = '" + cus_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex("_id"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return  id;
    }

    public int getCustomerIDLogin() {
        db = databaseHelper.getReadableDatabase();
        int id = 0;
        String selectQuery =  "SELECT * FROM CUSTOMER WHERE STATUS = '" + 1 + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex("Customer_ID"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return  id;
    }
}