package com.example.foodapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    // REMEMBER TO ADD 1 TO THIS CONSTANT WHEN YOU MAKE ANY CHANGES TO THE CONTRACT CLASS!
    public static final int DATABASE_VERSION = 43;
    private static String DB_PATH= "data/data/com.example.foodapplication/databases/";
    private static String DB_NAME = "foodapp";
    private final Context context;
    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, FoodManagementContract.DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String s : FoodManagementContract.SQL_CREATE_TABLE_ARRAY)
            db.execSQL(s);
        initRestaurants_Addresses_Branches_Products_Menu(db);

    }

    /* note: this snippet is copied directly from developer.android.com and is meant to be used for online caches;
    may need a more efficient way to upgrade the database schema should changes happen. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String s : FoodManagementContract.SQL_DELETE_TABLE_ARRAY)
            db.execSQL(s);
        onCreate(db);
    }

    /*public void createDB() throws IOException {

        this.getReadableDatabase();
        Log.i("Readable ends.......","end");

        try {
            copyDB();
            Log.i("copy db ends..........","end");

        } catch (IOException e) {

            throw new Error("Error copying database");
        }
    }

    public void copyDB() throws IOException {
        try {
            Log.i("inside copyDB.......","start");

            InputStream ip =  context.getAssets().open(DB_NAME + ".db");
            Log.i("Input Stream....",ip+"");
            String op=  DB_PATH  +  FoodManagementContract.DATABASE_NAME ;
            OutputStream output = new FileOutputStream( op);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ip.read(buffer))>0){
                output.write(buffer, 0, length);
                Log.i("Content.... ",length+"");
            }
            output.flush();
            output.close();
            ip.close();
        }
        catch (IOException e) {
            Log.v("error", e.toString());
        }
    }

    public void openDB() throws SQLException {

        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Log.i("open DB......",db.toString());
    }*/

