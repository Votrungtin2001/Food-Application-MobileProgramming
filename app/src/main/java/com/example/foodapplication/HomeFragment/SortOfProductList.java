package com.example.foodapplication.HomeFragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.HomeFragment.adapter.SortOfProductAdapter;
import com.example.foodapplication.HomeFragment.model.SortOfProductModel;
import com.example.foodapplication.R;

public class SortOfProductList extends AppCompatActivity {

    RecyclerView recyclerView_SortOfProductList;
    List<SortOfProductModel> sortOfProductModelList;
    RecyclerView.Adapter adapter;
    TextView title;
    ImageView back;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_sort_of_product_list);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

        title = findViewById(R.id.SortOfProduct_Title);
        back = findViewById(R.id.Back_SortOfProduct);

        recyclerView_SortOfProductList = findViewById(R.id.SortOfProduct_RecyclerView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int district_id = getIntent().getIntExtra("District ID", 0);
        String name_activity = getIntent().getStringExtra("Name Activity");

        if (name_activity.trim().equals("Discount Combo Product")) {
            title.setText("Combo Ưu Đãi");
            sortOfProductModelList = new ArrayList<>();
            adapter = new SortOfProductAdapter(this, sortOfProductModelList);
            AddDataForDiscountComboProduct(district_id);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            recyclerView_SortOfProductList.setLayoutManager(gridLayoutManager);
            recyclerView_SortOfProductList.setAdapter(adapter);
        }
        else if (name_activity.trim().equals("Cheapest Product")) {
            title.setText("Giá Rẻ Bất Ngờ");
            sortOfProductModelList = new ArrayList<>();
            adapter = new SortOfProductAdapter(this, sortOfProductModelList);
            AddDataForCheapestProduct(district_id);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            recyclerView_SortOfProductList.setLayoutManager(gridLayoutManager);
            recyclerView_SortOfProductList.setAdapter(adapter);
        }
    }

    public void AddDataForDiscountComboProduct(int id) {
        String selectQuery = "SELECT B._id, P.Image, P.Name, B.NAME, M.Price " +
                "FROM (((PRODUCTS P JOIN MENU M ON P._id = M.Product) " +
                "JOIN RESTAURANT R ON M.Restaurant = R._id) " +
                "JOIN BRANCHES B ON R._id = B.Restaurant) " +
                "JOIN ADDRESS A ON B.Address = A._id " +
                "WHERE A.District ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                    String key = "Combo";
                    String name_product = cursor.getString(cursor.getColumnIndex("Name"));
                    if (name_product.toLowerCase().contains(key.toLowerCase())) {
                        byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                        int branch_id = cursor.getInt(cursor.getColumnIndex("_id"));
                        Bitmap img_bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                        String name_branch = cursor.getString(cursor.getColumnIndex("NAME"));
                        int price = cursor.getInt(cursor.getColumnIndex("Price"));
                        SortOfProductModel sortOfProductModel = new SortOfProductModel(img_bitmap, name_product, name_branch, price, branch_id);
                        sortOfProductModelList.add(sortOfProductModel);
                    }
            } while (cursor.moveToNext());

        }
        cursor.close();
    }

    public void AddDataForCheapestProduct(int id) {
        String selectQuery = "SELECT B._id, P.Image, P.Name, B.NAME, M.Price " +
                "FROM (((PRODUCTS P JOIN MENU M ON P._id = M.Product) " +
                "JOIN RESTAURANT R ON M.Restaurant = R._id) " +
                "JOIN BRANCHES B ON R._id = B.Restaurant) " +
                "JOIN ADDRESS A ON B.Address = A._id  " +
                "WHERE M.Price <= 20000 AND M.Price >= 15000 AND P.Category != 4 AND P.Category != 12 AND A.District ='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String name_product = cursor.getString(cursor.getColumnIndex("Name"));
                byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                int branch_id = cursor.getInt(cursor.getColumnIndex("_id"));
                Bitmap img_bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                String name_branch = cursor.getString(cursor.getColumnIndex("NAME"));
                int price = cursor.getInt(cursor.getColumnIndex("Price"));
                SortOfProductModel sortOfProductModel = new SortOfProductModel(img_bitmap, name_product, name_branch, price, branch_id);
                sortOfProductModelList.add(sortOfProductModel);
            } while (cursor.moveToNext());

        }
        cursor.close();
    }
    private void transparentStatusAndNavigation()
    {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        //Change status bar text to black when screen is light white
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}