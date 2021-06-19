package com.example.foodapplication.HomeFragment.fragment;

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

import com.example.foodapplication.HomeFragment.adapter.MenuAdapter;
import models.ProductModel;


public class RestaurantInformation_DatDon extends Fragment {

    private RecyclerView recyclerView_Menu;
    private MenuAdapter menuAdapter;
    private List<ProductModel> productModelList;
    private int branch_id;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    public RestaurantInformation_DatDon() {
    }
    public RestaurantInformation_DatDon(int id) {
        branch_id = id;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_information__dat_don, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        initComponents(view);
        Run();

        return  view;
    }

    public void initComponents(View view) {
        recyclerView_Menu = view.findViewById(R.id.Menu_RecyclerView);
    }

    public void Run() {
        productModelList = new ArrayList<>();
        getAllProducts(branch_id);
        menuAdapter = new MenuAdapter(getActivity(), productModelList);
        LinearLayoutManager linearLayoutManager_Menu = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_Menu.setLayoutManager(linearLayoutManager_Menu);
        recyclerView_Menu.setAdapter(menuAdapter);
    }

    public void getAllProducts(int id) {
        String selectQuery = "SELECT P._id, P.Image, P.Name, P.Description AS PDescription, M.Description AS MDescription, M.Price " +
                "FROM ((RESTAURANT R JOIN BRANCHES B ON R._id = B.Restaurant) JOIN MENU M ON R._id = M.Restaurant) JOIN PRODUCTS P ON M.Product = P._id " +
                "WHERE B._id ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int product_id = cursor.getInt(cursor.getColumnIndex("_id"));
                byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                String name_product = cursor.getString(cursor.getColumnIndex("Name"));
                String description_product = cursor.getString(cursor.getColumnIndex("PDescription"));
                String valueOfSell = cursor.getString(cursor.getColumnIndex("MDescription"));
                int price = cursor.getInt(cursor.getColumnIndex("Price"));
                ProductModel productModel = new ProductModel(bitmap, name_product, description_product, valueOfSell, price, product_id);
                productModelList.add(productModel);

            } while (cursor.moveToNext());

        }
        cursor.close();
    }
}