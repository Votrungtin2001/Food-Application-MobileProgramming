package com.example.foodapplication.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapplication.R;
import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {

    ImageView imageView;
    TextView textView_ItemName;
    TextView textView_ItemDescription;
    String image;
    String item_Name, item_Description;
    ImageView imageView_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusAndNavigation();
        setContentView(R.layout.activity_details);

        initComponents();
        Run();

    }

    public void initComponents() {
        imageView = findViewById(R.id.ItemCollection_Image);
        textView_ItemName = findViewById(R.id.ItemCollection_Name);
        textView_ItemDescription = findViewById(R.id.ItemCollection_Description);
        imageView_Back = findViewById(R.id.ItemCollection_Back);
    }

    public void Run() {
        imageView_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image = getIntent().getStringExtra("image");
        item_Name = getIntent().getStringExtra("name");
        item_Description = getIntent().getStringExtra("description");

        if(image.trim().equals("")){
            imageView.setImageResource(R.drawable.noimage_restaurant);
        }else {
            Picasso.get ().load (image)
                    .placeholder(R.drawable.noimage_restaurant)
                    .error(R.drawable.error)
                    .into(imageView);
        }
        textView_ItemName.setText(item_Name);
        textView_ItemDescription.setText(item_Description);
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