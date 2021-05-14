package com.example.foodapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomArrayAdapter extends ArrayAdapter<String> {
    int checkAccumulator;
    CheckBox cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9,cb10,cb11,cb12,cb13,cb14,cb15,cb16,cb17;
    public CustomArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_select_province, parent, false);
        String stringelement = getItem(position);
        // TextView Text = (TextView) customView.findViewById(R.id.textelement);

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
        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                countCheck(isChecked);
                Log.i("MAIN", checkAccumulator + "");
            }
        };

        cb2.setOnCheckedChangeListener(checkListener);
        cb3.setOnCheckedChangeListener(checkListener);
        cb4.setOnCheckedChangeListener(checkListener);
        cb5.setOnCheckedChangeListener(checkListener);
        cb6.setOnCheckedChangeListener(checkListener);
        cb7.setOnCheckedChangeListener(checkListener);
        cb8.setOnCheckedChangeListener(checkListener);
        cb3.setOnCheckedChangeListener(checkListener);
        cb3.setOnCheckedChangeListener(checkListener);
        cb3.setOnCheckedChangeListener(checkListener);
        cb3.setOnCheckedChangeListener(checkListener);
        cb3.setOnCheckedChangeListener(checkListener);
        cb3.setOnCheckedChangeListener(checkListener);
        cb3.setOnCheckedChangeListener(checkListener);
        cb3.setOnCheckedChangeListener(checkListener);
        cb3.setOnCheckedChangeListener(checkListener);
        // Text.setText(stringelement);
        return view;
    }
    private void countCheck(boolean isChecked) {

            checkAccumulator += isChecked ? 1 : -1 ;
    }
}
