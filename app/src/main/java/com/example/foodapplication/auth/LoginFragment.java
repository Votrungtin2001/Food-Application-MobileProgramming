package com.example.foodapplication.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodapplication.AccountFragment;
import com.example.foodapplication.R;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.util.Arrays;


public class LoginFragment extends Fragment  {
    private final String TAG = "LoginFragment";
    AccountFragment accountFragment = new AccountFragment();
    private static final int RC_SIGN_IN = 9001 ;
   // public User user = new User();
    private AccessTokenTracker accessTokenTracker;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private GoogleSignInClient mGoogleSignInClient;
    private final int FACEBOOK_LOG_IN_REQUEST_CODE = 64206;
    private GoogleApiClient mGoogleApiClient; // for google sign in
    private CallbackManager mFacebookCallbackManager; // for facebook log in
    LoginButton mFacebookLoginButton;

    public LoginFragment() { }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleSignIn();
        initFBAuthentication();
        initFBAuthState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button googlesignInButton = view.findViewById(R.id.signin_gg);
        googlesignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogleSignIn();
            }
        });
        mFacebookLoginButton = (LoginButton) view.findViewById(R.id.signin_fb);
        Button signinUsername = (Button) view.findViewById(R.id.signin_username);
        signinUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithEmailAndPassword();
            }
        });

        TextView signUp = view.findViewById(R.id.signup);
        signUp.setOnClickListener(runSignUpFragment);

        return view;
    }

    View.OnClickListener runSignUpFragment = v -> {
        SignUpFragment newFragment = new SignUpFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    @Override
    public void onStart() {
        super.onStart();
        //  mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        facebookLogin();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
        else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    // [START auth_with_email_and_password]
    private void initFBAuthentication() {
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    private void initFBAuthState() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String message;
                if (firebaseUser != null) {
                    message = "onAuthStateChanged signed in : " + firebaseUser.getUid();
                } else {
                    message = "onAuthStateChanged signed out";
                }
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                // mAuthStateTextview.setText(message);
            }
        };
    }

    private void createUserWithEmailAndPassword() {
        String email = "oemilk@naver.com"; // email address format
        String password = "123456"; // at least 6 characters
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void signInWithEmailAndPassword() {
        String email = "test@test.com";
        String password = "123456";
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void signOut() {
        if (mFirebaseAuth != null) {
            mFirebaseAuth.signOut();
        }
    }
    // [END auth_with_email_and_password]
    // [START auth_with_google]
    public void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Check if signed in in Facebook
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isFacebookLoggedIn = accessToken != null && !accessToken.isExpired();

        if (!isFacebookLoggedIn && GoogleSignIn.getLastSignedInAccount(getActivity()) != null) {
            mGoogleSignInClient.silentSignIn().addOnCompleteListener(getActivity(), new OnCompleteListener<GoogleSignInAccount>() {
                @Override
                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                    if (task.isSuccessful()) {
                        // The signed in account is stored in the task's result.
                        GoogleSignInAccount signedInAccount = task.getResult();
                     //   user.setAccessToken(signedInAccount.getIdToken());
                        Log.d(TAG, "Got new access token " + signedInAccount.getIdToken());
                    } else {
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }
                }
            });
        }
    }

    private void signInWithGoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, accountFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            // Signed in successfully, show authenticated UI.
//            user.setName(account.getDisplayName());
//            user.setEmail(account.getEmail());
//            user.setAccessToken(idToken);
//            user.setLoginType(user.GOOGLE);

            //((LoginActivity) getActivity()).loginSuccess(user);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginFragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
    // [END auth_with_google]

  // [START auth_with_facebook]

    private void facebookLogin() {
        mFacebookLoginButton.setReadPermissions(Arrays.asList("email"));
        mFacebookLoginButton.setFragment(this);
        mFacebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String accessToken = loginResult.getAccessToken().getToken();
                        getFacebookDetails(loginResult.getAccessToken());
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

    public void getFacebookDetails(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {


                    @Override
                    public void onCompleted(JSONObject jsonObject,
                                            GraphResponse response) {

                        // Getting FB User Data
//                        Bundle facebookData = getFacebookData(jsonObject);
//                        user.setName(facebookData.getString("name"));
//                        user.setEmail(facebookData.getString("email"));
//                        user.setLoginType(user.FACEBOOK);
//                        user.setAccessToken(accessToken.getToken());
                        //  ((LoginActivity) getActivity()).loginSuccess(user);


                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
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



}