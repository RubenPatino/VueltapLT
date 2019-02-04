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
import android.widget.ImageView;

import com.karan.churi.PermissionManager.PermissionManager;
import com.vueltap.Api.ApiAdapter;
import com.vueltap.BuildConfig;
import com.vueltap.Models.ImageUpload;
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

import static com.vueltap.System.Constant.BILL_PAYMENT;
import static com.vueltap.System.Constant.DNI_BACK;
import static com.vueltap.System.Constant.DNI_FRONT;
import static com.vueltap.System.Constant.EMAIL;
import static com.vueltap.System.Constant.FILE_PROVIDER;
import static com.vueltap.System.Constant.IDENTIFY_BILL_PAYMENT;
import static com.vueltap.System.Constant.IDENTIFY_DNI_BACK;
import static com.vueltap.System.Constant.IDENTIFY_DNI_FRONT;

public class ImageDniDomicileUpload extends AppCompatActivity {

    private SessionManager manager;
    private SweetAlertDialog dialog;
    private ImageView imgDniFront, imgDniBack, imgAddress, imgCheckFront, imgCheckBack, imgCheckDomicile;
    private File photoFile;
    private PermissionManager permissionManager;
    private String email = "", urlDniFront = "", urlDniBack = "", urlAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_dni_domicile_upload);
        setTitle("Validar información");
        loadControls();
    }
    public void loadControls() {
        manager = new SessionManager(getApplicationContext());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        imgDniFront = findViewById(R.id.imageViewDniFront);
        imgDniBack = findViewById(R.id.imageViewDniBack);
        imgAddress = findViewById(R.id.imageViewAdress);
        imgCheckFront = findViewById(R.id.imageViewCheckFront);
        imgCheckBack = findViewById(R.id.imageViewCheckBack);
        imgCheckDomicile = findViewById(R.id.imageViewCheckDomicile);
        permissionManager = new PermissionManager() {};
        loadData();
    }
    public void loadData() {
        try {
            email = manager.getPersonalInfo().getString(EMAIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void OnClickCamera(View view){
        if (permissionManager.checkAndRequestPermissions(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null) {
                int id = view.getId();
                switch (id) {
                    case R.id.imageButtonCameraDniFront:
                        dispatchTakePictureIntent(intent,DNI_FRONT,IDENTIFY_DNI_FRONT);
                        break;
                    case R.id.imageButtonCameraDniBack:
                        dispatchTakePictureIntent(intent,DNI_BACK,IDENTIFY_DNI_BACK);
                        break;
                    case R.id.imageButtonDniCameraAddress:
                        dispatchTakePictureIntent(intent,BILL_PAYMENT,IDENTIFY_BILL_PAYMENT);
                        break;
                }
            }
        }
    }
    public void OnClickHelp(View view){
        int id=view.getId();
        switch (id){
            case R.id.buttonHelpPhotoFront:
                helpMessenger(id);
                break;
            case R.id.buttonHelpPhotoBack:
                helpMessenger(id);
                break;
            case R.id.buttonHelpPhotoAddress:
                helpMessenger(id);
                break;
        }
    }
    public void OnClickRegister(View view) {
        if (urlDniFront.isEmpty()) {
            helpMessenger(R.id.buttonHelpPhotoFront);
        } else if (urlDniBack.isEmpty()) {
            helpMessenger(R.id.buttonHelpPhotoBack);
        } else if (urlAddress.isEmpty()) {
            helpMessenger(R.id.buttonHelpPhotoAddress);
        } else {
            Log.d("OK","OK");
            // manager.setUrlInformation(urlDniFront, urlDniBack, urlAddress);
            // startActivity(new Intent().setClass(getApplicationContext(), ViewTransport.class));
        }
    }

    public void helpMessenger(int id){
        switch (id){
            case R.id.buttonHelpPhotoFront:
                SweetAlert("Tomale una foto a tu cédula de ciudadanía,(Parte frontal).<b>Si partes de" +
                        " la imagen están borrosas o no están claras, no " +
                        "podremos comprobar la validez de tu identificación");
                break;
            case R.id.buttonHelpPhotoBack:
                SweetAlert("Tomale una foto a tu cédula de ciudadanía,(Parte trasera).<b>Si partes " +
                        "de la imagen están borrosas o no están claras, no " +
                        "podremos comprobar la validez de tu identificación");
                break;
            case R.id.buttonHelpPhotoAddress:
                SweetAlert("Tomale una foto a tu comprobante de domicilio ya sea Agua,Luz " +
                        "o Teléfono,(No mayor a tres meses)." +
                        "<b>Si partes de la imagen están borrosas o no están claras, no " +
                        "podremos comprobar la validez de tu dirección.");
                break;
        }

    }

    public void SweetAlert(String msg){
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText(msg);
        dialog.setConfirmText("Aceptar");
        dialog.show();
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

    private void dispatchTakePictureIntent(Intent intent, String dni,int identify) {
        photoFile = createImageFile(dni);
        if(photoFile!=null){
            Uri photoURI = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + FILE_PROVIDER,
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, identify);
        }
    }

    public File getFile(Bitmap bmp, String prefix) {
        //File tempFile = null;
        //Uri uri = null;
        try {
            File tempDir = Environment.getExternalStorageDirectory();
            tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
            tempDir.mkdir();
            File tempFile = File.createTempFile(prefix + "_", ".jpg", tempDir);
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

        Call<ImageUpload> call = ApiAdapter.getApiService().UPLOAD_IMAGE(emailBody, type, imagePart);
        call.enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        switch (requestCode) {
                            case IDENTIFY_DNI_FRONT:
                                urlDniFront = response.body().getImage_url();
                                imgCheckFront.setVisibility(View.VISIBLE);
                                break;
                            case IDENTIFY_DNI_BACK:
                                urlDniBack = response.body().getImage_url();
                                imgCheckBack.setVisibility(View.VISIBLE);
                                break;
                            case IDENTIFY_BILL_PAYMENT:
                                urlAddress = response.body().getImage_url();
                                imgCheckDomicile.setVisibility(View.VISIBLE);
                                break;
                        }
                        dialog.dismissWithAnimation();
                        imageFile.delete();
                    }
                } else {
                    dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    dialog.setContentText(response.message());
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
                case IDENTIFY_DNI_FRONT:
                    imgDniFront.setImageBitmap(bitmap);
                    File dniFront = getFile(bitmap, DNI_FRONT);
                    if (dniFront != null) {
                        uploadImage(dniFront, DNI_FRONT, requestCode);
                    }
                    break;
                case IDENTIFY_DNI_BACK:
                    imgDniBack.setImageBitmap(bitmap);
                    File dniBack = getFile(bitmap, DNI_BACK);
                    if (dniBack != null) {
                        uploadImage(dniBack, DNI_BACK, requestCode);
                    }
                    break;
                case IDENTIFY_BILL_PAYMENT:
                    imgAddress.setImageBitmap(bitmap);
                    File domicile = getFile(bitmap, BILL_PAYMENT);
                    if (domicile != null) {
                        uploadImage(domicile, BILL_PAYMENT, requestCode);
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