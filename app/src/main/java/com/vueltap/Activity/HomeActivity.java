package com.vueltap.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.vueltap.R;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void OnClickExit(View view){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent().setClass(getApplicationContext(),LoginActivity.class));
    }
}
