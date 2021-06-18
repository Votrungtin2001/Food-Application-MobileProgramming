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

import com.example.foodapplication.HomeFragment.model.SortOfProductModel;

public class SortOfProductAdapter extends RecyclerView.Adapter<SortOfProductAdapter.ViewHolder> {

    List<SortOfProductModel> itemList;
    Context context;
    LayoutInflater inflater;

    public SortOfProductAdapter(Context ctx, List<SortOfProductModel> list){
        this.itemList = list;
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_layout_sortofproduct, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SortOfProductModel currentItem = itemList.get(position);
        holder.image_product.setImageBitmap(currentItem.getImage());
        holder.name_product.setText(currentItem.getNameProduct());
        holder.name_branch.setText(currentItem.getNameBranch());
        holder.price.setText(currentItem.getPrice() + "đ");
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantInformation.class);
                int branch_id = currentItem.getBranch_id();
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
        TextView name_product;
        TextView name_branch;
        ImageView image_product;
        TextView price;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name_product = itemView.findViewById(R.id.SortOfProduct_NameProduct);
            name_branch = itemView.findViewById(R.id.SortOfProduct_NameBranch);
            image_product = itemView.findViewById(R.id.ImageView_SortOfProduct);
            constraintLayout = itemView.findViewById(R.id.ConstraintLayout_SortOfProduct);
            price = itemView.findViewById(R.id.SortOfProduct_PriceProduct);
        }
    }
}