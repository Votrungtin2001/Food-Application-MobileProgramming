package com.example.foodapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private ArrayList<MenuItem> lMenuItems;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtMenuName, txtMenuDesc, txtMenuPrice;
        public Button btnOrder;

        public ViewHolder(View itemView) {
            super(itemView);

            txtMenuName = itemView.findViewById(R.id.txtMenuName);
            txtMenuDesc = itemView.findViewById(R.id.txtMenuDesc);
            txtMenuPrice = itemView.findViewById(R.id.txtMenuPrice);

            btnOrder = itemView.findViewById(R.id.btnOrder);

            btnOrder.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(btnOrder, position);
                    }
                }
            });
        }
    }

    public MenuItemAdapter(ArrayList<MenuItem> lMenuItems, Context context) { this.lMenuItems = lMenuItems; this.context = context;}

    @Override
    public MenuItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View MenuItemView = inflater.inflate(R.layout.restaurant_menu_item_layout, parent, false);
        return new ViewHolder(MenuItemView);
    }

    @Override
    public void onBindViewHolder(MenuItemAdapter.ViewHolder holder, int pos) {
        MenuItem item = lMenuItems.get(pos);

        holder.txtMenuName.setText(item.getName());
        holder.txtMenuDesc.setText(item.getDesc());
        holder.txtMenuPrice.setText(item.getPrice());
    }

    @Override
    public int getItemCount() { return lMenuItems.size(); }
}
