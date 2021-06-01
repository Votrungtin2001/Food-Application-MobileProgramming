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

import com.example.foodapplication.Item_Collection;
import com.example.foodapplication.R;
import com.example.foodapplication.RestaurantInformation;

import java.util.List;

import models.AllRestaurantModel;
import models.CollectionModel;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    List<AllRestaurantModel> itemList;
    Context context;

    public RestaurantListAdapter(List<AllRestaurantModel> ItemList, Context context){
        this.itemList = ItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_gridlayout_item_restaurantlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllRestaurantModel currentItem = itemList.get(position);
        holder.name_branch.setText(currentItem.getNameBranch());
        holder.address_branch.setText(currentItem.getAddressBranch());
        holder.linearIcon.setImageBitmap(currentItem.getImage());
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
        TextView name_branch;
        TextView address_branch;
        ImageView linearIcon;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name_branch = itemView.findViewById(R.id.ItemRestaurantList_NameBranch);
            address_branch = itemView.findViewById(R.id.ItemRestaurantList_AddressBranch);
            linearIcon = itemView.findViewById(R.id.ImageView_ItemRestaurantList);
            constraintLayout = itemView.findViewById(R.id.ItemRestaurantList_ConstraintLayout);
        }
    }
}
