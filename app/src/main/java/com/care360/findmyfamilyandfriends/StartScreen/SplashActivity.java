package com.care360.findmyfamilyandfriends.StartScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.splashscreen.SplashScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.care360.findmyfamilyandfriends.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SplashScreen splashScreen = SplashScreen.Companion.installSplashScreen(this);

        setContentView(R.layout.activity_splash);

        splashScreen.setKeepOnScreenCondition(() -> true);

        startActivity(new Intent(SplashActivity.this,StartScreenActivity.class));

    }
}