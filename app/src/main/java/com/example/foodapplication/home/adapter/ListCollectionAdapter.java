package com.example.foodapplication.home.adapter;

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

import com.example.foodapplication.home.Details;
import com.example.foodapplication.R;

import java.util.List;

import com.example.foodapplication.home.model.CollectionModel;
import com.squareup.picasso.Picasso;

public class ListCollectionAdapter extends RecyclerView.Adapter<ListCollectionAdapter.ViewHolder> {

    List<CollectionModel> my_list;
    Context context;

    public ListCollectionAdapter(List<CollectionModel> my_list, Context context){
        this.my_list = my_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ListCollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_itemlistcollection_grid_layout, parent, false);
        return new ListCollectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListCollectionAdapter.ViewHolder holder, int position) {
        CollectionModel collectionModel = my_list.get(position);
        holder.title.setText(collectionModel.getName());
        if(collectionModel.getImage().isEmpty()){
            holder.linearIcon.setImageResource(R.drawable.noimage_voucher);
        }else {
            Picasso.get ().load ( collectionModel.getImage () )
                    .placeholder(R.drawable.noimage_voucher)
                    .error(R.drawable.error)
                    .into(holder.linearIcon);
        };
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sDescription;
                Intent intent = new Intent(context, Details.class);
                intent.putExtra("image", collectionModel.getImage());
                intent.putExtra("name", collectionModel.getName());
                intent.putExtra("description", collectionModel.getDescription());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView linearIcon;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.ItemListCollection_NameVoucher);
            linearIcon = itemView.findViewById(R.id.imageView_ItemListCollection);
            constraintLayout = itemView.findViewById(R.id.ItemListCollection_ConstraintLayout);
        }
    }
}
