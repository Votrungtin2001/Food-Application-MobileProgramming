package com.example.foodapplication.Cart;

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

import java.util.List;

import models.ProductModel;

import static adapter.MenuAdapter.productModelList;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseHelper databaseHelper;

    TextView txtTotalPrice;
    Button btnPlaceOrder;
    OrderModel orderModel;

    // private List<OrderModel> cart = new ArrayList<>();
    CartAdapter cartAdapter;
    private SQLiteDatabase db;

    List<ProductModel> listCart = productModelList;
    private int branch_id;
    ProductModel productModel;

    public Cart(){

    }

    public Cart(int id) {
        branch_id = id;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
        databaseHelper = new DatabaseHelper(getBaseContext());
        db = databaseHelper.getReadableDatabase();

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // listCart = productModelList;
        loadListFood();
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
                ProductModel productModel = new ProductModel(name_product,1,price);
                listCart.add(productModel);

            } while (cursor.moveToNext());

        }
        cursor.close();
    }
//    public List<OrderModel> getCart() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//
//        String[] sqlSlect = {"OrderID","Item","Qty","Price"};
//        String sqlTable = "ORDER_DETAILS";
//
//        qb.setTables(sqlTable);
//        Cursor cursor = qb.query(db,sqlSlect,null, null, null, null, null);
//
//        final List<OrderModel> res = new ArrayList<>();
//        if(cursor.moveToFirst())
//        {
//            do{
//                res.add(new OrderModel(cursor.getInt(cursor.getColumnIndex("OrderID")),
//                        cursor.getString(cursor.getColumnIndex("Item")),
//                        cursor.getInt(cursor.getColumnIndex("Qty")),
//                        cursor.getInt(cursor.getColumnIndex("Price"))
//                ));
//            }while (cursor.moveToNext());
//        }
//        return res;
//    }
}