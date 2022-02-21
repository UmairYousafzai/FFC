package com.example.ffccloud.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ffccloud.Login.LoginActivity;
import com.example.ffccloud.MainActivity;
import com.example.ffccloud.R;
import com.example.ffccloud.utils.SharedPreferenceHelper;

public class SplashActivity extends AppCompatActivity {
    Boolean login_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        login_status = SharedPreferenceHelper.getInstance(getApplication()).getLogin_State();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent;
                if(login_status){

                    mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                }
                else
                {
                    mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(mainIntent);
                finish();
            }
        }, 1000);

    }
}