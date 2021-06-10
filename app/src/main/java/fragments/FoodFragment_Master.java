package fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.foodapplication.DatabaseHelper;
import com.example.foodapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class FoodFragment_Master extends Fragment {

    int master_id;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

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

    List<String> categoryList = new ArrayList<>();

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int REQUEST_STORAGE = 1001;

    int category_id;
    int branch_id;
    int restaurant_id;


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

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

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

    public List<String> getAllCategoryInSQLite() {
        List<String> categoryList = new ArrayList<>();

        String selectQuery = "SELECT * FROM CATEGORIES";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String category_name = cursor.getString(cursor.getColumnIndex("Name"));
                String category_description = cursor.getString(cursor.getColumnIndex("Description"));
                String category_item = category_name + " - " +category_description;
                categoryList.add(category_item);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return categoryList;
    }

    public void SetUpScreen() {
        boolean checkMasterHasRestaurant = CheckMasterHasRestaurant_Is_Right(master_id);
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
        categoryList = getAllCategoryInSQLite();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categoryList);
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

        spinner_Category_FoodFragment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_id = position + 1;
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
                int price = Integer.parseInt(sPrice);
                branch_id = getBranchID(master_id);

                databaseHelper.addProduct(name_product, description, price, imageViewToByte(imageView_ImageProduct_FoodFragment));
                int new_product_id = getCountProduct();
                restaurant_id = getRestaurantID(branch_id);

                databaseHelper.addMenu(restaurant_id, new_product_id, "", price);

                editText_NameProduct_FoodFragment.setText("");
                editText_Description_FoodFragment.setText("");
                editText_Price_FoodFragment.setText("");
                button_AddFood_FoodFragment.setEnabled(false);

            }
        });
    }

    public byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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


    public int getCountProduct() {
        int count = 0;
        String selectQuery = "SELECT * FROM PRODUCTS";
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

}