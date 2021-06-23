package com.example.foodapplication.account;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.foodapplication.R;

public class WebViewFragment extends Fragment {
    String path;

    public WebViewFragment() { }

    public static WebViewFragment newInstance() {
        return new WebViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("HTML_PATH");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        switch (path) {
            case "file:///android_asset/html/payment.html":
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_policy_payment));
                break;
            case "file:///android_asset/html/privacy.html":
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_policy_privacy));
                break;
            case "file:///android_asset/html/regulation.html":
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_policy_regulation));
                break;
            case "file:///android_asset/html/tos.html":
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_policy_tos));
                break;
            case "file:///android_asset/html/claims.html":
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_policy_dispute));
                break;
            default:
                break;
        }
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        WebView webView = view.findViewById(R.id.webView);
        try {
            webView.loadUrl(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}