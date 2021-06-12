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
    user user = new user();
    int addressId;

    public Cart(){ }

    public Cart(int id) { branch_id = id; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        databaseHelper = new DatabaseHelper(getBaseContext());
        db = databaseHelper.getReadableDatabase();

        txtTotalPrice = findViewById(R.id.total);

        loadListFood();

        Intent intent = new Intent();
        intent.putExtra("Address", addressId);

        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request req = new Request(currentTime,
                        user.getId(),
                        addressId,
                        Integer.parseInt(txtTotalPrice.getText().toString()),
                        0);
                databaseHelper.addOrder(req);

                for (int i = 0; i < listCart.size(); i++)
                {
                    databaseHelper.addOrderDetail(3,
                            1 ,
                            listCart.get(i).getQuantity(),
                            listCart.get(i).getPrice());
                }
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