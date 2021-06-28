package com.example.foodapplication.MySQL;

import android.provider.BaseColumns;

public final class FoodManagementContract {
    public static final String AUTHORITY = "com.example.foodapplication";
    public static final String SCHEME = "content://";
    public static final String SLASH = "/";
    public static final String DATABASE_NAME = "foodappmobile.db";

    public static final String[] SQL_CREATE_TABLE_ARRAY = {
            CMaster.CREATE_TABLE,
            CCustomer.CREATE_TABLE
    };

    public static final String[] SQL_DELETE_TABLE_ARRAY = {
            CMaster.DELETE_TABLE,
            CCustomer.DELETE_TABLE
    };

    private FoodManagementContract() {
    }

    public static final class CCustomer implements BaseColumns {
        private CCustomer() {
        }

        public static final String TABLE_NAME = "CUSTOMER",
                KEY_CUSTOMERID = "Customer_ID",
                KEY_STATUS = "Status";

        /* no id column here unlike in standard SQL: an SQLite table needs a column explicitly named
        "_id" in order to use the cursor class and its dependencies provided by Android */
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_CUSTOMERID + " INTEGER,"
                + KEY_STATUS + " INTEGER"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CMaster implements BaseColumns {
        private CMaster() {
        }

        public static final String TABLE_NAME = "MASTER",
                KEY_MASTERID = "Master_ID",
                KEY_STATUS = "Status";

        /* no id column here unlike in standard SQL: an SQLite table needs a column explicitly named
        "_id" in order to use the cursor class and its dependencies provided by Android */
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_MASTERID + " INTEGER,"
                + KEY_STATUS + " INTEGER"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}

