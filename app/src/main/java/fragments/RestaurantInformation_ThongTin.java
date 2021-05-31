package fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodapplication.CategoriesAdapter;
import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.R;


public class RestaurantInformation_ThongTin extends Fragment {

    String address;
    String opening_time;

    TextView textView_address;
    TextView textView_openingtime;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    int branch_id;

    public RestaurantInformation_ThongTin(int id) {
        this.branch_id = id;

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_information__thong_tin, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        textView_address = view.findViewById(R.id.RestaurantInformation_Address);
        address = getAddress(branch_id);
        textView_address.setText(address);

        textView_openingtime = view.findViewById(R.id.RestaurantInformation_OpeningTime);
        opening_time = getOpeningTime(branch_id);
        textView_openingtime.setText("Giờ mở cửa \t" + opening_time);

        return  view;
    }

    public String getAddress(int id) {
        String branch_address = "";
        String selectQuery = "SELECT A.Address FROM BRANCHES B JOIN ADDRESS A ON B.Address = A._id WHERE B._id ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                branch_address = cursor.getString(cursor.getColumnIndex("Address"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return branch_address;
    }

    public String getOpeningTime(int id) {
        String branch_openingtime = "";
        String selectQuery = "SELECT R.Opening_Times FROM BRANCHES B JOIN RESTAURANT R ON B.Restaurant = R._id WHERE B._id ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                branch_openingtime = cursor.getString(cursor.getColumnIndex("Opening_Times"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return branch_openingtime;
    }
}