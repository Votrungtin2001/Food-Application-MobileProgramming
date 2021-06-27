package com.example.foodapplication.account.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.foodapplication.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.foodapplication.MySQL.MySQLQuerry.UpdateCustomerDOB;

public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    int user_id = -1;
    private final String TAG = "DateFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.customer_id > 0)
            user_id = MainActivity.customer_id;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getContext(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        if (user_id != -1) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, dd);
            calendar.set(Calendar.MONTH, mm);
            calendar.set(Calendar.YEAR, yy);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);

            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String sDate = simpleDateFormat.format(date);
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            UpdateCustomerDOB(user_id, sDate, progressDialog, TAG, getActivity());
        }
        else
            Toast.makeText(getContext(), "Unknown user. Did you forget to log in?", Toast.LENGTH_LONG).show();
    }
}
