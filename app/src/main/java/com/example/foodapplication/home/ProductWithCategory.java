package com.example.foodapplication.home;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.R;
import com.example.foodapplication.home.adapter.ProductWithCategoryAdapter;
import com.example.foodapplication.home.model.ProductCategoryModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodapplication.mySQL.MySQLQuerry.GetCategoryName;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetProductsWithCategory;

public class ProductWithCategory extends AppCompatActivity {

    private RecyclerView recyclerView_ProductWithCategory;
    private ProductWithCategoryAdapter productWithCategoryAdapter;
    private List<ProductCategoryModel> productCategoryModelList;
    private int category_id;
    private int district_id;
    private String category_name = "";

    ImageView imageView;
    TextView textView;

    private static final String TAG = "ProductWithCategory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_product_with_category);

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

        textView.setText(category_name);
        GetCategoryName(category_id, textView, TAG, this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        productCategoryModelList = new ArrayList<>();
        productWithCategoryAdapter = new ProductWithCategoryAdapter(this, productCategoryModelList);
        GetProductsWithCategory(category_id, district_id, productCategoryModelList, productWithCategoryAdapter, TAG, this);
        LinearLayoutManager linearLayoutManager_Category = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_ProductWithCategory.setLayoutManager(linearLayoutManager_Category);
        recyclerView_ProductWithCategory.setAdapter(productWithCategoryAdapter);
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