package com.example.clotingstoremanagementapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.api.ApiService;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;
import com.example.clotingstoremanagementapp.response.ErrResponse;
import com.example.clotingstoremanagementapp.response.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView textViewErr;
    EditText textViewUsername, textViewPassword;

    Button buttonLogin;

    Dialog dialog;

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setView();
        setEvent();
    }

    public void setEvent() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = BaseActivity.openLoadingDialog(LoginActivity.this);
                String username = textViewUsername.getText().toString();
                String password = textViewPassword.getText().toString();
                textViewErr.setVisibility(View.GONE);
                ApiService.apiService.logIn(username, password).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        LoginResponse loginResponse = response.body();
                        if(loginResponse != null){
                            if(loginResponse.getErr() != null)
                                BaseActivity.openErrorDialog(LoginActivity.this, loginResponse.getErr());
                            else if (loginResponse.getToken() != null){

                                sessionManager = new SessionManager(LoginActivity.this);
                                sessionManager.saveJwt(loginResponse.getToken());
                                sessionManager.saveCustom("username", username);
                                sessionManager.saveCustom("role", loginResponse.getRole());
                                navigateToBaseActivity();
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
                                    textViewErr.setText(errResponse.getErr());
                                    textViewErr.setVisibility(View.VISIBLE);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        BaseActivity.openErrorDialog(LoginActivity.this, "Không thể truy cập api");
                    }
                });
            }
        });
    }

    public void setView() {
        textViewErr = findViewById(R.id.textViewErr);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewPassword = findViewById(R.id.textViewPassword);

        buttonLogin = findViewById(R.id.buttonLogin);
    }

    private void navigateToBaseActivity() {
        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
    }
}