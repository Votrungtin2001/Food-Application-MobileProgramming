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

import com.example.foodapplication.HomeFragment.model.ProductCategoryModel;

public class ProductWithCategoryAdapter extends RecyclerView.Adapter<ProductWithCategoryAdapter.ViewHolder> {

    List<ProductCategoryModel> itemList;
    Context context;
    LayoutInflater inflater;

    public ProductWithCategoryAdapter(Context ctx, List<ProductCategoryModel> ItemList) {
        this.context = ctx;
        this.itemList = ItemList;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_layout_item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductCategoryModel currentItem = itemList.get(position);

        holder.textView_NameProduct.setText(currentItem.getNameProduct());
        holder.textView_DescriptionProduct.setText(currentItem.getDescriptionProduct());
        holder.textView_NameBranch.setText(currentItem.getNameBranch());
        holder.imageView_ImageProduct.setImageBitmap(currentItem.getImage());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantInformation.class);
                int branch_id = currentItem.getBranchId();
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
        TextView textView_NameProduct;
        TextView textView_DescriptionProduct;
        TextView textView_NameBranch;
        ImageView imageView_ImageProduct;

        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_NameProduct = itemView.findViewById(R.id.TextView_NameProduct_ProductWithCategory);
            textView_DescriptionProduct = itemView.findViewById(R.id.TextView_ProductDescription_ProductWithCategory);
            textView_NameBranch = itemView.findViewById(R.id.TextView_NameBranch_ProductWithCategory);
            imageView_ImageProduct = itemView.findViewById(R.id.ImageView_ImageProduct_ProductWithCategory);
            constraintLayout = itemView.findViewById(R.id.ConstraintLayout_ProductCategory);
        }
    }
}
