package com.vueltap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vueltap.Api.ApiAdapter;
import com.vueltap.Models.UploadImageJsonResponse;
import com.vueltap.util.JsonUtils;
import com.vueltap.Models.JsonResponse;
import com.vueltap.R;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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

import static com.vueltap.System.Constant.DOMICILE_REQUEST_CODE;
import static com.vueltap.System.Constant.IDENTIFY_REQUEST_CODE_BACK;
import static com.vueltap.System.Constant.IDENTIFY_REQUEST_CODE_FRONT;

public class RegistrarDatosPersonales extends AppCompatActivity {

    private ArrayList<JsonResponse> arrayList;
    private SweetAlertDialog dialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private EditText etCorreo, etNombres, etApellidos, etDireccion, etTelefono, etCedula;
    private ImageView imgCedula, imgDireccion;
    private Bitmap bitmap;
    private String email;
    private StorageReference storageRef;
    private OkHttpClient clientHttp = new OkHttpClient();
    private ObjectMapper mapper = new ObjectMapper();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private File tempFile=null;

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
//        firebaseAuth = FirebaseAuth.getInstance();
//        user = firebaseAuth.getCurrentUser();
//        etCorreo = findViewById(R.id.etCorreoElectronico);
        etNombres = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etDireccion = findViewById(R.id.etDireccion);
        etTelefono = findViewById(R.id.etTelefono);
        etCedula = findViewById(R.id.etCedula);
        imgCedula = findViewById(R.id.imageViewCedula);
        imgDireccion = findViewById(R.id.imageViewDireccion);
        etNombres.requestFocus();
        // loadData();
    }

    private void loadData() {
        email = user.getEmail();
        etCorreo.setText(email);
    }

    public void OnClickRegistrar(View view) {
        String email, nom, ape, dir, tel, ced;
        email = etCorreo.getText().toString().trim();
        nom = etNombres.getText().toString().trim();
        ape = etApellidos.getText().toString().trim();
        dir = etDireccion.getText().toString().trim();
        tel = etTelefono.getText().toString().trim();
        ced = etCedula.getText().toString().trim();
        if (nom.isEmpty()) {
            etNombres.setError("Por favor complete este campo.");
            etNombres.requestFocus();
        } else if (ape.isEmpty()) {
            etApellidos.setError("Por favor complete este campo.");
            etApellidos.requestFocus();
        } else if (dir.isEmpty()) {
            etDireccion.setError("Por favor complete este campo.");
            etDireccion.requestFocus();
        } else if (tel.isEmpty()) {
            etTelefono.setError("Por favor complete este campo.");
            etTelefono.requestFocus();
        } else if (ced.isEmpty()) {
            etCedula.setError("Por favor complete este campo.");
            etCedula.requestFocus();
        } else {
            dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            dialog.setContentText("Procesando. por favor espere.");
            dialog.show();
            Call<JsonResponse> call = ApiAdapter.getApiService().USER_ADD(email, nom, ape, dir, tel, ced);
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


      /*  Call<JsonResponse> call = ApiAdapter.getApiService().List();
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().isStatus()){

                        alertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        alertDialog.setContentText(response.body().getMessage());
                    }

                    //arrayList=new ArrayList<JsonResponse>(response.body().getMessage());

                   // mostrar();
                   // Toast.makeText(getApplicationContext(),arrayList.get(1)+"\n"+arrayList.get(0),Toast.LENGTH_LONG).show();

                } else {
                    alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    alertDialog.setContentText(response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                alertDialog.setContentText(t.getMessage());
            }
        });*/


    }

    public void OnClickDniFront(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IDENTIFY_REQUEST_CODE_FRONT);
    }
    public void OnClickDniBack(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IDENTIFY_REQUEST_CODE_BACK);
    }

    public void OnClickDniHelpFront(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu cédula de ciudadanía,(Parte frontal).<b>Si partes de la imagen están borrosas o no están claras, no " +
                "podremos comprobar la validez de tu identificación");
        dialog.setConfirmText("Aceptar");
        dialog.show();
    }
    public void OnClickDniHelpBack(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu cédula de ciudadanía,(Parte trasera).<b>Si partes de la imagen están borrosas o no están claras, no " +
                "podremos comprobar la validez de tu identificación");
        dialog.setConfirmText("Aceptar");
        dialog.show();
    }
    public void OnClickDomicilie(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, DOMICILE_REQUEST_CODE);
    }

    public void helpDireccion(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu comprobante de domicilio ya sea Agua,Luz o Teléfono,(No mayor a tres meses)." +
                "<b>Si partes de la imagen están borrosas o no están claras, no " +
                "podremos comprobar la validez de tu dirección.");
        dialog.setConfirmText("Aceptar");
        dialog.show();
    }

    public File getFile(Bitmap bmp, String prefix) {
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
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IDENTIFY_REQUEST_CODE_FRONT:
                bitmap = (Bitmap) data.getExtras().get("data");
                imgCedula.setImageBitmap(bitmap);
                if(getFile(bitmap,"DNI_FRONT")!=null){
                    uploadImage(getFile(bitmap,"DNI"),"rap@gmail.com","DNI_FRONT");
                }
                break;
            case IDENTIFY_REQUEST_CODE_BACK:
                bitmap = (Bitmap) data.getExtras().get("data");
                imgCedula.setImageBitmap(bitmap);
                if(getFile(bitmap,"DNI_BACK")!=null) {
                    uploadImage(getFile(bitmap, "DNI_BACK"), "rap@gmail.com", "DNI_FRONT");
                }
                break;
            case DOMICILE_REQUEST_CODE:
                bitmap = (Bitmap) data.getExtras().get("data");
                imgDireccion.setImageBitmap(bitmap);
                uploadImageRetrofit(getFile(bitmap,"DNI_FRONT"),"rapa@gmail.com");
//                if(getFile(bitmap,"DOMICILE")!=null) {
//                    uploadImage(getFile(bitmap, "DOMICILE"), "rap@gmail.com", "SERVICE_BILL_PAYMENT");
//                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadImageRetrofit(File imageFile,String email){

        RequestBody emailBody = RequestBody.create(MultipartBody.FORM, email);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",imageFile.getName(),imageBody);
        Call<Void> call=ApiAdapter.getApiService().USER_UPLOAD_IMAGE(emailBody,imagePart);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("OK","ok");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    private void uploadImage(File file, String email, String imageType) {

        try {


            String attachmentName, attachmentFileName, extension;

            attachmentName = file.getName();
            extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
            attachmentFileName = attachmentName; //+ extension;

            File compressedImageFile = null;

            try {
                compressedImageFile = new Compressor(this).compressToFile(file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", attachmentFileName, RequestBody.create(MediaType.parse("image/png"), compressedImageFile != null ? compressedImageFile : file))
                    .build();

            Request request = new Request.Builder()
                    .header("Accept", "application/json")
                    /*.header("Authorization", basicAuth)
                    .header(WebConstants.Companion.getTYPE(), AppConstants.INSTANCE.getMESSENGER())
                    .header(WebConstants.Companion.getTOKEN(), messenger.getSession().getToken())*/
                    .url("http://msau.vueltap.com.co/api_1.0/Messengers/Add/Image/" + email + "/" + imageType)
                    .put(requestBody)
                    .build();


            clientHttp.newCall(request).enqueue(new okhttp3.Callback() {

                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                    e.printStackTrace();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            //TODO
                            //UI show dialogs

                        }
                    });

                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {


                    if (response.isSuccessful()) {

                        String responseBodyString = response.body().string();


                        Logger.json(responseBodyString);

                        if (JsonUtils.isJSON(responseBodyString)) {


                            try {

                                UploadImageJsonResponse uploadImageResponse = mapper.readValue(responseBodyString, UploadImageJsonResponse.class);

                                if (uploadImageResponse.getStatus()) {

                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(tempFile.exists()){
                                                tempFile.delete();
                                            }

                                            //TODO
                                            //UI show dialogs

                                            //SUCCESS

                                        }
                                    });

                                } else {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            //TODO
                                            //UI show dialogs

                                            //ERROR

                                        }
                                    });
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        //TODO
                                        //UI show dialogs

                                    }
                                });
                            }


                        } else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    //TODO
                                    //UI show dialogs

                                }
                            });
                        }


                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                //TODO
                                //UI show dialogs

                            }
                        });
                    }

                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
            //TODO
            //UI show dialogs
        }

    }


}
