package com.example.foodapplication.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

import com.example.foodapplication.HomeFragment.adapter.ProductWithCategoryAdapter;

import com.example.foodapplication.databaseHelper.DatabaseHelper;
import com.example.foodapplication.HomeFragment.model.ProductCategoryModel;
import com.example.foodapplication.R;

public class ProductWithCategory extends AppCompatActivity {

    private RecyclerView recyclerView_ProductWithCategory;
    private ProductWithCategoryAdapter productWithCategoryAdapter;
    private List<ProductCategoryModel> productCategoryModelList;
    private int category_id;
    private int district_id;
    private String category_name;

    ImageView imageView;
    TextView textView;

    SQLiteDatabase db;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_product_with_category);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

        initComponents();

        Run();
    }

    public void initComponents() {
        textView = findViewById(R.id.textView_Category);
        imageView = findViewById(R.id.Back_Category);
        recyclerView_ProductWithCategory = findViewById(R.id.ProductWithCategory_RecyclerView);
    }

    public void Run() {
        category_id = getIntent().getIntExtra("Category ID", 0);
        district_id = getIntent().getIntExtra("District ID", 0);

        category_name = getNameCategory(category_id);

        textView.setText(category_name);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        productCategoryModelList = new ArrayList<>();
        getAllProducts(category_id, district_id);
        productWithCategoryAdapter = new ProductWithCategoryAdapter(this, productCategoryModelList);
        LinearLayoutManager linearLayoutManager_Category = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_ProductWithCategory.setLayoutManager(linearLayoutManager_Category);
        recyclerView_ProductWithCategory.setAdapter(productWithCategoryAdapter);
    }

    public void getAllProducts(int id, int district) {
        String selectQuery = "SELECT P.Image, P.Name, P.Description, B.NAME, B._id " +
                "FROM ((((RESTAURANT R JOIN BRANCHES B ON R._id = B.Restaurant) " +
                "JOIN MENU M ON R._id = M.Restaurant) JOIN PRODUCTS P ON M.Product = P._id) " +
                "JOIN CATEGORIES C ON P.Category = C._id) " +
                "JOIN ADDRESS A ON B.Address = A._id " +
                "WHERE C._id ='" + id + "' AND A.District ='" + district + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                byte[] img_byte = cursor.getBlob(cursor.getColumnIndex("Image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
                String name_product = cursor.getString(cursor.getColumnIndex("Name"));
                String description_product = cursor.getString(cursor.getColumnIndex("Description"));
                String branch_name = cursor.getString(cursor.getColumnIndex("NAME"));
                int branch_id = cursor.getInt(cursor.getColumnIndex("_id"));
                ProductCategoryModel productCategoryModel = new ProductCategoryModel(bitmap, name_product, description_product, branch_name, branch_id);
                productCategoryModelList.add(productCategoryModel);

            } while (cursor.moveToNext());

        }
        cursor.close();
    }

    public String getNameCategory(int id) {
        String name = "";
        String selectQuery = "SELECT Description FROM CATEGORIES WHERE _id='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                name = cursor.getString(cursor.getColumnIndex("Description"));
            } while (cursor.moveToNext());

        }
        cursor.close();
        return name;
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