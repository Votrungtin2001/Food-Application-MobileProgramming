package com.example.foodapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class NotiSettingFragment extends Fragment {

    ImageView back_notif, select_pro;
    Fragment newFragment;
    TextView textView;

    public NotiSettingFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_noti_setting, container, false);

        back_notif = view.findViewById(R.id.back_notif);

        select_pro = view.findViewById(R.id.select_province);
        select_pro.setOnClickListener(SelectProvince);

        textView = (TextView) view.findViewById(R.id.textviewpro);

        return view;
    }

    View.OnClickListener SelectProvince = v -> {
        newFragment = new SelectProvinceFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    public void updateFragment(String str) {
        textView.setText(str);
    }
}