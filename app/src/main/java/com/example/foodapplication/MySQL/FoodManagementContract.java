package com.example.foodapplication.MySQL;

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
            CAddressLabel.CREATE_TABLE,
            CAddressLabel.POPULATE_TABLE,
            CCity.CREATE_TABLE,
            CCity.POPULATE_TABLE,
            CDistrict.CREATE_TABLE,
            CDistrict.POPULATE_TABLE,
            CCategory.CREATE_TABLE,
            CCategory.POPULATE_TABLE,
            CMaster.CREATE_TABLE,
            CMaster.POPULATE_TABLE,
            CRestaurant.CREATE_TABLE,
            CAddress.CREATE_TABLE,
            CBranch.CREATE_TABLE,
            CProduct.CREATE_TABLE,
            CMenu.CREATE_TABLE,
            CDelivery.CREATE_TABLE,
            CCustomer.CREATE_TABLE,
            CCustomerAddress.CREATE_TABLE,
            COrder.CREATE_TABLE,
            COrderDetails.CREATE_TABLE,
            COffer.CREATE_TABLE,
            CFavorites.CREATE_TABLE,
            CTransaction.CREATE_TABLE
    };

    public static final String[] SQL_DELETE_TABLE_ARRAY = {
            CTransaction.DELETE_TABLE,
            CFavorites.DELETE_TABLE,
            CDistrict.DELETE_TABLE,
            CMaster.DELETE_TABLE,
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
                KEY_CREDITS = "Credits",
                KEY_STATUS = "Status";

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
                + KEY_STATUS + " INTEGER,"
                + "FOREIGN KEY (" + KEY_CITY + ") REFERENCES " + CCity.TABLE_NAME + " (" + CCity._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CMaster implements BaseColumns {
        private CMaster() { }

        public static final String TABLE_NAME = "MASTER",
                KEY_NAME = "Name",
                KEY_PHONE = "Phone",
                KEY_EMAIL = "Email",
                KEY_FACEBOOK = "Facebook",
                KEY_USERNAME = "Username",
                KEY_PASSWORD = "Password",
                KEY_STATUS = "Status";

        /* no id column here unlike in standard SQL: an SQLite table needs a column explicitly named
        "_id" in order to use the cursor class and its dependencies provided by Android */
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_FACEBOOK + " TEXT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_STATUS + " INTEGER"
                + ");";

        public static final String POPULATE_TABLE = "INSERT INTO " + TABLE_NAME + " (" + KEY_NAME + ", " + KEY_PHONE + ", " + KEY_EMAIL + ", " + KEY_FACEBOOK + ", " + KEY_USERNAME + ", " + KEY_PASSWORD + ", " + KEY_STATUS + ")"
                + " VALUES "
                + " (\"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"0\");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CCity implements BaseColumns {
        private CCity() { }

        public static final String TABLE_NAME = "CITY", KEY_NAME = "Name";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT"
                + ");";

        public static final String POPULATE_TABLE = GetDataForCity(TABLE_NAME, KEY_NAME);

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CDistrict implements BaseColumns {
        private CDistrict() { }

        public static final String TABLE_NAME = "DISTRICT", KEY_NAME = "Name", KEY_CITY = "City";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CITY + " INTEGER NULL,"
                + "FOREIGN KEY (" + KEY_CITY + ") REFERENCES " + CCity.TABLE_NAME + " (" + CCity._ID + ")"
                + ");";

        public static final String POPULATE_TABLE = GetDataForDistrict(TABLE_NAME, KEY_NAME, KEY_CITY);

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CAddress implements BaseColumns {
        private CAddress() {}

        public static final String TABLE_NAME = "ADDRESS",
                KEY_ADDRESS = "Address",
                KEY_DISTRICT = "District",
                KEY_CITY = "City",
                KEY_FLOOR = "Floor",
                KEY_GATE = "Gate",
                KEY_LABEL = "Label";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_DISTRICT + " INTEGER NOT NULL,"
                + KEY_CITY + " INTEGER NOT NULL,"
                + KEY_FLOOR + " INTEGER NULL,"
                + KEY_GATE + " INTEGER NULL,"
                + KEY_LABEL + " INTEGER NULL,"
                + "FOREIGN KEY (" + KEY_DISTRICT + ") REFERENCES " + CDistrict.TABLE_NAME + " (" + CDistrict._ID + "),"
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
                + " (\"Home\"),"
                + " (\"Work\"),"
                + " (\"Other\"),"
                + " (\"Restaurant\");";

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
                KEY_OPEN = "Opening_Times",
                KEY_IMAGE = "Image";

        /* as quoted by SQLite documentation on https://www.sqlite.org/datatype3.html:
        SQLite does not have a storage class set aside for storing dates and/or times.
        Instead, the built-in Date And Time Functions of SQLite are capable of storing dates
        and times as TEXT, REAL, or INTEGER values. */
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_OPEN + " TEXT NOT NULL,"
                + KEY_IMAGE + " BLOB NOT NULL"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CBranch implements BaseColumns {
        private CBranch() { }

        public static final String TABLE_NAME = "BRANCHES",
                KEY_NAME = "NAME",
                KEY_PARENT = "Restaurant",
                KEY_ADDRESS = "Address",
                KEY_MASTER = "Master";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PARENT + " INTEGER NOT NULL,"
                + KEY_ADDRESS + " INTEGER NOT NULL,"
                + KEY_MASTER + " INTEGER NOT NULL,"
                + "FOREIGN KEY (" + KEY_PARENT + ") REFERENCES " + CRestaurant.TABLE_NAME + " (" + CRestaurant._ID + "),"
                + "FOREIGN KEY (" + KEY_ADDRESS + ") REFERENCES " + CAddress.TABLE_NAME + " (" + CAddress._ID + ")"
                + "FOREIGN KEY (" + KEY_MASTER + ") REFERENCES " + CMaster.TABLE_NAME + " (" + CMaster._ID + ")"
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

        public static final String POPULATE_TABLE = GetDataForCategory(TABLE_NAME, KEY_NAME, KEY_DESC);

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CProduct implements BaseColumns {
        private CProduct() { }

        public static final String TABLE_NAME = "PRODUCTS",
                KEY_NAME = "Name",
                KEY_DESC = "Description",
                KEY_CATEGORY = "Category",
                KEY_IMAGE = "Image";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_CATEGORY + " INTEGER NOT NULL,"
                + KEY_IMAGE + " BLOB NOT NULL,"
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
                KEY_FACEBOOK = "Facebook",
                KEY_USERNAME = "Username",
                KEY_PASSWORD = "Password",
                KEY_LICENSE = "License_Plate";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_FACEBOOK + " TEXT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_LICENSE + " TEXT"
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
                KEY_ORDER = "OrderID",
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
                KEY_DATE_ACTIVE_FROM = "FromDate",
                KEY_DATE_ACTIVE_TO = "ToDate",
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

    public static final class CFavorites implements BaseColumns {
        private CFavorites() { }

        public static final String TABLE_NAME = "FAVORITES",
                KEY_CUSTOMER = "Customer",
                KEY_RESTAURANT = "Restaurant";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_CUSTOMER + " INTEGER NOT NULL,"
                + KEY_RESTAURANT + " INTEGER NOT NULL,"
                + "FOREIGN KEY (" + KEY_CUSTOMER + ") REFERENCES " + CCustomer.TABLE_NAME + " (" + COrder._ID + "),"
                + "FOREIGN KEY (" + KEY_RESTAURANT + ") REFERENCES " + CRestaurant.TABLE_NAME + " (" + CMenu._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CTransaction implements BaseColumns {
        private CTransaction() {}

        public static final String TABLE_NAME = "Transactions",
                KEY_CUSTOMER = "Customer",
                KEY_DATE = "TransDate",
                KEY_CREDITS = "Credits";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_CUSTOMER + " INTEGER NOT NULL,"
                + KEY_DATE + " TEXT,"
                + KEY_CREDITS + " INTEGER,"
                + "FOREIGN KEY (" + KEY_CUSTOMER + ") REFERENCES " + CCustomer.TABLE_NAME + " (" + CCustomer._ID + ")"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final String GetDataForCity(String TABLE_NAME, String KEY_NAME) {
        String string = "INSERT INTO " + TABLE_NAME + " (" + KEY_NAME + ")"
                + " VALUES "
                + " (\"Đà Nẵng\"),"
                + " (\"Hà Nội\"),"
                + " (\"Thành phố Hồ Chí Minh\");";
        return  string;
    }

    public static final String GetDataForDistrict(String TABLE_NAME, String KEY_NAME, String KEY_CITY) {
        String string = "INSERT INTO " + TABLE_NAME + " (" + KEY_NAME + ", " + KEY_CITY + ")"
                + " VALUES "
                + " (\"Quận 1\", \"3\"),"
                + " (\"Quận 2\", \"3\"),"
                + " (\"Quận 3\", \"3\"),"
                + " (\"Quận 4\", \"3\"),"
                + " (\"Quận 5\", \"3\"),"
                + " (\"Quận 6\", \"3\"),"
                + " (\"Quận 7\", \"3\"),"
                + " (\"Quận 8\", \"3\"),"
                + " (\"Quận 9\", \"3\"),"
                + " (\"Quận 10\", \"3\"),"
                + " (\"Quận 11\", \"3\"),"
                + " (\"Quận 12\", \"3\"),"
                + " (\"Quận Bình Thạnh\", \"3\"),"
                + " (\"Quận Thủ Đức\", \"3\"),"
                + " (\"Quận Gò Vấp\", \"3\"),"
                + " (\"Quận Phú Nhuận\", \"3\"),"
                + " (\"Quận Tân Bình\", \"3\"),"
                + " (\"Quận Tân Phú\", \"3\"),"
                + " (\"Quận Bình Tân\", \"3\"),"
                + " (\"Huyện Nhà Bè\", \"3\"),"
                + " (\"Huyện Hóc Môn\", \"3\"),"
                + " (\"Huyện Bình Chánh\", \"3\"),"
                + " (\"Huyện Củ Chi\", \"3\"),"
                + " (\"Huyện Cần Giờ\", \"3\");";
        return  string;
    }

    public static final String GetDataForCategory(String TABLE_NAME, String KEY_NAME, String KEY_DESC) {
        String string = "INSERT INTO " + TABLE_NAME + " (" + KEY_NAME + ", " + KEY_DESC + ")"
                + " VALUES "
                + " (\"Thức uống\", \"Cà phê\"),"
                + " (\"Thức uống\", \"Đá xay\"),"
                + " (\"Thức uống\", \"Trà\"),"
                + " (\"Thức uống\", \"Khác\"),"
                + " (\"Bánh\", \"Bánh ngọt\"),"
                + " (\"Đồ ăn\", \"Gà rán\"),"
                + " (\"Đồ ăn\", \"Mì\"),"
                + " (\"Đồ ăn\", \"Cơm\"),"
                + " (\"Đồ ăn\", \"Hamburger/Sandwich\"),"
                + " (\"Đồ ăn\", \"Bánh mì\"),"
                + " (\"Đồ ăn\", \"Món nước\"),"
                + " (\"Đồ ăn\", \"Khác\"),"
                + " (\"Thức uống\", \"Trà sữa\"),"
                + " (\"Thức uống\", \"Nước ép\"),"
                + " (\"Đồ ăn\", \"Pizza\"),"
                + " (\"Đồ ăn\", \"Salad\"),"
                + " (\"Đồ ăn\", \"Xôi\"),"
                + " (\"Thức uống\", \"Chè\"),"
                + " (\"Đồ ăn\", \"Sushi\");";
        return  string;
    }



}
