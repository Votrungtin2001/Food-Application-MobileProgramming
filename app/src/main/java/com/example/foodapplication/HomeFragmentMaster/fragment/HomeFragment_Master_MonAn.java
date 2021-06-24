package com.example.foodapplication.HomeFragmentMaster.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.R;

import java.util.ArrayList;
import java.util.List;

import com.example.foodapplication.HomeFragmentMaster.adapter.MenuAdapter_HomeFragment_Master_DatDon;
import models.ProductModel;


public class HomeFragment_Master_MonAn extends Fragment {


    private List<ProductModel> productModelList;
    private int branch_id;

    RecyclerView recyclerView_HomeFragment_Master_DatDon;
    MenuAdapter_HomeFragment_Master_DatDon adapter;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;


    public HomeFragment_Master_MonAn() {
        // Required empty public constructor
    }

    public HomeFragment_Master_MonAn(int id) {
        branch_id = id;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home__master__mon_an, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        initComponents(view);

        //SetRecyclerView();




        return view;
    }

    public void initComponents(View view) {
        recyclerView_HomeFragment_Master_DatDon = (RecyclerView) view.findViewById(R.id.Menu_RecyclerView_HomeFragment_Master_DatDon);
    }

    /*public void getAllProducts(int id) {
        String selectQuery = "SELECT P._id, P.Image, P.Name, P.Description AS PDescription, M.Description AS MDescription, M.Price " +
                "FROM ((RESTAURANT R JOIN BRANCHES B ON R._id = B.Restaurant) JOIN MENU M ON R._id = M.Restaurant) JOIN PRODUCTS P ON M.Product = P._id " +
                "WHERE B._id ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
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

    public int getCountProduct(int id) {
        String selectQuery = "SELECT P._id, P.Image, P.Name, P.Description AS PDescription, M.Description AS MDescription, M.Price " +
                "FROM ((RESTAURANT R JOIN BRANCHES B ON R._id = B.Restaurant) JOIN MENU M ON R._id = M.Restaurant) JOIN PRODUCTS P ON M.Product = P._id " +
                "WHERE B._id ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {

            } while (cursor.moveToNext());

        }
        int count = cursor.getCount();
        cursor.close();
        return count;
    }*/

    public void SetRecyclerView()
    {
       /* int count_products = getCountProduct(branch_id);
        if(count_products > 0) {
            productModelList = new ArrayList<>();
            getAllProducts(branch_id);
            adapter = new MenuAdapter_HomeFragment_Master_DatDon(getActivity(), productModelList);
            LinearLayoutManager linearLayoutManager_Menu = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView_HomeFragment_Master_DatDon.setLayoutManager(linearLayoutManager_Menu);
            recyclerView_HomeFragment_Master_DatDon.setAdapter(adapter);
        }*/
    }
}