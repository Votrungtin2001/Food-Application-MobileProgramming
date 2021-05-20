package com.example.foodapplication;

import android.provider.BaseColumns;

public final class FoodManagementContract {
    /* i based all of the following spaghetti code on this answer from SO:
    https://stackoverflow.com/questions/17451931/how-to-use-a-contract-class-in-android
    without understanding jack shit so may need further revisions */
    public static final String AUTHORITY = "com.example.foodapplication";
    public static final String SCHEME = "content://";
    public static final String SLASH = "/";
    public static final String DATABASE_NAME = "foodapp.db";

    public static final String[] SQL_CREATE_TABLE_ARRAY = {
            CCity.CREATE_TABLE,
            CAddressLabel.CREATE_TABLE,
            CAddressLabel.POPULATE_TABLE,
            CCategory.CREATE_TABLE,
            CDelivery.CREATE_TABLE,
            CCustomer.CREATE_TABLE,
            CAddress.CREATE_TABLE,
            CCustomerAddress.CREATE_TABLE,
            CRestaurant.CREATE_TABLE,
            CBranch.CREATE_TABLE,
            CProduct.CREATE_TABLE,
            CMenu.CREATE_TABLE,
            COrder.CREATE_TABLE,
            COrderDetails.CREATE_TABLE,
            COffer.CREATE_TABLE
    };

    public static final String[] SQL_DELETE_TABLE_ARRAY = {
            COffer.DELETE_TABLE,
            COrderDetails.DELETE_TABLE,
            COrder.DELETE_TABLE,
            CMenu.DELETE_TABLE,
            CProduct.DELETE_TABLE,
            CBranch.DELETE_TABLE,
            CRestaurant.DELETE_TABLE,
            CCustomerAddress.DELETE_TABLE,
            CAddress.DELETE_TABLE,
            CCustomer.DELETE_TABLE,
            CDelivery.DELETE_TABLE,
            CCategory.DELETE_TABLE,
            CAddressLabel.DELETE_TABLE,
            CCity.DELETE_TABLE
    };

    private FoodManagementContract() {}

    public static final class CCustomer implements BaseColumns {
        private CCustomer() { }

        public static final String TABLE_NAME = "CUSTOMER",
                KEY_NAME = "Name",
                KEY_CITY = "City",
                KEY_PHONE = "Phone",
                KEY_EMAIL = "Email",
                KEY_FACEBOOK = "Facebook",
                KEY_USERNAME = "Username",
                KEY_PASSWORD = "Password",
                KEY_GENDER = "Gender",
                KEY_DOB = "DoB",
                KEY_OCCUPATION = "Occupation",
                KEY_CREDITS = "Credits";

