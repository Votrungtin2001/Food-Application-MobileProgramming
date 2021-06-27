package com.example.foodapplication.FoodFragment_Master;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import com.example.foodapplication.account.model.IdWithNameListItem;
import com.example.foodapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.foodapplication.Master_MainActivity.isMasterHasRestaurant;
import static com.example.foodapplication.MySQL.MySQLQuerry.AddProductAndMenu;
import static com.example.foodapplication.MySQL.MySQLQuerry.GetCategories;
import static com.facebook.FacebookSdk.getApplicationContext;


public class FoodFragment_Master extends Fragment {

    int master_id;

    TextView textView_Announcement_FoodFragment;
    TextView textView_Title_FoodFragment;
    TextView textView_NameProduct_FoodFragment;
    TextView textView_Description_FoodFraagment;
    TextView textView_Category_FoodFragment;
    TextView textView_ImageProduct_FoodFragment;
    TextView textView_Price_FoodFragment;

    EditText editText_NameProduct_FoodFragment;
    EditText editText_Description_FoodFragment;
    EditText editText_Price_FoodFragment;

    ImageView imageView_NoRestaurant_FoodFragment;
    ImageView imageView_ImageProduct_FoodFragment;

    Spinner spinner_Category_FoodFragment;

    Button button_ImageProduct_FoodFragment;
    Button button_AddFood_FoodFragment;

    ConstraintLayout constraintLayout;
    ConstraintLayout constraintLayout1;

    ArrayList<IdWithNameListItem> categoryList = new ArrayList<>();
    private final String TAG = "MasterFoodFragment";
    String encodedImage;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int REQUEST_STORAGE = 1001;

    Dialog AnnouncementDialog;


    public FoodFragment_Master() {
        // Required empty public constructor
    }

    public FoodFragment_Master(int id) {
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
        View view = inflater.inflate(R.layout.fragment_food__master, container, false);

        initComponents(view);

        SetUpScreen();

        return view;
    }

    public void initComponents(View view) {
        textView_Announcement_FoodFragment = (TextView) view.findViewById(R.id.TextView_Announcement_FoodFragment);
        imageView_NoRestaurant_FoodFragment = (ImageView) view.findViewById(R.id.ImageView_NoRestaurant_FoodFragment);

        textView_Title_FoodFragment = (TextView) view.findViewById(R.id.Title_FoodFragment);
        textView_NameProduct_FoodFragment = (TextView) view.findViewById(R.id.Label_NameProduct_FoodFragment);
        textView_Description_FoodFraagment = (TextView) view.findViewById(R.id.Label_Description_FoodFragment);
        textView_Category_FoodFragment = (TextView) view.findViewById(R.id.Label_Category_FoodFragment);
        textView_ImageProduct_FoodFragment = (TextView) view.findViewById(R.id.Label_ImageProduct_FoodFragment);
        textView_Price_FoodFragment = (TextView) view.findViewById(R.id.Label_Price_FoodFragment);

        imageView_ImageProduct_FoodFragment = (ImageView) view.findViewById(R.id.ImageView_ImageProduct_FoodFragment);

        editText_NameProduct_FoodFragment = (EditText) view.findViewById(R.id.EditText_NameProduct_FoodFragment);
        editText_Description_FoodFragment = (EditText) view.findViewById(R.id.EditText_Description_FoodFragment);
        editText_Price_FoodFragment = (EditText) view.findViewById(R.id.EditText_Price_FoodFragment);

        spinner_Category_FoodFragment = (Spinner) view.findViewById(R.id.Spinner_Category_FoodFragment);

        button_ImageProduct_FoodFragment = (Button) view.findViewById(R.id.Button_ImageProduct_FoodFragment);
        button_AddFood_FoodFragment = (Button) view.findViewById(R.id.Button_AddFood_FoodFragment);

        constraintLayout = (ConstraintLayout) view.findViewById(R.id.ConstraintLayout_Spinner_FoodFragment);
        constraintLayout1 = (ConstraintLayout) view.findViewById(R.id.ConstraintLayout_Components_FoodFragment);

        AnnouncementDialog = new Dialog(getActivity());
        View view1  = LayoutInflater.from(getActivity()).inflate(R.layout.custom_popup_require_login, null);
        AnnouncementDialog.setContentView(view1);
    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imagebytes = stream.toByteArray();

        encodedImage = android.util.Base64.encodeToString(imagebytes, Base64.DEFAULT);
    }

