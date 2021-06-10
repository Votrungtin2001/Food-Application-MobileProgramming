package fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.R;

import java.util.ArrayList;
import java.util.List;

import adapter.KindOfRestaurantAdapter;
import models.KindOfRestaurantModel;

public class NearMeRestaurantsFragment extends Fragment {

    RecyclerView recyclerView_KindOfRestaurant;
    KindOfRestaurantAdapter kindOfRestaurantAdapter;
    List<KindOfRestaurantModel> kindOfRestaurantModelList;
    int district_id = 14;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    public NearMeRestaurantsFragment() {
    }
    public NearMeRestaurantsFragment(int id) {
        this.district_id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearme_kindofrestaurant, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        recyclerView_KindOfRestaurant = view.findViewById(R.id.NearMeKindOfRestaurant_RecyclerView);

        AddDataForKindOfRestaurantWithNearMe(district_id);
        kindOfRestaurantAdapter = new KindOfRestaurantAdapter(getActivity(), kindOfRestaurantModelList);
        LinearLayoutManager linearLayoutManager_Categories = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_KindOfRestaurant.setLayoutManager(linearLayoutManager_Categories);
        recyclerView_KindOfRestaurant.setAdapter(kindOfRestaurantAdapter);

        return  view;

    }

    private void AddDataForKindOfRestaurantWithNearMe(int id)
    {
        kindOfRestaurantModelList = new ArrayList<>();
        String selectQuery = "SELECT B._id, R.Image, B.NAME, A.Address, R.Opening_Times " +
                "FROM (RESTAURANT R JOIN BRANCHES B ON R._id = B.Restaurant) " +
                "JOIN ADDRESS A ON B.Address = A._id WHERE A.District ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
               int branch_id = cursor.getInt(cursor.getColumnIndex("_id"));
               if(branch_id % 2 == 0) {
                   byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                   Bitmap bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                   String name_branch = cursor.getString(cursor.getColumnIndex("NAME"));
                   String address_branch = cursor.getString(cursor.getColumnIndex("Address"));
                   String opening_time = cursor.getString(cursor.getColumnIndex("Opening_Times"));
                   KindOfRestaurantModel kindOfRestaurantModel = new KindOfRestaurantModel(bitmap, name_branch, address_branch, opening_time, branch_id);
                    kindOfRestaurantModelList.add(kindOfRestaurantModel);

               }
            } while (cursor.moveToNext());

        }
        cursor.close();


    }
}