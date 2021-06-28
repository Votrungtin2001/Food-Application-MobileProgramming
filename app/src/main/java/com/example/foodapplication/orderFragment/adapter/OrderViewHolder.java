package com.example.foodapplication.orderFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.R;

import com.example.foodapplication.orderFragment.OrderDetailFragment;
import com.example.foodapplication.orderFragment.models.OrderModel;

import java.text.DecimalFormat;
import java.util.List;

public class OrderViewHolder extends RecyclerView.Adapter<OrderViewHolder.ViewHolder> {

    List<OrderModel> itemList;
    Context context;
    LayoutInflater inflater;

    public OrderViewHolder(Context ctx, List<OrderModel> ItemList) {
        this.context = ctx;
        this.itemList = ItemList;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public OrderViewHolder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_layout, parent, false);

        return new OrderViewHolder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel currentItem = itemList.get(position);

        holder.txtOrderId.setText(Integer.toString(currentItem.getOrderId()));
        DecimalFormat decimalFormat = new DecimalFormat( "###,###,###");
        holder.txtOrderStt.setText(decimalFormat.format(currentItem.getTotal()) + "đ");
        holder.txtPhone.setText(currentItem.getPhone());
        holder.txtAddress.setText(currentItem.getAddress());

        for(int i = 0; i < itemList.size(); i++) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    OrderDetailFragment myFragment = new OrderDetailFragment(currentItem.getOrderId());
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();
                }
            });
        }
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
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardlist_item);
            txtOrderId = itemView.findViewById(R.id.order_id);
            txtOrderStt = itemView.findViewById(R.id.order_status);
            txtPhone = itemView.findViewById(R.id.order_phone);
            txtAddress = itemView.findViewById(R.id.order_address);

        }
    }

}

