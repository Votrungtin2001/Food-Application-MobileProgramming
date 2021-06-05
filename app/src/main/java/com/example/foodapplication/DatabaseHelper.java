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
import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    // REMEMBER TO ADD 1 TO THIS CONSTANT WHEN YOU MAKE ANY CHANGES TO THE CONTRACT CLASS!
    public static final int DATABASE_VERSION = 45;
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

    public int getCredits(int cus_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.CFavorites.KEY_CUSTOMER + " = ?";
        String[] selectionArgs = { Integer.toString(cus_id) };

        Cursor cursor = db.query(FoodManagementContract.CCustomer.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        int credits = cursor.getInt(cursor.getColumnIndexOrThrow(FoodManagementContract.CCustomer.KEY_CREDITS));
        cursor.close();
        return credits;
    }

    public void updCredits(int cus_id, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCustomer.KEY_CREDITS, amount);

        String selection = FoodManagementContract.CCustomer._ID + " = ?";
        String[] selectionArgs = { Integer.toString(cus_id) };
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

    public Cursor getDistricts(int city_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.CDistrict.KEY_CITY + " = ?";
        String[] selectionArgs = { Integer.toString(city_id) };

        Cursor cursor = db.query(FoodManagementContract.CDistrict.TABLE_NAME, null, selection, selectionArgs, null, null, null);
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

    public long addAddressLabel(String type) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CAddressLabel.KEY_TYPE, type);

        SQLiteDatabase db = this.getWritableDatabase();
        return db.insertOrThrow(FoodManagementContract.CAddressLabel.TABLE_NAME, null, values);
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

    public void addTransaction(int cus_id, int amount) {
        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CTransaction.KEY_CUSTOMER, cus_id);
        values.put(FoodManagementContract.CTransaction.KEY_DATE, (Calendar.getInstance().getTime()).toString());
        values.put(FoodManagementContract.CTransaction.KEY_CREDITS, amount);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertOrThrow(FoodManagementContract.CTransaction.TABLE_NAME, null, values);
    }

    public Cursor getTransactions(int cus_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = FoodManagementContract.CTransaction.KEY_CUSTOMER + " = ?";
        String[] selectionArgs = { Integer.toString(cus_id) };

        Cursor cursor = db.query(FoodManagementContract.CTransaction.TABLE_NAME, null, selection, selectionArgs, null, null, null);
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

        img = R.drawable.phuclong_caphelatte;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cà Phê Latte");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Latte Coffee");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 56);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 4 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 40000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_caphecappuccino;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cà Phê Cappuccino");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cappuccino Coffee");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 57);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 35000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_caphecaramellatte;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cà Phê Caramel Latte");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Caramel Latte Coffee");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 58);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 45000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_caphephucbontuhanhnhandaxay;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cà Phê Phúc Bồn Tử Hạnh Nhân Đá Xay");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Espresso Raspberry Almond");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 2);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 59);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 6 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 65000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_socolacaphedaxay;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sô Cô La Cà Phê Đá Xay");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Choco Lover");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 2);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 60);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 55000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_traxanhdaxay;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Xanh Đá Xay");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Green Tea Ice Blended");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 2);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 61);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 55000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_traolongsua;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Ô Long Sữa");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Oolong Milk Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 62);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 100+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 45000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_trasuaphuclong;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Sữa Phúc Long");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Phuc Long Tea Latte");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 63);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 45000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_hongtrasuaphuclong;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Hồng Trà Sữa Phúc Long");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Phuc Long Black Milk Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 64);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 100+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 40000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_tradaophuclong;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Đào Phúc Long");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Phuc Long Peach Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 65);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 100+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 50000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_travailai;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Vải - Lài");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Lychee Tea (Jasmine)");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 66);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 100+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 45000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_tranhanlai;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Nhãn - Lài");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Longan Tea (Jasmine)");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 67);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 100+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 45000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_buoiep;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bưởi Ép");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Pomelo");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 68);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 9 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 40000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_taoep;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Táo Ép");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Apple");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 69);
        values.put(FoodManagementContract.CMenu.KEY_DESC,  "100+ đã bán | 9 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 40000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_dauep;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Dâp Ép");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Strawberry");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 70);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 40000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


        img = R.drawable.phuclong_socola;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sô Cô La");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Chocolate");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 71);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 40000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_suachuaphucbontudaccam;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sữa Chua Phúc Bồn Tử Đác Cam");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Berry Berry Yogurt");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 72);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 65000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_chocococobrownie;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Choco Coco Brownie");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Choco Coco Brownie");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 5);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 73);
        values.put(FoodManagementContract.CMenu.KEY_DESC,  "100+ đã bán | 3 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 20000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_pannacottastrawberry;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Panna Cotta Strawberry");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Panna Cotta Strawberry");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 5);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 74);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 5 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 38000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.phuclong_tiramisupax;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Tiramisu Pax");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Tiramisu Pax");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 5);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 4);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 75);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 47000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


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

        img = R.drawable.kfc_combogarana;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Gà Rán A");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "2 Miếng Gà + 1 Lon Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 76);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 79000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_combogaranb;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Gà Rán B");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "3 Miếng Hot Wings + 1 Khoai Tây Chiên + 1 Lon Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 77);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 79000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_combogaranc;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Gà Rán C");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Miếng Gà + 1 Burger Tôm + 1 Lon Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 78);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 85000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_combonhoma;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Nhóm A");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "2 Miếng Gà + 1 Burger Tôm + 2 Lon Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 79);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 129000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_combonhomb;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Nhóm B");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "3 Miếng Gà + 1 Khoai Tây Chiên + 2 Lon Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 80);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 149000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_combonhomc;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Nhóm C");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "4 Miếng Gà + 1 Khoai Tây Chiên + 2 Lon Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 81);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 185000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


        img = R.drawable.kfc_garanmotmieng;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Gà Rán (1 Miếng)");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Miếng Gà");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 82);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 8 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 68000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_garanhaimieng;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Gà Rán (2 Miếng)");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "2 Miếng Gà");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 83);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 68000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_garanbamieng;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Gà Rán (3 Miếng)");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "3 Miếng Gà");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 84);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 99000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_gaquaymotmieng;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Gà Quay (1 Miếng)");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Miếng Gà Quay");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 85);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 4 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 68000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_hotwingsbamieng;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Gà Hot Wings (3 Miếng)");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "3 Miếng KFC Hot Wings");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 86);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 7 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 49000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_comgagioncay;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Gà Giòn Cay");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Phần Cơm + 1 Miếng Gà Giòn Cay");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 87);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 6 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 41000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_comgagionkhongcay;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Gà Giòn Không Cay");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Phần Cơm + 1 Miếng Gà Giòn Không Cay");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 88);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 6 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 41000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_burgertom;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Burger Tôm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Burger Tôm");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 9);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 89);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 9 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 42000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_burgerzinger;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Burger Zinger");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Burger Zinger");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 9);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 90);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 9 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 51000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_banhtrungmotcai;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bánh Trứng (1 Cái)");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Bánh Trứng");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 91);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 8 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 17000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_khoaitaychien;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Khoai Tây Chiên");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Khoai Tây Chiên");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 92);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 27000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_khoaitaynghien;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Khoai Tây Nghiền");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Khoai Tây Nghiền");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 93);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 9 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 22000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_bapcaitron;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bắp Cải Trộn");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Bắp Cải Trộn");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 94);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 22000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_popcorn;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Popcorn");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Popcorn");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 95);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 57000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_pepsi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Pepsi Lon");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Lon Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 96);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 17000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.kfc_tradao;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Đào");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Ly Trà Đào");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 5);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 97);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 3 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 24000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


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

        img = R.drawable.thepizzacompany_pizzaaloha;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Pizza Aloha");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Thịt nguội, xúc xích tiêu cay và dứa hòa quyện với sốt Thousand Island");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 15);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 98);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 199000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_pizzaganuongdua;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Pizza Gà Nướng Dứa");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Thịt gà mang vị ngọt của dứa kết hợp với vị cay nóng của ớt");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 15);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 99);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 179000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_pizzahaisancaocap;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Pizza Hải Sản Cao Cấp");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Tôm, cua, mực và nghêu với sốt Marinara");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 15);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 100);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 209000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_pizzahaisanpestoxanh;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Pizza Hải Sản Pesto Xanh");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Tôm, cua, mực và bông cải xanh tươi ngon trên nền sốt Pesto Xanh");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 15);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 101);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 249000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_pizzathitxongkhoi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Pizza Thịt Xông Khói");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Thịt giăm bông, thịt xông khói và hai loại rau của ớt xanh, cà chua");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 15);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 102);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 199000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_pizzatomcocktail;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Pizza Tôm Cocktail");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Tôm với nấm, dứa, cà chua và sốt Thousand Island.");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 15);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 103);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "7 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 209000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_pizzaxucxichy;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Pizza Xúc Xích Ý");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Xúc xích cay kiểu Ý trên nền sốt cà chua");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 15);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 104);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 179000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_comcanhganuongbbqsupraucu;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Cánh Gà Nướng BBQ & Súp Rau Củ");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Món cơm truyền thống với cánh gà nướng BBQ và súp rau củ");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 105);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "8 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 69000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_comduigatambotchiengionsupraucu;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Cánh Gà Tẩm Chiên Giòn & Súp Rau Củ");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cánh gà chiên giòn kết hợp với cơm và súp rau củ");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 106);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "3 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 69000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_canhganuongbbqsaumieng;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cánh Gà Nướng BBQ (6 Miếng)");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cánh gà nướng thấm vị với lớp da mỏng giòn");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 107);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 129000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_gagionkhongxuong;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Gà Giòn Không Xương");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Gà giòn tan với sốt Cocktail thơm ngậy");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 6);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 108);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 99000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_miythitbobam;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mì Ý Thịt Bò Bằm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Sốt thịt bò bằm đặc trưng kết hợp cùng mỳ Ý");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 7);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 109);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 129000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_miytomsotkemccachua;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mì Ý Tôm Sốt Kem Cà Chua");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Sự tươi ngon của tôm kết hợp với sốt kem cà chua");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 7);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 110);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 129000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_miycaythitxongkhoi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mì Ý Cay Với Thịt Xông Khối");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Mỳ Ý cay nồng và ngậy hương thơm của thịt xông khói");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 7);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 111);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 99000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_saladgagionkhongxuong;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Salad Gà Giòn Không Xương");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Salad Gà giòn với trứng cút, thịt xông khói, phô mai parmesan và sốt Thousand Island");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 16);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 112);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 69000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_saladtronsotcaesar;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Salad Trộn Sốt Caesar");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Rau tươi trộn với sốt Caesar");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 16);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 113);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 8 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 69000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_giokhoaitaychien;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Giỏ Khoai Tây Chiên");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Sự kết hợp của nhiều kiểu chế biến khoai tây");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 114);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 5 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 79000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_banhkepnuongmexico;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bánh Kẹp Nướng Mêxicô");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Phô mai, sốt cà chua, nhân gà nướng bơ tỏi, ớt sừng dùng kèm sốt cocktail");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 115);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 109000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_pepsi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Pepsi Lon");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Lon Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 116);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 29000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thepizzacompany_strongbow;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Strongbow Các Loại");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Red Berries, Honey, Dark Fruit, ...");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 6);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 117);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 49000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


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

        img = R.drawable.pho24_phodacbiettovua;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Phở Tô Vừa (R) + Thức Uống");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Phở Tô Vừa + 1 Coca Cola Light");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 118);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "3 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 68000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_phodacbiettolon;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Phở Tô Lớn (L) + Thức Uống");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Phở Tô Lớn + 1 Coca Cola Light");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 119);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "6 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 79000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_phobotai;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Phở Bò Tái");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Phở Bò Tái");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 120);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 49000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_phobochin;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Phở Bò Chín");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Phở Bò Chín");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 121);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 49000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_phodacbiet;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "1 Phở Đặc Biệt");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Phở Đặc Biệt");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 122);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 59000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_phobovien;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Phở Bò Viên");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Phở Bò Viên");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 123);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "6 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 49000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_phoga;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Phở Gà");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Phở Gà");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 124);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "8 đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 49000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_comduiga;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Đùi Gà");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Cơm Đùi Gà");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 125);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 3 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 52000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_comsuonopla;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cơm Sườn Ốp La");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Cơm Sườn Ốp La");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 8);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 126);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "4 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 52000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_xoidacbiet;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Xôi Đặc Biệt");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Xôi Đặc Biệt");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 17);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 127);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "6 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 24000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_banhuot;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bánh Ướt");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Phần Bánh Ướt");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 128);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "4 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 24000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_bunboxaoxa;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bún Bò Xào Xả");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Bún Bò Xào Xả");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 129);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 45000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_bunchagio;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bún Chả Giò");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Bún Chả Giò");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 7);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 130);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "1 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 39000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_goicuon;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Gỏi Cuốn");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Gỏi Cuốn");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 131);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "5 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 19000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_chagio;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chả Giò");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Chả Giò");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 132);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "1 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 29000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_cocacola;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Coca Cola Light");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Coca Cola Light");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 133);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "5 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 19000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_suadaunanh;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sữa Đậu Nành");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Sữa Đậu Nành");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 134);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 19000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_nuocdua;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Nước Dừa");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Nước Dừa");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 135);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 29000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.pho24_dachanh;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Đá Chanh");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Đá Chanh");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 7);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 136);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "3 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 19000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

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

        img = R.drawable.tuanbaykeo_banhmithapcam;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bánh Mì Thập Cẩm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Bánh Mì Thập Cẩm");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 10);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 137);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 100+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 25000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_banhmiphalaulotaiheo;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bánh Mì Phá Lấu Lỗ Tai Heo");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Bánh Mì Phá Lấu Lỗ Tai Heo");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 10);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 138);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 25000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_banhmigaxe;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bánh Mì Gà Xé");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Bánh Mì Gà Xé");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 10);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 139);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 25000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_banhmichabong;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bánh Mì Chà Bông");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Bánh Mì Chà Bông");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 10);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 140);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 25000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_comboxoithapcam;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo 1: Xôi Thập Cẩm(đặc biệt) + Chè");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Xôi Thập Cẩm Đặc Biệt + 1 Chè");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 17);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 141);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 4 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 55000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_xoithapcam;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Xôi Thập Cẩm Tuấn 7 Kẹo");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Xôi Thập Cẩm");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 17);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 142);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 100+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 30000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_xoigaxe;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Xôi Gà Xé");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Xôi Gà Xé");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 17);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 143);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 30000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_xoixaxiu;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Xôi Xá Xíu");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Xôi Xá Xíu");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 17);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 144);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 30000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_chethapcam;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chè Thập Cẩm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Chè Thập Cẩm");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 18);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 145);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 18000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_chehatsencunang;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chè Hạt Sen Củ Năng");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Chè Hạt Sen Củ Năng");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 18);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 146);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 20000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_chesamboluong;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chè Sâm Bổ Lượng");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Chè Sâm Bổ Lượng");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 18);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 147);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 9 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 20000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_chethai;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chè Thái");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Chè Thái");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 18);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 148);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 20000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_banhplan;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bánh Plan");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Bánh Plan");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 149);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 10000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_cocacola;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Coca Tươi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Coca Cola");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 150);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 3 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 15000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.tuanbaykeo_samduasua;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sâm Dứa Sữa");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Sâm Dứa Sữa");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 8);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 151);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 15000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


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

        img = R.drawable.miquangbaanhem_miquangchacuachatom;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mì Quảng Chả Cua Chả Tôm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 152);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 43000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_miquangdacbiet;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mì Quảng Đặc Biệt");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 153);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 63000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_miquanggachoirutxuong;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mì Quảng Gà Chọi Rút Xương");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 154);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 53000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_miquanggata;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mì Quảng Gà Ta");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 155);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 3 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 43000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_miquangsuon;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mì Quảng Sườn");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 156);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 43000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_miquangthittrungcut;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mì Quảng Thịt Trứng Cút");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 157);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 6 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 43000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_miquangtom;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mì Quảng Tôm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 158);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 43000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_miquangtomthit;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mì Quảng Tôm Thịt");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 11);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 159);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 43000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_tomithem;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Tô Mì Thêm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 160);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 13000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_others;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chén Nhân Thêm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 161);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 33000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_others;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chén Nhân Gà Chọi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 162);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "5 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 43000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_others;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chén Nhân Thập Cẩm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 163);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "12 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 43000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_others;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Chén Nhân Bắp Bò Tái");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giá đã bao gồm 3.000đ tiền hộp");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 12);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 164);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "1 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 43000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_pepsi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Pepsi Lon");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Lon Pepsi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 165);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "5 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 15000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_sting;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sting Lon");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Lon Sting");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 166);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "5 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 15000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.miquangbaanhem_olong;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Ô Long");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Chai Ô Long");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 9);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 167);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "1 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 15000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


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

        img = R.drawable.thealley_acquy;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Ác Quỷ");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Size L");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 168);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "6 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 72000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_thienthan;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Thiên Thần");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Size L");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 169);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 72000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_dalgonatranchauduongden;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Dalgona Trân Châu Đường Đen");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Đường / Đá cố định");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 170);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 7 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 65000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_hongtrasuamatongkemtuoioccho;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Hồng Trà Sữa Mật Ong Kem Tươi Óc Chó");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Walnut Cream Honey Black Milk Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 171);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 58000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_luctrasuakemtuoioccho;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Lục Trà Sữa Kem Tươi Óc Chó");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Walnut Cream Green Milk Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 172);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 58000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_trasuakemcheeseduongden;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Sữa Kem Cheese Đường Đen");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Brown Sugar Cream Cheese Milk Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 173);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 58000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_trasuamamcay;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Sữa Mầm Cây");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Potted Plant Milk Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 174);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 6 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 59000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_trasuathietquanam;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Sữa Thiết Quan Âm");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Tiaguanyin Milk Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 175);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 3 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 52000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_trasuathaomoc;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Sữa Thảo Mộc");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Earl Grey Milk Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 176);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 5 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 52000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_trasuathaomockemtuoioccho;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Sữa Thảo Mộc Kem Tươi Óc Chó");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Walnut Cream Earl Grey Milk Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 13);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 177);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 58000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_luctrachanhdaythachdua;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Lục Trà Chanh Dây + Thạch Dừa");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Tropical Passion Fruit Green Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 178);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 59000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_trachanhbidao;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Chanh Bí Đao");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Lemon Winter Melon Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 179);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 6 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 52000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_trathaomockemcheese;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Thảo Mộc Kem Cheese");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Cream Cheese Earl Grey Tea");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 180);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 3 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 55000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_nuocepchanhoi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Nước Ép Chanh Ổi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Lemon Guava Juice");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 181);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 8 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 55000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_suachanhtrauchau;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sữa Chanh Trân Châu");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Lemon Milk");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 182);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 1 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 59000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_suachuatranchauduongden;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sữa Chua Trân Châu Đường Đen");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Mức giá cố định");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 183);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "500+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 65000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.thealley_suatuoitrauchauduongden;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sữa Tươi Chân Trâu Đường Đen");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Brown Sugar Deerioca Milk");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 4);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 10);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 184);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "999+ đã bán | 100+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 62000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


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

        img = R.drawable.greeci_combovitaminc;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Vitamin C, Tăng Đề Kháng");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Ly Cóc Táo Ổi + 1 Ly Bưởi Cam Chanh");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 185);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 3 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 52000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_combodetoxbanchay;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Detox Bán Chạy, Bổ Và Ngon");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Ly Táo Thơm Chanh Dây + 1 Ly Cóc Táo Ổi");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 186);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 52000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_combogiamcan;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Giảm Cân Greeci");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Ly Cóc Táo Cần Tây + 1 Ly Táo Thơm Cần Tây");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 187);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 65000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_combogiaikhaitraicaynhietdoi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Combo Giải Khát Trái Cây Nhiệt Đới");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "1 Ly Trà Dâu Nam Mỹ + 1 Ly Nước Ép Chanh Dây");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 188);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "7 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 45000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_buoiduahauchanhbacha;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bưởi, Dưa Hấu, Chanh, Bạc Hà");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Đẹp Da, Bổ Máu, Nhiều Vitamin");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 189);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 7 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 31000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_coctaooi;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cóc, Táo, Ổi");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Đẹp Da, Giảm Cân, Tăng Hệ Miễn Dịch");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 190);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 26000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_taodualecantaychanh;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Táo, Dưa Lê, Cần Tây, Chanh");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giảm Cân, Thải Độc, Thanh Lọc Cơ Thể");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 191);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 5 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 31000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_taothomchanhday;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Táo, Thơm, Chanh Dây");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giảm Tiểu Đường, Đẹp Da, Giảm Cân");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 192);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 5 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 26000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_thomcamcantaybacha;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Thơm, Cam, Cần Tây, Bạc Hà");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giảm Cân, Thải Độc, Tăng Miễn Dịch, Chống Lão Hóa");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 193);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 7 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 31000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_thomcarotcam;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Thơm, Cà Rốt, Cam");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Giảm Cân, Thải Độc, Chống Lão Hóa");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 194);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 5 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 26000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_mixdautayvietquatchuoisuachua;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Mix Dâu Tây, Việt Quất, Chuối, Sữa Chua");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Đẹp Da, Giữ Dáng, Sáng Mắt, Tốt Trong Quá Trình Trao Đổi Chất");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 195);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 8 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 41000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_sinhtobo;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sinh Tố Bơ");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Đẹp Da");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 196);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 10+ lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 33000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_sinhtodau;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sinh Tố Dâu");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Đẹp Da");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 197);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 5 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 33000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_sinhtoduacacao;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sinh Tố Dừa Cacao");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Đẹp Da");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 198);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "100+ đã bán | 9 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 26000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_sinhtomangcautay;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sinh Tố Mãng Cầu Tây");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Đẹp Da");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 199);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 6 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 29000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_sinhtovietquat;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Sinh Tố Việt Quất");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Đẹp Da");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 14);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 200);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "50+ đã bán | 4 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 29000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_tradao;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Đào");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Ngon");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 201);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 25000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_travai;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Vải");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Ngon");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 202);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán | 2 lượt thích");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 25000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_tradaunammy;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Trà Dâu Nam Mỹ");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Ngọt Dịu Vị Chua Thanh, Vị Dâu Lẫn Vào Vị Trà Lài");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 3);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 203);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "10+ đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 25000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_caphedenda;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cà Phê Đen Đá");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Đắng");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 204);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "5 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 16000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_caphesuada;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Cà Phê Sữa Đá");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Ngọt");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 205);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "8 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 20000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);

        img = R.drawable.greeci_bacxiu;
        drawable = resources.getDrawable(img);
        bitmap =  ((BitmapDrawable)drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        bitmapData = stream.toByteArray();

        values = new ContentValues();
        values.put(FoodManagementContract.CProduct.KEY_NAME, "Bạc Xỉu Đá");
        values.put(FoodManagementContract.CProduct.KEY_DESC, "Ngọt");
        values.put(FoodManagementContract.CProduct.KEY_CATEGORY, 1);
        values.put(FoodManagementContract.CProduct.KEY_IMAGE, bitmapData);
        db.insertOrThrow(FoodManagementContract.CProduct.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(FoodManagementContract.CMenu.KEY_RESTAURANT, 11);
        values.put(FoodManagementContract.CMenu.KEY_PRODUCT, 206);
        values.put(FoodManagementContract.CMenu.KEY_DESC, "5 đã bán");
        values.put(FoodManagementContract.CMenu.KEY_PRICE, 25000);
        db.insertOrThrow(FoodManagementContract.CMenu.TABLE_NAME, null, values);


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
