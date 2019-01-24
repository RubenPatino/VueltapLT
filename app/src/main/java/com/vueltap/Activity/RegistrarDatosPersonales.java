package com.vueltap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
import static com.vueltap.System.Constant.IDENTIFY_REQUEST_CODE;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos_personales);
        setTitle("Formulario de registro");
        loadControls();
    }

    private void loadControls() {
        storageRef = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        etCorreo = findViewById(R.id.etCorreoElectronico);
        etNombres = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etDireccion = findViewById(R.id.etDireccion);
        etTelefono = findViewById(R.id.etTelefono);
        etCedula = findViewById(R.id.etCedula);
        imgCedula = findViewById(R.id.imageViewCedula);
        imgDireccion = findViewById(R.id.imageViewDireccion);
        etNombres.requestFocus();
        loadData();
    }

    private void loadData() {
        email=user.getEmail();
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

    public void OnClickFotoCedula(View view) {


        Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intento1, IDENTIFY_REQUEST_CODE);

    }

    public void OnClickFotoDomicilio(View view) {
        Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intento1, DOMICILE_REQUEST_CODE);

    }

    public void helpCedula(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu cédula de ciudadanía,(Cara frontal).<b>Si partes de la imagen están borrosas o no están claras, no " +
                "podremos comprobar la validez de tu identificación");
        dialog.setConfirmText("Aceptar");
        dialog.show();
    }

    public void helpDireccion(View view) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu comprobante de domicilio ya sea Agua,Luz o Teléfono,(No mayor a tres meses)." +
                "<b>Si partes de la imagen están borrosas o no están claras, no " +
                "podremos comprobar la validez de tu dirección.");
        dialog.setConfirmText("Aceptar");
        dialog.show();
    }

    public void uploadFile(String path,Uri img){

       // Uri file = Uri.fromFile(new File(email+"/"+path+".jpg"));
        //Uri file=img;
        StorageReference riversRef = storageRef.child(email+"/"+path+".jpg");
        //StorageReference filePath = storageRef.child(email).child(img.getLastPathSegment());
        riversRef.putFile(img)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                       // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }
    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        Log.d("Location : ",root);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getImageData(Bitmap bmp) {

        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, ops);// bmp is bitmap from user image file
        bmp.recycle();
        byte[] byteArray = ops.toByteArray();
        String imageB64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //StorageReference filePath = storageRef.child(email);
        Log.d("ops",imageB64);
        //  store & retrieve this string to firebase
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this, "requestCode : " + requestCode + "_resultCode : " + requestCode, Toast.LENGTH_LONG).show();

        switch (requestCode) {
            case IDENTIFY_REQUEST_CODE:

                try {
                    //Uri uri=data.getData().;
                   // File file = new File ("cedula.jpeg");
                    File f = new File(this.getCacheDir(), "img");
                    f.createNewFile();
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imgCedula.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(byteArray);
                    fos.flush();
                    fos.close();

                    /*RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);

                    MultipartBody.Part body = MultipartBody.Part.createFormData("upload","image", reqFile);
                    Call<Void> call=ApiAdapter.getApiService().USER_UPLOAD_IMAGE(reqFile,body);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                Log.d("OK","OK");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Error",""+t);
                        }
                    });*/

                    uploadImage(f, email, "DNI_FRONT");


                   // bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
                    //getImageData(bitmap);
                    //SaveImage(bitmap);
                    /*FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);*/
                    //Log.d("URI","Guardado");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Uri uri=data.getData();

               /* StorageReference filePath = storageRef.child(email).child(uri.getLastPathSegment());
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Subida", Toast.LENGTH_LONG).show();

                    }
                });*/
               // uploadFile("cedula",file);
                break;
            case DOMICILE_REQUEST_CODE:
                bitmap = (Bitmap) data.getExtras().get("data");
                imgDireccion.setImageBitmap(bitmap);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadImage(File file, String email, String imageType){

        try{



            String attachmentName, attachmentFileName, extension;

            attachmentName = file.getName();
            extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
            attachmentFileName = attachmentName + extension;

            File compressedImageFile = null;

            try {
                compressedImageFile = new Compressor(this).compressToFile(file);
            }catch (Exception ex){
                ex.printStackTrace();
            }

            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", attachmentFileName, RequestBody.create(MediaType.parse("image/png"), compressedImageFile != null ?  compressedImageFile : file))
                    .build();

            Request request = new Request.Builder()
                    .header("Accept", "application/json")
                    /*.header("Authorization", basicAuth)
                    .header(WebConstants.Companion.getTYPE(), AppConstants.INSTANCE.getMESSENGER())
                    .header(WebConstants.Companion.getTOKEN(), messenger.getSession().getToken())*/
                    .url("http://msau.vueltap.com.co/api_1.0/Messengers/Add/Image/"+email+"/"+imageType)
                    .put(requestBody)
                    .build();


                    clientHttp.newCall(request).enqueue(new okhttp3.Callback(){

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


                            if(response.isSuccessful()){

                                String responseBodyString = response.body().string();

                                if(JsonUtils.isJSON(responseBodyString)){


                                    try{

                                        UploadImageJsonResponse uploadImageResponse = mapper.readValue(responseBodyString, UploadImageJsonResponse.class);

                                        if(uploadImageResponse.getStatus()){

                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {

                                                    //TODO
                                                    //UI show dialogs

                                                    //SUCCESS

                                                }
                                            });

                                        }else{
                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {

                                                    //TODO
                                                    //UI show dialogs

                                                    //ERROR

                                                }
                                            });
                                        }

                                    }catch(Exception e){
                                        e.printStackTrace();
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                //TODO
                                                //UI show dialogs

                                            }
                                        });
                                    }


                                }else{
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            //TODO
                                            //UI show dialogs

                                        }
                                    });
                                }


                            }else{
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



        }catch(Exception ex){
            ex.printStackTrace();
            //TODO
            //UI show dialogs
        }

    }
}
