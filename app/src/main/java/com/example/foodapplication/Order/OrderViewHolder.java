package com.example.foodapplication.Order;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.R;

import java.util.List;

public class OrderViewHolder extends RecyclerView.Adapter<OrderViewHolder.ViewHolder> {

    List<OrderModel> itemList;
    Context context;
    LayoutInflater inflater;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    public OrderViewHolder(Context ctx, List<OrderModel> ItemList) {
        this.context = ctx;
        this.itemList = ItemList;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public OrderViewHolder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_layout, parent, false);

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getReadableDatabase();

        return new OrderViewHolder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel currentItem = itemList.get(position);

        holder.txtOrderId.setText(currentItem.getOrderId());
        holder.txtOrderStt.setText(currentItem.getOrderStatus());
        holder.txtPhone.setText(currentItem.getPhone());
        holder.txtAddress.setText(currentItem.getTotal());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId;
        TextView txtOrderStt;
        TextView txtPhone;
        TextView txtAddress;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderId = itemView.findViewById(R.id.order_id);
            txtOrderStt = itemView.findViewById(R.id.order_status);
            txtPhone = itemView.findViewById(R.id.order_phone);
            txtAddress = itemView.findViewById(R.id.order_address);

        }
    }
}

