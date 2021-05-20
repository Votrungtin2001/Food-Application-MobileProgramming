package com.example.foodapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;


public class LoginFragment extends Fragment  {
    private static final int RC_SIGN_IN = 9001 ;
    Fragment newFragment;

    public User user = new User();
    private View view;
TextView mess;
    private AccessTokenTracker accessTokenTracker;
    private final String TAG = "LoginFragment";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private GoogleSignInClient mGoogleSignInClient;
    // for google sign in

    // for facebook log in
    private final int FACEBOOK_LOG_IN_REQUEST_CODE = 64206;

    private GoogleApiClient mGoogleApiClient; // for google sign in
    private CallbackManager mFacebookCallbackManager; // for facebook log in
    LoginButton mFacebookLoginButton;
    public LoginFragment() {

    }
private View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.signin_gg:
                googleSignIn();
                break;
        }
    }
};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleSignIn();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_login, container, false);

      Button googlesignInButton = view.findViewById(R.id.signin_gg);
      googlesignInButton.setOnClickListener(mOnClickListener);
      // initFBFacebookLogIn();
      mess = view.findViewById(R.id.message1);
      mFacebookLoginButton = (LoginButton) view.findViewById(R.id.signin_fb);
      return view;
    }

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
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
       else
           {mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
           mess.setText("SS");}
    }

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
                        user.setAccessToken(signedInAccount.getIdToken());
                        Log.d(TAG, "Got new access token " + signedInAccount.getIdToken());
                    } else {
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }
                }
            });
        }
    }
//    private void signInWithGoogleSignIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            // Signed in successfully, show authenticated UI.
            user.setName(account.getDisplayName());
            user.setEmail(account.getEmail());
            user.setAccessToken(idToken);
            user.setLoginType(user.GOOGLE);

            //((LoginActivity) getActivity()).loginSuccess(user);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);


        }
    }
    // [END auth_with_google]

//    // [START auth_with_facebook]
//        private void initFBFacebookLogIn() {
//            mFacebookCallbackManager = CallbackManager.Factory.create();
//            mFacebookLoginButton.setFragment(LoginFragment.this);
//            mFacebookLoginButton.setReadPermissions("email", "public_profile");
//            mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
//                @Override
//                public void onSuccess(LoginResult loginResult) {
////                    mFaceBookLogInTextView.setText("onSuccess");
//                    handleFacebookAccessToken(loginResult.getAccessToken());
//                }
//
//                @Override
//                public void onCancel() {
////                    mFaceBookLogInTextView.setText("onCancel");
//                }
//
//                @Override
//                public void onError(FacebookException error) {
////                    mFaceBookLogInTextView.setText("error : " + error.getMessage());
//                }
//
//            });
//        }
//
//        private void handleFacebookAccessToken(AccessToken token) {
//            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//            mFirebaseAuth.signInWithCredential(credential)
//                    .addOnCompleteListener(LoginFragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            String message;
//                            if (task.isSuccessful()) {
//                                message = "success firebaseAuthWithFacebook";
//                            } else {
//                                message = "fail firebaseAuthWithFacebook";
//                            }
////                            mFaceBookLogInTextView.setText(message);
//                        }
//                    });
//        }

    private void facebookLogin() {
       // mFacebookLoginButton.setReadPermissions(Arrays.asList("email"));
        mFacebookLoginButton.setFragment(this);
        mFacebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        String accessToken = loginResult.getAccessToken().getToken();
                        getFacebookDetails(loginResult.getAccessToken());

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
                        Bundle facebookData = getFacebookData(jsonObject);
                        user.setName(facebookData.getString("name"));
                        user.setEmail(facebookData.getString("email"));
                        user.setLoginType(user.FACEBOOK);
                        user.setAccessToken(accessToken.getToken());
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