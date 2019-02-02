package com.vueltap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.vueltap.R;

public class MainActivityPruebaCam extends AppCompatActivity {
    private ImageView ivPrueba;
    private final int REQUEST_CODE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_prueba_cam);
        loadControls();
    }

    private void loadControls() {
        ivPrueba=findViewById(R.id.imageViewPrueba);
    }

    public void OnClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("RequestCode = ","_"+requestCode);
        Log.d("ResultCode = ","_"+requestCode);
        Log.d("Data = ","_"+data);
        /*switch (requestCode){
            case REQUEST_CODE:
                Bitmap bitmap=data.getExtras().get("data");
                break;
        }*/
    }
}


