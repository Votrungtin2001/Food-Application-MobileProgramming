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

public class MaybeYouLikeAdapter extends RecyclerView.Adapter<MaybeYouLikeAdapter.ViewHolder> {

    List<String> titles1;
    List<String> titles2;
    List<String> prices;
    List<String> values;
    List<Integer> images;
    LayoutInflater inflater;

    public MaybeYouLikeAdapter(Context ctx, List<String> sTitles1, List<String> sTitles2, List<String> sPrices, List<String> sValues, List<Integer> sImages){
        this.titles1 = sTitles1;
        this.titles2 = sTitles2;
        this.images = sImages;
        this.prices = sPrices;
        this.values = sValues;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public MaybeYouLikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_maybe_you_like_linear_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaybeYouLikeAdapter.ViewHolder holder, int position) {
        holder.title1.setText(titles1.get(position));
        holder.title2.setText(titles2.get(position));
        holder.price.setText(prices.get(position));
        holder.value.setText(values.get(position));
        holder.relativeIcon.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return titles1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1;
        TextView title2;
        TextView price;
        TextView value;
        ImageView relativeIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title1 = itemView.findViewById(R.id.TextView_NameProduct_MayBeYouLike);
            title2 = itemView.findViewById(R.id.TextView_NameRestaurant_MayBeYouLike);
            price = itemView.findViewById(R.id.TextView_Price_MayBeYouLike);
            value = itemView.findViewById(R.id.TextView_ValueOfLike);
            relativeIcon = itemView.findViewById(R.id.ImageView_MayBeYouLike);
        }
    }
}