    public void SetUpScreen() {
        textView_Announcement_FoodFragment.setVisibility(View.VISIBLE);
        imageView_NoRestaurant_FoodFragment.setVisibility(View.VISIBLE);

        textView_Title_FoodFragment.setVisibility(View.GONE);
        textView_NameProduct_FoodFragment.setVisibility(View.GONE);
        textView_Description_FoodFraagment.setVisibility(View.GONE);
        textView_Category_FoodFragment.setVisibility(View.GONE);
        textView_ImageProduct_FoodFragment.setVisibility(View.GONE);
        textView_Price_FoodFragment.setVisibility(View.GONE);

        imageView_ImageProduct_FoodFragment.setVisibility(View.GONE);

        editText_NameProduct_FoodFragment.setVisibility(View.GONE);
        editText_Description_FoodFragment.setVisibility(View.GONE);
        editText_Price_FoodFragment.setVisibility(View.GONE);

        spinner_Category_FoodFragment.setVisibility(View.GONE);

        button_ImageProduct_FoodFragment.setVisibility(View.GONE);
        button_AddFood_FoodFragment.setVisibility(View.GONE);

        constraintLayout.setVisibility(View.GONE);
        constraintLayout1.setVisibility(View.GONE);

        boolean checkMasterHasRestaurant = isMasterHasRestaurant;
        if(checkMasterHasRestaurant == true) {
            textView_Announcement_FoodFragment.setVisibility(View.GONE);
            imageView_NoRestaurant_FoodFragment.setVisibility(View.GONE);

            textView_Title_FoodFragment.setVisibility(View.VISIBLE);
            textView_NameProduct_FoodFragment.setVisibility(View.VISIBLE);
            textView_Description_FoodFraagment.setVisibility(View.VISIBLE);
            textView_Category_FoodFragment.setVisibility(View.VISIBLE);
            textView_ImageProduct_FoodFragment.setVisibility(View.VISIBLE);
            textView_Price_FoodFragment.setVisibility(View.VISIBLE);

            imageView_ImageProduct_FoodFragment.setVisibility(View.VISIBLE);

            editText_NameProduct_FoodFragment.setVisibility(View.VISIBLE);
            editText_Description_FoodFragment.setVisibility(View.VISIBLE);
            editText_Price_FoodFragment.setVisibility(View.VISIBLE);

            spinner_Category_FoodFragment.setVisibility(View.VISIBLE);

            button_ImageProduct_FoodFragment.setVisibility(View.VISIBLE);
            button_AddFood_FoodFragment.setVisibility(View.VISIBLE);

            constraintLayout.setVisibility(View.VISIBLE);
            constraintLayout1.setVisibility(View.VISIBLE);

            Run();

        }
        else {
            textView_Announcement_FoodFragment.setVisibility(View.VISIBLE);
            imageView_NoRestaurant_FoodFragment.setVisibility(View.VISIBLE);

            textView_Title_FoodFragment.setVisibility(View.GONE);
            textView_NameProduct_FoodFragment.setVisibility(View.GONE);
            textView_Description_FoodFraagment.setVisibility(View.GONE);
            textView_Category_FoodFragment.setVisibility(View.GONE);
            textView_ImageProduct_FoodFragment.setVisibility(View.GONE);
            textView_Price_FoodFragment.setVisibility(View.GONE);

            imageView_ImageProduct_FoodFragment.setVisibility(View.GONE);

            editText_NameProduct_FoodFragment.setVisibility(View.GONE);
            editText_Description_FoodFragment.setVisibility(View.GONE);
            editText_Price_FoodFragment.setVisibility(View.GONE);

            spinner_Category_FoodFragment.setVisibility(View.GONE);

            button_ImageProduct_FoodFragment.setVisibility(View.GONE);
            button_AddFood_FoodFragment.setVisibility(View.GONE);

            constraintLayout.setVisibility(View.GONE);
            constraintLayout1.setVisibility(View.GONE);

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
                    imageView_ImageProduct_FoodFragment.setImageBitmap(bitmap);

                    imageStore(bitmap);

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

    public void Run() {
        categoryList = new ArrayList<>();
        ArrayAdapter categoryAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, categoryList);
        GetCategories(categoryList, categoryAdapter, TAG, getActivity());
        spinner_Category_FoodFragment.setAdapter(categoryAdapter);

        button_ImageProduct_FoodFragment.setOnClickListener(new View.OnClickListener() {
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

        imageView_ImageProduct_FoodFragment.setOnClickListener(new View.OnClickListener() {
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

        editText_NameProduct_FoodFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameProduct_FoodFragment.getText().toString().trim().equals("") &&
                        !editText_Description_FoodFragment.getText().toString().trim().equals("") &&
                        !editText_Price_FoodFragment.getText().toString().trim().equals("") &&
                        spinner_Category_FoodFragment.getSelectedItem() != null) {
                    button_AddFood_FoodFragment.setEnabled(true);
                }
                else button_AddFood_FoodFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_Description_FoodFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameProduct_FoodFragment.getText().toString().trim().equals("") &&
                        !editText_Description_FoodFragment.getText().toString().trim().equals("") &&
                        !editText_Price_FoodFragment.getText().toString().trim().equals("") &&
                        spinner_Category_FoodFragment.getSelectedItem() != null) {
                    button_AddFood_FoodFragment.setEnabled(true);
                }
                else button_AddFood_FoodFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_Price_FoodFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_NameProduct_FoodFragment.getText().toString().trim().equals("") &&
                        !editText_Description_FoodFragment.getText().toString().trim().equals("") &&
                        !editText_Price_FoodFragment.getText().toString().trim().equals("") &&
                        spinner_Category_FoodFragment.getSelectedItem() != null) {
                    button_AddFood_FoodFragment.setEnabled(true);
                }
                else button_AddFood_FoodFragment.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_Price_FoodFragment.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        spinner_Category_FoodFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!editText_NameProduct_FoodFragment.getText().toString().trim().equals("") &&
                        !editText_Description_FoodFragment.getText().toString().trim().equals("") &&
                        !editText_Price_FoodFragment.getText().toString().trim().equals("") &&
                        spinner_Category_FoodFragment.getSelectedItem() != null) {
                    button_AddFood_FoodFragment.setEnabled(true);
                }
                else button_AddFood_FoodFragment.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_AddFood_FoodFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_product = editText_NameProduct_FoodFragment.getText().toString();
                String description = editText_Description_FoodFragment.getText().toString();
                String sPrice = editText_Price_FoodFragment.getText().toString();
                double price = Double.parseDouble(sPrice);
                IdWithNameListItem spinnerItem = (IdWithNameListItem) spinner_Category_FoodFragment.getSelectedItem();
                int category_id = spinnerItem.getId();

                Bitmap bitmap = ((BitmapDrawable) imageView_ImageProduct_FoodFragment.getDrawable()).getBitmap();
                imageStore(bitmap);
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                AddProductAndMenu(name_product, description, category_id, encodedImage, price, master_id,
                        editText_NameProduct_FoodFragment, editText_Description_FoodFragment, editText_Price_FoodFragment,
                        button_AddFood_FoodFragment, progressDialog, TAG, getActivity());
            }
        });
    }

}