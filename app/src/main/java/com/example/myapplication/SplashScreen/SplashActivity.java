package com.example.myapplication.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import com.example.myapplication.Login.LoginActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.utils.CustomLocation;
import com.example.myapplication.utils.SharedPreferenceHelper;

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
                if(login_status){

                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
                else
                {
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, 4000);

    }
}