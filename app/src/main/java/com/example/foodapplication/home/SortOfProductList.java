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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.foodapplication.home.adapter.SortOfProductAdapter;
import com.example.foodapplication.home.model.SortOfProductModel;
import com.example.foodapplication.R;

import static com.example.foodapplication.mySQL.MySQLQuerry.GetDataForCheapestProduct;
import static com.example.foodapplication.mySQL.MySQLQuerry.GetDataForDiscountComboProduct;

public class SortOfProductList extends AppCompatActivity {

    RecyclerView recyclerView_SortOfProductList;
    List<SortOfProductModel> sortOfProductModelList;
    SortOfProductAdapter adapter;
    TextView title;
    ImageView back;

    private static final String TAG = "SortOfProductList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_sort_of_product_list);

        initComponents();

        Run();
    }

    private void initComponents() {
        title = findViewById(R.id.SortOfProduct_Title);
        back = findViewById(R.id.Back_SortOfProduct);

        recyclerView_SortOfProductList = findViewById(R.id.SortOfProduct_RecyclerView);
    }

    private void Run() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int district_id = getIntent().getIntExtra("District ID", 0);
        String name_activity = getIntent().getStringExtra("Name Activity");

        if (name_activity.trim().equals("Discount Combo Product")) {
            title.setText("Combo ??u ????i");
            sortOfProductModelList = new ArrayList<>();
            adapter = new SortOfProductAdapter(this, sortOfProductModelList);
            GetDataForDiscountComboProduct(district_id, sortOfProductModelList, adapter, TAG, this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            recyclerView_SortOfProductList.setLayoutManager(gridLayoutManager);
            recyclerView_SortOfProductList.setAdapter(adapter);
        }
        else if (name_activity.trim().equals("Cheapest Product")) {
            title.setText("Gi?? R??? B???t Ng???");
            sortOfProductModelList = new ArrayList<>();
            adapter = new SortOfProductAdapter(this, sortOfProductModelList);
            GetDataForCheapestProduct(district_id, sortOfProductModelList, adapter, TAG, this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            recyclerView_SortOfProductList.setLayoutManager(gridLayoutManager);
            recyclerView_SortOfProductList.setAdapter(adapter);
        }
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