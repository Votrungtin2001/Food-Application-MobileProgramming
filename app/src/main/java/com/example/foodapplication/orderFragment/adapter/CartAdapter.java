package com.example.foodapplication.orderFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.R;

import java.text.DecimalFormat;
import java.util.List;

import com.example.foodapplication.HomeFragment.model.ProductModel;

import static com.example.foodapplication.HomeFragment.adapter.MenuAdapter.productModelList;
import static com.example.foodapplication.orderFragment.cart.Cart.dTotal;
import static com.example.foodapplication.orderFragment.cart.Cart.txtTotalPrice;

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

    double total = 0;

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
        double price = (listCart.get(position).getPrice())*(listCart.get(position).getQuantity());
        DecimalFormat decimalFormat = new DecimalFormat( "###,###,###");
        holder.price.setText(decimalFormat.format(price) + "đ");
        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = listCart.get(position).getQuantity();
                listCart.get(position).setQuantity(newQuantity + 1);
                holder.countItem.setText(Integer.toString(listCart.get(position).getQuantity()));
                double price = (listCart.get(position).getPrice())*(listCart.get(position).getQuantity());
                holder.price.setText(decimalFormat.format(price) + "đ");
                total = 0;
                for(int i = 0; i < listCart.size(); i++)
                    total += ((listCart.get(i).getPrice()*(listCart.get(i).getQuantity())));

                txtTotalPrice.setText(decimalFormat.format(total));
                dTotal = total;
            }
        });
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = listCart.get(position).getQuantity();
                listCart.get(position).setQuantity(newQuantity - 1);
                holder.countItem.setText(Integer.toString(listCart.get(position).getQuantity()));
                double price = (listCart.get(position).getPrice())*(listCart.get(position).getQuantity());
                holder.price.setText(decimalFormat.format(price) + "đ");
                if(listCart.get(position).quantity == 0)
                {
                    listCart.remove(listCart.get(position));
                }
                total = 0;
                for(int i = 0; i < listCart.size(); i++)
                    total += ((listCart.get(i).getPrice()*(listCart.get(i).getQuantity())));

                txtTotalPrice.setText(decimalFormat.format(total));
            }
        });
    }


    @Override
    public int getItemCount() {
        return listCart.size();
    }
}
