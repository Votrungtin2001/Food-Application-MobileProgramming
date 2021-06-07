package com.example.foodapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

// where to open this fragment?
public class RestaurantFragment extends Fragment {
    ArrayList<MenuItem> items;
    int res_id, cus_id; // the trigger that opens the restaurant fragment needs to pass the id of the restaurant being viewed and the user's id
    // as an arg in a bundle
    // need to override the onCreate method to obtain the id from the bundle

    long order_id;

    RelativeLayout checkoutBar;
    TextView txtCheckoutNoOfItems, txtCheckoutTotal;
    Button btnCheckout;

    DatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        cus_id = args.getInt("CUSTOMER_ID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        order_id = -1;

        dbHelper = new DatabaseHelper(view.getContext());

        checkoutBar = view.findViewById(R.id.checkoutBar);
        txtCheckoutNoOfItems = view.findViewById(R.id.txtCheckoutNoOfItems);
        txtCheckoutTotal = view.findViewById(R.id.txtCheckoutTotal);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        checkoutBar.setVisibility(RelativeLayout.GONE);

        RecyclerView rvMenu = view.findViewById(R.id.rvMenu);

        items = MenuItem.createMenu(res_id, view.getContext());
        MenuItemAdapter adapter = new MenuItemAdapter(items, view.getContext());

        adapter.setOnItemClickListener((view1, position) -> {
            if (order_id == -1) {
                order_id = dbHelper.addOrder(Calendar.getInstance().getTime(), cus_id);
                MenuItem item = items.get(position);
                dbHelper.addOrderDetail((int)order_id, item.getId(), 1, item.getPrice());

                checkoutBar.setVisibility(RelativeLayout.VISIBLE);
                txtCheckoutNoOfItems.setText(Integer.toString(1));
                txtCheckoutTotal.setText(Integer.toString(item.getPrice()));
            }
            else{
                MenuItem item = items.get(position);

                Cursor cursor = dbHelper.getOrderDetail((int)order_id, item.getId());
                if (cursor.moveToFirst()) {
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.COrderDetails.KEY_QUANTITY)) + 1;
                    dbHelper.updOrderDetail((int)order_id, item.getId(), quantity, item.getPrice() * quantity);
                }
                else
                    dbHelper.addOrderDetail((int)order_id, item.getId(), 1, item.getPrice());

                txtCheckoutNoOfItems.setText(Integer.toString(Integer.parseInt(txtCheckoutNoOfItems.getText().toString()) + 1));
                int total = dbHelper.calcOrderTotal((int)order_id);
                txtCheckoutTotal.setText(Integer.toString(total));
                dbHelper.updOrderTotal((int)order_id, total);
            }
        });

        rvMenu.setAdapter(adapter);
        rvMenu.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

    public void onStop() {
        super.onStop();

        dbHelper.delOrderDetail((int)order_id);
        dbHelper.delOrder((int)order_id);
        order_id = -1;
        checkoutBar.setVisibility(RelativeLayout.GONE);
        dbHelper.close();
    }
}
