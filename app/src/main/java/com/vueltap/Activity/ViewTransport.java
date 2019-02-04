package com.vueltap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;
import com.vueltap.Api.ApiAdapter;
import com.vueltap.BuildConfig;
import com.vueltap.Models.ImageUpload;
import com.vueltap.Models.JsonResponse;
import com.vueltap.R;
import com.vueltap.System.SessionManager;

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
import static com.vueltap.System.Constant.FILE_PROVIDER;
import static com.vueltap.System.Constant.IDENTIFY_LICENCE;
import static com.vueltap.System.Constant.IDENTIFY_PROPERTY;
import static com.vueltap.System.Constant.IDENTIFY_SOAT;
import static com.vueltap.System.Constant.IDENTIFY_TECNO;
import static com.vueltap.System.Constant.LAST_NAME;
import static com.vueltap.System.Constant.NAMES;
import static com.vueltap.System.Constant.PHONE;
import static com.vueltap.System.Constant.PROPERTY_CARD;
import static com.vueltap.System.Constant.SOAT;
import static com.vueltap.System.Constant.TECNOMECANICA;
import static com.vueltap.System.Constant.TYPE_CICLA;
import static com.vueltap.System.Constant.TYPE_MOTO;
import static com.vueltap.System.Constant.UID;
import static com.vueltap.System.Constant.URL_DNI_BACK;
import static com.vueltap.System.Constant.URL_DNI_FRONT;
import static com.vueltap.System.Constant.URL_DOMICILE;

public class ViewTransport extends AppCompatActivity {

