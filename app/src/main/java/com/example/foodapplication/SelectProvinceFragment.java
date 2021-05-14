package com.example.foodapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SelectProvinceFragment extends Fragment {

    int checkAccumulator = 0;
    CheckBox cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9,cb10,cb11,cb12,cb13,cb14,cb15,cb16,cb17;
    TextView finish;
    CommunicationInterface listener;
    ImageView back;
    String s;
    public SelectProvinceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CommunicationInterface) {
            listener = (CommunicationInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "Can phai implement");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        s = Integer.toString(checkAccumulator);

        View view = inflater.inflate(R.layout.fragment_select_province, container, false);

        cb2 = (CheckBox) view.findViewById(R.id.checkBox2);
        cb3 = (CheckBox) view.findViewById(R.id.checkBox3);
        cb4 = (CheckBox) view.findViewById(R.id.checkBox4);
        cb5 = (CheckBox) view.findViewById(R.id.checkBox5);
        cb6 = (CheckBox) view.findViewById(R.id.checkBox6);
        cb7 = (CheckBox) view.findViewById(R.id.checkBox7);
        cb8 = (CheckBox) view.findViewById(R.id.checkBox8);
        cb9 = (CheckBox) view.findViewById(R.id.checkBox9);
        cb10 = (CheckBox) view.findViewById(R.id.checkBox10);
        cb11 = (CheckBox) view.findViewById(R.id.checkBox11);
        cb12 = (CheckBox) view.findViewById(R.id.checkBox12);
        cb13 = (CheckBox) view.findViewById(R.id.checkBox13);
        cb14 = (CheckBox) view.findViewById(R.id.checkBox14);
        cb15 = (CheckBox) view.findViewById(R.id.checkBox15);
        cb16 = (CheckBox) view.findViewById(R.id.checkBox16);
        cb17 = (CheckBox) view.findViewById(R.id.checkBox17);

        back = view.findViewById(R.id.back_setting);

        finish = view.findViewById(R.id.finish);


        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                countCheck(isChecked);
                Log.i("MAIN", checkAccumulator + "");
            }
        };

        cb2.setOnCheckedChangeListener(checkedChangeListener);
        cb3.setOnCheckedChangeListener(checkedChangeListener);
        cb4.setOnCheckedChangeListener(checkedChangeListener);
        cb5.setOnCheckedChangeListener(checkedChangeListener);
        cb6.setOnCheckedChangeListener(checkedChangeListener);
        cb7.setOnCheckedChangeListener(checkedChangeListener);
        cb8.setOnCheckedChangeListener(checkedChangeListener);
        cb9.setOnCheckedChangeListener(checkedChangeListener);
        cb10.setOnCheckedChangeListener(checkedChangeListener);
        cb11.setOnCheckedChangeListener(checkedChangeListener);
        cb12.setOnCheckedChangeListener(checkedChangeListener);
        cb13.setOnCheckedChangeListener(checkedChangeListener);
        cb14.setOnCheckedChangeListener(checkedChangeListener);
        cb15.setOnCheckedChangeListener(checkedChangeListener);
        cb16.setOnCheckedChangeListener(checkedChangeListener);
        cb17.setOnCheckedChangeListener(checkedChangeListener);

        finish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                listener.onClickTopFragment(s); }
        });

        return view;
    }

    private void countCheck(boolean isChecked) {
        checkAccumulator += isChecked ? 1 : -1 ;
    }
};

