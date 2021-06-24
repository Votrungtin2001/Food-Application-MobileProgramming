package com.example.foodapplication.HomeFragmentMaster.adapter;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import models.ProductModel;

public class MenuAdapter_HomeFragment_Master_DatDon extends RecyclerView.Adapter<MenuAdapter_HomeFragment_Master_DatDon.ViewHolder> {

    List<ProductModel> itemList;
    Context context;
    LayoutInflater inflater;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    Dialog AnnouncementDialog;

    public MenuAdapter_HomeFragment_Master_DatDon(Context ctx, List<ProductModel> ItemList) {
        this.context = ctx;
        this.itemList = ItemList;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_menu_homefragment_master, parent, false);

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getReadableDatabase();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel currentItem = itemList.get(position);

        holder.textView_NameProduct.setText(currentItem.getNameProduct());
        holder.textView_DescriptionProduct.setText(currentItem.getDescriptionProduct());
        holder.textView_ValueOfSell.setText(currentItem.getValueOfSell());
        String price = Double.toString(currentItem.getPrice());
        holder.textView_Price.setText(price);

        if(currentItem.getImage().isEmpty()){
            holder.imageView_ImageProduct.setImageResource(R.drawable.noimage_product);
        }else {
            Picasso.get ().load ( currentItem.getImage () )
                    .placeholder(R.drawable.noimage_product)
                    .error(R.drawable.error)
                    .into(holder.imageView_ImageProduct);
        }

        holder.imageView_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int product_id = currentItem.getProduct_id();
                int menu_id = getMenuID(product_id);
                itemList.remove(currentItem);
                notifyDataSetChanged();
                databaseHelper.delMenu(menu_id);
                databaseHelper.delProduct(product_id);
                ShowPopDeleteSuccesfully();

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
        ImageView imageView_Delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_NameProduct = itemView.findViewById(R.id.TextView_NameProduct_Menu_HomeFragment_Master);
            textView_DescriptionProduct = itemView.findViewById(R.id.TextView_ProductDescription_Menu_HomeFragment_Master);
            textView_ValueOfSell = itemView.findViewById(R.id.TextView_ValueOfSell_Menu_HomeFragment_Master);
            textView_Price = itemView.findViewById(R.id.TextView_PriceProduct_Menu_HomeFragment_Master);
            imageView_ImageProduct = itemView.findViewById(R.id.ImageView_ImageProduct_Menu_HomeFragment_Master);
            imageView_Delete = itemView.findViewById(R.id.ImageView_Delete_Menu_HomeFragment_Master);
            AnnouncementDialog = new Dialog(context);
            AnnouncementDialog.setContentView(R.layout.custom_popup_require_login);
        }
    }

    public int getMenuID(int product_id) {
        int id = -1;
        String selectQuery = "SELECT * FROM MENU WHERE Product ='" + product_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex("_id"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return id;
    }

    public void ShowPopDeleteSuccesfully() {
        TextView textView_Close;
        textView_Close = (TextView) AnnouncementDialog.findViewById(R.id.Close_PopUpLogin);
        textView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnnouncementDialog.dismiss();
            }
        });

        TextView textView_Text;
        textView_Text = (TextView) AnnouncementDialog.findViewById(R.id.TextView_PopUp_RequireLogin);
        textView_Text.setText("Bạn đã xóa món thành công!!!");

        AnnouncementDialog.show();
    }
}
