package com.example.foodapplication.account.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;
import com.example.foodapplication.auth.LoginFragment;
import com.example.foodapplication.auth.LoginFragmentMaster;

import java.util.HashMap;
import java.util.Map;

import static com.example.foodapplication.MainActivity.master_id;
import static com.example.foodapplication.MySQL.MySQLQuerry.LogOutAccount;
import static com.example.foodapplication.Master_MainActivity.isMasterHasRestaurant;
import static com.example.foodapplication.MySQL.MySQLQuerry.SetMasterAccountInformationInMasterAccountFragment;

public class AccountFragment_Master extends Fragment {

    TextView txtName_Master;
    Button btnSettings_Master, btnAbout_Master, btnLogout_Master;
    ImageView imgMaster;
    Fragment newFragment;
    Dialog LoginDialog;
    int choose_role = 0;

    int role = 0;
    int namefragment = 0;
    private final String TAG = "AccountFragmentMaster";
    public AccountFragment_Master() {}

    public AccountFragment_Master(int role) {this.role = role;}

    public AccountFragment_Master(int role,int name) {
        this.role = role;
        this.namefragment = name;
    }

    public static AccountFragment_Master newInstance() {
        return new AccountFragment_Master();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account__master, container, false);

        initComponents(view);

        Run();

        return view;
    }

    private void initComponents(View view) {
        LoginDialog = new Dialog(getActivity());
        LoginDialog.setContentView(R.layout.custom_pop_up_login);

        txtName_Master = view.findViewById(R.id.txtName_Master);
        txtName_Master.setOnClickListener(onNameClick);
        imgMaster = view.findViewById(R.id.imgMaster);
        btnSettings_Master = view.findViewById(R.id.btnSettings_Master);
        btnSettings_Master.setOnClickListener(openSettingsFragment);
        btnAbout_Master = view.findViewById(R.id.btnAbout_Master);
        btnAbout_Master.setOnClickListener(openAboutFragment);
        btnLogout_Master = view.findViewById(R.id.btnLogout_Master);
    }

    private void Run() {
        btnLogout_Master.setOnClickListener(v -> {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            LogOutAccount(txtName_Master, imgMaster, btnLogout_Master, progressDialog, TAG, getActivity());
            MainActivity.customer_id = 0;
            master_id = 0;
        });

        if (master_id > 0) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            CheckMasterHasAddress(master_id);
            SetMasterAccountInformationInMasterAccountFragment(master_id, txtName_Master, btnLogout_Master, TAG, getActivity());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 1500);
        }
        else {
            txtName_Master.setText("Đăng nhập/Đăng ký");
            btnLogout_Master.setVisibility(View.INVISIBLE);
            imgMaster.setImageResource(0);
        }
    }

    View.OnClickListener onNameClick = v -> {
        if (master_id > 0) {
            Fragment fragment = new AccountSettingsInfoMaster();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), fragment, null)
                    .addToBackStack(null)
                    .commit();
        }
        else
            ShowPopUpLogin(v);
    };

    View.OnClickListener openSettingsFragment = v -> {
        if (master_id > 0) {
            newFragment = new AccountSettingsInfoMaster();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), newFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
        else
            Toast.makeText(getContext(), "Bạn không thể dùng chức năng này vì bạn chưa đăng nhập.", Toast.LENGTH_LONG).show();
    };

    View.OnClickListener openAboutFragment = v -> {
        newFragment = new AccountAbout();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                .addToBackStack(null)
                .commit();
    };

    public void ShowPopUpLogin(View v) {
        TextView textView_Close;
        textView_Close = (TextView) LoginDialog.findViewById(R.id.Close_PopUpLogin);
        textView_Close.setOnClickListener(v1 -> LoginDialog.dismiss());

        ImageView imageView_CustomerOption;
        imageView_CustomerOption = (ImageView) LoginDialog.findViewById(R.id.ImageView_Customer_PopUpLogin);
        imageView_CustomerOption.setOnClickListener(v12 -> {
            choose_role = 1;
            namefragment = 2;
            newFragment = new LoginFragment(namefragment,choose_role);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                    .addToBackStack(null)
                    .commit();
            LoginDialog.dismiss();

        });

        ImageView imageView_MasterOption;
        imageView_MasterOption = (ImageView) LoginDialog.findViewById(R.id.ImageView_Master_PopUpLogin);
        imageView_MasterOption.setOnClickListener(v13 -> {
            choose_role = 2;
            namefragment = 2;
            newFragment = new LoginFragmentMaster(namefragment, choose_role);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                    .addToBackStack(null)
                    .commit();
            LoginDialog.dismiss();
        });
        LoginDialog.show();
    }

    private void CheckMasterHasAddress(int master_id) {
        String url = "https://foodapplicationmobile.000webhostapp.com/checkMasterHasAddress.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String announcement = "";
                if(response.toString().trim().equals("true")) {
                    isMasterHasRestaurant = true;
                }
                else isMasterHasRestaurant = false;
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
                params.put("master_id", String.valueOf(master_id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}