package com.vueltap.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vueltap.R;

import java.util.Timer;
import java.util.TimerTask;

public class splash_screen extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);



        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent().setClass(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
