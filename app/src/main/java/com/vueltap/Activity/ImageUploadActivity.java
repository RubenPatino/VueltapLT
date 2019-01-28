package com.vueltap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;
import com.vueltap.Api.ApiAdapter;
import com.vueltap.Models.JsonResponse;
import com.vueltap.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vueltap.System.Constant.DOMICILE_REQUEST_CODE;
import static com.vueltap.System.Constant.IDENTIFY_REQUEST_CODE_BACK;
import static com.vueltap.System.Constant.IDENTIFY_REQUEST_CODE_FRONT;

public class ImageUploadActivity extends AppCompatActivity {

    private SweetAlertDialog dialog;
    private ImageView imgDniFront, imgDniBack, imgAddress,imgCheckFront,imgCheckBack,imgCheckDomicile;
    private Bitmap bitmap;
    private PermissionManager permissionManager;
    private String email, names, lastName, address, phone, dniNumber;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String urlDniFront="",urlDniBack="",urlDomicile="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        loadControls();
    }

    public void loadControls() {
        imgDniFront = findViewById(R.id.imageViewDniFront);
        imgDniBack = findViewById(R.id.imageViewDniBack);
        imgAddress = findViewById(R.id.imageViewAdress);
        imgCheckFront = findViewById(R.id.imageViewCheckFront);
        imgCheckBack = findViewById(R.id.imageViewCheckBack);
        imgCheckDomicile = findViewById(R.id.imageViewCheckDomicile);
        permissionManager=new PermissionManager(){};
        loadData();
    }

    public void loadData() {
        email = "rr@gmail.com";
       /* names = getIntent().getStringExtra(NAMES);
        lastName = getIntent().getStringExtra(LAST_NAME);
        address = getIntent().getStringExtra(ADDRESS);
        phone = getIntent().getStringExtra(PHONE);
        dniNumber = getIntent().getStringExtra(DNI_NUMBER);*/
    }

    public void OnClickDniFront(View view) {
       if(permissionManager.checkAndRequestPermissions(this)) {
           Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           startActivityForResult(intent, IDENTIFY_REQUEST_CODE_FRONT);
       }
    }

    public void OnClickDniHelpFront(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu cédula de ciudadanía,(Parte frontal).<b>Si partes de" +
                " la imagen están borrosas o no están claras, no " +
                "podremos comprobar la validez de tu identificación");
        dialog.setConfirmText("Aceptar");
        dialog.show();
    }

    public void OnClickDniBack(View view) {
        if(permissionManager.checkAndRequestPermissions(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, IDENTIFY_REQUEST_CODE_BACK);
        }
    }

    public void OnClickDniHelpBack(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu cédula de ciudadanía,(Parte trasera).<b>Si partes " +
                "de la imagen están borrosas o no están claras, no " +
                "podremos comprobar la validez de tu identificación");
        dialog.setConfirmText("Aceptar");
        dialog.show();
    }

    public void OnClickDomiciled(View view) {
        if(permissionManager.checkAndRequestPermissions(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, DOMICILE_REQUEST_CODE);
        }
    }

    public void OnClickDomicileHelp(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu comprobante de domicilio ya sea Agua,Luz " +
                "o Teléfono,(No mayor a tres meses)." +
                "<b>Si partes de la imagen están borrosas o no están claras, no " +
                "podremos comprobar la validez de tu dirección.");
        dialog.setConfirmText("Aceptar");
        dialog.show();
    }

    public void OnClickRegister(View view) {

      if(urlDniFront.isEmpty()){
            OnClickDniHelpFront(view);
        }else if(urlDniBack.isEmpty()){
          OnClickDniHelpBack(view);
      }else if(urlDomicile.isEmpty()){
          OnClickDomicileHelp(view);
      }
          /*Call<JsonResponse> call = ApiAdapter.getApiService().USER_ADD(email, names, lastName, address, phone, dniNumber);
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
        });
*/
    }

    public File getFile(Bitmap bmp) {
        //File tempFile = null;
        //Uri uri = null;
        try {
            File tempDir = Environment.getExternalStorageDirectory();
            tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
            tempDir.mkdir();
            File tempFile = File.createTempFile("temp",".jpg", tempDir);
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

    public void uploadDniFront(RequestBody emailBody, MultipartBody.Part imagePart, final File imageFile){
        Call<JsonResponse> call = ApiAdapter.getApiUser().UPLOAD_DNI_FRONT(emailBody, imagePart);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        urlDniFront=response.body().getMessage();
                        imgCheckFront.setVisibility(View.VISIBLE);
                        dialog.dismissWithAnimation();
                        imageFile.delete();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                dialog.setContentText(t.getMessage());
                imageFile.delete();
            }
        });
    }
    public void uploadDniBack(RequestBody emailBody, MultipartBody.Part imagePart, final File imageFile){
        Call<JsonResponse> call = ApiAdapter.getApiUser().UPLOAD_DNI_BACK(emailBody, imagePart);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        urlDniBack=response.body().getMessage();
                        imgCheckBack.setVisibility(View.VISIBLE);
                        dialog.dismissWithAnimation();
                        imageFile.delete();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                dialog.setContentText(t.getMessage());
                imageFile.delete();
            }
        });
    }
    public void uploadDomicile(RequestBody emailBody, MultipartBody.Part imagePart, final File imageFile){
        Call<JsonResponse> call = ApiAdapter.getApiUser().UPLOAD_DOMICILE(emailBody, imagePart);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        urlDomicile=response.body().getMessage();
                        imgCheckDomicile.setVisibility(View.VISIBLE);
                        dialog.dismissWithAnimation();
                        imageFile.delete();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                dialog.setContentText(t.getMessage());
                imageFile.delete();
            }
        });
    }

    public void getMultipart(final File imageFile, int requestCode) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Subiendo...");
        dialog.setContentText("Por favor espere.");
        dialog.show();
        RequestBody emailBody = RequestBody.create(MultipartBody.FORM, email);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",imageFile.getName(), imageBody);
        switch (requestCode){
            case IDENTIFY_REQUEST_CODE_FRONT:
                uploadDniFront(emailBody,imagePart,imageFile);
                break;
            case IDENTIFY_REQUEST_CODE_BACK:
                uploadDniBack(emailBody,imagePart,imageFile);
                break;
            case DOMICILE_REQUEST_CODE:
                uploadDomicile(emailBody,imagePart,imageFile);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            switch (requestCode) {
                case IDENTIFY_REQUEST_CODE_FRONT:

                    imgDniFront.setImageBitmap(bitmap);
                    File dniFront = getFile(bitmap);
                    if(dniFront!=null) {
                        getMultipart(dniFront,requestCode);
                    }
                    break;
                case IDENTIFY_REQUEST_CODE_BACK:

                    imgDniBack.setImageBitmap(bitmap);
                    File dniBack = getFile(bitmap);
                    if (dniBack != null) {
                        getMultipart(dniBack,requestCode);
                    }
                    break;
                case DOMICILE_REQUEST_CODE:

                    imgAddress.setImageBitmap(bitmap);
                    File domicile = getFile(bitmap);
                    if (domicile != null) {
                        getMultipart(domicile,requestCode);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        permissionManager.checkResult(requestCode,permissions,grantResults);
    }
}
