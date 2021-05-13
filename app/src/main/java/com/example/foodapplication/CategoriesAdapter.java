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

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    List<String> name_Restaurant;
    List<String> value_Rating;
    List<String> value_Time;
    List<String> value_Distance;
    List<String> name_Voucher;
    List<Integer> images;
    LayoutInflater inflater;

    public CategoriesAdapter(Context ctx, List<String> sTitles1, List<String> sTitles2, List<String> sTitles3, List<String> sTitles4, List<String> sTitles5, List<Integer> sImages){
        this.name_Restaurant = sTitles1;
        this.value_Rating= sTitles2;
        this.value_Time = sTitles3;
        this.value_Distance = sTitles4;
        this.name_Voucher = sTitles5;
        this.images = sImages;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_categories_constraint_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView_NameRestaurant.setText(name_Restaurant.get(position));
        holder.textView_Rating.setText(value_Rating.get(position));
        holder.textView_Time.setText(value_Time.get(position));
        holder.textView_Distance.setText((value_Distance.get(position)));
        holder.textView_NameVoucher.setText(name_Voucher.get(position));
        holder.constraintIcon.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return name_Restaurant.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_NameRestaurant;
        TextView textView_Rating;
        TextView textView_Time;
        TextView textView_Distance;
        TextView textView_NameVoucher;
        ImageView constraintIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_NameRestaurant = itemView.findViewById(R.id.Categories_NameRestaurant);
            textView_Rating = itemView.findViewById(R.id.Categories_Rating);
            textView_Time = itemView.findViewById(R.id.Categories_Time);
            textView_Distance = itemView.findViewById(R.id.Categories_Distance);
            textView_NameVoucher = itemView.findViewById(R.id.Categories_NameVoucher);
            constraintIcon = itemView.findViewById(R.id.imageView_Categories);
        }
    }
}
