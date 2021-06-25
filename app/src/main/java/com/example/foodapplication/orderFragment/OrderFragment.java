package com.example.foodapplication.orderFragment;

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

import com.example.foodapplication.R;
import com.example.foodapplication.MySQL.DatabaseHelper;
import com.example.foodapplication.orderFragment.adapter.OrderViewHolder;
import com.example.foodapplication.orderFragment.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodapplication.MainActivity.customer_id;


public class OrderFragment extends Fragment {

    public RecyclerView recyclerView;
    List<OrderModel> orderModelList = new ArrayList<>();
    OrderViewHolder orderViewHolder;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    LinearLayout linearLayout;
    LinearLayoutManager linearLayoutManager_Menu;

    boolean order_isAvailable = false;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        recyclerView = (RecyclerView) view.findViewById(R.id.listOrders);
        linearLayout = view.findViewById(R.id.none);

        if(getOId() > 0) {order_isAvailable = true;}
        SetAllData();
        setUpSreen(order_isAvailable);

        return view;
    }

    public void SetAllData() {
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
        String selectQuery =  " SELECT O._id, O.Total, C.Phone, A.Address " +
                " FROM ((ORDERS O JOIN CUSTOMER C ON O.Customer = C._id) JOIN CUSTOMER_ADDRESS CA ON CA.Customer = C._id ) JOIN ADDRESS A ON A._id = CA.Address " +
                "WHERE O.Customer ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int orderid = cursor.getInt(cursor.getColumnIndex("_id"));
                int total = cursor.getInt(cursor.getColumnIndex("Total"));
                String phone = cursor.getString(cursor.getColumnIndex("Phone"));
                String address = cursor.getString(cursor.getColumnIndex("Address"));
                OrderModel orderModel = new OrderModel(orderid,total,phone,address);
                orderModelList.add(orderModel);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }

    public int getOId() {
        int orderid = -1;
        String selectQuery = "SELECT * FROM ORDERS WHERE Customer ='" + customer_id + "';";
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