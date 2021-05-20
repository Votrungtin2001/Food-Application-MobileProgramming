package com.example.foodapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "AuthenticationFragment";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private  SignInClient mGoogleSigninClient;
    // for google sign in
    private final String WEB_CLIENT_ID = "928488830433-camll1e451a5i4a0vrart4aia0rq707i.apps.googleusercontent.com";
    private final int GOOGLE_SIGN_IN_REQUEST_CODE = 0;

    // for facebook log in
    private final int FACEBOOK_LOG_IN_REQUEST_CODE = 64206;

    private GoogleApiClient mGoogleApiClient; // for google sign in
    private CallbackManager mFacebookCallbackManager; // for facebook log in
    private LoginButton mFacebookLoginButton;
    public LoginFragment() {
        // Required empty public constructor
    }
private View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.signin_gg:
                signInWithGoogleSignIn();
                break;
        }
    }
};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFBGoogleSignIn();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_login, container, false);

      Button googlesignInButton = (Button) view.findViewById(R.id.signin_gg);
      googlesignInButton.setOnClickListener(mOnClickListener);

      initFBGoogleSignIn();

      return view;
    }

    @Override
    public void onStart() {
        super.onStart();
       //  mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
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
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }  //

        } else if (requestCode == FACEBOOK_LOG_IN_REQUEST_CODE) {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // [START auth_with_google]
    private void initFBGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        // Context context = getContext();
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity(),0, (GoogleApiClient.OnConnectionFailedListener) this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()){
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity() /* FragmentActivity */, (GoogleApiClient.OnConnectionFailedListener) this /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }}

    private void signInWithGoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginFragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String message;
                        if (task.isSuccessful()) {
                            message = "success firebaseAuthWithGoogle";
                        } else {
                            message = "fail firebaseAuthWithGoogle";
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                mGoogleSignInTextView.setText(e.getMessage());
                e.printStackTrace();
            }
        });
    }
    // [END auth_with_google]

    // [START auth_with_facebook]
        private void initFBFacebookLogIn() {
            mFacebookCallbackManager = CallbackManager.Factory.create();
            mFacebookLoginButton.setFragment(this);
            mFacebookLoginButton.setReadPermissions("email", "public_profile");
            mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
//                    mFaceBookLogInTextView.setText("onSuccess");
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
//                    mFaceBookLogInTextView.setText("onCancel");
                }

                @Override
                public void onError(FacebookException error) {
//                    mFaceBookLogInTextView.setText("error : " + error.getMessage());
                }

            });
        }

        private void handleFacebookAccessToken(AccessToken token) {
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mFirebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(LoginFragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String message;
                            if (task.isSuccessful()) {
                                message = "success firebaseAuthWithFacebook";
                            } else {
                                message = "fail firebaseAuthWithFacebook";
                            }
//                            mFaceBookLogInTextView.setText(message);
                        }
                    });
        }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}