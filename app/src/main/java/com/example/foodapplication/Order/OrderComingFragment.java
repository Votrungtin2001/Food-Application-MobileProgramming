package com.example.foodapplication.Order;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import static com.example.foodapplication.MainActivity.customer_id;

public class OrderComingFragment extends Fragment {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    private List<OrderModel> orderModelList;
    OrderViewHolder orderViewHolder;
    DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public OrderComingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_coming, container, false);

        recyclerView = view.findViewById(R.id.listOrders);
        orderModelList = new ArrayList<>();
        getOrder();
        orderViewHolder = new OrderViewHolder(getActivity(), orderModelList);
        LinearLayoutManager linearLayoutManager_Menu = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager_Menu);
        recyclerView.setAdapter(orderViewHolder);

        return view;
    }
    public void getOrder() {
        String selectQuery = " SELECT O._id, O.Status, O.Total, C.Phone " +
                "FROM ORDERS O JOIN CUSTOMER C ON O.Customer = C._id " +
                "WHERE O.Customer ='" + customer_id + "';";
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

}