package com.vueltap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.vueltap.R;

public class RegisterWelcome extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_welcome);
        setTitle("Crear nueva cuenta");
        loadControls();
    }

    private void loadControls() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void OnClickNext(View view){
        startActivity(new Intent().setClass(getApplicationContext(), RegisterConditions.class));
       // startActivity(new Intent().setClass(getApplicationContext(), MainTableActivity.class));
      //  startActivity(new Intent().setClass(getApplicationContext(), MainBasicActivity.class));
    }
    public void OnClickExit(View view){
         finish();
         if(firebaseAuth!=null){
             firebaseAuth.signOut();
         }
         startActivity(new Intent().setClass(getApplicationContext(),LoginActivity.class));
    }

}
