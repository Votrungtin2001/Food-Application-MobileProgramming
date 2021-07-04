package com.example.foodapplication.orderFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.R;
import com.example.foodapplication.orderFragment.models.OrderDetailModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    List<OrderDetailModel> itemList;
    Context context;
    LayoutInflater inflater;

    public OrderDetailAdapter(Context ctx, List<OrderDetailModel> ItemList) {
        this.context = ctx;
        this.itemList = ItemList;
        this.inflater = LayoutInflater.from(ctx);
    }


    @NonNull
    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_detail_list, parent, false);

        return new OrderDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetailModel currentItem = itemList.get(position);

        if(currentItem.getImageProduct().isEmpty()){
            holder.imgProduct.setImageResource(R.drawable.noimage_product);
        } else {
            Picasso.get ().load ( currentItem.getImageProduct())
                    .placeholder(R.drawable.noimage_product)
                    .error(R.drawable.error)
                    .into(holder.imgProduct);
        }
        holder.txtName.setText(currentItem.getName());
        DecimalFormat decimalFormat = new DecimalFormat( "###,###,###" );
        holder.txtPrice.setText(decimalFormat.format(currentItem.getPrice()) + "đ");
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

