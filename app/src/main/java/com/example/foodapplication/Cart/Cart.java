package com.example.foodapplication.Cart;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.Order.OrderModel;
import com.example.foodapplication.R;
import com.example.foodapplication.auth.user;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.ProductModel;
import models.Request;

import static adapter.MenuAdapter.productModelList;
import static com.example.foodapplication.MainActivity.customer_id;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseHelper databaseHelper;
    TextView txtTotalPrice;
    Button btnPlaceOrder;
    OrderModel orderModel;
    CartAdapter cartAdapter;
    private SQLiteDatabase db;
    List<ProductModel> listCart = productModelList;
    private int branch_id;
    ProductModel productModel;
    Date currentTime = Calendar.getInstance().getTime();
    user user;

    public Cart(){ }

    public Cart(int id) { branch_id = id; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        user = new user();

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        databaseHelper = new DatabaseHelper(getBaseContext());
        db = databaseHelper.getReadableDatabase();
        txtTotalPrice = findViewById(R.id.total);

        loadListFood();

        Intent intent = getIntent();
        int addressID = intent.getIntExtra("Address",0);

        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request req = new Request(currentTime,
                        customer_id,
                        addressID,
                        Integer.parseInt(txtTotalPrice.getText().toString()),
                        0);
                databaseHelper.addOrder(req);

                for (int i = 0; i < listCart.size(); i++)
                {
                    databaseHelper.addOrderDetail(getOrderId(customer_id),
                            getMenuId(listCart.get(i).getProduct_id()) ,
                            listCart.get(i).getQuantity(),
                            listCart.get(i).getPrice());
                }

                finish();
            }
        });
    }

    private void loadListFood() {
        getAllProducts(branch_id);
        cartAdapter = new CartAdapter(listCart, getApplicationContext());
        LinearLayoutManager linearLayoutManager_Menu = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager_Menu);
        recyclerView.setAdapter(cartAdapter);

        int total = 0;

        for(int i = 0; i < listCart.size(); i++)
            total += ((listCart.get(i).getPrice()*(listCart.get(i).getQuantity())));

        txtTotalPrice.setText(Integer.toString(total));

    }
    public int getCusId() {
        String selectQuery = "SELECT _id FROM CUSTOMER '" + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

            } while (cursor.moveToNext());
        }
        int cusid = cursor.getColumnIndex("_id");
        return  cusid;
    }

    public int getMenuId(int product_id) {
        int menuid = -1;
        String selectQuery = "SELECT _id FROM MENU WHERE Product ='" + product_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                menuid = cursor.getColumnIndex("_id");
            } while (cursor.moveToNext());
        }
        cursor.close();
        return  menuid;
    }

    public int getOrderId(int customer_id) {
        int orderid = -1;
        String selectQuery = "SELECT _id FROM ORDERS WHERE Customer ='" + customer_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                orderid = cursor.getColumnIndex("_id");
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  orderid;
    }

    public void getAllProducts(int id) {
        String selectQuery = "SELECT P._id, P.Image, P.Name, P.Description AS PDescription, M.Description AS MDescription, M.Price " +
                "FROM ((RESTAURANT R JOIN BRANCHES B ON R._id = B.Restaurant) JOIN MENU M ON R._id = M.Restaurant) JOIN PRODUCTS P ON M.Product = P._id " +
                "WHERE B._id ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String name_product = cursor.getString(cursor.getColumnIndex("Name"));
                String valueOfSell = cursor.getString(cursor.getColumnIndex("MDescription"));
                int price = cursor.getInt(cursor.getColumnIndex("Price"));
                int productId = cursor.getInt(cursor.getColumnIndex("Product Id"));
                ProductModel productModel = new ProductModel(name_product,1,price,productId);
                listCart.add(productModel);
            } while (cursor.moveToNext());

        }
        cursor.close();
    }

}