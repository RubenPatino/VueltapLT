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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;
import com.vueltap.Activity.LoginActivity;
import com.vueltap.Api.ApiAdapter;
import com.vueltap.Models.JsonResponse;
import com.vueltap.R;
import com.vueltap.Transport.Adapter.AdapterTransport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vueltap.System.Constant.ADDRESS;
import static com.vueltap.System.Constant.DNI_NUMBER;
import static com.vueltap.System.Constant.LAST_NAME;
import static com.vueltap.System.Constant.NAMES;
import static com.vueltap.System.Constant.PHONE;
import static com.vueltap.System.Constant.URL_DNI_BACK;
import static com.vueltap.System.Constant.URL_DNI_FRONT;
import static com.vueltap.System.Constant.URL_DOMICILE;

public class ViewTransport extends AppCompatActivity {

    private static final int _PPROPERTY = 0;
    private static final int _LYCENCE = 1;
    private static final int _SOAT = 2;
    private static final int _TECNO=3;
    private RecyclerView recyclerView;
    private AdapterTransport adapter;
    private LinearLayout linearLayout;
    private CheckBox cbClicla, cbMoto;
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
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
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
        checkSoat=findViewById(R.id.imageViewCheckTecno);
       /* recyclerView=findViewById(R.id.rvTypeTransport);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });*/

        linearLayout = findViewById(R.id.LinearLayoutTransport);
        cbClicla = findViewById(R.id.checkBoxBicicleta);
        cbMoto = findViewById(R.id.checkBoxMoto);

        cbClicla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                validateCheck();
            }
        });
        cbMoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                validateCheck();
            }
        });

        loadData();

    }

    private void loadData() {
        email=user.getEmail();
        dniNumber = getIntent().getStringExtra(DNI_NUMBER);
        names = getIntent().getStringExtra(NAMES);
        lastName = getIntent().getStringExtra(LAST_NAME);
        address = getIntent().getStringExtra(ADDRESS);
        phone = getIntent().getStringExtra(PHONE);
        urlDniFront=getIntent().getStringExtra(URL_DNI_FRONT);
        urlDniBack=getIntent().getStringExtra(URL_DNI_BACK);
        urlAddress =getIntent().getStringExtra(URL_DOMICILE);
    }

    private Boolean validateMoto(View view) {
        numPlaca = etPlaca.getText().toString().trim();
        if (numPlaca.isEmpty()) {
            etPlaca.setError("Completa este campo");
            etPlaca.requestFocus();
            return false;
        } else if (urlLicence.isEmpty()) {
            OnClickHelpLicence(view);
            return false;
        } else if (urlProperty.isEmpty()) {
            OnClickHelpProperty(view);
            return false;
        } else if (urlSOAT.isEmpty()) {
            OnClickHelpSoat(view);
            return false;
        }else if(urlTecno.isEmpty()){
           OnClickHelpTecno(view);
            return false;
        }else {
            return true;
        }
    }

    private void validateCheck() {
        if (cbMoto.isChecked()) {
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


    public void OnclickPhotoLicence(View view) throws IOException {
        if (permissionManager.checkAndRequestPermissions(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,_LYCENCE);
        }
    }

    public void OnclickPhotoProperty(View view) {
        if (permissionManager.checkAndRequestPermissions(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,_PPROPERTY);
        }
    }

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

        if(cbClicla.isChecked()&&cbMoto.isChecked()){
            if(validateMoto(view)){
                savedUser();
            }
        }else if(cbClicla.isChecked()){
            savedUser();
        }else if(cbMoto.isChecked()){
            if(validateMoto(view)){
                savedUser();
            }
        }else{
            dialog=new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE);
            dialog.setContentText("Por favor selecciona una opción.");
            dialog.setConfirmText("Aceptar");
            dialog.show();
        }
    }

    public void savedUser(){
            dialog=new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
            dialog.setTitleText("Felicitaciones");
           // dialog.setContentText("Por favor espere.");
            dialog.setContentText("Has finalizado tu proceso de registro exitosamente.  Procederemos con un chequeo de seguridad de toda la información suministrada. Si eres seleccionado te llegará un mensaje de texto invitándote a una capacitación. Este proceso tardará una semana aproximadamente.");
            dialog.setConfirmText("Aceptar");
            dialog.show();

            firebaseAuth.signOut();
            Log.d("Datos",email+"_"+dniNumber+"_"+names+"_"+lastName+"_"+address+"_"+phone+"_"+urlDniFront+
                    "_"+urlDniBack+"_"+urlAddress+"_"+urlProperty+"_"+urlLicence+"_"+urlSOAT+"_"+urlTecno);
       /* new Timer().schedule(new TimerTask() {
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

    public void getMultipart(final File imageFile, int requestCode) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Subiendo...");
        dialog.setContentText("Por favor espere.");
        dialog.show();
        RequestBody emailBody = RequestBody.create(MultipartBody.FORM, email);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);
        switch (requestCode) {
            case _PPROPERTY:
                uploadpProperty(emailBody, imagePart, imageFile);
                break;
            case _LYCENCE:
                uploadLicence(emailBody, imagePart, imageFile);
                break;
            case _SOAT:
                uploadSoat(emailBody, imagePart, imageFile);
                break;
        }
    }

    public void uploadpProperty(RequestBody emailBody, MultipartBody.Part imagePart, final File imageFile) {
        Call<JsonResponse> call = ApiAdapter.getApiService().UPLOAD_PROPERTY(emailBody, imagePart);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        urlProperty = response.body().getMessage();
                        checkProperty.setVisibility(View.VISIBLE);
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

    public void uploadLicence(RequestBody emailBody, MultipartBody.Part imagePart, final File imageFile) {
        Call<JsonResponse> call = ApiAdapter.getApiService().UPLOAD_LICENCE(emailBody, imagePart);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        urlLicence = response.body().getMessage();
                        checkLicence.setVisibility(View.VISIBLE);
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

    public void uploadSoat(RequestBody emailBody, MultipartBody.Part imagePart, final File imageFile) {
        Call<JsonResponse> call = ApiAdapter.getApiService().UPLOAD_SOAT(emailBody, imagePart);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        urlSOAT = response.body().getMessage();
                        checkSoat.setVisibility(View.VISIBLE);
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

    public void uploadTecno(RequestBody emailBody, MultipartBody.Part imagePart, final File imageFile) {
        Call<JsonResponse> call = ApiAdapter.getApiService().UPLOAD_TECNO(emailBody, imagePart);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        urlTecno = response.body().getMessage();
                        checkTecno.setVisibility(View.VISIBLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            switch (requestCode) {
                case _PPROPERTY:
                    ivProperty.setImageBitmap(bitmap);
                    File property = getFile(bitmap);
                    if (property != null) {
                       getMultipart(property, requestCode);
                    }
                    break;
                case _LYCENCE:
                    ivLicence.setImageBitmap(bitmap);
                    File licence = getFile(bitmap);
                    if (licence != null) {
                        getMultipart(licence, requestCode);
                    }
                    break;
                case _SOAT:

                    ivSoat.setImageBitmap(bitmap);
                    File soat = getFile(bitmap);
                    if (soat != null) {
                        getMultipart(soat, requestCode);
                    }
                    break;
                case _TECNO:

                    ivSoat.setImageBitmap(bitmap);
                    File tecno = getFile(bitmap);
                    if (tecno != null) {
                        getMultipart(tecno, requestCode);
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

