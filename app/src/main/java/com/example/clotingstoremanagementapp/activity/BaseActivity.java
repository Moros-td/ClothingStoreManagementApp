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

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.fragment.CategoryFragment;
import com.example.clotingstoremanagementapp.fragment.CustomerFragment;
import com.example.clotingstoremanagementapp.fragment.HomeFragment;
import com.example.clotingstoremanagementapp.fragment.OrderFragment;
import com.example.clotingstoremanagementapp.fragment.OrderHistoryFragment;
import com.example.clotingstoremanagementapp.fragment.ProductFragment;
import com.example.clotingstoremanagementapp.fragment.StaffFragment;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {
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

    public void setView() {
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


    public void setEvent() {
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

}