package com.example.foodapplication.MySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.HomeFragment.adapter.AllRestaurantAdapter;
import com.example.foodapplication.HomeFragment.adapter.CollectionAdapter;
import com.example.foodapplication.HomeFragment.adapter.DiscountComboProductAdapter;
import com.example.foodapplication.HomeFragment.adapter.KindOfRestaurantAdapter;
import com.example.foodapplication.HomeFragment.adapter.ListCollectionAdapter;
import com.example.foodapplication.HomeFragment.adapter.ListRestaurantAdapter;
import com.example.foodapplication.HomeFragment.adapter.MenuAdapter;
import com.example.foodapplication.HomeFragment.adapter.ProductWithCategoryAdapter;
import com.example.foodapplication.HomeFragment.adapter.SortOfProductAdapter;
import com.example.foodapplication.HomeFragment.model.AllRestaurantModel;
import com.example.foodapplication.HomeFragment.model.CollectionModel;
import com.example.foodapplication.HomeFragment.model.ImageSliderModel;
import com.example.foodapplication.HomeFragment.model.KindOfRestaurantModel;
import com.example.foodapplication.HomeFragment.model.ProductCategoryModel;
import com.example.foodapplication.HomeFragment.model.ProductModel;
import com.example.foodapplication.HomeFragment.model.SortOfProductModel;
import com.example.foodapplication.IdWithNameListItem;
import com.example.foodapplication.R;
import com.example.foodapplication.Transaction;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.TransactionAdapter;

import static com.example.foodapplication.MainActivity.customer_id;
import static com.example.foodapplication.MainActivity.master_id;

public class MySQLQuerry {

