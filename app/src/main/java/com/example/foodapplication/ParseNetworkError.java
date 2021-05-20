package com.example.foodapplication;

import com.android.volley.VolleyError;

public class ParseNetworkError {
    public static final String GetErrorMessage(VolleyError error) {
        String body;
        //final String statusCode = String.valueOf(error.networkResponse.statusCode);
        try {
            body = new String(error.networkResponse.data, "UTF-8");
            return body;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
