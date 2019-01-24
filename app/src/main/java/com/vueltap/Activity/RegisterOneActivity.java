package com.vueltap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vueltap.R;

public class RegisterOneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);
        setTitle("Crear nueva cuenta");
    }

    public void OnClickNext(View view){
        startActivity(new Intent().setClass(getApplicationContext(),RegisterTwoActivity.class));
    }
    public void OnClickExit(View view){

         finish();
         startActivity(new Intent().setClass(getApplicationContext(),LoginActivity.class));
    }

}
