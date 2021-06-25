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

import com.example.foodapplication.HomeFragment.ProductWithCategory;
import com.example.foodapplication.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<String> titles;
    List<Integer> images;
    LayoutInflater inflater;
    Context context;
    int district_id;
    boolean district_isAvailable;

    public CategoryAdapter(Context ctx, List<String> sTitles, List<Integer> sImages, int District_ID, boolean Sign){
        this.context = ctx;
        this.titles = sTitles;
        this.images = sImages;
        this.inflater = LayoutInflater.from(ctx);
        this.district_id = District_ID;
        this.district_isAvailable = Sign;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_gridlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.gridIcon.setImageResource(images.get(position));
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(district_isAvailable == true) {
                    int category_id = -1;
                    Intent intent = new Intent(context, ProductWithCategory.class);
                    switch (position) {
                        case 0:
                            category_id = 10;
                            break;

                        case 1:
                            category_id = 4;
                            break;

                        case 2:
                            category_id = 9;
                            break;

                        case 3:
                            category_id = 2;
                            break;

                        case 4:
                            category_id = 8;
                            break;

                        case 5:
                            category_id = 1;
                            break;

                        case 6:
                            category_id = 14;
                            break;

                        case 7:
                            category_id = 3;
                            break;

                        case 8:
                            category_id = 11;
                            break;

                        case 9:
                            category_id = 5;
                            break;

                        case 10:
                            category_id = 12;
                            break;

                        case 11:
                            category_id = 6;
                            break;

                        case 12:
                            category_id = 15;
                            break;

                        case 13:
                            category_id = 19;
                            break;

                        case 14:
                            category_id = 17;
                            break;

                        case 15:
                            category_id = 16;
                            break;


                    }
                    intent.putExtra("Category ID", category_id);
                    intent.putExtra("District ID", district_id);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView gridIcon;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.option_textView);
            gridIcon = itemView.findViewById(R.id.option_imageView);
            constraintLayout = itemView.findViewById(R.id.ConstraintLayout_List);
        }
    }
}
