package models;

import java.util.Date;
import java.util.List;

import static com.example.foodapplication.HomeFragment.adapter.MenuAdapter.productModelList;

public class Request {
    Date DateTime;
    int CustomerId, AddressId, Total, Status;
    List<ProductModel> listCart = productModelList;

    public Request(Date dateTime,int customerId,int addressId ,int total,int status){
        DateTime = dateTime;
        CustomerId = customerId;
        AddressId = addressId;
        Total = total;
        Status = status;
    }

    public Request(List<ProductModel> listCart){
        this.listCart = listCart;
    }

    public Date getDateTime() {
        return DateTime;
    }

    public void setDateTime(Date dateTime) {
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

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }
}
