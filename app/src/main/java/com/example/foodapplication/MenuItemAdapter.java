package com.example.foodapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private List<MenuItem> lMenuItems;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtMenuName, txtMenuDesc, txtMenuPrice;
        public ViewHolder(View itemView) {
            super(itemView);

            txtMenuName = (TextView) itemView.findViewById(R.id.txtMenuName);
            txtMenuDesc = (TextView) itemView.findViewById(R.id.txtMenuDesc);
            txtMenuPrice = (TextView) itemView.findViewById(R.id.txtMenuPrice);
        }
    }

    public MenuItemAdapter(List<MenuItem> lMenuItems) { this.lMenuItems = lMenuItems; }

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