    private SessionManager manager;
    private LinearLayout linearLayout;
    private RadioButton rbClicla, rbMoto;
    private SweetAlertDialog dialog;
    private String urlProperty = "", urlSOAT = "", urlLicence = "", numPlaca = "",urlTecno="";
    private String uid,email,names, lastName, address, phone, dniNumber,urlDniFront,urlDniBack, urlAddress;
    private EditText etPlaca;
    private ImageView ivLicence,ivProperty,ivSoat,ivTecno;
    private PermissionManager permissionManager;
    private ImageView checkProperty,checkLicence,checkSoat,checkTecno;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private File photoFile;


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
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
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
                if(rbClicla.isChecked())
                    linearLayout.setVisibility(View.GONE);
                else
                    linearLayout.setVisibility(View.VISIBLE);

            }
        });

        loadData();

    }
    private void loadData() {
        try {
            uid=manager.getPersonalInfo().getString(UID);
            dniNumber = manager.getPersonalInfo().getString(DNI_NUMBER);
            email=manager.getPersonalInfo().getString(EMAIL);
            names = manager.getPersonalInfo().getString(NAMES);
            lastName = manager.getPersonalInfo().getString(LAST_NAME);
            address = manager.getPersonalInfo().getString(ADDRESS);
            phone = manager.getPersonalInfo().getString(PHONE);
            urlDniFront=manager.getUrlInformation().getString(URL_DNI_FRONT);
            urlDniBack=manager.getUrlInformation().getString(URL_DNI_BACK);
            urlAddress =manager.getUrlInformation().getString(URL_DOMICILE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private Boolean validateMoto() {
        numPlaca = etPlaca.getText().toString().trim();
        if (numPlaca.isEmpty()) {
            etPlaca.setError("Completa este campo");
            etPlaca.requestFocus();
            return false;
        } else if (urlLicence.isEmpty()) {
            helpMessenger(R.id.buttonHelpLicence);
            ivLicence.requestFocus();
            return false;
        } else if (urlProperty.isEmpty()) {
            helpMessenger(R.id.buttonHelpProperty);
            ivProperty.requestFocus();
            return false;
        } else if (urlSOAT.isEmpty()) {
            helpMessenger(R.id.buttonHelpSoat);
            ivSoat.requestFocus();
            return false;
        }else if(urlTecno.isEmpty()){
            helpMessenger(R.id.buttonHelpTecno);
           ivTecno.requestFocus();
            return false;
        }else {
            return true;
        }
    }

    public void OnClickCamera(View view){
        if (permissionManager.checkAndRequestPermissions(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null) {
                int id = view.getId();
                switch (id) {
                    case R.id.imageButtonPhotoLicence:
                        dispatchTakePictureIntent(intent,DRIVER_LICENSE,IDENTIFY_LICENCE);
                        break;
                    case R.id.imageButtonPhotoProperty:
                        dispatchTakePictureIntent(intent,PROPERTY_CARD,IDENTIFY_PROPERTY);
                        break;
                    case R.id.imageButtonPhotoSoat:
                        dispatchTakePictureIntent(intent,SOAT,IDENTIFY_SOAT);
                        break;
                    case R.id.imageButtonPhotoTecno:
                        dispatchTakePictureIntent(intent,TECNOMECANICA,IDENTIFY_TECNO);
                        break;
                }
            }
        }
    }
    private void dispatchTakePictureIntent(Intent intent, String fileName,int identify) {
        photoFile = createImageFile(fileName);
        if(photoFile!=null){
            Uri photoURI = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + FILE_PROVIDER,
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, identify);
        }
    }
    private File createImageFile(String imageFileName){
        String prefix = imageFileName+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    prefix,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public void OnClickHelp(View view){
        helpMessenger(view.getId());
    }
    public void helpMessenger(int id){
        switch (id){
            case R.id.buttonHelpLicence:
                SweetAlert("Toma una foto a tu licencia de conducir. <b>Si partes de" +
                        " la imagen están borrosas o no están claras, no" +
                        " podremos comprobar la validez de tu documento.");
                break;
            case R.id.buttonHelpProperty:
                SweetAlert("Toma una foto a tu tarjeta de propidad. <b>Si partes de" +
                        " la imagen están borrosas o no están claras, no" +
                        " podremos comprobar la validez de tu documento.");
                break;
            case R.id.buttonHelpSoat:
                SweetAlert("Tomale una foto a tu SOAP. <b>Si partes de" +
                        " la imagen están borrosas o no están claras, no" +
                        " podremos comprobar la validez de tu documento.");
                break;
            case R.id.buttonHelpTecno:
                SweetAlert("Toma una foto a la Revisión Técnico mecánica. <b>Si partes de" +
                        " la imagen están borrosas o no están claras, no" +
                        " podremos comprobar la validez de tu documento.");
                break;
        }
    }
    public void SweetAlert(String msg){
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText(msg);
        dialog.setConfirmText("Aceptar");
        dialog.show();
    }
    public void OnClickRegister(View view) {
        dialog=new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Creando cuenta");
        dialog.setContentText("Por favor espere...");
        dialog.show();
        if(rbClicla.isChecked()){
            Call<JsonResponse>call=ApiAdapter.getApiService().USER_ADD_CICLA(uid,email,dniNumber,names,lastName,address,phone,urlDniFront,urlDniBack,urlAddress,TYPE_CICLA);
           call.enqueue(new Callback<JsonResponse>() {
               @Override
               public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                   if(response.isSuccessful()){
                       if(response.body().getStatus()){
                          messageSuccess();
                       }else{
                           dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                           dialog.setTitleText("Error");
                           dialog.setContentText(response.body().getMessage());
                       }
                   }else{
                       dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                       dialog.setTitleText("Error");
                       dialog.setContentText(response.message());
                   }
               }

               @Override
               public void onFailure(Call<JsonResponse> call, Throwable t) {
                   dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                   dialog.setTitleText("Error");
                   dialog.setContentText(t.getMessage());
               }
           });

        }else{
            if(validateMoto()){
                Call<JsonResponse>call=ApiAdapter.getApiService().USER_ADD_MOTO(uid,email,dniNumber,names,lastName,
                        address,phone,urlDniFront,urlDniBack,urlAddress,TYPE_MOTO,numPlaca,urlLicence,
                        urlProperty,urlSOAT,urlTecno);
                call.enqueue(new Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                        if(response.isSuccessful()){
                            if(response.body().getStatus()){
                                messageSuccess();
                            }else{
                                dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                dialog.setTitleText("Error");
                                dialog.setContentText(response.body().getMessage());
                            }
                        }else{
                            dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            dialog.setTitleText("Error");
                            dialog.setContentText(response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {
                        dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        dialog.setTitleText("Error");
                        dialog.setContentText(t.getMessage());
                    }
                });
            }
        }
    }
    public void messageSuccess(){
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        dialog.setTitleText("Felicitaciones");
        dialog.setContentText("Has finalizado tu proceso de registro exitosamente.  Procederemos con un chequeo de seguridad de toda la información suministrada. Si eres seleccionado te llegará un mensaje de texto invitándote a una capacitación. Este proceso tardará una semana aproximadamente.");
        dialog.setConfirmButton("Aceptar", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismissWithAnimation();
                //firebaseAuth.signOut();
                //finish();
                //startActivity(new Intent().setClass(getApplicationContext(),LoginActivity.class));
            }
        });
    }

    public File getFile (Bitmap bmp,String prefix){
        //File tempFile = null;
        //Uri uri = null;
        try {
            File tempDir = Environment.getExternalStorageDirectory();
            tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
            tempDir.mkdir();
            File tempFile = File.createTempFile(prefix+"_", ".jpg", tempDir);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 30, bytes);
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
                            case IDENTIFY_PROPERTY:
                                urlProperty = response.body().getImage_url();
                                checkProperty.setVisibility(View.VISIBLE);
                                break;
                            case IDENTIFY_LICENCE:
                                urlLicence = response.body().getImage_url();
                                checkLicence.setVisibility(View.VISIBLE);
                                break;
                            case IDENTIFY_SOAT:
                                urlSOAT = response.body().getImage_url();
                                checkSoat.setVisibility(View.VISIBLE);
                                break;
                            case IDENTIFY_TECNO:
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
        if (resultCode == RESULT_OK) {
            String pathName=photoFile.getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(pathName);
            switch (requestCode) {
                case IDENTIFY_PROPERTY:
                    ivProperty.setImageBitmap(bitmap);
                    File property = getFile(bitmap,PROPERTY_CARD);
                    if (property != null) {
                       uploadImage(property,PROPERTY_CARD, requestCode);
                    }
                    break;
                case IDENTIFY_LICENCE:
                    ivLicence.setImageBitmap(bitmap);
                    File licence = getFile(bitmap,DRIVER_LICENSE);
                    if (licence != null) {
                        uploadImage(licence,DRIVER_LICENSE, requestCode);
                    }
                    break;
                case IDENTIFY_SOAT:

                    ivSoat.setImageBitmap(bitmap);
                    File soat = getFile(bitmap,SOAT);
                    if (soat != null) {
                        uploadImage(soat,SOAT,requestCode);
                    }
                    break;
                case IDENTIFY_TECNO:

                    ivTecno.setImageBitmap(bitmap);
                    File tecno = getFile(bitmap,TECNOMECANICA);
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

