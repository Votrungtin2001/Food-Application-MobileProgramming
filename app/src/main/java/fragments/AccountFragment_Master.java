package fragments;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.foodapplication.account.AccountAbout;
import com.example.foodapplication.AccountSettingsInfoMaster;
import com.example.foodapplication.MySQL.DatabaseHelper;
import com.example.foodapplication.MySQL.FoodManagementContract;
import com.example.foodapplication.MainActivity;
import com.example.foodapplication.R;
import com.example.foodapplication.auth.LoginFragment;
import com.example.foodapplication.auth.LoginFragmentMaster;

public class AccountFragment_Master extends Fragment {

    TextView txtName_Master;
    Button btnSettings_Master, btnAbout_Master, btnLogout_Master;
    DatabaseHelper dbHelper;
    Fragment newFragment;
    Dialog LoginDialog;
    int choose_role = 0;

    int role = 0;
    int namefragment = 0;
    public AccountFragment_Master() {}

    public AccountFragment_Master(int role) {this.role = role;}

    public AccountFragment_Master(int role,int name) {
        this.role = role;
        this.namefragment = namefragment;
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
        dbHelper = new DatabaseHelper(getContext());

        LoginDialog = new Dialog(getActivity());
        LoginDialog.setContentView(R.layout.custom_pop_up_login);

        txtName_Master = view.findViewById(R.id.txtName_Master);
        txtName_Master.setOnClickListener(onNameClick);
        btnSettings_Master = view.findViewById(R.id.btnSettings_Master);
        btnSettings_Master.setOnClickListener(openSettingsFragment);
        btnAbout_Master = view.findViewById(R.id.btnAbout_Master);
        btnAbout_Master.setOnClickListener(openAboutFragment);
        btnLogout_Master = view.findViewById(R.id.btnLogout_Master);
        btnLogout_Master.setOnClickListener(v -> {
            dbHelper.updAllAcountLogOutStatus();
            txtName_Master.setText("Đăng nhập/Đăng ký");
            btnLogout_Master.setVisibility(View.INVISIBLE);
            MainActivity.customer_id = 0;
            MainActivity.master_id = 0;
        });

        if (MainActivity.master_id > 0) {
            Cursor cursor = dbHelper.getMasterById(MainActivity.master_id);
            if (cursor.moveToFirst()) {
                btnLogout_Master.setVisibility(View.VISIBLE);
                txtName_Master.setText(cursor.getString(cursor.getColumnIndexOrThrow(FoodManagementContract.CMaster.KEY_NAME)));
            }
            cursor.close();
        }
        else {
            txtName_Master.setText("Đăng nhập/Đăng ký");
            btnLogout_Master.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    View.OnClickListener onNameClick = v -> {
        if (MainActivity.master_id > 0) {
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
        if (MainActivity.master_id > 0) {
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
            newFragment = new LoginFragmentMaster(choose_role);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup)getView().getParent()).getId(), newFragment, null)
                    .addToBackStack(null)
                    .commit();
            LoginDialog.dismiss();
        });
        LoginDialog.show();
    }
}