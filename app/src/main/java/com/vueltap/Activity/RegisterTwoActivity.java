package com.vueltap.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.vueltap.R;

public class RegisterTwoActivity extends AppCompatActivity {

    private CheckBox cbTerminosCondicciones, cbTratamientoDatos;
    private Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        setTitle("Términos y condiciones");
        loadControls();
    }

    private void loadControls() {
        cbTerminosCondicciones = findViewById(R.id.checkBoxTerminos);
        cbTratamientoDatos = findViewById(R.id.checkBoxTratamiento);
        btnSiguiente = findViewById(R.id.buttonSiguiente);
        cbTerminosCondicciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                validateCheck();
            }
        });
        cbTratamientoDatos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                validateCheck();

            }
        });

    }

    private void validateCheck() {
        if (cbTerminosCondicciones.isChecked() && cbTratamientoDatos.isChecked()) {
            btnSiguiente.setBackgroundResource(R.drawable.boton_redondo);
            btnSiguiente.setEnabled(true);
        } else {
            btnSiguiente.setBackgroundResource(R.drawable.button_enable_false);
            btnSiguiente.setEnabled(false);
        }
    }


    public void OnClickNext(View view) {
        startActivity(new Intent().setClass(getApplicationContext(), RegistrarDatosPersonales.class));
    }

    public void onclickTerminos(View view){
        Uri uri = Uri.parse("https://vueltap.herokuapp.com/terminos.html");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void onclickTratamiento(View view){
        Uri uri = Uri.parse("https://vueltap.herokuapp.com/tratamiento.html");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
