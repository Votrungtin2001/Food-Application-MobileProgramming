package com.example.foodapplication.orderFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.R;
import com.example.foodapplication.orderFragment.adapter.OrderDetailAdapter;
import com.example.foodapplication.orderFragment.models.OrderDetailModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodapplication.mySQL.MySQLQuerry.GetOrderDetailsWithOrderID;

public class OrderDetailFragment extends Fragment {

    private RecyclerView recyclerView_OrderDetail;
    private OrderDetailAdapter menuAdapter;
    private List<OrderDetailModel> orderDetailModelList;
    private final String TAG = "OrderDetailsFragment";

    private int orderId;

    public OrderDetailFragment() {
    }

    public OrderDetailFragment(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Chi tiết đơn hàng");
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(null, 0);
        });

        initComponents(view);
        Run();

        return view;
    }

    public void initComponents(View view) {
        recyclerView_OrderDetail = view.findViewById(R.id.listOrdersDetail);
    }

    public void Run() {
        orderDetailModelList = new ArrayList<>();
        menuAdapter = new OrderDetailAdapter(getActivity(), orderDetailModelList);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        GetOrderDetailsWithOrderID(orderId, orderDetailModelList, menuAdapter, progressDialog, TAG, getActivity());

        LinearLayoutManager linearLayoutManager_Menu = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_OrderDetail.setLayoutManager(linearLayoutManager_Menu);
        recyclerView_OrderDetail.setAdapter(menuAdapter);
    }

}