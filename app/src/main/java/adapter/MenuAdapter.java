package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.R;

import java.util.ArrayList;
import java.util.List;

import models.ProductModel;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    List<ProductModel> itemList;
    Context context;
    LayoutInflater inflater;

    public static List<ProductModel> productModelList = new ArrayList<>();


    public MenuAdapter(Context ctx, List<ProductModel> ItemList) {
        this.context = ctx;
        this.itemList = ItemList;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_layout_item_menu, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel currentItem = itemList.get(position);

        holder.textView_NameProduct.setText(currentItem.getNameProduct());
        holder.textView_DescriptionProduct.setText(currentItem.getDescriptionProduct());
        holder.textView_ValueOfSell.setText(currentItem.getValueOfSell());
        String price = Integer.toString(currentItem.getPrice());
        holder.textView_Price.setText(price);
        holder.imageView_ImageProduct.setImageBitmap(currentItem.getImage());

        holder.imageView_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productModelList.add(new ProductModel(currentItem.getNameProduct(),currentItem.getQuantity(),currentItem.getPrice(),currentItem.getProduct_id()));
                Toast.makeText(context,"Thêm vào giỏ hàng thành công!",Toast.LENGTH_SHORT).show();
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
        TextView textView_ValueOfSell;
        TextView textView_Price;
        ImageView imageView_ImageProduct;

        ImageView imageView_addItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           textView_NameProduct = itemView.findViewById(R.id.TextView_NameProduct_Menu);
           textView_DescriptionProduct = itemView.findViewById(R.id.TextView_ProductDescription_Menu);
           textView_ValueOfSell = itemView.findViewById(R.id.TextView_ValueOfSell_Menu);
           textView_Price = itemView.findViewById(R.id.TextView_PriceProduct_Menu);
           imageView_ImageProduct = itemView.findViewById(R.id.ImageView_ImageProduct_Menu);
           imageView_addItem = itemView.findViewById(R.id.ImageView_Plus_Menu);
        }
    }


}
