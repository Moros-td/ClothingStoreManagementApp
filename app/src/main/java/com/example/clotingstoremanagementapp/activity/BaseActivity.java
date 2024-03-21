package com.example.clotingstoremanagementapp.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.fragment.CategoryFragment;
import com.example.clotingstoremanagementapp.fragment.CustomerFragment;
import com.example.clotingstoremanagementapp.fragment.HomeFragment;
import com.example.clotingstoremanagementapp.fragment.OrderFragment;
import com.example.clotingstoremanagementapp.fragment.OrderHistoryFragment;
import com.example.clotingstoremanagementapp.fragment.ProductFragment;
import com.example.clotingstoremanagementapp.fragment.StaffFragment;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;
import com.example.clotingstoremanagementapp.response.ErrResponse;
import com.example.clotingstoremanagementapp.response.LoginResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends InterceptorActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private NavigationView navigationView;

    private TextView pageName;

    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_CATEGORY = 2;
    public static final int FRAGMENT_PRODUCT = 3;
    public static final int FRAGMENT_CUSTOMER = 4;
    public static final int FRAGMENT_ORDER = 5;
    public static final int FRAGMENT_ORDER_HISTORY = 6;
    public static final int FRAGMENT_STAFF = 7;

    public int currentFragment = FRAGMENT_HOME;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setView();
        setEvent();

        // set fragment default
        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    private void setView() {
        //setup toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setup toggle drawer
        drawerLayout = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//
//        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        pageName = findViewById(R.id.pageName);
    }


    private void setEvent() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Xử lý sự kiện khi một mục được chọn trong NavigationView
                int id = item.getItemId();

                if(id == R.id.nav_home){
                    openFragment(FRAGMENT_HOME);
                } else if(id == R.id.nav_category){
                    openFragment(FRAGMENT_CATEGORY);
                } else if(id == R.id.nav_product){
                    openFragment(FRAGMENT_PRODUCT);
                } else if(id == R.id.nav_order){
                    openFragment(FRAGMENT_ORDER);
                } else if(id == R.id.nav_orderHistory){
                    openFragment(FRAGMENT_ORDER_HISTORY);
                } else if(id == R.id.nav_staff){
                    openFragment(FRAGMENT_STAFF);
                } else if(id == R.id.nav_customer){
                    openFragment(FRAGMENT_CUSTOMER);
                }
                else if(id == R.id.nav_logout){
                    dialog = BaseActivity.openLoadingDialog(BaseActivity.this);
                    callApiLogout();
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Xử lý khi người dùng bấm nút back

                // kiểm tra nếu drawer đang mở
                if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
                else{
                    // nếu có fragment trong backstack thì pop
                    // Thực hiện hành động mặc định khi không có fragment trên back stack
                    finish();
                }
            }
        };

        // Đăng ký callback với dispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.toolbar_menu){
            drawerLayout.openDrawer(GravityCompat.END);
        }
        return true;
    }

    public void openFragment(int fragment) {
        if (currentFragment != fragment) {
            switch (fragment) {
                case FRAGMENT_HOME:
                    replaceFragment(new HomeFragment());
                    currentFragment = FRAGMENT_HOME;
                    pageName.setText(R.string.menu_home);
                    break;
                case FRAGMENT_PRODUCT:
                    replaceFragment(new ProductFragment());
                    currentFragment = FRAGMENT_PRODUCT;
                    pageName.setText(R.string.menu_product);
                    break;
                case FRAGMENT_CATEGORY:
                    replaceFragment(new CategoryFragment());
                    currentFragment = FRAGMENT_CATEGORY;
                    pageName.setText(R.string.menu_category);
                    break;
                case FRAGMENT_ORDER:
                    replaceFragment(new OrderFragment());
                    currentFragment = FRAGMENT_ORDER;
                    pageName.setText(R.string.menu_order);
                    break;
                case FRAGMENT_ORDER_HISTORY:
                    replaceFragment(new OrderHistoryFragment());
                    currentFragment = FRAGMENT_ORDER_HISTORY;
                    pageName.setText(R.string.menu_orderHistory);
                    break;
                case FRAGMENT_STAFF:
                    replaceFragment(new StaffFragment());
                    currentFragment = FRAGMENT_STAFF;
                    pageName.setText(R.string.menu_account);
                    break;
                case FRAGMENT_CUSTOMER:
                    replaceFragment(new CustomerFragment());
                    currentFragment = FRAGMENT_CUSTOMER;
                    pageName.setText(R.string.menu_customer);
                    break;
            }
        }
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void openSuccessDialog(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);

        // setView trên dialog
        TextView successTextView = dialog.findViewById(R.id.successTextView);

        successTextView.setText(message);

        // Khởi tạo Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Đóng dialog sau 3 giây
                dialog.dismiss();
            }
        }, 3000); // Thời gian đợi 3 giây trước khi đóng dialog

        // click ra ngoài thì tắt dialog
        dialog.setCancelable(false);

        dialog.show();
    }

    public static void openErrorDialog(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.error_dialog);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);

        // click ra ngoài thì tắt dialog
        dialog.setCancelable(true);

        // setView trên dialog
        TextView errorTextView = dialog.findViewById(R.id.errorTextView);
        Button oKConfirmBtn = dialog.findViewById(R.id.oKConfirmBtn);

        errorTextView.setText(message);

        //set event
        oKConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static Dialog openLoadingDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);

        Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);

        // click ra ngoài thì tắt dialog
        dialog.setCancelable(false);

        dialog.show();
        return dialog;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void callApiLogout() {
        SessionManager sessionManager = new SessionManager(this);
        if(sessionManager.isLoggedIn()){
            ApiService.apiService.loginOut(sessionManager.getJwt(), sessionManager.getCustom("username"))
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            LoginResponse loginResponse = response.body();
                            if(loginResponse != null){
                                if(loginResponse.getErr() != null)
                                    BaseActivity.openErrorDialog(BaseActivity.this, loginResponse.getErr() );
                                else{
                                    sessionManager.logout();
                                    sessionManager.deleteCustom("username");
                                    navigateToLoginActivity();
                                    finish();
                                }
                            }
                            else {
                                //Toast.makeText(getContext(), "Api không trả về data", Toast.LENGTH_LONG).show();
                                ResponseBody responseBody = response.errorBody();
                                Gson gson = new Gson();
                                try {
                                    if(responseBody != null){
                                        ErrResponse errResponse = gson.fromJson(responseBody.string(), ErrResponse.class);
                                        BaseActivity.openErrorDialog(BaseActivity.this, errResponse.getErr());
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            BaseActivity.openErrorDialog(BaseActivity.this, "Không thể truy cập api");
                        }
                    });
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}