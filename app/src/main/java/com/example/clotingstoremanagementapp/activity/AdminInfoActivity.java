package com.example.clotingstoremanagementapp.activity;

import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.adapter.RoleArrayAdapter;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.entity.AdminEntity;
import com.example.clotingstoremanagementapp.fragment.CategoryFragment;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;
import com.example.clotingstoremanagementapp.response.LoginResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminInfoActivity extends InterceptorActivity {
    private Toolbar toolbar;

    private TextView srceenName;
    private EditText editTextUsername, editTextPassword;

    private Spinner spinnerRole;

    private RoleArrayAdapter roleArrayAdapter;

    private Button saveAdminBtn;

    private AdminEntity staff;

    Dialog dialog;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_info);

        setView();

        Bundle bundle = getIntent().getExtras();
        staff = null;
        if(bundle != null)
        {
            // Kiểm tra xem dữ liệu đã được truyền qua không
            staff = (AdminEntity) bundle.get("admin_entity");
            if(staff != null){
                setData(staff);
            }
        }

        setEvent();
    }

    private void setData(AdminEntity staff) {
        editTextUsername.setText(staff.getUsername());
        editTextUsername.setEnabled(false);
        editTextPassword.setVisibility(View.GONE);
        // set role
        List<String> tmp = getListRole();
        int i = 0;
        for (String x : tmp) {
            if(x.trim().equals(staff.getRole().trim())){
                spinnerRole.setSelection(i);
                break;
            }
            i += 1;
        }
    }

    private void setView() {
        toolbar = findViewById(R.id.toolbarInfo);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            // set hiện nút back
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sessionManager = new SessionManager(AdminInfoActivity.this);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        // set data for spinner
        roleArrayAdapter = new RoleArrayAdapter(this, R.layout.item_role_selected, getListRole());
        spinnerRole.setAdapter(roleArrayAdapter);
        saveAdminBtn = findViewById(R.id.saveAdminBtn);
        srceenName = findViewById(R.id.srceenName);
        srceenName.setText("Thông tin nhân viên");
    }

    private void setEvent() {
        saveAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String role = (String) spinnerRole.getSelectedItem();
                boolean check = false;
                if(staff == null) {
                    if(username.isEmpty()){
                        check = true;
                        editTextUsername.setError("Vui lòng nhập username");
                    }

                    if(password.isEmpty()){
                        check = true;
                        editTextPassword.setError("Vui lòng nhập mật khẩu");
                    }

                    if(check){
                        return;
                    }

                    if(!validUsername(username)){
                        editTextUsername.setError("Username có ít nhất 3 kí tự, bắt đầu bằng chữ cái và không chứa kí tự đặc biệt");
                        return;
                    }
                    if(staff == null) {
                        if(!isStrongPassword(password)){
                            editTextPassword.setError("Mật khẩu yêu cầu ít nhất 6 kí tự, chứa ít nhất 1 kí tự số, 1 chữ cái và 1 kí tự đặc biệt");
                            return;
                        }
                    }
                }

                if(staff != null)
                    callApiEditStaff(username, role);
                else
                    callApiAddStaff(username, password, role);
            }
        });
    }

    private void callApiEditStaff(String username, String role) {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            dialog = BaseActivity.openLoadingDialog(AdminInfoActivity.this);
            ApiService.apiService.editStaff(token, username, role)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            LoginResponse loginResponse = response.body();
                            if(loginResponse.getErr() != null){
                                BaseActivity.openErrorDialog(AdminInfoActivity.this, loginResponse.getErr());
                            }
                            else if(loginResponse.getMessage() != null){
                                BaseActivity.openSuccessDialog(AdminInfoActivity.this, loginResponse.getMessage());
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // set result để bên fragment reload
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                }, 3000); // 3000 milliseconds = 3 second
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(AdminInfoActivity.this, "Không thể kết nối api");
                        }
                    });
        }
    }

    private void callApiAddStaff(String username, String password, String role) {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            dialog = BaseActivity.openLoadingDialog(AdminInfoActivity.this);
            ApiService.apiService.addStaff(token, username, password, role)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            LoginResponse loginResponse = response.body();
                            if(loginResponse.getErr() != null){
                                BaseActivity.openErrorDialog(AdminInfoActivity.this, loginResponse.getErr());
                            }
                            else if(loginResponse.getMessage() != null){
                                BaseActivity.openSuccessDialog(AdminInfoActivity.this, loginResponse.getMessage());
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // set result để bên fragment reload
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                }, 3000); // 3000 milliseconds = 3 second
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(AdminInfoActivity.this, "Không thể kết nối api");
                        }
                    });
        }

    }

    private boolean validUsername(String username){
        if(username.length() < 3){
            return false;
        }

        Pattern pattern = Pattern.compile("^\\d");

        Matcher matcher = pattern.matcher(username);
        if (matcher.find()) {
            return false;
        }

        if (username.matches(".*[^a-zA-Z0-9].*")) {
            return false;
        }

        return true;
    }


    private boolean isStrongPassword(String password) {
        // Kiểm tra độ dài ít nhất
        if (password.length() < 6) {
            return false;
        }

        // Kiểm tra có ít nhất một ký tự đặc biệt
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            return false;
        }

        // Kiểm tra có ít nhất một ký tự chữ cái
        if (!password.matches(".*[a-zA-Z].*")) {
            return false;
        }

        // Kiểm tra có ít nhất một ký tự số
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        return true;
    }

    private List<String> getListRole() {
        List<String> list = new ArrayList<>();
        list.add("manager");
        list.add("staff");
        return list;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Xử lý khi bấm nút back trên toolbar
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}