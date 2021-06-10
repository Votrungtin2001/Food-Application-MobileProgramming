package fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.R;

import models.SortOfProductModel;


public class HomeFragment_Master extends Fragment {

    int master_id;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    TextView textView_Annoucement;
    ImageView imageView_No_Restaurant;
    TextView textView_Test;

    public HomeFragment_Master() {
        // Required empty public constructor
    }

    public HomeFragment_Master(int master_id) {
        this.master_id = master_id;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home__master, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        textView_Annoucement = view.findViewById(R.id.TextView_Announcement_Master);
        imageView_No_Restaurant = view.findViewById(R.id.ImageView_NoRestaurant_Master);
        textView_Test = view.findViewById(R.id.TextView_Master);


        boolean checkMasterHasRestaurant = CheckMasterHasRestaurant_Is_Right(master_id);
        if(checkMasterHasRestaurant == true) {
            textView_Annoucement.setVisibility(View.GONE);
            imageView_No_Restaurant.setVisibility(View.GONE);
            textView_Test.setVisibility(View.VISIBLE);
        }
        else {
            textView_Annoucement.setVisibility(View.VISIBLE);
            imageView_No_Restaurant.setVisibility(View.VISIBLE);
            textView_Test.setVisibility(View.GONE);
        }

        return view;
    }

    public boolean CheckMasterHasRestaurant_Is_Right(int id) {
        int count = 0;
        String selectQuery = "SELECT _id FROM BRANCHES WHERE Master='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        count = cursor.getCount();
        cursor.close();

        if(count > 0) return true;
        else return false;
    }
}