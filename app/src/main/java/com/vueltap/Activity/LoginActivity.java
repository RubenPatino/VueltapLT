package com.vueltap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;
import com.vueltap.Api.ApiAdapter;
import com.vueltap.Models.JsonResponse;
import com.vueltap.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vueltap.System.Constant.EMAIL;
import static com.vueltap.System.Constant.ID_USER;
import static com.vueltap.System.Constant.VALIDATE_FINISHED;
import static com.vueltap.System.Constant.VALIDATE_INFORMATION;
import static com.vueltap.System.Constant.VALIDATE_TRAINING;
import static com.vueltap.System.Constant.VALIDATE_TRANSPORT;


public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etEmail;
    private TextInputEditText etPass;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private SweetAlertDialog dialog;
    private String email;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadControls();
    }

    private void loadControls() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etEmail.requestFocus();
        loadPermission();
        loadAuthState();
    }

    private void loadPermission() {
        PermissionManager permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(this);
    }

    private void loadAuthState() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (user.isEmailVerified()) {
                        if (dialog != null) {
                            dialog.dismissWithAnimation();
                        }
                        checkEmail(user.getEmail());
                    } else {
                        dialog.dismissWithAnimation();
                        emailVerified();
                    }
                }
            }
        };
    }

    // Verificamos si el correo ya esta registrado
    private void checkEmail(String email) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setContentText("Procesando, por favor espere.");
        dialog.show();
        Call<JsonResponse> call = ApiAdapter.getApiService().EMAIL_CHECK(email);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        if (response.body().getUser().getState()) {
                            String checkIn = response.body().getUser().getCheckIn();
                            if (checkIn.equals(VALIDATE_INFORMATION)) {
                                dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                dialog.setContentText("Se esta revisando la información suministrada, este proceso" +
                                        " pude durar hasta una semana aproximadamente. Gracias por su compresión.");
                                dialog.setConfirmText("Aceptar");
                                firebaseAuth.signOut();
                            } else if (checkIn.equals(VALIDATE_TRANSPORT)) {
                                final String id = response.body().getUser().getId();
                                dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                dialog.setContentText("Muy bien, toda la información suministrada se ha revisado correctamente, lo " +
                                        "invitamos a que seleccione tu tipo de transporte");
                                dialog.setConfirmButton("Aceptar", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent intent = new Intent(getApplicationContext(), TypeTransport.class);
                                        intent.putExtra(ID_USER, id);
                                        startActivity(intent);
                                    }
                                });
                            } else if (checkIn.equals(VALIDATE_TRAINING)) {
                                dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                dialog.setContentText("¡Excelente!, Ya estamos listos para empezar a trabajar. Te esperamos por nuestras" +
                                        " instalaciones para una capacitación de manejo del programa y entrega de material. Recuerda que nos encontramos en bogota" +
                                        " en la direccion xxx, tel xxx");
                                dialog.setConfirmButton("Aceptar", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        firebaseAuth.signOut();
                                        dialog.dismissWithAnimation();
                                      }
                                });

                            } else if (checkIn.equals(VALIDATE_FINISHED)) {
                                dialog.dismissWithAnimation();
                                finish();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra(ID_USER, response.body().getUser().getId());
                                startActivity(intent);
                            }


                        } else {
                            dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                            dialog.setContentText("Lo sentimos esta cuenta esta bloqueada, lo invitamos a comunicarse con nosotros" +
                                    " al tel. (031)432 5760, para aclarar las dudas.  ");
                            dialog.setConfirmText("Aceptar");
                            firebaseAuth.signOut();
                        }


                    } else {
                        dialog.dismissWithAnimation();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), RegisterWelcome.class);
                        intent.putExtra(EMAIL, user.getEmail());
                        startActivity(intent);
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

    private void emailVerified() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.setConfirmText("Aceptar");
                    dialog.setContentText("Se te ha enviado un correo electrónico a <b>" + email + "</b>. Haz clic en el enlace dentro del " +
                            "correo electrónico para verificar tu cuenta. Si no ves el correo electrónico en tu bandeja " +
                            "de entrada, revisa otros lugares donde prodría estar, como tus carpetas de correo no deseado, sociales u otras. Una vez " +
                            "verificada por favor inicia sesión.");
                    firebaseAuth.signOut();

                } else {
                    dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    dialog.setContentText(task.getException().getMessage());
                    dialog.setCancelText("Salir");
                    dialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setVisibility(View.GONE);
                }

            }
        });
        dialog.show();
    }

    public void onClickLogIn(View view) {
        email = etEmail.getText().toString().trim();
        pass = etPass.getText().toString().trim();
        if (verifyMail(email, etEmail) && verifyPass(pass, etPass)) {
            dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            dialog.setContentText("Ingresando.");
            dialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        dialog.setCancelText("Salir");
                        dialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setVisibility(View.GONE);
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            dialog.setContentText("No hay registro de usuario correspondiente a esta dirección de correo electrónico o su cuenta se encuentra inhabilitada.");
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            dialog.setContentText("Los datos son incorrectos, verifica he intenta de nuevo.");
                        } catch (Exception e) {
                            dialog.setContentText(e.getMessage());
                        }
                    }
                }
            });
        }
    }

    public void forgotPassword(View view) {
        final EditText editText = new EditText(this);

        dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitle("Ingresa tu correo electronico");
        dialog.setCustomView(editText);
        dialog.show();
        dialog.setConfirmButton("Enviar", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                email = editText.getText().toString().trim();
                if (verifyMail(email, editText)) {
                    dialog.dismissWithAnimation();
                    sendPasswordReset(email);
                }
            }
        });
    }

    public void sendPasswordReset(final String email) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setContentText("Procesando.");
        dialog.show();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setContentText("Se te ha enviado un correo electrónico a <b> " + email + "</b>. Haz clic en el enlace dentro del " +
                            "correo electronico para restablecer tu contraseña. Si no ves el correo electrónico en tu bandeja" +
                            "de entrada, revisa otros lugares donde prodría estar, como tus carpetas de correo no deseado, sociales u otras.");
                    dialog.setConfirmText("Aceptar");
                } else {
                    dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    dialog.setCancelText("Salir");
                    dialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setVisibility(View.GONE);
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        dialog.setContentText("El correo electrónico no esta registrado o su cuenta se encuentra inhabilitada.");
                    } catch (Exception e) {
                        dialog.setContentText(e.getMessage());
                    }
                }
            }
        });
    }

    public void OnClickRegister(View view) {
        LayoutInflater inflater = this.getLayoutInflater();
        final View input = inflater.inflate(R.layout.input, null);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        dialog.setCustomView(input);
        dialog.show();
        dialog.setConfirmButton("Registrar", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                EditText etEmail = input.findViewById(R.id.etRegisterEmail);
                EditText etPass = input.findViewById(R.id.etRegisterPass);
                email = etEmail.getText().toString();
                pass = etPass.getText().toString();
                if (verifyMail(email, etEmail) && verifyPass(pass, etPass)) {
                    dialog.dismissWithAnimation();
                    createAccount(email, pass);
                }

            }
        });
    }

    private void createAccount(String email, String pass) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setContentText("Procesando el registro<br>Por favor espere.");
        dialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                    dialog.setCancelText("Salir");
                    dialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setVisibility(View.GONE);
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        dialog.setContentText("La contraseña dada no es válida. [La contraseña debe tener al menos 6 caracteres].");
                    } catch (FirebaseAuthUserCollisionException e) {
                        dialog.setContentText("La dirección de correo electrónico ya está en uso por otra cuenta.");
                    } catch (Exception e) {
                        dialog.setContentText(e.getMessage());
                    }
                }
            }
        });
    }

    private boolean verifyPass(String pass, EditText editText) {
        if (pass.isEmpty()) {
            editText.setError("Introduce la contraseña.");
            editText.requestFocus();
            return false;
        } else if (pass.length() < 5) {
            editText.setError("Por favor introduzca una contraseña  mayor de 5 caracteres (números y letras) solamente.");
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyMail(String email, EditText editText) {
        if (email.isEmpty()) {
            editText.setError("Por favor introduzca un correo electrónico.");
            editText.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText.setError("Correo electrónico no válida.");
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
