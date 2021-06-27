package com.example.foodapplication.UpdateFragmentMaster;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
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

import com.example.foodapplication.account.model.IdWithNameListItem;
import com.example.foodapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import static android.app.Activity.RESULT_OK;
import static com.example.foodapplication.Master_MainActivity.isMasterHasRestaurant;
import static com.example.foodapplication.MySQL.MySQLQuerry.CreateRestaurantAndBranchWithMaster;
import static com.example.foodapplication.MySQL.MySQLQuerry.GetCities;
import static com.example.foodapplication.MySQL.MySQLQuerry.GetDistrictsWithCity;
import static com.example.foodapplication.MySQL.MySQLQuerry.GetRestaurantInformationForUpdateWithMaster;
import static com.example.foodapplication.MySQL.MySQLQuerry.UpdateRestaurantInformation;
import static com.facebook.FacebookSdk.getApplicationContext;


public class UpdateFragment_Master extends Fragment {

    int master_id;

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

    List<IdWithNameListItem> cityList = new ArrayList<>();
    List<IdWithNameListItem> districtList = new ArrayList<>();

    private final String TAG = "MasterUpdateFragment";

    String encodedImage;

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

    public void SetUpScreen() {
        if(isMasterHasRestaurant == true) {
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

                    imageStore(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imagebytes = stream.toByteArray();

        encodedImage = android.util.Base64.encodeToString(imagebytes, Base64.DEFAULT);
    }

    public void SelectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    public void Run1() {
        cityList = new ArrayList<>();
        ArrayAdapter cityAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, cityList);
        GetCities(cityList, cityAdapter, TAG, getActivity());
        spinner_City_UpdateFragment.setAdapter(cityAdapter);

        spinner_City_UpdateFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IdWithNameListItem spinnerItem = (IdWithNameListItem) spinner_City_UpdateFragment.getSelectedItem();
                int city_id = spinnerItem.getId();
                districtList =  new ArrayList<>();
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                ArrayAdapter districtAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, districtList);
                GetDistrictsWithCity(city_id, districtList, districtAdapter, progressDialog, TAG, getActivity());
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_District_UpdateFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                IdWithNameListItem selectedCity = (IdWithNameListItem) spinner_City_UpdateFragment.getSelectedItem();
                IdWithNameListItem selectedDistrict = (IdWithNameListItem) spinner_District_UpdateFragment.getSelectedItem();
                String name_restaurant = editText_NameRestaurant_UpdateFragment.getText().toString();
                String opening_time = editText_OpeningTime_UpdateFragment.getText().toString() + " AM - " + editText_ClosingTime_UpdateFragment.getText().toString() + " PM";
                String address = editText_Address_UpdateFragment.getText().toString() + ", " + selectedDistrict.getName() + ", " + selectedCity.getName();
                String name_branch = editText_NameBranch_UpdateFragment.getText().toString();

                CreateRestaurantAndBranchWithMaster(name_restaurant, opening_time, encodedImage,
                        address, selectedDistrict.getId(), selectedCity.getId(), 0, 0, 4,
                        name_branch, master_id, textView_Title_UpdateFragment,
                        button_CreateRestaurant_UpdateFragment, editText_NameRestaurant_UpdateFragment, editText_OpeningTime_UpdateFragment,
                        editText_ClosingTime_UpdateFragment, editText_NameBranch_UpdateFragment, editText_Address_UpdateFragment,
                        progressDialog, TAG, getActivity());

            }
        });
    }

    public void Run2() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        cityList = new ArrayList<>();
        ArrayAdapter cityAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, cityList);
        GetCities(cityList, cityAdapter, TAG, getActivity());
        spinner_City_UpdateFragment.setAdapter(cityAdapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GetRestaurantInformationForUpdateWithMaster(master_id, editText_NameRestaurant_UpdateFragment,
                        editText_OpeningTime_UpdateFragment, editText_ClosingTime_UpdateFragment,
                        editText_NameBranch_UpdateFragment, editText_Address_UpdateFragment,
                        spinner_City_UpdateFragment, spinner_District_UpdateFragment,
                        imageView_ImageRestaurant_UpdateFragment, progressDialog, TAG, getActivity());
            }
        }, 1500);


        spinner_City_UpdateFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IdWithNameListItem spinnerItem = (IdWithNameListItem) spinner_City_UpdateFragment.getSelectedItem();
                int city_id = spinnerItem.getId();
                districtList =  new ArrayList<>();
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                ArrayAdapter districtAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, districtList);
                GetDistrictsWithCity(city_id, districtList, districtAdapter, progressDialog, TAG, getActivity());
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_District_UpdateFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                IdWithNameListItem selectedCity = (IdWithNameListItem) spinner_City_UpdateFragment.getSelectedItem();
                IdWithNameListItem selectedDistrict = (IdWithNameListItem) spinner_District_UpdateFragment.getSelectedItem();
                String name_restaurant = editText_NameRestaurant_UpdateFragment.getText().toString();
                String opening_time = editText_OpeningTime_UpdateFragment.getText().toString() + " AM - " + editText_ClosingTime_UpdateFragment.getText().toString() + " PM";
                String address = editText_Address_UpdateFragment.getText().toString() + ", " + selectedDistrict.getName() + ", " + selectedCity.getName();
                String name_branch = editText_NameBranch_UpdateFragment.getText().toString();

                Bitmap bitmap = ((BitmapDrawable) imageView_ImageRestaurant_UpdateFragment.getDrawable()).getBitmap();
                imageStore(bitmap);
                Toast.makeText(getActivity(), encodedImage, Toast.LENGTH_SHORT).show();
                UpdateRestaurantInformation(name_restaurant, opening_time, encodedImage,
                        address, selectedDistrict.getId(), selectedCity.getId(), 0, 0, 4,
                        name_branch, master_id, textView_Title_UpdateFragment,
                        button_CreateRestaurant_UpdateFragment, editText_NameRestaurant_UpdateFragment, editText_OpeningTime_UpdateFragment,
                        editText_ClosingTime_UpdateFragment, editText_NameBranch_UpdateFragment, editText_Address_UpdateFragment,
                        progressDialog, TAG, getActivity());

            }
        });
    }
}