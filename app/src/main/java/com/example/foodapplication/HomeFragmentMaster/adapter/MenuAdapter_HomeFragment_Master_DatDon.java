package com.example.foodapplication.HomeFragmentMaster.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.foodapplication.HomeFragment.model.ProductModel;

public class MenuAdapter_HomeFragment_Master_DatDon extends RecyclerView.Adapter<MenuAdapter_HomeFragment_Master_DatDon.ViewHolder> {

    List<ProductModel> itemList;
    Context context;
    LayoutInflater inflater;

    Dialog AnnouncementDialog;

    private final String TAG = "MenuAdapterHFMDD";

    public MenuAdapter_HomeFragment_Master_DatDon(Context ctx, List<ProductModel> ItemList) {
        this.context = ctx;
        this.itemList = ItemList;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_menu_homefragment_master, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel currentItem = itemList.get(position);

        holder.textView_NameProduct.setText(currentItem.getNameProduct());
        holder.textView_DescriptionProduct.setText(currentItem.getDescriptionProduct());
        holder.textView_ValueOfSell.setText(currentItem.getValueOfSell());
        DecimalFormat decimalFormat = new DecimalFormat( "###,###,###" );
        holder.textView_Price.setText(decimalFormat.format(currentItem.getPrice()) + " đ");

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
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                int product_id = currentItem.getProduct_id();
                int menu_id = currentItem.getMenu_id();
                DeleteProductAndMenu(product_id, menu_id, position, progressDialog);

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

    private void DeleteProductAndMenu(int product_id, int menu_id, int position, ProgressDialog progressDialog) {
        String url = "https://foodapplicationmobile.000webhostapp.com/delProductAndMenu.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "Đã xóa thành công!!!";
                    itemList.remove(position);
                    notifyDataSetChanged();
                }
                else announcement = "Xóa món thất bại!!!";
                Toast.makeText(context, announcement, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("product_id", String.valueOf(product_id));
                params.put("menu_id", String.valueOf(menu_id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
