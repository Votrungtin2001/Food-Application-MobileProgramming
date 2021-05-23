package com.example.foodapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Item_Collection extends AppCompatActivity {

    ImageView imageView;
    TextView textView_ItemName;
    TextView textView_ItemDescription;
    int image;
    String item_Name, item_Description;
    ImageView imageView_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item__collection);

        imageView = findViewById(R.id.ItemCollection_Image);
        textView_ItemName = findViewById(R.id.ItemCollection_Name);
        textView_ItemDescription = findViewById(R.id.ItemCollection_Description);
        imageView_Back = findViewById(R.id.ItemCollection_Back);

        imageView_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image = getIntent().getIntExtra("image", 0);
        item_Name = getIntent().getStringExtra("name");
        item_Description = getIntent().getStringExtra("description");

        imageView.setImageResource(image);
        textView_ItemName.setText(item_Name);
        textView_ItemDescription.setText(item_Description);


    }
}