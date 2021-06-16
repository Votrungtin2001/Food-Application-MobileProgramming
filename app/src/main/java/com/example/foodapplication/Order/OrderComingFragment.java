package com.example.foodapplication.Order;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodapplication.MainActivity.customer_id;

public class OrderComingFragment extends Fragment {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    List<OrderModel> orderModelList = new ArrayList<>();
    OrderViewHolder orderViewHolder;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    LinearLayout linearLayout;
    LinearLayoutManager linearLayoutManager_Menu;
    int order_id = 1;
    boolean order_isAvailable = false;

    public OrderComingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_coming, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        recyclerView = (RecyclerView) view.findViewById(R.id.listOrders);
        linearLayout = view.findViewById(R.id.none);

        if(getOId() > 0) {order_isAvailable = true;}
        SetAllData(getOId());
        setUpSreen(order_isAvailable);

        return view;
    }
    public void SetAllData(int a) {
        order_isAvailable = false;
        if(a >= 0) order_isAvailable = true;
        getOrder(customer_id);
        orderViewHolder = new OrderViewHolder(getActivity(), orderModelList);
        linearLayoutManager_Menu = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager_Menu);
        recyclerView.setAdapter(orderViewHolder);
    }
    private void setUpSreen(boolean sign) {
        if (sign == true) {
            recyclerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }


    public void getOrder(int id) {
        orderModelList = new ArrayList<>();
        String selectQuery = " SELECT O._id, O.Status, O.Total, C.Phone " +
                "FROM ORDERS O JOIN CUSTOMER C ON O.Customer = C._id " +
                "WHERE O.Customer ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int orderid = cursor.getInt(cursor.getColumnIndex("_id"));
                int stt =  cursor.getInt(cursor.getColumnIndex("Status"));
                int total = cursor.getInt(cursor.getColumnIndex("Total"));
                String phone = cursor.getString(cursor.getColumnIndex("Phone"));
                OrderModel orderModel = new OrderModel(orderid,stt,total,phone);
                orderModelList.add(orderModel);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }

    public int getOId() {
        int orderid = -1;
        String selectQuery = "SELECT _id FROM ORDERS WHERE Customer ='" + customer_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                orderid = cursor.getInt(cursor.getColumnIndex("_id"));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  orderid;
    }

}