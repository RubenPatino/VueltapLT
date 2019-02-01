package com.vueltap.Transport.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;
import com.vueltap.Api.ApiAdapter;
import com.vueltap.Models.ImageUpload;
import com.vueltap.Models.JsonResponse;
import com.vueltap.R;
import com.vueltap.System.SessionManager;
import com.vueltap.Transport.Adapter.AdapterTransport;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vueltap.System.Constant.ADDRESS;
import static com.vueltap.System.Constant.DNI_NUMBER;
import static com.vueltap.System.Constant.DRIVER_LICENSE;
import static com.vueltap.System.Constant.EMAIL;
import static com.vueltap.System.Constant.LAST_NAME;
import static com.vueltap.System.Constant.NAMES;
import static com.vueltap.System.Constant.PHONE;
import static com.vueltap.System.Constant.PROPERTY_CARD;
import static com.vueltap.System.Constant.SOAT;
import static com.vueltap.System.Constant.TECNOMECANICA;
import static com.vueltap.System.Constant.URL_DNI_BACK;
import static com.vueltap.System.Constant.URL_DNI_FRONT;
import static com.vueltap.System.Constant.URL_DOMICILE;
import static com.vueltap.System.Constant._PROPERTY;
import static com.vueltap.System.Constant._LYCENCE;
import static com.vueltap.System.Constant._SOAT;
import static com.vueltap.System.Constant._TECNO;

public class ViewTransport extends AppCompatActivity {

