package com.example.scheme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scheme.classes.SharedPreferencesHelper;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=500;
    SharedPreferencesHelper sharedPreferencesHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferencesHelper=new SharedPreferencesHelper(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferencesHelper.getRegisterpassword().equals("success")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashScreen.this, Home.class));
                            finish();
                        }
                    },SPLASH_TIME_OUT);
                }else{
                    Intent goToHome=new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(goToHome);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);
    }
}