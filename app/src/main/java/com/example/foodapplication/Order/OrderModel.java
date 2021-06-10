package com.example.foodapplication.Order;

public class OrderModel {

    public int OrderId, Quantity, Price, ProductId;
    public String ProductName;


    public OrderModel(String productName, int quantity, int price){
        ProductName = productName;
        Quantity = quantity;
        Price = price;
    }

    public OrderModel(int orderId, String productName, int quantity, int price){
        OrderId = orderId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
    }

    public int getOrderId() { return OrderId; }
    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getProductName() {
        return ProductName;
    }
    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getQuantity() {
        return Quantity;
    }
    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getPrice() {return Price;}
    public void setPrice(int price) {
        Price = price;
    }
}