package com.example.foodapplication.Cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.Order.OrderModel;
import com.example.foodapplication.R;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView nameItem, price, countItem;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        nameItem = itemView.findViewById(R.id.item_name);
        price = itemView.findViewById(R.id.item_price);
        countItem = itemView.findViewById(R.id.item_amount);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<OrderModel> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<OrderModel> listData,Context context) {
        this.listData = listData;
        this.context= context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_product_list,parent,false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.countItem.setText(listData.get(position).getQuantity());

        int price = (listData.get(position).getPrice())*(listData.get(position).getQuantity());
        holder.price.setText(price);

        holder.nameItem.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
