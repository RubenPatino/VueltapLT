package com.vueltap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vueltap.R;
import com.vueltap.System.SessionManager;

import static com.vueltap.System.Constant.ADDRESS;
import static com.vueltap.System.Constant.DNI_NUMBER;
import static com.vueltap.System.Constant.LAST_NAME;
import static com.vueltap.System.Constant.NAMES;
import static com.vueltap.System.Constant.PHONE;

public class RegisterFormData extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private EditText etEmail, etNames, etLastNames, etAddress, etPhone, etDniNumber;
    private String email;
    private String uid;
    private SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form_data);
        setTitle("Formulario de registro");
        loadControls();

        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private void loadControls() {
        manager=new SessionManager(getApplicationContext());
//        storageRef = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        etEmail = findViewById(R.id.etEmail);
        etNames = findViewById(R.id.etNames);
        etLastNames = findViewById(R.id.etLastNames);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etDniNumber = findViewById(R.id.etDniNumber);
        etDniNumber.requestFocus();
        loadData();
    }

    private void loadData() {
         email = user.getEmail();
         uid=user.getUid();
       // email = "prueba@gmail.com";
        etEmail.setText(email);
    }

    public void OnClickNext(View view) {
        String names, lastName, address, phone, dniNumber;
        dniNumber = etDniNumber.getText().toString().trim();
        names = etNames.getText().toString().trim();
        lastName = etLastNames.getText().toString().trim();
        address = etAddress.getText().toString().trim();
        phone = etPhone.getText().toString().trim();

        if (dniNumber.isEmpty()) {
            etDniNumber.setError(getString(R.string.msg_isempty));
            etDniNumber.requestFocus();
        }else if(dniNumber.length()<7){
            etDniNumber.setError("Por favor introduzca una número de cedula valido.");
            etDniNumber.requestFocus();
        }else if (names.isEmpty()) {
            etNames.setError(getString(R.string.msg_isempty));
            etNames.requestFocus();
        } else if (lastName.isEmpty()) {
            etLastNames.setError(getString(R.string.msg_isempty));
            etLastNames.requestFocus();
        } else if (address.isEmpty()) {
            etAddress.setError(getString(R.string.msg_isempty));
            etAddress.requestFocus();
        } else if (phone.isEmpty()) {
            etPhone.setError(getString(R.string.msg_isempty));
            etPhone.requestFocus();
        } else if(phone.length()<10){
            etPhone.setError("Por favor digite un número de teléfono valido.");
            etPhone.requestFocus();
        }else {
            manager.setPersonalInformation(uid,email,dniNumber,names,lastName,address,phone);
            startActivity(new Intent().setClass(this,ImageDniDomicileUpload.class));
           /* Intent intent = new Intent(this, ImageDniDomicileUpload.class);
            intent.putExtra(DNI_NUMBER, dniNumber);
            intent.putExtra(NAMES, names);
            intent.putExtra(LAST_NAME, lastName);
            intent.putExtra(ADDRESS, address);
            intent.putExtra(PHONE, phone);
            startActivity(intent);*/
        }
    }
}