//    public user getData(int id, String name, String username, String email, String age, String amount, String password){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from users where email='" + email + "'", null);
//        res.moveToFirst();
//        while (res.isAfterLast() == false) {
//            user response = new user(id, name, username, email, age, amount, password);
//            response.email = res.getString(res.getColumnIndex(Table_Column_2_Email));
//            response.name = res.getString(res.getColumnIndex(Table_Column_1_Name));
//            response.username = res.getString(res.getColumnIndex(Table_Column_1_Username));
//            response.age = res.getString(res.getColumnIndex(Table_Column_3_Age));
//            response.amount = res.getString(res.getColumnIndex(Table_Column_3_Amount));
//            return response;
//        }
//        return null;
//    }
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
    }

    public void addMaster(String name, String phone, String email, String fb, String user, String pass) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CMaster.KEY_NAME, name);
        values.put(FoodManagementContract.CMaster.KEY_PHONE, phone);
        values.put(FoodManagementContract.CMaster.KEY_EMAIL, email);
        values.put(FoodManagementContract.CMaster.KEY_FACEBOOK, fb);
        values.put(FoodManagementContract.CMaster.KEY_USERNAME, user);
        values.put(FoodManagementContract.CMaster.KEY_PASSWORD, pass);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CMaster.TABLE_NAME, null, values);
    }

    public void updMaster(int id, String name, String phone, String email, String fb, String user, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CMaster.KEY_NAME, name);
        values.put(FoodManagementContract.CMaster.KEY_PHONE, phone);
        values.put(FoodManagementContract.CMaster.KEY_EMAIL, email);
        values.put(FoodManagementContract.CMaster.KEY_FACEBOOK, fb);
        values.put(FoodManagementContract.CMaster.KEY_USERNAME, user);
        values.put(FoodManagementContract.CMaster.KEY_PASSWORD, pass);

        String selection = FoodManagementContract.CMaster._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CMaster.TABLE_NAME, values, selection, selectionArgs);
    }

    public void addDelivery(String name, String address, String phone, String email, String fb, String user, String pass, String license) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CDelivery.KEY_NAME, name);
        values.put(FoodManagementContract.CDelivery.KEY_ADDRESS, address);
        values.put(FoodManagementContract.CDelivery.KEY_PHONE, phone);
        values.put(FoodManagementContract.CDelivery.KEY_EMAIL, email);
        values.put(FoodManagementContract.CDelivery.KEY_FACEBOOK, fb);
        values.put(FoodManagementContract.CDelivery.KEY_USERNAME, user);
        values.put(FoodManagementContract.CDelivery.KEY_PASSWORD, pass);
        values.put(FoodManagementContract.CDelivery.KEY_LICENSE, license);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CDelivery.TABLE_NAME, null, values);
    }

    public void updDelivery(int id, String name, String address, String phone, String email, String fb, String user, String pass, String license) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CDelivery.KEY_NAME, name);
        values.put(FoodManagementContract.CDelivery.KEY_ADDRESS, address);
        values.put(FoodManagementContract.CDelivery.KEY_PHONE, phone);
        values.put(FoodManagementContract.CDelivery.KEY_EMAIL, email);
        values.put(FoodManagementContract.CDelivery.KEY_FACEBOOK, fb);
        values.put(FoodManagementContract.CDelivery.KEY_USERNAME, user);
        values.put(FoodManagementContract.CDelivery.KEY_PASSWORD, pass);
        values.put(FoodManagementContract.CDelivery.KEY_LICENSE, license);

        String selection = FoodManagementContract.CDelivery._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CDelivery.TABLE_NAME, values, selection, selectionArgs);
    }

    public void addDistrict(String name, int city_id) {
        ContentValues values = new ContentValues();

        values.put(FoodManagementContract.CDistrict.KEY_NAME, name);
        values.put(FoodManagementContract.CDistrict.KEY_CITY, city_id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CDistrict.TABLE_NAME, null, values);
    }

    public Cursor getDistricts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CDistrict.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void addCity(String name) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCity.KEY_NAME, name);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CCity.TABLE_NAME, null, values);
    }

    public Cursor getCities() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CCity.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void addAddress(String address, int district_id, int city_id, int floor, int gate, int label_id) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, address);
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, district_id);
        values.put(FoodManagementContract.CAddress.KEY_CITY, city_id);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, floor);
        values.put(FoodManagementContract.CAddress.KEY_GATE, gate);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, label_id);

        SQLiteDatabase db= this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);
    }

    public void updAddress(int id, String address, int district_id, int city_id, int floor, int gate, int label_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, address);
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, district_id);
        values.put(FoodManagementContract.CAddress.KEY_CITY, city_id);
        values.put(FoodManagementContract.CAddress.KEY_GATE, gate);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, floor);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, label_id);

        String selection = FoodManagementContract.CAddress._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CAddress.TABLE_NAME, values, selection, selectionArgs);
    }

    public void delAddress(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CAddress._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CAddress.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getAddress() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CAddress.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void addAddressLabel(String type) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CAddressLabel.KEY_TYPE, type);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CAddressLabel.TABLE_NAME, null, values);
    }

    public void updAddressLabel(int id, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CAddressLabel.KEY_TYPE, type);

        String selection = FoodManagementContract.CAddressLabel._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CAddressLabel.TABLE_NAME, values, selection, selectionArgs);
    }

    public void delAddressLabel(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CAddressLabel._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CAddressLabel.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getAddressLabel() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CAddressLabel.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void addCustomerAddress(int customer_id, int address_id) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCustomerAddress.KEY_CUSTOMER, customer_id);
        values.put(FoodManagementContract.CCustomerAddress.KEY_ADDRESS, address_id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CCustomerAddress.TABLE_NAME, null, values);
    }

    public void updCustomerAddress(int id, int customer_id, int address_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCustomerAddress.KEY_CUSTOMER, customer_id);
        values.put(FoodManagementContract.CCustomerAddress.KEY_ADDRESS, address_id);

        String selection = FoodManagementContract.CCustomerAddress._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CCustomerAddress.TABLE_NAME, values, selection, selectionArgs);
    }

    public void delCustomerAddress(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CCustomerAddress._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CCustomerAddress.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getCustomerAddress() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CCustomerAddress.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void addRestaurant(String name, String opening_time, byte[] image) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, name);
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, opening_time);
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, image);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);
    }

    public Cursor getRestaurant() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CRestaurant.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor getRestaurant(int res_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.CRestaurant._ID + " = ?";
        String[] selectionArgs = { Integer.toString(res_id) };

        Cursor cursor = db.query(FoodManagementContract.CRestaurant.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        return cursor;
    }

    public void updRestaurant(int id, String name, String opening_time, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, name);
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, opening_time);
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, image);

        String selection = FoodManagementContract.CRestaurant._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CRestaurant.TABLE_NAME, values, selection, selectionArgs);
    }

    public void delRestaurant(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CRestaurant._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CRestaurant.TABLE_NAME, selection, selectionArgs);
    }

    public void addBranch (String name, int restaurant_id, int address_id, int master_id) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, name);
        values.put(FoodManagementContract.CBranch.KEY_PARENT, restaurant_id);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, address_id);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, master_id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);
    }

    public Cursor getBranch() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CBranch.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void delBranch(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CBranch._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CBranch.TABLE_NAME, selection, selectionArgs);
    }

    public void updBranch(int id, String name, int restaurant_id, int address_id, int master_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, name);
        values.put(FoodManagementContract.CBranch.KEY_PARENT, restaurant_id);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, address_id);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, master_id);

        String selection = FoodManagementContract.CBranch._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CBranch.TABLE_NAME, values, selection, selectionArgs);
    }

    public Cursor getCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CCategory.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void addCategory(String name, String description) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCategory.KEY_NAME, name);
        values.put(FoodManagementContract.CCategory.KEY_DESC, description);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CCategory.TABLE_NAME, null, values);
    }

    public void delCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CCategory._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CCategory.TABLE_NAME, selection, selectionArgs);
    }

    public void addProduct(String name, String description, int category_id, byte[] image) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, name);
        values.put(FoodManagementContract.CProduct.KEY_DESC, description);
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, category_id);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, image);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);
    }

    public Cursor getProduct() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FoodManagementContract.CProduct.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void delProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CProduct._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CProduct.TABLE_NAME, selection, selectionArgs);
    }

    public void updProduct(int id, String name, String description, int category_id, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, name);
        values.put(FoodManagementContract.CProduct.KEY_DESC, description);
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, category_id);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, image);

        String selection = FoodManagementContract.CProduct._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.update(FoodManagementContract.CProduct.TABLE_NAME, values, selection, selectionArgs);
    }

    public void addMenu(int res_id, int prod_id, String desc, int price) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, res_id);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, prod_id);
        values.put(FoodManagementContract.CMenu.KEY_DESC, desc);
        values.put(FoodManagementContract.CMenu.KEY_PRICE, price);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);
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
    }

    public void delMenu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CMenu._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.CMenu.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getMenu(int res_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.CMenu.KEY_RESTAURANT + " = ?";
        String[] selectionArgs = { Integer.toString(res_id) };
        String[] columns = { FoodManagementContract.CMenu.KEY_PRODUCT, FoodManagementContract.CMenu.KEY_DESC, FoodManagementContract.CMenu.KEY_PRICE};

        Cursor cursor = db.query(FoodManagementContract.CMenu.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
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
    }

    public long addOrder(Date date, int customer_id) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.COrder.KEY_DATETIME, date.toString());
        values.put(FoodManagementContract.COrder.KEY_CUSTOMER, customer_id);
        values.put(FoodManagementContract.COrder.KEY_STATUS, 0);
        values.put(FoodManagementContract.COrder.KEY_STATUS, 0);

        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insertOrThrow(FoodManagementContract.COrder.TABLE_NAME, null, values);
        return id;
    }

    public void updOrderStatus(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.COrder.KEY_STATUS, 1);

        String selection = FoodManagementContract.CMenu._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        db.update(FoodManagementContract.COrder.TABLE_NAME, values, selection, selectionArgs);
    }

    public void updOrderTotal(int id, int total) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.COrder.KEY_TOTAL, total);

        String selection = FoodManagementContract.CMenu._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        db.update(FoodManagementContract.COrder.TABLE_NAME, values, selection, selectionArgs);
    }

    public void delOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.COrder._ID + " = ?";
        String[] selectionArgs = { Integer.toString(id) };
        db.delete(FoodManagementContract.COrder.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getOrder(int customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.COrder.KEY_CUSTOMER + " = ?";
        String[] selectionArgs = { Integer.toString(customer_id) };

        Cursor cursor = db.query(FoodManagementContract.COrder.TABLE_NAME, null, selection, selectionArgs, null, null, null);
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
    }

    public void delOrderDetail(int order_id, int item_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.COrderDetails.KEY_ORDER + " = ? "
                + "AND " + FoodManagementContract.COrderDetails.KEY_MENUITEM + " = ?";
        String[] selectionArgs = { Integer.toString(order_id), Integer.toString(item_id) };
        db.delete(FoodManagementContract.COrderDetails.TABLE_NAME, selection, selectionArgs);
    }

    public void delOrderDetail(int order_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.COrderDetails.KEY_ORDER + " = ? ";
        String[] selectionArgs = { Integer.toString(order_id)};
        db.delete(FoodManagementContract.COrderDetails.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getOrderDetail(int order_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.COrderDetails.KEY_ORDER + " = ?";
        String[] selectionArgs = { Integer.toString(order_id) };

        Cursor cursor = db.query(FoodManagementContract.COrderDetails.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        return cursor;
    }

    public Cursor getOrderDetail(int order_id, int item_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.COrderDetails.KEY_ORDER + " = ? "
                + "AND " + FoodManagementContract.COrderDetails.KEY_MENUITEM + " = ?";
        String[] selectionArgs = { Integer.toString(order_id), Integer.toString(item_id) };

        Cursor cursor = db.query(FoodManagementContract.COrderDetails.TABLE_NAME, null, selection, selectionArgs, null, null, null);
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

        cursor.close();
        return total;
    }

    public void addFavorites(int cus_id, int res_id) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CFavorites.KEY_CUSTOMER, cus_id);
        values.put(FoodManagementContract.CFavorites.KEY_RESTAURANT, res_id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CFavorites.TABLE_NAME, null, values);
    }

    public void delFavorites(int cus_id, int res_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FoodManagementContract.CFavorites.KEY_CUSTOMER + " = ? "
                + "AND " + FoodManagementContract.CFavorites.KEY_RESTAURANT + " = ?";
        String[] selectionArgs = { Integer.toString(cus_id), Integer.toString(res_id) };
        db.delete(FoodManagementContract.CFavorites.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getFavorites(int cus_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.CFavorites.KEY_CUSTOMER + " = ?";
        String[] selectionArgs = { Integer.toString(cus_id) };

        Cursor cursor = db.query(FoodManagementContract.CFavorites.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        return cursor;
    }

    public void initRestaurants_Addresses_Branches_Products_Menu(SQLiteDatabase db) {
        // Highlands Coffee
        int img = R.drawable.highlandscoffee;
        Resources resources = context.getResources();
        Drawable drawable = resources.getDrawable(img);
        Bitmap bitmap =  ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Highlands Coffee");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "07:00 - 22:30");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "118 Hoàng Diệu 2, Phường Linh Trung, Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Highlands Coffee - Hoàng Diệu 2");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 1);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 1);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_phindihanhnhan;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "PhinDi Hạnh Nhân");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Iced PHIN Coffee with Almond Milk");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 1);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 50+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_phindichoco;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "PhinDi Choco");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Iced PHIN Coffee with Chocolate");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 2);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_phinsuada;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Phin Sữa Đá");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "PHIN Coffee & Condensed Milk");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 3);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 29000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_phinsuada;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Phin Sữa Đá Đặc Biệt");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Special PHIN Coffee & Condensed Milk");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 4);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 100+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_bacxiuda;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bạc Xỉu Đá");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "White PHIN Coffee & Condensed Milk");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 5);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 29000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_latte;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Latte");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Latte");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 6);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 4 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 55000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_cappuccino;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cappuccino");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cappuccino");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 7);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 5 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 55000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_caramelmacchiato;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Caramel Macchiato");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Caramel Maccchiato");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 8);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 59000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_americano;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Americano");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Americano");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 9);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 4 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 35000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_freezetraxanh;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Freeze Trà Xanh");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Green Tea Freeze");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 2);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 10);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 50+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 49000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_freezechocolate;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Freeze Sô Cô La");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Chocolate Freeze");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 2);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 11);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 49000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_classicfreeze;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Freeze Phin Truyền Thống");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Classic PHIN Freeze");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 2);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 12);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 49000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_trathachdao;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Thạch Đào");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Peach Jelly Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 13);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 50+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_trathachvai;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Thạch Vải");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Tea with Lychee Jelly");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 14);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 50+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_trathanhdao;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Thanh Đào");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Peach Lemongrass Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 15);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_chanhdaxay;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chanh Đá Xay");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Lime Ice-Blended");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 16);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_chanhdaydavien;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chanh Dây Đá Viên");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Iced Passion Fruit Juice");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 17);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 3 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_tacquatdavien;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Tắc/Quất Đá Viên");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Iced Kumquat Juice");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 18);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 5 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_tiramisu;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bánh Tiramisu");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Tiramisu Cake");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 5);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 19);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 29000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_phomaichanhday;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bánh Phô Mai Chanh Dây");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Passion Fruit Cheese Cake");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 5);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 20);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 29000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.highlandscoffee_phomaicaramel;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bánh Phô Mai Caramel");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Caramel Cheese Cake");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 5);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 1);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 21);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 8 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 29000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


        // Jolibee
        img = R.drawable.jollibee;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Jollibee");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "09:00 - 21:30");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "106 Đặng Văn Bi, Phường Bình Thọ, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Jollibee - Đặng Văn Bi");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 2);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 2);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        img = R.drawable.jollibee_khamphamuahe_mi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Khám Phá Mùa Hè - Mì");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Mì Ý + Khoai + Pepsi + Kem Vani + Balo Jollibee");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 7);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 22);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 109000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_khamphamuahe_ga;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Khám Phá Mùa Hè - Gà");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Gà giòn + Khoai + Pepsi + Kem Vani + Balo Jollibee");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 23);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "5 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 109000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_motmienggagionvuive;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "1 Miếng Gà Giòn Vui Vẻ");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Chickenjoy");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 24);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 3 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 30000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_haimienggagionvuive_motkhoaitay_motpepsi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "2 Miếng Gà Giòn Vui Vẻ + 1 Khoai Tây + 1 Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "2 Chickenjoy + 1 French Fries + 1 Softdrink");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 25);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 94000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_motmienggasotcay;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "1 Miếng Gà Sốt Cay");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Chili chicken");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 26);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 4 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 32000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_haimienggasotcay_motkhoaitay_motpepsi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "2 Miếng Gà Sốt Cay + 1 Khoai Tây + 1 Pepsi ");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "2 Chili chicken + 1 French Fries + 1 Softdrink");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 27);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "5 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 94000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_comgasotcay_motsoup_motpepsi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "1 Cơm gà sốt cay + 1 Súp bí đỏ + 1 Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Chilli chicken with rice + 1 Soup + 1 Softdrink");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 28);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 55000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_sandwichthitnuongbbq;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "1 Sandwich thịt nướng BBQ + 1 Khoai Tây + 1 Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Pork Sandwich + 1 French Fries + 1 Softdrink");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 9);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 29);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 65000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


        img = R.drawable.jollibee_sandwichgagionvuive;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "1 Sandwich Gà Giòn Vui Vẻ + 1 Khoai Tây + 1 Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Chickenjoy Sandwich + 1 French Fries + 1 Softdrink");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 9);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 30);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 55000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


        img = R.drawable.jollibee_miysotbobam_motga_motpepsi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "1 Mì ý sốt bò bằm + 1 Miếng Gà Giòn Vui Vẻ + 1 Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Spaghetti + 1 Chickenjoy + 1 Softdrink");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 7);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 31);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 80000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_miysotbobam_motga_motpepsi_motkhoaitay;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "1 Mì ý sốt bò bằm + 1 Miếng Gà Giòn Vui Vẻ + 1 Khoai Tây + 1 Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Spaghetti + 1 Chickenjoy + 1 French Fries + 1 Softdrink");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 7);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 32);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 85000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_tradao;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Đào");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Peach Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 33);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 4 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 20000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_cacaosuada;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cacao Sữa Đá");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Iced Milk Cocoa");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 34);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 5 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 23000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_pepsi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Nước Ngọt");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 35);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 15000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_comtrang;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Trắng");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Rice");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 36);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 5000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_suopbido;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Súp Bí Đỏ");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Soup");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 37);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 15000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.jollibee_khoaitaychien;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Khoai Tây Chiên");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "French Fries");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 2);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 38);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 9 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 25000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


        // Com Tam Phuc Loc Tho
        img = R.drawable.comtamphucloctho;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Cơm Tấm Phúc Lộc Thọ");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "06:00 - 22:00");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "745 Kha Vạn Cân, Phường Linh Chiểu, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Cơm Tấm Phúc Lộc Thọ - Kha Vạn Cân");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 3);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 3);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_combophuc;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Phúc");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cơm Ba Rọi + Ốp La + Canh Rong Biển + Sâm Bí Đao");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 39);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 65000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_comboloc;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Lộc");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cơm Sườn + Chả Hấp + Canh Rong Biển + Sâm Bí Đao");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 40);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 62000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_combotho;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Thọ");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cơm Đùi Gà Nướng Ngũ Vị + Canh Rong Biển + Sâm Bí Đao");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 41);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 5 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 55000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_combophucloctho;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Phúc - Lộc - Thọ");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cơm Sườn + Chả Hấp + Bì Thịt + Canh Rong Biển + Sâm Bí Đao");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 42);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 72000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_comsuon;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Sườn");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cơm Sườn");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 43);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_combaroi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Ba Rọi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cơm Ba Rọi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 44);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 43000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_comganuongnguvi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Đùi Gà Nướng Ngũ Vị");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cơm Đùi Gà Nướng Ngũ Vị");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 45);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_comsuonnon;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Sườn Non");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cơm Sườn Non");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 46);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 65000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_combicha;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Bì Chả");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cơm Bì Chả");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 47);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 29000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_comthem;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Thêm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cơm Thêm");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 48);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 4 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 5000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_opla;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trứng Ốp La");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Trứng Ốp La");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 49);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 10000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_chahap;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chả Hấp");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Chả Hấp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 50);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 13000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_bithit;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bì Thịt");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Bì Thịt");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 51);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 13000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_canhrongbienthitbam;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Canh Rong Biển Thịt Bằm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Canh Rong Biển Thịt Bằm");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 52);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 15000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_canhkhoquanhoithit;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Canh Khổ Qua Nhồi Thịt");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Canh Khổ Qua Nhồi Thịt");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 53);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 16000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_cocacola;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Nước Coca Tươi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Coca Cola");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 54);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 15000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.comtamphucloctho_sambidaohatchia;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sâm Bí Đao Hạt Chia");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Sâm Bí Đao Hạt Chia");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 3);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 55);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 15000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


        // Phuc Long
        img = R.drawable.phuclong;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Phúc Long");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "07:15 - 21:00");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "1012 Kha Vạn Cân, Phường Linh Chiểu, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Phúc Long - Kha Vạn Cân");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 4);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 4);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);



        // KFC
        img = R.drawable.kfc;
        resources = context.getResources();
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "KFC");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "09:00 - 21:50");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "251 Võ Văn Ngân, Phường Linh Chiểu, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "KFC- Võ Văn Ngân");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 5);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 5);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        // The Pizza Company
        img = R.drawable.thepizzacompany;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "The Pizza Company");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "10:00 - 21:45");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "30 Võ Văn Ngân, Phường Trường Thọ, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "The Pizza Company - Võ Văn Ngân");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 6);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 6);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        // Pho 24 - Nguyen Tri Phuong
        img = R.drawable.pho24;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Phở 24");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "06:00 - 21:30");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "75 Hoàng Diệu 2, Phường Linh Trung, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Phở 24 - Hoàng Diệu 2");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 7);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 7);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        // Tuan Bay Keo - Banh Mi- Xoi - Che
        img = R.drawable.tuanbaykeo;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Tuấn 7 Kẹo - Bánh Mì, Xôi & Chè");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "06:30 - 23:00");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "147 Hoàng Diệu 2, Phường Linh Trung, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Tuấn 7 Kẹo - Bánh Mì, Xôi & Chè");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 8);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 8);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        // Mi Quang 3 Anh Em
        img = R.drawable.miquangbaanhem;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Mì Quảng 3 Anh Em");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "06:00 - 23:00");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "497A1 Kha Vạn Cân, Phường Linh Đông, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Mì Quảng 3 Anh Em - Kha Vạn Cân");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 9);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 9);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        // Tra Sua The Alley
        img = R.drawable.thealley;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "The Alley");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "09:00 - 22:30");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "156 Võ Văn Ngân, Phường Bình Thọ, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "The Alley - Võ Văn Ngân");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 10);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 10);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        // Greeci - Nuoc Ep Detox, Sinh to, Tea & Coffee
        img = R.drawable.greeci;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Greeci - Nước Ép Detox, Sinh Tố, Tea & Coffee");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "08:00 - 21:45");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "230 Linh Trung, Phường Linh Trung, Quận Thủ Đức, TP. HCM");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Greeci - Nước Ép Detox, Sinh Tố, Tea & Coffee");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 11);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 11);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        // Ut Nguyen Quan - Bun - Banh Canh
        img = R.drawable.utnguyenquan;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Út Nguyên Quán - Bún & Bánh Canh");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "06:00 - 22:30");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "193 Hoàng Diệu 2, Phường Linh Trung, Quận Thủ Đức, TP. HCM");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Út Nguyên Quán - Bún & Bánh Canh");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 12);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 12);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        // Quan 49 - Bun Bo - Com Tam
        img = R.drawable.quan49;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Quán 49 - Bún & Cơm Tấm");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "06:30 - 19:30");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "49/17 Đường Số 7, Phường Linh Tây, Quận Thủ Đức, Thành Phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Quán 49 - Bún & Cơm Tấm");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 13);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 13);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        // Heo Con – Gà Rán, Hamburger, Sushi
        img = R.drawable.heocon;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Heo Con - Gà Rán, Hamburger & Sushi");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "10:58 - 23:59");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "16 Đường D3, Khu Phố 1, Phường Linh Tây, Quận Thủ Đức, Thành Phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Heo Con - Gà Rán, Hamburger & Sushi");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 14);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 14);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        //Cô Liên – Xôi & Bánh Mì
        img = R.drawable.colien;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Cô Liên - Xôi & Bánh Mì");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "06:00 - 23:59");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "179A Hoàng Diệu 2, Phường Linh Trung, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Cô Liên - Xôi & Bánh Mì");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 15);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 15);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        //Tiệm Chè Phan – Hoàng Gia
        img = R.drawable.tiemchephan;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Tiệm Chè Phan - Hoàng Gia");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "12:15 - 22:30");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "42 Hoàng Diệu 2, Phường Linh Chiểu, Quận Thủ Đức, Thành Phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Tiệm Chè Phan - Hoàng Gia");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 16);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 16);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        //Sushi Buffet Kunimoto – Dân Chủ
        img = R.drawable.sushibuffetkunimoto;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Sushi Buffet Kunimoto");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "10:10 - 21:00");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "107 Dân Chủ, Phường Bình Thọ, Quận Thủ Đức, Thành Phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Sushi Buffet Kunimoto - Dân Chủ");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 17);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 17);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);


        //LALA Salad – Healthy Food Online
        img = R.drawable.lalasalad;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "LALA Salad - Healthy Food Online");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "08:00 - 16:30");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "4 Tô Vĩnh Diện, Phường Linh Chiểu, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "LALA Salad - Healthy Food Online");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 18);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 18);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        //Homemade Fresh Cake - Shop Online
        img = R.drawable.homemadefreshcake;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Homemade Fresh Cake - Shop Online");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "08:00 - 16:30");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "36C/30 Đường Số 16, Phường Linh Trung, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Homemade Fresh Cake - Shop Online");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 19);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 19);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        //Milano Coffee - Sinh Tố, Nước Ép & Đá Xay
        img = R.drawable.millanocoffee;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Milano Coffee - Sinh Tố, Nước Ép & Đá Xay");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "09:00 - 21:30");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "11 Thống Nhất, Phường Bình Thọ, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Milano Coffee - Sinh Tố, Nước Ép & Đá Xay");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 20);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 20);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        //Hapi - Hamburger Bò Teriyaki - Chương Dương
        img = R.drawable.hapi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Hapi - Hamburger Bò Teriyaki - Chương Dương");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "00:00 - 23:59");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "114 Chương Dương, Phường Linh Chiểu, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Hapi - Hamburger Bò Teriyaki - Chương Dương");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 21);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 21);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        //Pizza King - Linh Đông
        img = R.drawable.pizzaking;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Pizza King - Linh Đông");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "08:00 - 21:00");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "187 Linh Đông, Phường Linh Đông, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Pizza King - Linh Đông");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 22);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 22);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        //TocoToco Bubble Tea - Hoàng Diệu 2
        img = R.drawable.tocotoco;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "TocoToco Bubble Tea");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "09:30 - 22:00");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "109 Hoàng Diệu 2, Khu Phố 2, Phường Linh Trung, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "TocoToco Bubble Tea - Hoàng Diệu 2");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 23);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 23);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        //Lotteria - Võ Văn Ngân
        img = R.drawable.lotteria;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Lotteria");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "08:00 - 22:00");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "10 Võ Văn Ngân, Phường Trường Thọ, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Lotteria - Võ Văn Ngân");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 24);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 24);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);

        //Trà Sữa & Pizza H2T - Võ Văn Ngân
        img = R.drawable.h2t;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CRestaurant.KEY_NAME, "Trà Sữa & Pizza H2T");
        values.put(FoodManagementContract.CRestaurant.KEY_OPEN, "10:30 - 21:30");
        values.put(FoodManagementContract.CRestaurant.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CRestaurant.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CAddress.KEY_ADDRESS, "200 Võ Văn Ngân, Phường Bình Thọ, Quận Thủ Đức, Thành phố Hồ Chí Minh");
        values.put(FoodManagementContract.CAddress.KEY_DISTRICT, 14);
        values.put(FoodManagementContract.CAddress.KEY_CITY, 3);
        values.put(FoodManagementContract.CAddress.KEY_FLOOR, 0);
        values.put(FoodManagementContract.CAddress.KEY_GATE, 0);
        values.put(FoodManagementContract.CAddress.KEY_LABEL, 4);
        db.insertOrThrow(FoodManagementContract.CAddress.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CBranch.KEY_NAME, "Trà Sữa & Pizza H2T - Võ Văn Ngân");
        values.put(FoodManagementContract.CBranch.KEY_PARENT, 25);
        values.put(FoodManagementContract.CBranch.KEY_ADDRESS, 25);
        values.put(FoodManagementContract.CBranch.KEY_MASTER, 1);
        db.insertOrThrow(FoodManagementContract.CBranch.TABLE_NAME, null, values);



    }


}
