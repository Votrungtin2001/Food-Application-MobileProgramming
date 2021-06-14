package adapter;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.R;

import java.util.ArrayList;
import java.util.List;

import models.ProductModel;

import static com.example.foodapplication.MainActivity.customer_id;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    List<ProductModel> itemList;
    Context context;
    LayoutInflater inflater;

    Dialog AnnouncementDialog;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

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
        String price = Integer.toString(currentItem.getPrice());
        holder.textView_Price.setText(price);

        holder.imageView_ImageProduct.setImageBitmap(currentItem.getImage());

        holder.imageView_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = currentItem.getQuantity();
                if (customer_id > 0) {
                    boolean checkCustomerHasAddress = CheckCustomerHasAddress(customer_id);
                    if(checkCustomerHasAddress == true ) {
                        productModelList.add(new ProductModel(currentItem.getNameProduct(), qty, currentItem.getPrice(),currentItem.getProduct_id()));
                        Toast.makeText(context, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ShowPopUpRequireAddress();
                    }

                }
                else ShowPopUpRequireLogin();
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

            AnnouncementDialog = new Dialog(context);
            AnnouncementDialog.setContentView(R.layout.custom_popup_require_login);
        }
    }

    public void ShowPopUpRequireLogin() {
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
        textView_Text.setText("Vui lòng đăng nhập với vai trò là khách hàng!!!");

        AnnouncementDialog.show();
    }

    public void ShowPopUpRequireAddress() {
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
        textView_Text.setText("Vui lòng thêm địa chỉ giao hàng!!!   ");

        AnnouncementDialog.show();
    }

    public boolean checkProduct(int product_id) {
        int count = 0;

        String selectQuery = "SELECT * FROM PRODUCT WHERE _id = '" + product_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

            } while (cursor.moveToNext());
        }
        count = cursor.getCount();
        cursor.close();
        if(count > 0) return true;
        else return false;
    }
        public boolean CheckCustomerHasAddress(int customer_id) {
            int count = 0;

            String selectQuery = "SELECT * FROM CUSTOMER_ADDRESS WHERE Customer ='" + customer_id + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                do {

                } while (cursor.moveToNext());
            }
            count = cursor.getCount();
            cursor.close();
            if(count > 0) return true;
            else return false;
        }
}
