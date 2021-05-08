package com.example.foodapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecentlyEatenAdapter extends RecyclerView.Adapter<RecentlyEatenAdapter.ViewHolder> {

    List<String> titles1;
    List<String> titles2;
    List<Integer> images;
    LayoutInflater inflater;

    public RecentlyEatenAdapter(Context ctx, List<String> sTitles1, List<String> sTitles2, List<Integer> sImages){
        this.titles1 = sTitles1;
        this.titles2 = sTitles2;
        this.images = sImages;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_recentlyeaten_linear_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title1.setText(titles1.get(position));
        holder.title2.setText(titles2.get(position));
        holder.relativeIcon.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return titles1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1;
        TextView title2;
        ImageView relativeIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title1 = itemView.findViewById(R.id.TextView_NameRestaurant);
            title2 = itemView.findViewById(R.id.TextView_NameVoucher);
            relativeIcon = itemView.findViewById(R.id.ImageView_RecentlyEaten);
        }
    }
}
