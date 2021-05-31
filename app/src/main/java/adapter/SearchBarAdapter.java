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
import com.example.foodapplication.RestaurantInformation;

import java.util.List;

import models.SearchBarModel;

public class SearchBarAdapter extends RecyclerView.Adapter<SearchBarAdapter.ViewHolder> {

    List<SearchBarModel> itemList;
    LayoutInflater inflater;
    Context context;

    public SearchBarAdapter(Context ctx, List<SearchBarModel> ItemList){
        this.context = ctx;
        itemList = ItemList;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_layout_item_search_bar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchBarModel currentItem = itemList.get(position);

        holder.textView.setText(currentItem.getText());
        holder.imageView.setImageBitmap(currentItem.getImage());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantInformation.class);
                intent.putExtra("Branch ID", currentItem.getBranch_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView_ItemSeachBar);
            imageView = itemView.findViewById(R.id.imageView_ItemSeachBar);
            constraintLayout = itemView.findViewById(R.id.ConstraintLayout_SearchBar);
        }
    }

    public void filterList(List<SearchBarModel> filteredList) {
        itemList = filteredList;
        notifyDataSetChanged();
    }
}
