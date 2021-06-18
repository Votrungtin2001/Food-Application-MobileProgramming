package adapter;

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

import models.KindOfRestaurantModel;

public class KindOfRestaurantAdapter extends RecyclerView.Adapter<KindOfRestaurantAdapter.ViewHolder> {

    Context context;
    List<KindOfRestaurantModel> itemList;
    LayoutInflater inflater;

    public KindOfRestaurantAdapter(Context ctx, List<KindOfRestaurantModel> List) {
        this.context = ctx;
        this.itemList = List;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_kindofrestaurant_constraint_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KindOfRestaurantModel currentItem = itemList.get(position);
        holder.image.setImageBitmap(currentItem.getImage());
        holder.name_branch.setText(currentItem.getNameBranch());
        holder.address_branch.setText(currentItem.getAddressBranch());
        holder.openingtime_restaurant.setText(currentItem.getOpeningTime());
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
        ImageView image;
        TextView name_branch;
        TextView address_branch;
        TextView openingtime_restaurant;
        ConstraintLayout constraintLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView_KindOfRestaurant);
            name_branch = itemView.findViewById(R.id.KindOfRestaurant_NameBranch);
            address_branch = itemView.findViewById(R.id.KindOfRestaurant_Address);
            openingtime_restaurant = itemView.findViewById(R.id.KindOfRestaurant_OpeningTime);
            constraintLayout = itemView.findViewById(R.id.ConstraintLayout_KindOfRestaurant);
        }
    }
}
