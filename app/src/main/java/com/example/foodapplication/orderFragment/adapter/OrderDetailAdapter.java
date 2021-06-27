package com.example.foodapplication.orderFragment.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.R;
import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.orderFragment.models.OrderDetailModel;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    List<OrderDetailModel> itemList;
    Context context;
    LayoutInflater inflater;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    public OrderDetailAdapter(Context ctx, List<OrderDetailModel> ItemList) {
        this.context = ctx;
        this.itemList = ItemList;
        this.inflater = LayoutInflater.from(ctx);
    }


    @NonNull
    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_detail_list, parent, false);

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getReadableDatabase();

        return new OrderDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetailModel currentItem = itemList.get(position);

        holder.imgProduct.setImageBitmap(currentItem.getImageProduct());
        holder.txtName.setText(currentItem.getName());
        holder.txtPrice.setText(Integer.toString(currentItem.getPrice()));
        holder.txtQuantity.setText(Integer.toString(currentItem.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtPrice;
        TextView txtQuantity;
        ImageView imgProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.product_name);
            txtPrice = itemView.findViewById(R.id.price);
            txtQuantity = itemView.findViewById(R.id.count);
        }
    }
}

