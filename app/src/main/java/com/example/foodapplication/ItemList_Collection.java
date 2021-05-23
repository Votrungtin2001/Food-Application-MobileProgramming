package com.example.foodapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import models.CollectionModel;

public class ItemList_Collection extends AppCompatActivity {

    RecyclerView recyclerView_ItemListCollection;
    List<CollectionModel> collectionModels;
    RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list__collection);

        recyclerView_ItemListCollection = findViewById(R.id.ItemListCollection_recyclerView);
        collectionModels = new ArrayList<>();
        adapter = new ItemListCollectionAdapter(collectionModels, this);
        AddDataForCollection();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView_ItemListCollection.setLayoutManager(gridLayoutManager);
        recyclerView_ItemListCollection.setAdapter(adapter);
    }

    public void AddDataForCollection()
    {
        collectionModels.add(new CollectionModel(R.drawable.banh_mi_0d, "Bánh Mì 0Đ"));
        collectionModels.add(new CollectionModel(R.drawable.bay_ngay_tien_trieu_ve_tui, "7 Ngày Review - \nTiền Triệu Về Túi"));
        collectionModels.add(new CollectionModel(R.drawable.chi_5k, "Chi 5K - \nƯu Đãi Freeship 15k"));
        collectionModels.add(new CollectionModel(R.drawable.cuoi_tuan_freeship, "Cuối Tuần \nFree Ship"));
        collectionModels.add(new CollectionModel(R.drawable.day_don_ngay_le, "Đón Lễ Lớn"));
        collectionModels.add(new CollectionModel(R.drawable.deal_nua_gia_quan_gan_nha, "Deal Nửa Giá - \nQuán Gần Nhà"));
        collectionModels.add(new CollectionModel(R.drawable.deal_xin, "Deal Xịn - \n Giảm Tới 70k"));
        collectionModels.add(new CollectionModel(R.drawable.di_het_viet_nam, "Đi Hết Việt Nam - \nFreeship"));
        collectionModels.add(new CollectionModel(R.drawable.he_xinh_tiec_xin, "Hè Xinh - \nTiệc Xịn 55k"));
        collectionModels.add(new CollectionModel(R.drawable.le_to_deal_xin_xo, "Lễ To - \nDeal Xịn Xò"));

    }
}