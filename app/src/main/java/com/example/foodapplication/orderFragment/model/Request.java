package com.example.foodapplication.orderFragment.model;

import com.example.foodapplication.HomeFragment.model.ProductModel;

import java.util.Date;
import java.util.List;

import static com.example.foodapplication.HomeFragment.adapter.MenuAdapter.productModelList;

public class Request {
    String DateTime;
    int CustomerId, AddressId, Status;
    double Total;
    List<ProductModel> listCart = productModelList;

    public Request(String dateTime,int customerId,int addressId ,double total,int status){
        DateTime = dateTime;
        CustomerId = customerId;
        AddressId = addressId;
        Total = total;
        Status = status;
    }

    public Request(List<ProductModel> listCart){
        this.listCart = listCart;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public int getAddressId() {
        return AddressId;
    }

    public void setAddressId(int addressId) {
        AddressId = addressId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }
}
