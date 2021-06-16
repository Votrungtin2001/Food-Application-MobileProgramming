package com.example.foodapplication.Cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.R;

import java.util.List;

import models.ProductModel;

import static adapter.MenuAdapter.productModelList;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView nameItem, price, countItem, addItem, removeItem;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        nameItem = itemView.findViewById(R.id.item_name);
        price = itemView.findViewById(R.id.item_price);
        countItem = itemView.findViewById(R.id.item_amount);
        addItem = itemView.findViewById(R.id.add_item);
        removeItem = itemView.findViewById(R.id.remove_item);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    public List<ProductModel> listCart;
    private Context context;

    public CartAdapter(List<ProductModel> listCart,Context context) {
        this.listCart = listCart;
        this.context= context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_product_list,parent,false);

        listCart = productModelList;

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        holder.nameItem.setText(listCart.get(position).getNameProduct());
        holder.countItem.setText(Integer.toString(listCart.get(position).getQuantity()));
        int price = (listCart.get(position).getPrice())*(listCart.get(position).getQuantity());
        holder.price.setText(Integer.toString(price));
        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.countItem.setText(Integer.toString(++listCart.get(position).quantity));
                int price = (listCart.get(position).getPrice())*(listCart.get(position).getQuantity());
                holder.price.setText(Integer.toString(price));
            }
        });
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.countItem.setText(Integer.toString(listCart.get(position).quantity--));
                int price = (listCart.get(position).getPrice())*(listCart.get(position).getQuantity());
                holder.price.setText(Integer.toString(price));
                if(listCart.get(position).quantity == 0)
                {
                    listCart.remove(listCart.get(position));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listCart.size();
    }
}
