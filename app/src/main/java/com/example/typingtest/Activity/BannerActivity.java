package com.example.typingtest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.typingtest.R;
import com.example.typingtest.Utils.Constants;

public class BannerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        int TIME_OUT = Constants.BANNER_SCREEN_TIMEOUT;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(BannerActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME_OUT);
    }
}
