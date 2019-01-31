package com.vueltap.Transport.View;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.vueltap.Transport.Adapter.AdapterTransport;
import com.vueltap.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ViewTransport extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterTransport adapter;
    private LinearLayout linearLayout;
    private CheckBox cbClicla,cbMoto;
    private SweetAlertDialog dialog;
    private String urlProperty="",urlSOAT="",urlLicence="",numPlaca="";
    private EditText etPlaca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_view);
        setTitle("Transporte");
        loadControls();
    }

    private void loadControls() {
        etPlaca=findViewById(R.id.TextInputPlaca);
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

       linearLayout=findViewById(R.id.LinearLayoutTransport);
       cbClicla=findViewById(R.id.checkBoxBicicleta);
       cbMoto=findViewById(R.id.checkBoxMoto);

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

    }

    private Boolean validateMoto(View view){
        numPlaca=etPlaca.getText().toString().trim();
        if(numPlaca.isEmpty()){
            etPlaca.setError("Completa este campo");
            etPlaca.requestFocus();
            return false;
        }else if(urlLicence.isEmpty()){
            OnClickHelpLicence(view);
            return false;
        }else if(urlLicence.isEmpty()){
            OnClickHelpProperty(view);
            return false;
        }else if(urlSOAT.isEmpty()){
            OnClickHelpSoat(view);
            return false;
        }else{
            return true;
        }
    }

    private void validateCheck() {
        if(cbMoto.isChecked()){
            linearLayout.setVisibility(View.VISIBLE);
        }else{
            linearLayout.setVisibility(View.GONE);
        }

    }
    public void OnclickPhotoSoat(View view){
        urlSOAT="url";
    }
    public void OnclickPhotoLicence(View view){
        urlLicence="url";
    }
    public void OnclickPhotoProperty(View view){
        urlProperty="url";
    }

    public void OnClickHelpSoat(View view){
        dialog=new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu SOAP");
        dialog.show();
    }
    public void OnClickHelpLicence(View view){
        dialog=new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu licencia de conducir");
        dialog.show();

    }
    public void OnClickHelpProperty(View view){
        dialog=new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        dialog.setContentText("Tomale una foto a tu tarjeta de propidad");
        dialog.show();
    }
    public void OnClickRegister(View view){
        if(cbMoto.isChecked()){
            if(validateMoto(view)){
                Log.d("TAG","moto, todo OK");
            }
        }else if(cbClicla.isChecked()){
            Log.d("TAG","cicla, todo OK");
        }else{
            Log.d("TAG","Por favor selecciona alguna.");
        }
        if(cbClicla.isChecked() && cbMoto.isChecked()){
            if(validateMoto(view)){

            }
        }else if(cbClicla.isChecked()){

        }else if(cbMoto.isChecked()){
            if(validateMoto(view)){

            }
        }else{

        }
    }
}
