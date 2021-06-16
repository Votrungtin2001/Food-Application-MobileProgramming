package com.example.foodapplication.Order;

public class OrderModel {

    public int OrderId, OrderStatus,Total;
    public String Phone, Address;


    public OrderModel(int orderId,int orderStatus,int total,String phone){
        OrderId = orderId;
        OrderStatus =orderStatus;
        Total = total;
        Phone = phone;
    }

    public int getTotal() {
        return Total;
    }

    public int getOrderId() {
        return OrderId;
    }

    public int getOrderStatus() {
        return OrderStatus;
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

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}