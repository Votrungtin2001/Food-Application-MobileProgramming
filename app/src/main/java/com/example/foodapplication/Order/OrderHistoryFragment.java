package com.example.foodapplication.Order;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.foodapplication.DatePickerFragment;
import com.example.foodapplication.R;

import java.text.DateFormat;
import java.util.Calendar;


public class OrderHistoryFragment extends Fragment {

    Spinner service1, all ;
    private String currentDate;
    private TextView showDateTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        service1 = (Spinner) view.findViewById(R.id.service);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.service_array, android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        service1.setAdapter(staticAdapter);

        showDateTextView = view.findViewById(R.id.setDate_textView);
        showDateTextView.setOnClickListener(new ClickListener());


        String [] values =
                {"Tất cả","Đã hủy","Hoàn thành",};
        all = (Spinner) view.findViewById(R.id.all);
        ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        all.setAdapter(LTRadapter);

        return view;
    }

//    private void initspinnerfooter() {
//        String[] items = new String[]{
//                "Tất cả", "Đã hủy", "Hoàn thành",
//        };
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
//        all.setAdapter(adapter);
//        all.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.v("item", (String) parent.getItemAtPosition(position));
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//    }
    private DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            showDateTextView.setText(currentDate);
        }
    };
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {

            DatePickerFragment dpf = DatePickerFragment.newInstance();
            dpf.setCallBack(onDate);
            dpf.show(getFragmentManager().beginTransaction(),"DatePickerFragment");
        }
    }


}