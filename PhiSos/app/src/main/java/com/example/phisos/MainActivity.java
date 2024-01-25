package com.example.phisos;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 0;
    CardView cdOpenCamera,cdCapture, cdExit, cdAboutus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideNavigationBar();

        cdExit = findViewById(R.id.cdexit);
        cdAboutus = findViewById(R.id.cdaboutus);
        cdCapture = findViewById(R.id.cdCapture);

        cdAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutUs.class);
                startActivity(intent);

            }
        });

        cdExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        cdCapture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //launch
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent = new   Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);


                }else{
                    requestPermissions(new String[]{Manifest.permission.CAMERA},100);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK ){
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                //Starting activity (ImageViewActivity in my code) to preview image
                Intent intent = new Intent(this, CaptureMoney.class);
                intent.putExtra("BitmapImage", photo);
                startActivity(intent);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);


    }
    private void hideNavigationBar() {

        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY );

    }
}