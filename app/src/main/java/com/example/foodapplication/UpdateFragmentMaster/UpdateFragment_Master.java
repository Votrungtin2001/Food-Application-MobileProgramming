package com.example.foodapplication.UpdateFragmentMaster;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.R;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import models.ProductModel;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class UpdateFragment_Master extends Fragment {

    int master_id;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    boolean sign_update = false;

    TextView textView_Title_UpdateFragment;

    ImageView imageView_ImageRestaurant_UpdateFragment;

    EditText editText_NameRestaurant_UpdateFragment;
    EditText editText_OpeningTime_UpdateFragment;
    EditText editText_ClosingTime_UpdateFragment;
    EditText editText_NameBranch_UpdateFragment;
    EditText editText_Address_UpdateFragment;


    Spinner spinner_City_UpdateFragment;
    Spinner spinner_District_UpdateFragment;

    Button button_ImageRestaurant_UpdateFragment;
    Button button_CreateRestaurant_UpdateFragment;

    List<String> cityList = new ArrayList<>();
    List<String> districtList = new ArrayList<>();

    ArrayAdapter<String> districtAdapter;

    int city_id;
    int district_id;
    String name_city;
    String name_district;
    int restaurant_id;
    int branch_id;
    int address_id;
    String opening_time;
    String closing_time;
    String branch_name;
    String address_name;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int REQUEST_STORAGE = 1001;

    Dialog AnnouncementDialog;

    public UpdateFragment_Master() {
        // Required empty public constructor
    }

    public UpdateFragment_Master(int id) {
        this.master_id = id;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update__master, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        cityList = getAllCityInSQLite();

        initComponents(view);

       SetUpScreen();



        return view;
    }

    public void initComponents(View view) {
        textView_Title_UpdateFragment = (TextView) view.findViewById(R.id.Title_UpdateFragment);
        imageView_ImageRestaurant_UpdateFragment = (ImageView) view.findViewById(R.id.ImageView_ImageRestaurant_UpdateFragment);
        spinner_City_UpdateFragment = (Spinner) view.findViewById(R.id.Spinner_City_UpdateFragment);
        spinner_District_UpdateFragment = (Spinner) view.findViewById(R.id.Spinner_District_UpdateFragment);
        button_ImageRestaurant_UpdateFragment = (Button) view.findViewById(R.id.Button_ImageRestaurant_UpdateFragment);
        button_CreateRestaurant_UpdateFragment = (Button) view.findViewById(R.id.Button_CreateRestaurant_UpdateFragment);
        editText_NameRestaurant_UpdateFragment = (EditText) view.findViewById(R.id.EditText_NameRestaurant_UpdateFragment);
        editText_OpeningTime_UpdateFragment = (EditText) view.findViewById(R.id.EditText_OpeningTime_UpdateFragment);
        editText_ClosingTime_UpdateFragment = (EditText) view.findViewById(R.id.EditText_ClosingTime_UpdateFragment);
        editText_NameBranch_UpdateFragment = (EditText) view.findViewById(R.id.EditText_NameBranch_UpdateFragment);
        editText_Address_UpdateFragment = (EditText) view.findViewById(R.id.EditText_Address_UpdateFragment);


        AnnouncementDialog = new Dialog(getActivity());
        View view1  = LayoutInflater.from(getActivity()).inflate(R.layout.custom_popup_require_login, null);
        AnnouncementDialog.setContentView(view1);
    }

    public boolean CheckMasterHasRestaurant_Is_Right(int id) {
        int count = 0;
        String selectQuery = "SELECT _id FROM BRANCHES WHERE Master='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        count = cursor.getCount();
        cursor.close();

        if(count > 0) return true;
        else return false;
    }

    public List<String> getAllCityInSQLite() {
        List<String> cityList = new ArrayList<>();

        String selectQuery = "SELECT * FROM CITY";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String city_name = cursor.getString(cursor.getColumnIndex("Name"));
                cityList.add(city_name);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return cityList;
    }

    public List<String> getAllDistrictInSQLite(int id) {
        List<String> districtList = new ArrayList<>();

        String selectQuery = "SELECT * FROM DISTRICT WHERE City='" + id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String district_name = cursor.getString(cursor.getColumnIndex("Name"));
                districtList.add(district_name);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return districtList;
    }

    public void SetUpScreen() {
        boolean checkMasterHasRestaurant = CheckMasterHasRestaurant_Is_Right(master_id);
        if(checkMasterHasRestaurant == true) {
            textView_Title_UpdateFragment.setText("CẬP NHẬT NHÀ HÀNG");
            button_CreateRestaurant_UpdateFragment.setText("UPDATE");
            Run2();


        }
        else {
            textView_Title_UpdateFragment.setText("TẠO NHÀ HÀNG");
            button_CreateRestaurant_UpdateFragment.setText("CREATE");
            Run1();
        }






    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK){

            if(data != null) {
                Uri uri = data.getData();

                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView_ImageRestaurant_UpdateFragment.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void SelectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    public byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public int getCountRestaurant() {
        int count = 0;
        String selectQuery = "SELECT * FROM RESTAURANT";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {

            } while (cursor.moveToNext());

        }
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getBranchID(int master_id) {
        int id = -1;
        String selectQuery = "SELECT * FROM BRANCHES WHERE Master ='" + master_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex("_id"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return id;
    }

    public String getBranchName(int branch_id) {
        String branch_nane = "";
        String selectQuery = "SELECT * FROM BRANCHES WHERE _id='" + branch_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                branch_nane = cursor.getString(cursor.getColumnIndex("NAME"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return branch_nane;
    }

    public int getAddressID(int branch_id) {
        int id = -1;
        String selectQuery = "SELECT * FROM BRANCHES WHERE _id='" + branch_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex("Address"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return id;
    }

    public String getAddressName(int address_id) {
        String address_name = "";
        String selectQuery = "SELECT * FROM ADDRESS WHERE _id='" + address_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                address_name = cursor.getString(cursor.getColumnIndex("Address"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return address_name;
    }

    public int getCityID(int address_id) {
        int id = -1;
        String selectQuery = "SELECT * FROM ADDRESS WHERE _id='" + address_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex("City"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return id;
    }

    public String getCityName(int city_id) {
        String city_name = "";
        String selectQuery = "SELECT * FROM CITY WHERE _id='" + city_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                city_name = cursor.getString(cursor.getColumnIndex("Name"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return city_name;
    }


    public int getDistrictID(int address_id) {
        int id = -1;
        String selectQuery = "SELECT * FROM ADDRESS WHERE _id='" + address_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex("District"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return id;
    }

    public String getDistrictName(int district_id) {
        String district_name = "";
        String selectQuery = "SELECT * FROM DISTRICT WHERE _id='" + district_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                district_name = cursor.getString(cursor.getColumnIndex("Name"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return district_name;
    }

    public byte[] getImage(int restaurant_id) {
        byte[] image = null;
        String selectQuery = "SELECT * FROM RESTAURANT WHERE _id='" + restaurant_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                image = cursor.getBlob(cursor.getColumnIndex("Image"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return image;
    }


    public int getRestaurantID(int branch_id) {
        int id = -1;
        String selectQuery = "SELECT * FROM BRANCHES WHERE _id='" + branch_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex("Restaurant"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return id;
    }

    public String getOpeningTime(int restaurant_id) {
        String opening_time = "";
        String selectQuery = "SELECT * FROM RESTAURANT WHERE _id='" + restaurant_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                opening_time = cursor.getString(cursor.getColumnIndex("Opening_Times"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return opening_time;
    }

    public String getRestaurantName(int restaurant_id) {
        String restaurant_name = "";
        String selectQuery = "SELECT * FROM RESTAURANT WHERE _id='" + restaurant_id + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                restaurant_name = cursor.getString(cursor.getColumnIndex("Name"));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return restaurant_name;
    }

    public int getCountAddress() {
        int count = 0;
        String selectQuery = "SELECT * FROM Address";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {

            } while (cursor.moveToNext());

        }
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void Run1() {
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, cityList);
        spinner_City_UpdateFragment.setAdapter(cityAdapter);

        spinner_City_UpdateFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 2) {
                    city_id = 3;
                    name_city = spinner_City_UpdateFragment.getSelectedItem().toString();
                    spinner_District_UpdateFragment.setAdapter(null);
                    districtList = getAllDistrictInSQLite(position + 1);
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, districtList);
                    spinner_District_UpdateFragment.setAdapter(districtAdapter);

                    if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                            !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                            !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                            !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                            !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                            !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                            spinner_District_UpdateFragment.getSelectedItem() != null) {

                        button_CreateRestaurant_UpdateFragment.setEnabled(true);
                    }
                    else button_CreateRestaurant_UpdateFragment.setEnabled(false);
                }
                else {
                    spinner_District_UpdateFragment.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_District_UpdateFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                district_id = position + 1;
                name_district = spinner_District_UpdateFragment.getSelectedItem().toString();
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_ImageRestaurant_UpdateFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_STORAGE
                    );
                }
                else {
                    SelectImage();
                }
            }
        });

        imageView_ImageRestaurant_UpdateFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_STORAGE
                    );
                }
                else {
                    SelectImage();
                }
            }
        });

        editText_NameRestaurant_UpdateFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_OpeningTime_UpdateFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_OpeningTime_UpdateFragment.setKeyListener(DigitsKeyListener.getInstance("0123456789:"));

        editText_ClosingTime_UpdateFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_ClosingTime_UpdateFragment.setKeyListener(DigitsKeyListener.getInstance("0123456789:"));

        editText_NameBranch_UpdateFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_Address_UpdateFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_CreateRestaurant_UpdateFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_restaurant = editText_NameRestaurant_UpdateFragment.getText().toString();
                String opening_time = editText_OpeningTime_UpdateFragment.getText().toString() + " AM - " + editText_ClosingTime_UpdateFragment.getText().toString() + " PM";
                databaseHelper.addRestaurant(name_restaurant, opening_time, imageViewToByte(imageView_ImageRestaurant_UpdateFragment));

                String address = editText_Address_UpdateFragment.getText().toString() + ", " + name_district + ", " + name_city;
                databaseHelper.addAddress(address, district_id, city_id, 0, 0, 4);

                int new_id_restaurant = getCountRestaurant();
                int new_id_address = getCountAddress();
                String name_branch = editText_NameBranch_UpdateFragment.getText().toString();
                databaseHelper.addBranch(name_branch, new_id_restaurant, new_id_address, master_id);

                textView_Title_UpdateFragment.setText("CẬP NHẬT NHÀ HÀNG");
                button_CreateRestaurant_UpdateFragment.setText("UPDATE");
                editText_NameRestaurant_UpdateFragment.setText("");
                editText_OpeningTime_UpdateFragment.setText("");
                editText_ClosingTime_UpdateFragment.setText("");
                editText_NameBranch_UpdateFragment.setText("");
                editText_Address_UpdateFragment.setText("");
                button_CreateRestaurant_UpdateFragment.setEnabled(false);
                sign_update = true;
                if(sign_update == true) Run2();
                ShowPopUpCreateSuccesfully();

            }
        });
    }

    public void Run2() {

        branch_id = getBranchID(master_id);
        address_id = getAddressID(branch_id);
        city_id = getCityID(address_id);
        String city_name = getCityName(city_id);
        district_id = getDistrictID(address_id);
        String district_name = getDistrictName(district_id);

        spinner_City_UpdateFragment.setAdapter(null);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, cityList);
        spinner_City_UpdateFragment.setAdapter(cityAdapter);

        spinner_City_UpdateFragment.setSelection(cityAdapter.getPosition(city_name));

        spinner_City_UpdateFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 2) {
                    city_id = 3;
                    name_city = spinner_City_UpdateFragment.getSelectedItem().toString();
                    spinner_District_UpdateFragment.setAdapter(null);
                    districtList = getAllDistrictInSQLite(position + 1);
                    districtAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, districtList);
                    spinner_District_UpdateFragment.setAdapter(districtAdapter);
                    spinner_District_UpdateFragment.setSelection(districtAdapter.getPosition(district_name));

                    if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                            !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                            !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                            !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                            !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                            !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                            spinner_District_UpdateFragment.getSelectedItem() != null) {

                        button_CreateRestaurant_UpdateFragment.setEnabled(true);
                    }
                    else button_CreateRestaurant_UpdateFragment.setEnabled(false);
                }
                else {
                    spinner_District_UpdateFragment.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_District_UpdateFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                district_id = position + 1;
                name_district = spinner_District_UpdateFragment.getSelectedItem().toString();
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button_ImageRestaurant_UpdateFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_STORAGE
                    );
                }
                else {
                    SelectImage();
                }
            }
        });

        restaurant_id = getRestaurantID(branch_id);
        byte[] img = getImage(restaurant_id);
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        imageView_ImageRestaurant_UpdateFragment.setImageBitmap(bitmap);

        imageView_ImageRestaurant_UpdateFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_STORAGE
                    );
                }
                else {
                    SelectImage();
                }
            }
        });

        String name_restaurant = getRestaurantName(restaurant_id);
        editText_NameRestaurant_UpdateFragment.setText(name_restaurant);
        editText_NameRestaurant_UpdateFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        String opening_closing_time = getOpeningTime(restaurant_id);
        String[] string_split = opening_closing_time.split(" AM - ");
        opening_time = string_split[0];
        closing_time = string_split[1];

        editText_OpeningTime_UpdateFragment.setText(opening_time);
        editText_OpeningTime_UpdateFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_OpeningTime_UpdateFragment.setKeyListener(DigitsKeyListener.getInstance("0123456789:"));

        String[] string_split2 = closing_time.split(" PM");
        closing_time = string_split2[0];
        editText_ClosingTime_UpdateFragment.setText(closing_time);
        editText_ClosingTime_UpdateFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_ClosingTime_UpdateFragment.setKeyListener(DigitsKeyListener.getInstance("0123456789:"));


        branch_name = getBranchName(branch_id);
        editText_NameBranch_UpdateFragment.setText(branch_name);
        editText_NameBranch_UpdateFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        address_name = getAddressName(address_id);
        String[] string_split3 = address_name.split(", ");
        address_name = string_split3[0];

        editText_Address_UpdateFragment.setText(address_name);
        editText_Address_UpdateFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameRestaurant_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_OpeningTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_ClosingTime_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_NameBranch_UpdateFragment.getText().toString().trim().equals("") &&
                        !editText_Address_UpdateFragment.getText().toString().trim().equals("") &&
                        !spinner_City_UpdateFragment.getSelectedItem().toString().trim().equals("") &&
                        spinner_District_UpdateFragment.getSelectedItem() != null) {

                    button_CreateRestaurant_UpdateFragment.setEnabled(true);
                }
                else button_CreateRestaurant_UpdateFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_CreateRestaurant_UpdateFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_restaurant = editText_NameRestaurant_UpdateFragment.getText().toString();
                String opening_time = editText_OpeningTime_UpdateFragment.getText().toString() + " AM - " + editText_ClosingTime_UpdateFragment.getText().toString() + " PM";
                databaseHelper.updRestaurant(restaurant_id,name_restaurant, opening_time, imageViewToByte(imageView_ImageRestaurant_UpdateFragment));

                String address = editText_Address_UpdateFragment.getText().toString() + ", " + name_district + ", " + name_city;
                databaseHelper.updAddress(address_id, address, district_id, city_id, 0, 0, 4);

                int new_id_restaurant = getCountRestaurant();
                int new_id_address = getCountAddress();
                String name_branch = editText_NameBranch_UpdateFragment.getText().toString();
                databaseHelper.updBranch(branch_id,name_branch, new_id_restaurant, new_id_address, master_id);

                Toast.makeText(getActivity(), "Đã cập nhật thành công", Toast.LENGTH_SHORT);
                button_CreateRestaurant_UpdateFragment.setEnabled(false);
                Run2();
                ShowPopUpUpdateSuccesfully();
            }
        });
    }

    public void ShowPopUpCreateSuccesfully() {
        TextView textView_Close;
        textView_Close = (TextView) AnnouncementDialog.findViewById(R.id.Close_PopUpLogin);
        textView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnnouncementDialog.dismiss();
            }
        });

        TextView textView_Text;
        textView_Text = (TextView) AnnouncementDialog.findViewById(R.id.TextView_PopUp_RequireLogin);
        textView_Text.setText("  Bạn đã tạo nhà hàng thành công!!!    \n    ");

        AnnouncementDialog.show();
    }

    public void ShowPopUpUpdateSuccesfully() {
        TextView textView_Close;
        textView_Close = (TextView) AnnouncementDialog.findViewById(R.id.Close_PopUpLogin);
        textView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnnouncementDialog.dismiss();
            }
        });

        TextView textView_Text;
        textView_Text = (TextView) AnnouncementDialog.findViewById(R.id.TextView_PopUp_RequireLogin);
        textView_Text.setText("Bạn đã cập nhật nhà hàng thành công!!!");

        AnnouncementDialog.show();
    }
}