    private SessionManager manager;
    private LinearLayout linearLayout;
    private RadioButton rbClicla, rbMoto;
    private SweetAlertDialog dialog;
    private String urlProperty = "", urlSOAT = "", urlLicence = "", numPlaca = "",urlTecno="";
    private String email,names, lastName, address, phone, dniNumber,urlDniFront,urlDniBack, urlAddress;
    private EditText etPlaca;
    private final int PICTURE_RESULT=1;
    private Uri imageUri;
    private ImageView ivLicence,ivProperty,ivSoat,ivTecno;
    private Bitmap bitmap;
    private PermissionManager permissionManager;
    private ImageView checkProperty,checkLicence,checkSoat,checkTecno;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_view);
        setTitle("Transporte");
        loadControls();
    }

    private void loadControls() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        manager=new SessionManager(getApplicationContext());
        //firebaseAuth = FirebaseAuth.getInstance();
        //user = firebaseAuth.getCurrentUser();
        //email="rpm8530@gmail.com";
        permissionManager = new PermissionManager() {};
        etPlaca = findViewById(R.id.TextInputPlaca);
        ivLicence=findViewById(R.id.imageViewLicence);
        ivProperty=findViewById(R.id.imageViewProperty);
        ivSoat=findViewById(R.id.imageViewSoat);
        ivTecno=findViewById(R.id.imageViewTecno);
        checkProperty=findViewById(R.id.imageViewCheckProperty);
        checkLicence=findViewById(R.id.imageViewCheckLicence);
        checkSoat=findViewById(R.id.imageViewCheckSoat);
        checkTecno=findViewById(R.id.imageViewCheckTecno);
        linearLayout = findViewById(R.id.LinearLayoutTransport);
        rbClicla = findViewById(R.id.radioButtonCicla);
        rbMoto = findViewById(R.id.radioButtonMoto);

        rbClicla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                validateCheck();
            }
        });
        rbMoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                validateCheck();
            }
        });

        loadData();

    }

    private void loadData() {
        try {
            dniNumber = manager.getDataConfig().getString(DNI_NUMBER);
            email=manager.getDataConfig().getString(EMAIL);
            names = manager.getDataConfig().getString(NAMES);
            lastName = manager.getDataConfig().getString(LAST_NAME);
            address = manager.getDataConfig().getString(ADDRESS);
            phone = manager.getDataConfig().getString(PHONE);
            urlDniFront=manager.getDataConfig().getString(URL_DNI_FRONT);
            urlDniBack=manager.getDataConfig().getString(URL_DNI_BACK);
            urlAddress =manager.getDataConfig().getString(URL_DOMICILE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Boolean validateMoto(View view) {
        numPlaca = etPlaca.getText().toString().trim();
        if (numPlaca.isEmpty()) {
            etPlaca.setError("Completa este campo");
            etPlaca.requestFocus();
            return false;
        } else if (urlLicence.isEmpty()) {
            OnClickHelpLicence(view);
            ivLicence.requestFocus();
            return false;
        } else if (urlProperty.isEmpty()) {
            OnClickHelpProperty(view);
            ivProperty.requestFocus();
            return false;
        } else if (urlSOAT.isEmpty()) {
            OnClickHelpSoat(view);
            ivSoat.requestFocus();
            return false;
        }else if(urlTecno.isEmpty()){
           OnClickHelpTecno(view);
           ivTecno.requestFocus();
            return false;
        }else {
            return true;
        }
    }

    private void validateCheck() {
        if (rbMoto.isChecked()) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }

    }

    public void OnclickPhotoSoat(View view) {
        if (permissionManager.checkAndRequestPermissions(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,_SOAT);
        }
    }
    public void OnclickPhotoTecno(View view) {
        if (permissionManager.checkAndRequestPermissions(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,_TECNO);
        }
    }
    public void OnclickPhotoLicence(View view){
        if (permissionManager.checkAndRequestPermissions(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,_LYCENCE);
        }
    }
    public void OnclickPhotoProperty(View view) {
        if (permissionManager.checkAndRequestPermissions(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,_PROPERTY);
        }
    }

    //HELP

    public void OnClickHelpSoat(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu SOAP");
        dialog.show();
    }
    public void OnClickHelpLicence(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu licencia de conducir");
        dialog.show();

    }
    public void OnClickHelpProperty(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu tarjeta de propidad");
        dialog.show();
    }
    public void OnClickHelpTecno(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a la Revisión Técnico mecánica");
        dialog.show();
    }


    public void OnClickRegister(View view) {

        if(rbClicla.isChecked()){
            Log.d("Saved","Cicla");
        }else{
            if(validateMoto(view)){
                Log.d("Saved","MOTO");
            }
        }
    }

    public void savedUser(){
            dialog=new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
            dialog.setTitleText("Felicitaciones");
            dialog.setContentText(dniNumber+"<br>"+email+"<br>"+names+"<br>"+lastName+"<br>"+address+"<br>"+phone+"<br>");
           // dialog.setContentText("Por favor espere.");
           // dialog.setContentText("Has finalizado tu proceso de registro exitosamente.  Procederemos con un chequeo de seguridad de toda la información suministrada. Si eres seleccionado te llegará un mensaje de texto invitándote a una capacitación. Este proceso tardará una semana aproximadamente.");
            dialog.setConfirmText("Aceptar");
            dialog.show();
 /*
            firebaseAuth.signOut();
            Log.d("Datos",email+"_"+dniNumber+"_"+names+"_"+lastName+"_"+address+"_"+phone+"_"+urlDniFront+
                    "_"+urlDniBack+"_"+urlAddress+"_"+urlProperty+"_"+urlLicence+"_"+urlSOAT+"_"+urlTecno);
       new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                dialog.setContentText("Felicitaciones has finalizado tu proceso de registro exitosamente.  Procederemos con un chequeo de seguridad de toda la información suministrada. Si eres seleccionado te llegará un mensaje de texto invitándote a una capacitación. Este proceso tardará una semana aproximadamente.");
            }
        }, 2000);
            Call<JsonResponse> call = ApiAdapter.getApiService().USER_ADD(email,dniNumber, names, lastName, address,phone,urlDniBack,urlDniFront,urlAddress);
            call.enqueue(new Callback<JsonResponse>() {
                @Override
                public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            dialog.setContentText(response.body().getMessage());
                            dialog.setConfirmButton("Aceptar", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    dialog.dismissWithAnimation();
                                    firebaseAuth.signOut();
                                    finish();
                                    startActivity(new Intent().setClass(getApplicationContext(), LoginActivity.class));
                                }
                            });
                        } else {
                            dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            dialog.setContentText(response.body().getMessage());
                        }
                    } else {
                        dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        dialog.setContentText(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<JsonResponse> call, Throwable t) {
                    dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    dialog.setContentText(t.getMessage());
                }
            });*/
    }

    public File getFile (Bitmap bmp){
        //File tempFile = null;
        //Uri uri = null;
        try {
            File tempDir = Environment.getExternalStorageDirectory();
            tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
            tempDir.mkdir();
            File tempFile = File.createTempFile("temp", ".jpg", tempDir);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] bitmapData = bytes.toByteArray();
            FileOutputStream fos = new FileOutputStream(tempFile);
            // uri = Uri.fromFile(tempFile);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void uploadImage(final File imageFile, String typeImg, final int requestCode) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Subiendo...");
        dialog.setContentText("Por favor espere.");
        dialog.show();
        RequestBody emailBody = RequestBody.create(MultipartBody.FORM, email);
        RequestBody type = RequestBody.create(MultipartBody.FORM, typeImg);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);

        Call<ImageUpload> call = ApiAdapter.getApiService().UPLOAD_IMAGE(emailBody,type,imagePart);
        call.enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        switch (requestCode) {
                            case _PROPERTY:
                                urlProperty = response.body().getImage_url();
                                checkProperty.setVisibility(View.VISIBLE);
                                break;
                            case _LYCENCE:
                                urlLicence = response.body().getImage_url();
                                checkLicence.setVisibility(View.VISIBLE);
                                break;
                            case _SOAT:
                                urlSOAT = response.body().getImage_url();
                                checkSoat.setVisibility(View.VISIBLE);
                                break;
                            case _TECNO:
                                urlTecno = response.body().getImage_url();
                                checkTecno.setVisibility(View.VISIBLE);
                                break;
                        }
                        dialog.dismissWithAnimation();
                        imageFile.delete();
                    }
                }
            }

            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {
                dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                dialog.setContentText(t.getMessage());
                imageFile.delete();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            switch (requestCode) {
                case _PROPERTY:
                    ivProperty.setImageBitmap(bitmap);
                    File property = getFile(bitmap);
                    if (property != null) {
                       uploadImage(property,PROPERTY_CARD, requestCode);
                    }
                    break;
                case _LYCENCE:
                    ivLicence.setImageBitmap(bitmap);
                    File licence = getFile(bitmap);
                    if (licence != null) {
                        uploadImage(licence,DRIVER_LICENSE, requestCode);
                    }
                    break;
                case _SOAT:

                    ivSoat.setImageBitmap(bitmap);
                    File soat = getFile(bitmap);
                    if (soat != null) {
                        uploadImage(soat,SOAT,requestCode);
                    }
                    break;
                case _TECNO:

                    ivTecno.setImageBitmap(bitmap);
                    File tecno = getFile(bitmap);
                    if (tecno != null) {
                        uploadImage(tecno, TECNOMECANICA,requestCode);
                    }
                    break;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
    }
}

