package com.example.foodapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment  {

    DatePickerDialog.OnDateSetListener onDateSet;
    public static DatePickerFragment newInstance(){
        DatePickerFragment frag = new DatePickerFragment();
        return frag;
    }

    public DatePickerFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),onDateSet,year,month,day);
    }
    public void setCallBack(DatePickerDialog.OnDateSetListener onDate) {
        onDateSet = onDate;
    }

    public void show(FragmentTransaction beginTransaction, String datePickerFragment) {
    }
}