package com.example.foodapplication.account;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapplication.R;

import static androidx.core.content.ContextCompat.getSystemService;

public class AccountInvite extends Fragment {

    public AccountInvite() { }

    public static AccountInvite newInstance() {
        return new AccountInvite();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_invite, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_account_invite));
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        TextView txtInviteSMS = view.findViewById(R.id.txtInviteSMS);
        txtInviteSMS.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
            intent.putExtra("sms_body", "Tải ứng dụng FoodApplication tại: https://github.com/Votrungtin2001/Food-Application-MobileProgramming");
            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), "Không mở được ừng dụng nhắn tin SMS!", Toast.LENGTH_SHORT).show();
            }
        });

        TextView txtInviteClipboard = view.findViewById(R.id.txtInviteClipboard);
        txtInviteClipboard.setOnClickListener(v -> {
            ClipboardManager clipboard = getSystemService(getContext(), ClipboardManager.class);
            ClipData clip = ClipData.newPlainText("link", "https://github.com/Votrungtin2001/Food-Application-MobileProgramming");
            clipboard.setPrimaryClip(clip);
        });

        return view;
    }
}