package com.example.foodapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

//import com.savvi.rangedatepicker.CalendarPickerView;

public class OrderHistoryFragment extends Fragment {

    Spinner service1,dateSpinner;
    // CalendarPickerView calendar;
    Button button;

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

//        final Calendar nextYear = Calendar.getInstance();
//        nextYear.add(Calendar.YEAR, 10);
//
//        final Calendar lastYear = Calendar.getInstance();
//        lastYear.add(Calendar.YEAR, - 10);
//
//        calendar = view.findViewById(R.id.calendar_view);
//
//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(2);
//
//        calendar.deactivateDates(list);
//        ArrayList<Date> arrayList = new ArrayList<>();
//        try {
//            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
//
//            String strdate = "22-4-2019";
//            String strdate2 = "26-4-2019";
//
//            Date newdate = dateformat.parse(strdate);
//            Date newdate2 = dateformat.parse(strdate2);
//            arrayList.add(newdate);
//            arrayList.add(newdate2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        calendar.init(lastYear.getTime(), nextYear.getTime(), new SimpleDateFormat("MMMM, YYYY", Locale.getDefault())) //
//                .inMode(CalendarPickerView.SelectionMode.RANGE) //
//                .withDeactivateDates(list)
//                .withHighlightedDates(arrayList);
//
//        calendar.scrollToDate(new Date());
//
//
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "list " + calendar.getSelectedDates().toString(), Toast.LENGTH_LONG).show();
//            }
//        });


        return view;
    }


}