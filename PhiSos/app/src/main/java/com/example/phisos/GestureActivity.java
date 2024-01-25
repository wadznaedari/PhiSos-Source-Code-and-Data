package com.example.phisos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.gesture.Gesture;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class GestureActivity extends AppCompatActivity  {

    private float x1, x2,y1,y2;
TextView rresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);

        TextView textView = findViewById(R.id.rresult);

        int cd = getIntent().getIntExtra("value_of_c", 0);
        textView.setText(String.valueOf(cd));

    }

//    public boolean onTouchEvent(MotionEvent touchEvent){
//
//        switch (touchEvent.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                x1 = touchEvent.getX();
//                y1 = touchEvent.getY();
//                break;
//            case MotionEvent.ACTION_UP:
//                x2 = touchEvent.getX();
//                y2 = touchEvent.getY();
//
//                if (x1 < x2){
//                    Toast.makeText(this,"Swipe Left", Toast.LENGTH_SHORT).show();
//                }else{
//                    Intent i = new Intent(GestureActivity.this, OpenCamera.class);
//                    startActivity(i);
//                }
//                break;
//        }
//        return false;
//    }



}