package com.example.foodapplication.HomeFragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.R;
import com.example.foodapplication.HomeFragment.RestaurantInformation;

import java.util.List;

import com.example.foodapplication.HomeFragment.model.AllRestaurantModel;

public class AllRestaurantAdapter extends RecyclerView.Adapter<AllRestaurantAdapter.ViewHolder> {

    List<AllRestaurantModel> itemList;
    Context context;
    LayoutInflater inflater;

    public AllRestaurantAdapter(Context ctx, List<AllRestaurantModel> list){
        this.itemList = list;
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @NonNull
    @Override
    public AllRestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_allrestaurants_constraint_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllRestaurantAdapter.ViewHolder holder, int position) {
        AllRestaurantModel currentItem = itemList.get(position);
        holder.title1.setText(currentItem.getNameBranch());
        holder.title2.setText(currentItem.getAddressBranch());
        holder.constraintIcon.setImageBitmap(currentItem.getImage());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantInformation.class);
                int branch_id = currentItem.getBranchID();
                intent.putExtra("Branch ID", branch_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1;
        TextView title2;
        ImageView constraintIcon;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title1 = itemView.findViewById(R.id.AllRestaurants_NameRestaurant);
            title2 = itemView.findViewById(R.id.AllRestaurants_Address);
            constraintIcon = itemView.findViewById(R.id.imageView_AllRestaurants);
            constraintLayout = itemView.findViewById(R.id.AllRestaurant_ConstraintLayout);
        }
    }
}
