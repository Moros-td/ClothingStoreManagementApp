package com.example.clotingstoremanagementapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.clotingstoremanagementapp.R;
import com.example.clotingstoremanagementapp.interceptor.SessionManager;

public class InterceptorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interceptor);


        if(!isLoggedIn()){
            navigateToLoginActivity();
            finish();
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean isLoggedIn() {
        String token = "";
        SessionManager sessionManager = new SessionManager(this);
        //sessionManager.logout();
        if(sessionManager.isLoggedIn())
            token = sessionManager.getJwt();

        if(token.isEmpty())
            return false;
        else return true;
    }
}