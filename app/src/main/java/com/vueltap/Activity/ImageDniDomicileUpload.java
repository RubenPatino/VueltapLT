package com.vueltap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.karan.churi.PermissionManager.PermissionManager;
import com.vueltap.Api.ApiAdapter;
import com.vueltap.Models.ImageUpload;
import com.vueltap.R;
import com.vueltap.System.SessionManager;
import com.vueltap.Transport.View.ViewTransport;

import org.json.JSONException;

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

import static com.vueltap.System.Constant.BILL_PAYMENT;
import static com.vueltap.System.Constant.DNI_BACK;
import static com.vueltap.System.Constant.DNI_FRONT;
import static com.vueltap.System.Constant.DOMICILE_REQUEST_CODE;
import static com.vueltap.System.Constant.EMAIL;
import static com.vueltap.System.Constant.IDENTIFY_REQUEST_CODE_BACK;
import static com.vueltap.System.Constant.IDENTIFY_REQUEST_CODE_FRONT;

public class ImageDniDomicileUpload extends AppCompatActivity {

    private SessionManager manager;

    private SweetAlertDialog dialog;
    private ImageView imgDniFront, imgDniBack, imgAddress, imgCheckFront, imgCheckBack, imgCheckDomicile;
    private Bitmap bitmap;
    private PermissionManager permissionManager;
  /*  private String email, names, lastName, address, phone, dniNumber;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;*/
    private String email="", urlDniFront = "", urlDniBack = "", urlAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_dni_domicile_upload);
        setTitle("Validar información");
        loadControls();
    }

    public void loadControls() {
        manager=new SessionManager(getApplicationContext());
      //  Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
      //  firebaseAuth = FirebaseAuth.getInstance();
      //  user = firebaseAuth.getCurrentUser();
        imgDniFront = findViewById(R.id.imageViewDniFront);
        imgDniBack = findViewById(R.id.imageViewDniBack);
        imgAddress = findViewById(R.id.imageViewAdress);
        imgCheckFront = findViewById(R.id.imageViewCheckFront);
        imgCheckBack = findViewById(R.id.imageViewCheckBack);
        imgCheckDomicile = findViewById(R.id.imageViewCheckDomicile);
        permissionManager = new PermissionManager() {
        };
        loadData();
    }

    public void loadData() {
        try {
            email=manager.getPersonalInfo().getString(EMAIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//
//        email="pruebas@gmail.com";
//        names="nn";
//        lastName="nn";
//        address="nn";
//        phone="2123232323";
//        dniNumber="345332113";
//        /*
//        email = user.getEmail();
//        names = getIntent().getStringExtra(NAMES);
//        lastName = getIntent().getStringExtra(LAST_NAME);
//        address = getIntent().getStringExtra(ADDRESS);
//        phone = getIntent().getStringExtra(PHONE);
//        dniNumber = getIntent().getStringExtra(DNI_NUMBER);
//        */
   }

    public void OnClickDniFront(View view) {
        if (permissionManager.checkAndRequestPermissions(this)) {
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
        if (permissionManager.checkAndRequestPermissions(this)) {
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
        if (permissionManager.checkAndRequestPermissions(this)) {
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
        if (urlDniFront.isEmpty()) {
            OnClickDniHelpFront(view);
        } else if (urlDniBack.isEmpty()) {
            OnClickDniHelpBack(view);
        } else if (urlAddress.isEmpty()) {
            OnClickDomicileHelp(view);
        } else {

            manager.setUrlInformation(urlDniFront,urlDniBack,urlAddress);

            startActivity(new Intent().setClass(getApplicationContext(),ViewTransport.class));
/*
            //Log.d("URL",urlDniFront+"_"+urlDniBack+"_"+urlAddress);
            Intent intent = new Intent(this, ViewTransport.class);
            intent.putExtra(DNI_NUMBER, dniNumber);
            intent.putExtra(NAMES, names);
            intent.putExtra(LAST_NAME, lastName);
            intent.putExtra(ADDRESS, address);
            intent.putExtra(PHONE, phone);
            intent.putExtra(URL_DNI_FRONT, urlDniFront);
            intent.putExtra(URL_DNI_BACK, urlDniFront);
            intent.putExtra(URL_DOMICILE, urlDniFront);
            startActivity(new Intent().setClass(getApplicationContext(), ViewTransport.class));*/
        }
    }

    public File getFile(Bitmap bmp, String prefix) {
        //File tempFile = null;
        //Uri uri = null;
        try {
            File tempDir = Environment.getExternalStorageDirectory();
            tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
            tempDir.mkdir();
            File tempFile = File.createTempFile(prefix+"_", ".jpg", tempDir);
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

        final RequestBody emailBody = RequestBody.create(MultipartBody.FORM, email);
        final RequestBody type = RequestBody.create(MultipartBody.FORM, typeImg);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        final MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);
        Call<ImageUpload> call = ApiAdapter.getApiService().UPLOAD_IMAGE(emailBody, type, imagePart);
        call.enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        switch (requestCode) {
                            case IDENTIFY_REQUEST_CODE_FRONT:
                                urlDniFront = response.body().getImage_url();
                                imgCheckFront.setVisibility(View.VISIBLE);

                                break;
                            case IDENTIFY_REQUEST_CODE_BACK:
                                urlDniBack = response.body().getImage_url();
                                imgCheckBack.setVisibility(View.VISIBLE);
                                break;
                            case DOMICILE_REQUEST_CODE:
                                urlAddress = response.body().getImage_url();
                                imgCheckDomicile.setVisibility(View.VISIBLE);
                                break;
                        }
                        dialog.dismissWithAnimation();
                        imageFile.delete();
                    }
                }else{
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
        if (data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            switch (requestCode) {
                case IDENTIFY_REQUEST_CODE_FRONT:
                    imgDniFront.setImageBitmap(bitmap);
                    File dniFront = getFile(bitmap,DNI_FRONT);
                    if (dniFront != null) {
                        uploadImage(dniFront, DNI_FRONT, requestCode);
                    }
                    break;
                case IDENTIFY_REQUEST_CODE_BACK:
                    imgDniBack.setImageBitmap(bitmap);
                    File dniBack = getFile(bitmap, DNI_BACK);
                    if (dniBack != null) {
                        uploadImage(dniBack, DNI_BACK, requestCode);
                    }
                    break;
                case DOMICILE_REQUEST_CODE:
                    imgAddress.setImageBitmap(bitmap);
                    File domicile = getFile(bitmap, BILL_PAYMENT);
                    if (domicile != null) {
                        uploadImage(domicile, BILL_PAYMENT, requestCode);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
    }
}
