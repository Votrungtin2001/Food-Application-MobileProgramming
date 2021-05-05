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

public class ViewHistoryAdapter extends RecyclerView.Adapter<ViewHistoryAdapter.ViewHolder> {

    List<String> titles1;
    List<String> titles2;
    List<Integer> images;
    LayoutInflater inflater;

    public ViewHistoryAdapter(Context ctx, List<String> sTitles1, List<String> sTitles2, List<Integer> sImages){
        this.titles1 = sTitles1;
        this.titles2 = sTitles2;
        this.images = sImages;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_viewhistory_constraint_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHistoryAdapter.ViewHolder holder, int position) {
        holder.title1.setText(titles1.get(position));
        holder.title2.setText(titles2.get(position));
        holder.constraintIcon.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return titles1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title1;
        TextView title2;
        ImageView constraintIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title1 = itemView.findViewById(R.id.textView_NameRestaurant);
            title2 = itemView.findViewById(R.id.textView_ViewHistory);
            constraintIcon = itemView.findViewById(R.id.imageView_ViewHistory);
        }
    }
}
