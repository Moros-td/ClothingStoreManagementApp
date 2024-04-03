package com.example.clotingstoremanagementapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.adapter.RoleArrayAdapter;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.entity.AdminEntity;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;
import com.example.clotingstoremanagementapp.response.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends InterceptorActivity {

    private Toolbar toolbar;

    private TextView srceenName;
    private EditText editTextRetypePassword, editTextPassword, editTextUsername;

    private Button savePasswordBtn;

    Dialog dialog;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        setView();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            // Kiểm tra xem dữ liệu đã được truyền qua không
            AdminEntity staff = (AdminEntity) bundle.get("admin_entity");
            if(staff != null){
                setData(staff);
            }
        }
        setEvent();
    }

    private void setData(AdminEntity staff) {
        editTextUsername.setText(staff.getUsername());
        editTextUsername.setEnabled(false);
    }

    private void setView() {
        toolbar = findViewById(R.id.toolbarInfo);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            // set hiện nút back
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sessionManager = new SessionManager(ResetPasswordActivity.this);
        editTextRetypePassword = findViewById(R.id.editTextRetypePassword);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUsername = findViewById(R.id.editTextUsername);
        savePasswordBtn = findViewById(R.id.savePasswordBtn);
        srceenName = findViewById(R.id.srceenName);
        srceenName.setText("Đổi mật khẩu");
    }

    private void setEvent() {
        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String retypePassword = editTextRetypePassword.getText().toString();
                String password = editTextPassword.getText().toString();
                String username = editTextUsername.getText().toString();
                boolean check = false;
                if(retypePassword.isEmpty()){
                    check = true;
                    editTextRetypePassword.setError("Vui lòng nhập lại mật khẩu");
                }

                if(password.isEmpty()){
                    check = true;
                    editTextPassword.setError("Vui lòng nhập mật khẩu");
                }

                if(check){
                    return;
                }

                if(!password.equals(retypePassword)){
                    editTextRetypePassword.setError("Yêu cầu nhập cùng một mật khẩu");
                    return;
                }

                callApiResetPassword(username, password);
            }
        });
    }

    private void callApiResetPassword(String username, String password) {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            dialog = BaseActivity.openLoadingDialog(ResetPasswordActivity.this);
            ApiService.apiService.resetPassword(token, username, password)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            LoginResponse loginResponse = response.body();
                            if(loginResponse.getErr() != null){
                                BaseActivity.openErrorDialog(ResetPasswordActivity.this, loginResponse.getErr());
                            }
                            else if(loginResponse.getMessage() != null){
                                BaseActivity.openSuccessDialog(ResetPasswordActivity.this, loginResponse.getMessage());
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
                            BaseActivity.openErrorDialog(ResetPasswordActivity.this, "Không thể kết nối api");
                        }
                    });
        }
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