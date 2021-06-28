package com.example.foodapplication.mySQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // REMEMBER TO ADD 1 TO THIS CONSTANT WHEN YOU MAKE ANY CHANGES TO THE CONTRACT CLASS!
    public static final int DATABASE_VERSION = 2;
    private static String DB_PATH = "data/data/com.example.foodapplication/databases/";
    private static String DB_NAME = "foodappmobile";
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String s : FoodManagementContract.SQL_DELETE_TABLE_ARRAY)
            db.execSQL(s);
        onCreate(db);
    }

    public void addCustomer(int cus_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCustomer.KEY_CUSTOMERID, cus_id);
        values.put(FoodManagementContract.CCustomer.KEY_STATUS, 0);

        db.insert(FoodManagementContract.CCustomer.TABLE_NAME, null, values);
        db.close();

    }

    public void updCustomerLoginStatus(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CCustomer.KEY_STATUS, 1);

        String selection = FoodManagementContract.CCustomer._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        db.update(FoodManagementContract.CCustomer.TABLE_NAME, values, selection, selectionArgs);
    }

    public void updAllAcountLogOutStatus() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put(FoodManagementContract.CCustomer.KEY_STATUS, 0);

        db.update(FoodManagementContract.CCustomer.TABLE_NAME, values1, null, null);

        ContentValues values2 = new ContentValues();
        values2.put(FoodManagementContract.CMaster.KEY_STATUS, 0);

        db.update(FoodManagementContract.CMaster.TABLE_NAME, values2, null, null);
    }

    public void updMasterLoginStatus(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CMaster.KEY_STATUS, 1);

        String selection = FoodManagementContract.CMaster._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        db.update(FoodManagementContract.CMaster.TABLE_NAME, values, selection, selectionArgs);
    }

    public void addMaster(int mas_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodManagementContract.CMaster.KEY_MASTERID, mas_id);
        values.put(FoodManagementContract.CMaster.KEY_STATUS, 0);

        db.insert(FoodManagementContract.CMaster.TABLE_NAME, null, values);
        db.close();

    }
}
