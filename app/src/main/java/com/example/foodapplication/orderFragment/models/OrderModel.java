package com.example.foodapplication.orderFragment.models;

public class OrderModel {

    public int OrderId, Total;
    public String Phone, Address;


    public OrderModel(int orderId,int total,String phone,String address){
        OrderId = orderId;
        Total = total;
        Phone = phone;
        Address = address;
    }

    public int getTotal() {
        return Total;
    }

    public int getOrderId() {
        return OrderId;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}