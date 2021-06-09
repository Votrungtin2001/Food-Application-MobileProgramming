package com.example.foodapplication.Cart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.Order.OrderModel;
import com.example.foodapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseHelper databaseHelper;

    TextView txtTotalPrice;
    Button btnPlaceOrder;
    OrderModel orderModel;

    private List<OrderModel> cart = new ArrayList<>();
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        
        loadListFood();
    }

    private void loadListFood() {
        cart = new DatabaseHelper(this).getCart();
        cartAdapter = new CartAdapter(cart,this);
    }

}