        /* no id column here unlike in standard SQL: an SQLite table needs a column explicitly named
        "_id" in order to use the cursor class and its dependencies provided by Android */
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CITY + " INTEGER NOT NULL,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_FACEBOOK + " TEXT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_GENDER + " INTEGER,"
                + KEY_DOB + " TEXT,"
                + KEY_OCCUPATION + " TEXT,"
                + KEY_CREDITS + " INTEGER,"
                + "FOREIGN KEY (" + KEY_CITY + ") REFERENCES " + CCity.TABLE_NAME + " (" + CCity._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CCity implements BaseColumns {
        private CCity() { }

        public static final String TABLE_NAME = "CITY", KEY_NAME = "Name";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CAddress implements BaseColumns {
        private CAddress() {}

        public static final String TABLE_NAME = "ADDRESS",
                KEY_ADDRESS = "Address",
                KEY_CITY = "City",
                KEY_FLOOR = "Floor",
                KEY_GATE = "Gate",
                KEY_LABEL = "Label";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_CITY + " INTEGER NOT NULL,"
                + KEY_FLOOR + " INTEGER NULL,"
                + KEY_GATE + " INTEGER NULL,"
                + KEY_LABEL + " INTEGER NULL,"
                + "FOREIGN KEY (" + KEY_CITY + ") REFERENCES " + CCity.TABLE_NAME + " (" + CCity._ID + "),"
                + "FOREIGN KEY (" + KEY_LABEL + ") REFERENCES " + CAddressLabel.TABLE_NAME + " (" + CAddressLabel._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CAddressLabel implements BaseColumns {
        private CAddressLabel() {}

        public static final String TABLE_NAME = "ADDRESS_LABEL", KEY_TYPE = "Type";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_TYPE + " TEXT"
                +");";

        public static final String POPULATE_TABLE = "INSERT INTO " + TABLE_NAME + " (" + KEY_TYPE + ")"
                + " VALUES "
                + " (Home),"
                + " (Work);";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CCustomerAddress implements BaseColumns {
        private CCustomerAddress() {}

        public static final String TABLE_NAME = "CUSTOMER_ADDRESS",
                KEY_CUSTOMER = "Customer",
                KEY_ADDRESS = "Address";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_CUSTOMER + " INTEGER NOT NULL,"
                + KEY_ADDRESS + " INTEGER NOT NULL,"
                + "FOREIGN KEY (" + KEY_CUSTOMER + ") REFERENCES " + CCustomer.TABLE_NAME + " (" + CCustomer._ID+ "),"
                + "FOREIGN KEY (" + KEY_ADDRESS + ") REFERENCES " + CAddress.TABLE_NAME + " (" + CAddress._ID+ ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CRestaurant implements BaseColumns {
        private CRestaurant() { }

        public static final String TABLE_NAME = "RESTAURANT",
                KEY_NAME = "Name",
                KEY_CITY = "City",
                KEY_OPEN = "Opening_Times";

        /* as quoted by SQLite documentation on https://www.sqlite.org/datatype3.html:
        SQLite does not have a storage class set aside for storing dates and/or times.
        Instead, the built-in Date And Time Functions of SQLite are capable of storing dates
        and times as TEXT, REAL, or INTEGER values. */
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CITY + " INTEGER NOT NULL,"
                + KEY_OPEN + " TEXT NOT NULL,"
                + "FOREIGN KEY (" + KEY_CITY + ") REFERENCES " + CCity.TABLE_NAME + " (" + CCity._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CBranch implements BaseColumns {
        private CBranch() { }

        public static final String TABLE_NAME = "BRANCHES",
                KEY_PARENT = "Restaurant",
                KEY_ADDRESS = "Address";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_PARENT + " INTEGER NOT NULL,"
                + KEY_ADDRESS + " TEXT,"
                + "FOREIGN KEY (" + KEY_PARENT + ") REFERENCES " + CRestaurant.TABLE_NAME + " (" + CRestaurant._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CCategory implements BaseColumns {
        private CCategory() { }

        public static final String TABLE_NAME = "CATEGORIES",
                KEY_NAME = "Name",
                KEY_DESC = "Description";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CProduct implements BaseColumns {
        private CProduct() { }

        public static final String TABLE_NAME = "PRODUCTS",
                KEY_NAME = "Name",
                KEY_CATEGORY = "Category";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CATEGORY + " INTEGER NOT NULL,"
                + "FOREIGN KEY (" + KEY_CATEGORY + ") REFERENCES " + CCategory.TABLE_NAME + " (" + CCategory._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CMenu implements BaseColumns {
        private CMenu() { }

        public static final String TABLE_NAME = "MENU",
                KEY_RESTAURANT = "Restaurant",
                KEY_PRODUCT = "Product",
                KEY_DESC = "Description",
                KEY_PRICE = "Price";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_RESTAURANT + " INTEGER NOT NULL,"
                + KEY_PRODUCT + " INTEGER NOT NULL,"
                + KEY_DESC + " TEXT,"
                + KEY_PRICE + " INTEGER,"
                + "FOREIGN KEY (" + KEY_RESTAURANT + ") REFERENCES " + CRestaurant.TABLE_NAME + " (" + CRestaurant._ID + "),"
                + "FOREIGN KEY (" + KEY_PRODUCT + ") REFERENCES " + CProduct.TABLE_NAME + " (" + CProduct._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CDelivery implements BaseColumns {
        private CDelivery() { }

        public static final String TABLE_NAME = "DELIVERY",
                KEY_NAME = "Name",
                KEY_ADDRESS = "Address",
                KEY_PHONE = "Phone",
                KEY_EMAIL = "Email",
                KEY_LICENSE = "License_Plate";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_LICENSE + " TEXT,"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class COrder implements BaseColumns {
        private COrder() { }

        public static final String TABLE_NAME = "ORDERS",
                KEY_DATETIME = "Date_Time",
                KEY_CUSTOMER = "Customer",
                KEY_DELIVERY = "Delivery",
                KEY_ADDRESS = "Address",
                KEY_TOTAL = "Total",
                KEY_STATUS = "Status";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_DATETIME + " TEXT,"
                + KEY_CUSTOMER + " INTEGER NOT NULL,"
                + KEY_DELIVERY + " INTEGER NULL,"
                + KEY_ADDRESS + " INTEGER NOT NULL,"
                + KEY_TOTAL + " INTEGER,"
                + KEY_STATUS + " INTEGER,"
                + "FOREIGN KEY (" + KEY_CUSTOMER + ") REFERENCES " + CCustomer.TABLE_NAME + " (" + CCustomer._ID + "),"
                + "FOREIGN KEY (" + KEY_DELIVERY + ") REFERENCES " + CDelivery.TABLE_NAME + " (" + CDelivery._ID + "),"
                + "FOREIGN KEY (" + KEY_ADDRESS + ") REFERENCES " + CAddress.TABLE_NAME + " (" + CAddress._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class COrderDetails implements BaseColumns {
        private COrderDetails() { }

        public static final String TABLE_NAME = "ORDER_DETAILS",
                KEY_ORDER = "Order",
                KEY_MENUITEM = "Item",
                KEY_QUANTITY = "Qty",
                KEY_PRICE = "Price";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_ORDER + " INTEGER NOT NULL,"
                + KEY_MENUITEM + " INTEGER NOT NULL,"
                + KEY_QUANTITY + " INTEGER,"
                + KEY_PRICE + " INTEGER,"
                + "FOREIGN KEY (" + KEY_ORDER + ") REFERENCES " + COrder.TABLE_NAME + " (" + COrder._ID + "),"
                + "FOREIGN KEY (" + KEY_MENUITEM + ") REFERENCES " + CMenu.TABLE_NAME + " (" + CMenu._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class COffer implements BaseColumns {
        private COffer() { }

        public static final String TABLE_NAME = "OFFERS",
                KEY_MENUITEM = "Item",
                KEY_DATE_ACTIVE_FROM = "From",
                KEY_DATE_ACTIVE_TO = "To",
                KEY_PRICE = "Price";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_MENUITEM + " INTEGER NOT NULL,"
                + KEY_DATE_ACTIVE_FROM + " TEXT,"
                + KEY_DATE_ACTIVE_TO + " TEXT,"
                + KEY_PRICE + " TEXT,"
                + "FOREIGN KEY (" + KEY_MENUITEM + ") REFERENCES " + CMenu.TABLE_NAME + " (" + CMenu._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
