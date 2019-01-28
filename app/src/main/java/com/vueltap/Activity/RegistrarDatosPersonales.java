package com.vueltap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vueltap.Api.ApiAdapter;
import com.vueltap.Models.JsonResponse;
import com.vueltap.Models.UploadImageJsonResponse;
import com.vueltap.R;
import com.vueltap.util.JsonUtils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vueltap.System.Constant.ADDRESS;
import static com.vueltap.System.Constant.DNI_NUMBER;
import static com.vueltap.System.Constant.DOMICILE_REQUEST_CODE;
import static com.vueltap.System.Constant.EMAIL;
import static com.vueltap.System.Constant.IDENTIFY_REQUEST_CODE_BACK;
import static com.vueltap.System.Constant.IDENTIFY_REQUEST_CODE_FRONT;
import static com.vueltap.System.Constant.LAST_NAME;
import static com.vueltap.System.Constant.NAMES;
import static com.vueltap.System.Constant.PHONE;

public class RegistrarDatosPersonales extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private EditText etEmail, etNames, etLastNames, etAddress, etPhone, etDniNumber;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos_personales);
        setTitle("Formulario de registro");
        loadControls();

        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private void loadControls() {
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
        } else if (names.isEmpty()) {
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
        } else {
            Intent intent = new Intent(this, ImageUploadActivity.class);
            intent.putExtra(DNI_NUMBER, dniNumber);
            intent.putExtra(NAMES, names);
            intent.putExtra(LAST_NAME, lastName);
            intent.putExtra(ADDRESS, address);
            intent.putExtra(PHONE, phone);
            startActivity(intent);
        }
    }
}
