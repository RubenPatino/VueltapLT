package com.vueltap.Transport.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_view);
        setTitle("Transporte");
        loadControls();
    }

    private void loadControls() {
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

    private void validateCheck() {
        if(cbMoto.isChecked()){
            linearLayout.setVisibility(View.VISIBLE);
        }else{
            linearLayout.setVisibility(View.GONE);
        }

    }

    public void OnClickHelpSoat(View view){
        dialog=new SweetAlertDialog(this,SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        dialog.setContentText("Tomale una foto a tu SOAP");
        dialog.show();
    }
    public void OnClickHelpLicence(View view){}
    public void OnClickHelpProperty(View view){}
}
