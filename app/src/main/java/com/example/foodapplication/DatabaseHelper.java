package com.example.foodapplication;

import android.app.admin.FactoryResetProtectionPolicy;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    // REMEMBER TO ADD 1 TO THIS CONSTANT WHEN YOU MAKE ANY CHANGES TO THE CONTRACT CLASS!
    public static final int DATABASE_VERSION = 6;

    public DatabaseHelper(Context context) {
        super(context, FoodManagementContract.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String s : FoodManagementContract.SQL_CREATE_TABLE_ARRAY)
            db.execSQL(s);


    }

    /* note: this snippet is copied directly from developer.android.com and is meant to be used for online caches;
    may need a more efficient way to upgrade the database schema should changes happen. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String s : FoodManagementContract.SQL_DELETE_TABLE_ARRAY)
            db.execSQL(s);
        onCreate(db);
    }

    public void addCustomer(String name, int city_id, String phone, String email, String fb, String user, String pass, int gender, Date DoB, String job) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCustomer.KEY_NAME, name);
        values.put(FoodManagementContract.CCustomer.KEY_CITY, city_id);
        values.put(FoodManagementContract.CCustomer.KEY_PHONE, phone);
        values.put(FoodManagementContract.CCustomer.KEY_EMAIL, email);
        values.put(FoodManagementContract.CCustomer.KEY_FACEBOOK, fb);
        values.put(FoodManagementContract.CCustomer.KEY_USERNAME, user);
        values.put(FoodManagementContract.CCustomer.KEY_PASSWORD, pass);
        values.put(FoodManagementContract.CCustomer.KEY_GENDER, gender);
        values.put(FoodManagementContract.CCustomer.KEY_DOB, DoB.toString());
        values.put(FoodManagementContract.CCustomer.KEY_OCCUPATION, job);
        values.put(FoodManagementContract.CCustomer.KEY_CREDITS, 0);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CCustomer.TABLE_NAME, null, values);
        db.close();
    }

    public void updCustomer(int id, String name, int city_id, String phone, String email, String fb, String user, String pass, int gender, Date DoB, String job) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCustomer.KEY_NAME, name);
        values.put(FoodManagementContract.CCustomer.KEY_CITY, city_id);
        values.put(FoodManagementContract.CCustomer.KEY_PHONE, phone);
        values.put(FoodManagementContract.CCustomer.KEY_EMAIL, email);
        values.put(FoodManagementContract.CCustomer.KEY_FACEBOOK, fb);
        values.put(FoodManagementContract.CCustomer.KEY_USERNAME, user);
        values.put(FoodManagementContract.CCustomer.KEY_PASSWORD, pass);
        values.put(FoodManagementContract.CCustomer.KEY_GENDER, gender);
        values.put(FoodManagementContract.CCustomer.KEY_DOB, DoB.toString());
        values.put(FoodManagementContract.CCustomer.KEY_OCCUPATION, job);

        String selection = FoodManagementContract.CCustomer._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CCustomer.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void addCity(String name) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCity.KEY_NAME, name);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CCity.TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getCities() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CCity.TABLE_NAME, null, null, null, null, null, null);
        db.close();
        return cursor;
    }

    public void addAddress(String address, int city_id, int floor, int gate, int label_id) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, address);
        values.put(FoodManagementContract.CAddress.KEY_CITY, city_id);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, floor);
        values.put(FoodManagementContract.CAddress.KEY_GATE, gate);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, label_id);

        SQLiteDatabase db= this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);
        db.close();
    }

    public void updAddress(int id, String address, int city_id, int floor, int gate, int label_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, address);
        values.put(FoodManagementContract.CAddress.KEY_CITY, city_id);
        values.put(FoodManagementContract.CAddress.KEY_GATE, gate);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, floor);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, label_id);

        String selection = FoodManagementContract.CAddress._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CAddress.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void delAddress(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CAddress._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CAddress.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public Cursor getAddress() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CAddress.TABLE_NAME, null, null, null, null, null, null);
        db.close();
        return cursor;
    }

    public void addAddressLabel(String type) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CAddressLabel.KEY_TYPE, type);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CAddressLabel.TABLE_NAME, null, values);
        db.close();
    }

    public void updAddressLabel(int id, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CAddressLabel.KEY_TYPE, type);

        String selection = FoodManagementContract.CAddressLabel._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CAddressLabel.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void delAddressLabel(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CAddressLabel._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CAddressLabel.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public Cursor getAddressLabel() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CAddressLabel.TABLE_NAME, null, null, null, null, null, null);
        db.close();
        return cursor;
    }

    public void addCustomerAddress(int customer_id, int address_id) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCustomerAddress.KEY_CUSTOMER, customer_id);
        values.put(FoodManagementContract.CCustomerAddress.KEY_ADDRESS, address_id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CCustomerAddress.TABLE_NAME, null, values);
        db.close();
    }

    public void updCustomerAddress(int id, int customer_id, int address_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCustomerAddress.KEY_CUSTOMER, customer_id);
        values.put(FoodManagementContract.CCustomerAddress.KEY_ADDRESS, address_id);

        String selection = FoodManagementContract.CCustomerAddress._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CCustomerAddress.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void delCustomerAddress(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CCustomerAddress._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CCustomerAddress.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public Cursor getCustomerAddress() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CCustomerAddress.TABLE_NAME, null, null, null, null, null, null);
        db.close();
        return cursor;
    }

    public Cursor getRestaurant() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CRestaurant.TABLE_NAME, null, null, null, null, null, null);
        db.close();
        return cursor;
    }

    public Cursor getBranch() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CBranch.TABLE_NAME, null, null, null, null, null, null);
        db.close();
        return cursor;
    }

    public Cursor getCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CCategory.TABLE_NAME, null, null, null, null, null, null);
        db.close();
        return cursor;
    }

    public void addProduct(String name, int category_id) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, name);
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, category_id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getProduct() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CProduct.TABLE_NAME, null, null, null, null, null, null);
        db.close();
        return cursor;
    }

    public void addMenu(int res_id, int prod_id, String desc, int price) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, res_id);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, prod_id);
        values.put(FoodManagementContract.CMenu.KEY_DESC, desc);
        values.put(FoodManagementContract.CMenu.KEY_PRICE, price);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);
        db.close();
    }

    public void updMenu(int id, int res_id, int prod_id, String desc, int price) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, res_id);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, prod_id);
        values.put(FoodManagementContract.CMenu.KEY_DESC, desc);
        values.put(FoodManagementContract.CMenu.KEY_PRICE, price);

        String selection = FoodManagementContract.CMenu._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CMenu.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void delMenu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CMenu._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CMenu.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public Cursor getMenu(int res_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.CMenu.KEY_RESTAURANT + " = ?";
        String[] selectionArgs = { Integer.toString(res_id) };
        String[] columns = { FoodManagementContract.CMenu.KEY_PRODUCT, FoodManagementContract.CMenu.KEY_DESC, FoodManagementContract.CMenu.KEY_PRICE};

        Cursor cursor = db.query(FoodManagementContract.CMenu.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        db.close();
        return cursor;
    }

    public void addOrder(Date date, int customer_id, int delivery_id, int address_id, int total) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.COrder.KEY_DATETIME, date.toString());
        values.put(FoodManagementContract.COrder.KEY_CUSTOMER, customer_id);
        values.put(FoodManagementContract.COrder.KEY_DELIVERY, delivery_id);
        values.put(FoodManagementContract.COrder.KEY_ADDRESS, address_id);
        values.put(FoodManagementContract.COrder.KEY_TOTAL, total);
        values.put(FoodManagementContract.COrder.KEY_STATUS, 0);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.COrder.TABLE_NAME, null, values);
        db.close();
    }

    public long addOrder(Date date, int customer_id) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.COrder.KEY_DATETIME, date.toString());
        values.put(FoodManagementContract.COrder.KEY_CUSTOMER, customer_id);
        values.put(FoodManagementContract.COrder.KEY_STATUS, 0);
        values.put(FoodManagementContract.COrder.KEY_STATUS, 0);

        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insertOrThrow(FoodManagementContract.COrder.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public void updOrderStatus(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.COrder.KEY_STATUS, 1);

        String selection = FoodManagementContract.CMenu._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        db.update(FoodManagementContract.COrder.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void updOrderTotal(int id, int total) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.COrder.KEY_TOTAL, total);

        String selection = FoodManagementContract.CMenu._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        db.update(FoodManagementContract.COrder.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void delOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.COrder._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.COrder.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public Cursor getOrder(int customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.COrder.KEY_CUSTOMER + " = ?";
        String[] selectionArgs = { Integer.toString(customer_id) };

        Cursor cursor = db.query(FoodManagementContract.COrder.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        db.close();
        return cursor;
    }

    public void addOrderDetail(int order_id, int item_id, int quantity, int price) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.COrderDetails.KEY_ORDER, order_id);
        values.put(FoodManagementContract.COrderDetails.KEY_MENUITEM, item_id);
        values.put(FoodManagementContract.COrderDetails.KEY_QUANTITY, quantity);
        values.put(FoodManagementContract.COrderDetails.KEY_PRICE, price);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.COrderDetails.TABLE_NAME, null, values);
        db.close();
    }

    public void updOrderDetail(int order_id, int item_id, int quantity, int price) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.COrderDetails.KEY_ORDER, order_id);
        values.put(FoodManagementContract.COrderDetails.KEY_MENUITEM, item_id);
        values.put(FoodManagementContract.COrderDetails.KEY_QUANTITY, quantity);
        values.put(FoodManagementContract.COrderDetails.KEY_PRICE, price);

        String selection = FoodManagementContract.COrderDetails.KEY_ORDER + " = ? "
                + "AND " + FoodManagementContract.COrderDetails.KEY_MENUITEM + " = ?";
        String[] selectionArgs = { Integer.toString(order_id), Integer.toString(item_id) };
        db.update(FoodManagementContract.COrderDetails.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void delOrderDetail(int order_id, int item_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.COrderDetails.KEY_ORDER + " = ? "
                + "AND " + FoodManagementContract.COrderDetails.KEY_MENUITEM + " = ?";
        String[] selectionArgs = { Integer.toString(order_id), Integer.toString(item_id) };
        db.delete(FoodManagementContract.COrderDetails.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public void delOrderDetail(int order_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.COrderDetails.KEY_ORDER + " = ? ";
        String[] selectionArgs = { Integer.toString(order_id)};
        db.delete(FoodManagementContract.COrderDetails.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public Cursor getOrderDetail(int order_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.COrderDetails.KEY_ORDER + " = ?";
        String[] selectionArgs = { Integer.toString(order_id) };

        Cursor cursor = db.query(FoodManagementContract.COrderDetails.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        db.close();
        return cursor;
    }

    public Cursor getOrderDetail(int order_id, int item_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.COrderDetails.KEY_ORDER + " = ? "
                + "AND " + FoodManagementContract.COrderDetails.KEY_MENUITEM + " = ?";
        String[] selectionArgs = { Integer.toString(order_id), Integer.toString(item_id) };

        Cursor cursor = db.query(FoodManagementContract.COrderDetails.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        db.close();
        return cursor;
    }

    public int calcOrderTotal(int order_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.COrderDetails.KEY_ORDER + " = ?";
        String[] selectionArgs = { Integer.toString(order_id) };
        Cursor cursor = db.query(FoodManagementContract.COrderDetails.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        int total = 0;
        while(cursor.moveToNext()) {
            total += cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.COrderDetails.KEY_PRICE));
        }

        return total;
    }
}
