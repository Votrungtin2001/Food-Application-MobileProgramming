package com.example.foodapplication.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodapplication.AccountFragment;
import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.Master_MainActivity;
import com.example.foodapplication.R;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

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
    StorageReference storageReference;
    private GoogleApiClient mGoogleApiClient; // for google sign in
    private CallbackManager mFacebookCallbackManager; // for facebook log in
    LoginButton mFacebookLoginButton;
    private DatabaseHelper databaseHelper;
    private user user;
    private FragmentLoginBinding binding;
    FirebaseFirestore fStore;
    String userId;
    MainActivity mainActivity = new MainActivity();

    int role = 0;

    public LoginFragment() { }

    public LoginFragment(int choose_role) {
        this.role = choose_role;
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        databaseHelper = new DatabaseHelper(getActivity());
        fStore = FirebaseFirestore.getInstance();
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

        initObjects();

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
                if (role == 2) {
                    boolean isExist = databaseHelper.checkMaster(binding.username.getText().toString().trim(),binding.password.getText().toString().trim());

                    if(isExist){
                        int master_id = databaseHelper.getIdMasterByUsername(binding.username.getText().toString().trim());
                        databaseHelper.updAllAcountLogOutStatus();
                        databaseHelper.updMasterLoginStatus(master_id);
                        Intent intent = new Intent(getActivity(), Master_MainActivity.class);
                        intent.putExtra("Master ID",master_id);
                        startActivity(intent);
                    } else {
                        binding.password.setText(null);
                        Toast.makeText(getActivity(), "Đăng nhập không thành công. Vui lòng điền email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    boolean isExist = databaseHelper.checkUser(binding.username.getText().toString().trim(),binding.password.getText().toString().trim());

                    if(isExist){
                        customer_id = databaseHelper.getIdByUsername(binding.username.getText().toString().trim());
                        databaseHelper.updAllAcountLogOutStatus();
                        databaseHelper.updCustomerLoginStatus(customer_id);
                        Toast.makeText(getActivity(), "Login id: " + customer_id, Toast.LENGTH_SHORT).show();

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, accountFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        binding.password.setText(null);
                        Toast.makeText(getActivity(), "Đăng nhập không thành công. Vui lòng điền email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                }
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
    public void onPause() {
        super.onPause();
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.stopAutoManage(getActivity());
//            mGoogleApiClient.disconnect();
//        }
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

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }


    private void initObjects() {
        databaseHelper = new DatabaseHelper(getActivity());
    }

    private void emptyInputEditText() {
        binding.username.setText(null);
        binding.password.setText(null);
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

                            // check login first time
//                        if (isNew){

                            if (GoogleSignIn.getLastSignedInAccount(getActivity()) != null) {
                                mGoogleSignInClient.silentSignIn().addOnCompleteListener(getActivity(), new OnCompleteListener<GoogleSignInAccount>() {
                                    @Override
                                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                        if (task.isSuccessful()) {
                                            // The signed in account is stored in the task's result.
                                            GoogleSignInAccount signedInAccount = task.getResult();
                                            //   user.setAccessToken(signedInAccount.getIdToken());
                                             String email =  signedInAccount.getEmail();
                                            if(databaseHelper.checkUser(User.getEmail()))
                                            {
                                                customer_id = databaseHelper.getIdByUsername(User.getEmail());
                                                databaseHelper.updAllAcountLogOutStatus();
                                                databaseHelper.updCustomerLoginStatus(customer_id);
                                                Toast.makeText(getActivity(), "Login id: " + customer_id, Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(getActivity(), "Email chưa được đăng ký. Vui lòng đăng ký!", Toast.LENGTH_LONG).show();
                                            }
}
                                        else {
                                            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                                            startActivityForResult(signInIntent, RC_SIGN_IN);
                                        }
                                    }
                                });
                            }
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_container, accountFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
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
                                            if(databaseHelper.checkUser(email)){
                                                databaseHelper.updUserFacebook(customer_id, email);
                                                databaseHelper.updAllAcountLogOutStatus();
                                                databaseHelper.updCustomerLoginStatus(customer_id);
                                                customer_id = databaseHelper.getIdByUsername(email);
                                                Toast.makeText(getActivity(), "Login id: " + customer_id, Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(getActivity(), "Vui lòng đăng kí tài khoản mới!", Toast.LENGTH_LONG).show();
                                            }

                                        } catch ( JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });



                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "email");
                        request.setParameters(parameters);
                        request.executeAsync();

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, accountFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
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


    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");

            bundle.putString("idFacebook", id);
            if (object.has("name"))
                bundle.putString("name", object.getString("name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));

        } catch (Exception e) {
            Log.d(TAG, "BUNDLE Exception : "+e.toString());
        }

        return bundle;
    }





    private  void loadUserGG(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            if(databaseHelper.checkUser(personEmail))
            {
                customer_id = databaseHelper.getIdByUsername(personEmail);
                //databaseHelper.addCustomer(user);
                 binding.username.setText(personEmail);
                 Toast.makeText(getActivity(), "Login id: " + customer_id, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), "Email chưa được đăng ký. Vui lòng đăng ký!", Toast.LENGTH_SHORT).show();
            }
        }
    }



}