    public static void GetDataForAllRestaurants(int id, List<AllRestaurantModel> list, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getAllRestaurants.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String name = object.getString("NAME");
                            String address = object.getString("ADDRESS");

                            AllRestaurantModel allRestaurantModel = new AllRestaurantModel(id, image, name, address);
                            list.add(allRestaurantModel);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("district_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetDataForAllRestaurants(int id, List<AllRestaurantModel> list, String TAG, Context context, AllRestaurantAdapter adapter) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getAllRestaurants.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int count = 0;
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            if(count < 10) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                int id = object.getInt("_ID");
                                String image = object.getString("IMAGE");
                                String name = object.getString("NAME");
                                String address = object.getString("ADDRESS");

                                AllRestaurantModel allRestaurantModel = new AllRestaurantModel(id, image, name, address);
                                list.add(allRestaurantModel);
                                adapter.notifyDataSetChanged();
                                count++;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("district_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetDataForAllRestaurants(int id, List<AllRestaurantModel> list, String TAG, Context context, ListRestaurantAdapter adapter) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getAllRestaurants.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String name = object.getString("NAME");
                            String address = object.getString("ADDRESS");

                            AllRestaurantModel allRestaurantModel = new AllRestaurantModel(id, image, name, address);
                            list.add(allRestaurantModel);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("district_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetDataForAllImageSliders(List<ImageSliderModel> list, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getAllImageSliders.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String name = object.getString("NAME");
                            String description = object.getString("DESCRIPTION");
                            ImageSliderModel imageSliderModel = new ImageSliderModel(id, image, name, description);
                            list.add(imageSliderModel);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetDataForAllCollections(List<CollectionModel> list, CollectionAdapter adapter, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getAllCollections.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String name = object.getString("NAME");
                            String description = object.getString("DESCRIPTION");
                            CollectionModel collectionModel = new CollectionModel(id, image, name, description);
                            list.add(collectionModel);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetDataForAllCollections(List<CollectionModel> list, ListCollectionAdapter adapter, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getAllCollections.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String name = object.getString("NAME");
                            String description = object.getString("DESCRIPTION");
                            CollectionModel collectionModel = new CollectionModel(id, image, name, description);
                            list.add(collectionModel);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetDataForDiscountComboProductAndCheapestProduct(int id, List<SortOfProductModel> list1, List<SortOfProductModel> list2,
                                                                  DiscountComboProductAdapter adapter1, DiscountComboProductAdapter adapter2,
                                                                  String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getAllProducts.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list1.clear();
                list2.clear();
                String key = "Combo";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String product_name = object.getString("PNAME");
                            String branch_name = object.getString("BNAME");
                            double price = object.getDouble("PRICE");
                            int category = object.getInt("CATEGORY");


                            if (product_name.toLowerCase().contains(key.toLowerCase())) {
                                int count = 0;
                                if (count < 10) {
                                    SortOfProductModel sortOfProductModel = new SortOfProductModel(image, product_name, branch_name, price, id);
                                    list1.add(sortOfProductModel);
                                    adapter1.notifyDataSetChanged();
                                    count++;
                                }
                            }

                            if ((price < 20000 && price >= 15000) && category != 7 && category != 18) {
                                int count = 0;
                                if (count < 10) {
                                    SortOfProductModel sortOfProductModel = new SortOfProductModel(image, product_name, branch_name, price, id);
                                    list2.add(sortOfProductModel);
                                    adapter2.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("district_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetDataForDiscountComboProduct(int id, List<SortOfProductModel> list, SortOfProductAdapter adapter, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getAllProducts.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                String key = "Combo";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String product_name = object.getString("PNAME");
                            String branch_name = object.getString("BNAME");
                            double price = object.getDouble("PRICE");

                            if (product_name.toLowerCase().contains(key.toLowerCase())) {
                                SortOfProductModel sortOfProductModel = new SortOfProductModel(image, product_name, branch_name, price, id);
                                list.add(sortOfProductModel);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("district_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetDataForCheapestProduct(int id, List<SortOfProductModel> list, SortOfProductAdapter adapter, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getAllProducts.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String product_name = object.getString("PNAME");
                            String branch_name = object.getString("BNAME");
                            double price = object.getDouble("PRICE");
                            int category = object.getInt("CATEGORY");

                            if ((price < 20000 && price >= 15000) && category != 7 && category != 18) {
                                SortOfProductModel sortOfProductModel = new SortOfProductModel(image, product_name, branch_name, price, id);
                                list.add(sortOfProductModel);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("district_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetProductsWithCategory(int id, int district_id, List<ProductCategoryModel> list,
                                               ProductWithCategoryAdapter adapter, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getProductsWithCategory.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String product_name = object.getString("PNAME");
                            String branch_name = object.getString("BNAME");
                            String description = object.getString("PDESCRIPTION");

                            ProductCategoryModel productCategoryModel = new ProductCategoryModel(image, product_name, description, branch_name, id);
                            list.add(productCategoryModel);
                            adapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("category_id", String.valueOf(id));
                params.put("district_id", String.valueOf(district_id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetCategoryName(int id, TextView textView, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCategoryName.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String category_name = "";
                Log.e(TAG, response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            category_name = object.getString("DESCRIPTION");
                            if(!category_name.trim().equals("")) textView.setText(category_name);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("category_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetRestaurantImage(int id, ImageView imageView, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getImageRestaurant.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String image_restaurant = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            image_restaurant = object.getString("IMAGE");
                            if(image_restaurant.trim().equals("")){
                                imageView.setImageResource(R.drawable.noimage_restaurant);
                            }else {
                                Picasso.get ().load (image_restaurant)
                                        .placeholder(R.drawable.noimage_restaurant)
                                        .error(R.drawable.error)
                                        .into(imageView);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetBranchName(int id, TextView textView, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getBranchName.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String branch_name = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            branch_name = object.getString("NAME");
                            if(!branch_name.trim().equals("")) textView.setText(branch_name);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetDataForKindOfRestaurantWithType(int id, List<KindOfRestaurantModel> list, KindOfRestaurantAdapter adapter,
                                                          int number, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getAllRestaurants.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String name = object.getString("NAME");
                            String address = object.getString("ADDRESS");
                            String openingtime = object.getString("OPENING_TIMES");

                            if(id % number == 0) {
                                KindOfRestaurantModel kindOfRestaurantModel = new KindOfRestaurantModel(image, name, address, openingtime, id);
                                list.add(kindOfRestaurantModel);
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("district_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetProducts(int id, List<ProductModel> list, MenuAdapter adapter, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getProducts.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String image = object.getString("IMAGE");
                            String name = object.getString("NAME");
                            String product_description = object.getString("PDESCRIPTION");
                            String menu_description = object.getString("MDESCRIPTION");
                            double price = object.getDouble("PRICE");
                            int menu_id = object.getInt("MID");

                            ProductModel productModel = new ProductModel(image, name, product_description, menu_description, price, id, menu_id);
                            list.add(productModel);
                            adapter.notifyDataSetChanged();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetBranchAddress(int id, TextView textView, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getBranchAddress.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String branch_address = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            branch_address = object.getString("ADDRESS");
                            if(!branch_address.trim().equals("")) textView.setText(branch_address);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetOpeningTime(int id, TextView textView, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getRestaurantOpeningTime.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String restaurant_openingtime = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            restaurant_openingtime = object.getString("OPENING_TIMES");
                            if(!restaurant_openingtime.trim().equals("")) textView.setText("Giờ mở cửa \t" + restaurant_openingtime);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("branch_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void CreateCustomerAccount(String name, String email, String password,
                                             EditText editText1, EditText editText2, EditText editText3, EditText editText4,
                                             ProgressDialog progressDialog,
                                             String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/createCustomerAccount.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Registered successfully")) {
                    announcement = "Đã tạo tài khoản thành công!!!";
                }
                else if(response.toString().trim().equals("This account was existed!")) {
                   announcement = "Email này đã được đăng ký trước đó!!!";
                }
                else announcement = "Lỗi kết nối mạng!!!";
                Toast.makeText(context, announcement, Toast.LENGTH_SHORT).show();
                editText1.setText(null);
                editText2.setText(null);
                editText3.setText(null);
                editText4.setText(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", String.valueOf(email));
                params.put("password", String.valueOf(password));
                params.put("name", String.valueOf(name));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void CreateMasterAccount(String name, String email, String password,
                                             EditText editText1, EditText editText2, EditText editText3, EditText editText4,
                                             ProgressDialog progressDialog,
                                             String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/createMasterAccount.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Registered successfully")) {
                    announcement = "Đã tạo tài khoản thành công!!!";
                }
                else if(response.toString().trim().equals("This account was existed!")) {
                    announcement = "Email này đã được đăng ký trước đó!!!";
                }
                else announcement = "Lỗi kết nối mạng!!!";
                Toast.makeText(context, announcement, Toast.LENGTH_SHORT).show();
                editText1.setText(null);
                editText2.setText(null);
                editText3.setText(null);
                editText4.setText(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", String.valueOf(email));
                params.put("password", String.valueOf(password));
                params.put("name", String.valueOf(name));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void SetCustomerAccountInformationInAccountFragment(int id, TextView textView, ImageView imageView, Button button, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAccountInformation.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("NAME");
                            int gender = object.getInt("GENDER");

                            textView.setText(name);
                            button.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);

                            switch (gender) {
                                case 0:
                                    imageView.setImageResource(R.drawable.avatar_male);
                                    break;
                                case 1:
                                    imageView.setImageResource(R.drawable.avatar_female);
                                    break;
                                default:
                                    imageView.setImageResource(R.drawable.avatar_none);
                                    break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void LogOutCustomerAccount(TextView textView, ImageView imageView, Button button, ProgressDialog progressDialog, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getLogOutAccount.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Logged out successfully")) {
                    announcement = "Đăng xuất thành công!!!";
                    imageView.setImageResource(0);
                    textView.setText("Đăng nhập/Đăng ký");
                    button.setVisibility(View.VISIBLE);
                    customer_id = 0;
                    master_id = 0;
                }
                else announcement = "Đăng xuất không thành công!!!";
                Toast.makeText(context, announcement, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetTransactionHistory(int id, List<Transaction> list, TransactionAdapter adapter, ProgressDialog progressDialog, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getTransactionHistory.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            double credit = object.getInt("CREDIT");
                            String date = object.getString("DATE");

                            Transaction transaction = new Transaction(credit, date);
                            list.add(transaction);
                            adapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetCities(List<IdWithNameListItem> list, ArrayAdapter adapter, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCities.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String name = object.getString("NAME");

                            IdWithNameListItem item = new IdWithNameListItem(id, name);
                            list.add(item);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetDistrictsWithCity(int id, List<IdWithNameListItem> list, ArrayAdapter adapter, ProgressDialog progressDialog, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getDistrictsWithCity.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("_ID");
                            String name = object.getString("NAME");

                            IdWithNameListItem item = new IdWithNameListItem(id, name);
                            list.add(item);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("city_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetCustomerPhone(int id, EditText editText, ProgressDialog progressDialog, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAccountInformation.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String phone = object.getString("PHONE");
                            if(phone.trim().equals("null") || phone.trim().equals("")) editText.setHint("Chưa có thông tin...");
                            else editText.setHint(phone);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetCustomerPhone(int id, TextView textView, ProgressDialog progressDialog, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAccountInformation.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String phone = object.getString("PHONE");
                            if(phone.trim().equals("null") || phone.trim().equals("")) textView.setText("Chưa có thông tin...");
                            else textView.setText("Điện thoại: " + phone);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetCustomerName(int id, EditText editText, ProgressDialog progressDialog, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAccountInformation.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("NAME");
                            if(name.trim().equals("null") || name.trim().equals("")) editText.setHint("Chưa có thông tin...");
                            else editText.setHint(name);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetCustomerName(int id, TextView textView, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAccountInformation.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("NAME");
                            if(name.trim().equals("null") || name.trim().equals("")) textView.setText("Chưa có thông tin...");
                            else textView.setText("Tên liên lạc: " + name);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetCustomerEmail(int id, EditText editText, ProgressDialog progressDialog, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAccountInformation.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String email = object.getString("EMAIL");
                            if(email.trim().equals("null") || email.trim().equals("")) editText.setHint("Chưa có thông tin...");
                            else editText.setHint(email);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void GetCustomerUsername(int id, TextView textView, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/getCustomerAccountInformation.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if(success.equals("1")) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String username = object.getString("USERNAME");
                            if(username.trim().equals("null") || username.trim().equals("")) textView.setText("Chưa có thông tin...");
                            else textView.setText(username);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void UpdateCustomerGender(int customer_id, int gender, ProgressDialog progressDialog, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateCustomerGender.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "Cập nhật giới tính thành công!";
                }
                else announcement = "Việc cập nhật giới tính thất bại!!!";
                Toast.makeText(context, announcement, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(customer_id));
                params.put("gender", String.valueOf(gender));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void UpdateCustomerDOB(int customer_id, String date, ProgressDialog progressDialog, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateCustomerDOB.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "Cập nhật ngày sinh thành công!";
                }
                else announcement = "Việc cập nhật ngày sinh thất bại!!!";
                Toast.makeText(context, announcement, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(customer_id));
                params.put("dob", String.valueOf(date));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void UpdateCustomerOccupation(int customer_id, String occupation, ProgressDialog progressDialog, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateCustomerOccupation.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "Cập nhật việc làm thành công!";
                }
                else announcement = "Việc cập nhật việc làm thất bại!!!";
                Toast.makeText(context, announcement, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(customer_id));
                params.put("occupation", String.valueOf(occupation));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void UpdateCustomerPassword(int customer_id, String password, ProgressDialog progressDialog, String TAG, Context context) {
        String url = "https://foodapplicationmobile.000webhostapp.com/updateCustomerPassword.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String announcement = "";
                if(response.toString().trim().equals("Successfully")) {
                    announcement = "Cập nhật mật khẩu thành công!";
                }
                else announcement = "Việc cập nhật mật khẩu thất bại!!!";
                Toast.makeText(context, announcement, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_id", String.valueOf(customer_id));
                params.put("password", String.valueOf(password));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }




}
