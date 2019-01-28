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
import com.orhanobut.logger.Logger;
import com.vueltap.Api.ApiAdapter;
import com.vueltap.Models.JsonResponse;
import com.vueltap.Models.UploadImageJsonResponse;
import com.vueltap.R;
import com.vueltap.util.JsonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vueltap.System.Constant.ADDRESS;
import static com.vueltap.System.Constant.DNI_NUMBER;
import static com.vueltap.System.Constant.DOMICILE_REQUEST_CODE;
import static com.vueltap.System.Constant.IDENTIFY_REQUEST_CODE_BACK;
import static com.vueltap.System.Constant.IDENTIFY_REQUEST_CODE_FRONT;
import static com.vueltap.System.Constant.LAST_NAME;
import static com.vueltap.System.Constant.NAMES;
import static com.vueltap.System.Constant.PHONE;

public class ImageUploadActivity extends AppCompatActivity {

    private SweetAlertDialog dialog;
    private ImageView imgDniFront, imgDniBack, imgAddress;
    private Bitmap bitmap;
    private String email, names, lastName, address, phone, dniNumber;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

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
        loadData();
    }

    public void loadData() {
        names = getIntent().getStringExtra(NAMES);
        lastName = getIntent().getStringExtra(LAST_NAME);
        address = getIntent().getStringExtra(ADDRESS);
        phone = getIntent().getStringExtra(PHONE);
        dniNumber = getIntent().getStringExtra(DNI_NUMBER);

    }

    public void OnClickDniFront(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IDENTIFY_REQUEST_CODE_FRONT);
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IDENTIFY_REQUEST_CODE_BACK);
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, DOMICILE_REQUEST_CODE);
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
        Call<JsonResponse> call = ApiAdapter.getApiService().USER_ADD(email, names, lastName, address, phone, dniNumber);
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

    }

    public File getFile(Bitmap bmp, String prefix) {
        File tempFile = null;
        //Uri uri = null;
        try {
            File tempDir = Environment.getExternalStorageDirectory();
            tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
            tempDir.mkdir();
            tempFile = File.createTempFile(prefix, ".jpg", tempDir);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] bitmapData = bytes.toByteArray();
            FileOutputStream fos = new FileOutputStream(tempFile);
            // uri = Uri.fromFile(tempFile);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    public void uploadImageRetrofit(File imageFile, String email) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Subiendo...");
        dialog.setContentText("Por favor espere.");
        RequestBody emailBody = RequestBody.create(MultipartBody.FORM, email);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);
        Call<JsonResponse> call = ApiAdapter.getApiUser().USER_UPLOAD_IMAGE(emailBody, imagePart);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        dialog.dismissWithAnimation();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case IDENTIFY_REQUEST_CODE_FRONT:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imgDniFront.setImageBitmap(bitmap);
                    if (getFile(bitmap, "DNI_FRONT") != null) {
                        // uploadImage(getFile(bitmap,"DNI"),"rap@gmail.com","DNI_FRONT");

                    }
                    break;
                case IDENTIFY_REQUEST_CODE_BACK:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imgDniBack.setImageBitmap(bitmap);
                    if (getFile(bitmap, "DNI_BACK") != null) {
                        //  uploadImage(getFile(bitmap, "DNI_BACK"), "rap@gmail.com", "DNI_FRONT");
                    }
                    break;
                case DOMICILE_REQUEST_CODE:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imgAddress.setImageBitmap(bitmap);
                    uploadImageRetrofit(getFile(bitmap, "DNI_FRONT"), "rr@gmail.com");
//                if(getFile(bitmap,"DOMICILE")!=null) {
//                    uploadImage(getFile(bitmap, "DOMICILE"), "rap@gmail.com", "SERVICE_BILL_PAYMENT");
//                }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
