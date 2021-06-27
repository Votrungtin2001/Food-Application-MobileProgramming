package com.example.foodapplication.orderFragment;

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

import com.example.foodapplication.R;

import com.example.foodapplication.orderFragment.adapter.OrderDetailAdapter;
import com.example.foodapplication.orderFragment.models.OrderDetailModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodapplication.MainActivity.customer_id;

public class OrderDetailFragment extends Fragment {

    private RecyclerView recyclerView_OrderDetail;
    private OrderDetailAdapter menuAdapter;
    private List<OrderDetailModel> orderDetailModelList;

    SQLiteDatabase db;

    private int orderId;

    public OrderDetailFragment() {
    }

    public OrderDetailFragment(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        initComponents(view);
        Run();

        return view;
    }

    public void initComponents(View view) {
        recyclerView_OrderDetail = view.findViewById(R.id.listOrdersDetail);
    }

    public void Run() {
        orderDetailModelList = new ArrayList<>();
        getAllProducts(orderId);
        menuAdapter = new OrderDetailAdapter(getActivity(), orderDetailModelList);
        LinearLayoutManager linearLayoutManager_Menu = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_OrderDetail.setLayoutManager(linearLayoutManager_Menu);
        recyclerView_OrderDetail.setAdapter(menuAdapter);
    }

    public void getAllProducts(int id) {
        String selectQuery = "SELECT P.Image, P.Name, M.Price, OD.Qty " +
                "FROM (((ORDER_DETAILS OD JOIN ORDERS O ON OD.OrderID = O._id ) JOIN MENU M ON OD.Item = M._id) JOIN PRODUCTS P ON M.Product = P._id) " +
                "WHERE O._id ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                String name_product = cursor.getString(cursor.getColumnIndex("Name"));
                int quantity = cursor.getInt(cursor.getColumnIndex("Qty"));
                int price = cursor.getInt(cursor.getColumnIndex("Price"));
                OrderDetailModel orderDetailModel = new OrderDetailModel(bitmap, name_product, price, quantity);
                orderDetailModelList.add(orderDetailModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public int getOrderId() {
        int orderid = -1;
        String selectQuery = "SELECT OD.OrderID " +
                "FROM (ORDER_DETAILS OD JOIN ORDERS O ON O._ID = OD.OrderID) JOIN CUSTOMER C ON C._id = O.Customer " +
                "WHERE O.Customer ='" + customer_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                orderid = cursor.getInt(cursor.getColumnIndex("OrderID"));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  orderid;
    }

}