package com.example.foodapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class User {
    private String email;
    private String name;
    private String accessToken;
    private String loginType;

    private static final String TAG = User.class.getSimpleName();

    public static final String FACEBOOK = "facebook";
    public static final String GOOGLE = "google";


    public User () {

    }

    public User(String namw, String email) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void preformRegister (Context context, final AsyncLoginResponse loginCallBack) {

        // Request a string response from the provided URL.
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST,
                URLS.SOCIAL_LOGIN_URL, new JSONObject(getLoginParams()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    AppController.getInstance().set_token(response.getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null != loginCallBack) loginCallBack.LoginResponseReceived("Success", 200);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorResponse = ParseNetworkError.GetErrorMessage(error);
                if (null != loginCallBack) loginCallBack.LoginResponseReceived(errorResponse, error.networkResponse.statusCode);
            }
        });

        NetworkController.getInstance(context).addToRequestQueue(loginRequest);

    }

    private HashMap<String, String> getLoginParams() {
        HashMap<String, String> loginParams = new HashMap<>();
        loginParams.put("name", getName());
        loginParams.put("access_token", getAccessToken());
        loginParams.put("email", getEmail());
       //  loginParams.put("pushToken", FirebaseInstanceId.getInstance().getToken());
        loginParams.put("loginType",getLoginType());
        return loginParams;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
