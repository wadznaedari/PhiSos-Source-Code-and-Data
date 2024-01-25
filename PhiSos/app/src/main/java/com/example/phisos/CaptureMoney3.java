package com.example.phisos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phisos.ml.MoneyNewmodel;
import com.example.phisos.ml.MoneyNewmodelv2;
import com.example.phisos.ml.MoneyNewmodelv3;
import com.example.phisos.ml.MoneyNewmodelv4;
import com.example.phisos.ml.MoneyNewmodelv5;
import com.example.phisos.ml.MoneyNewmodelv6;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

public class CaptureMoney3 extends AppCompatActivity {
    ImageView imageView;
    int imageSize = 224;
    TextView txtResult,textfirst,textsecond,textequals, textpercent;
    Button btnback;
    CardView cdcapturebtn;

    private float x1, x2,y1,y2;

    private Bitmap bitmap;
    private String uri;
    public int a3,b3;

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_money3);
        hideNavigationBar();

        imageView = findViewById(R.id.imageView);
        txtResult = findViewById(R.id.txtresult);
        btnback = findViewById(R.id.btnbck);
        textfirst = findViewById(R.id.txtfirst);
        textsecond = findViewById(R.id.txtsecond);
        textequals = findViewById(R.id.txtequals);


        String imagePath = getIntent().getStringExtra("imagePath");

        // Load and display the image from the file path
        if (imagePath != null) {
            Bitmap capturedBitmap = BitmapFactory.decodeFile(imagePath);
            if (capturedBitmap != null) {
                imageView.setImageBitmap(capturedBitmap);
                capturedBitmap = Bitmap.createScaledBitmap(capturedBitmap, imageSize, imageSize, false);
                classifyImage(capturedBitmap);
            } else {
                // Error handling if bitmap couldn't be decoded
            }
        }


        String sourceActivity = getIntent().getStringExtra("source_activity3");

        if ("PreviousActivity3".equals(sourceActivity)){
            String receivedText = getIntent().getStringExtra("value_of_c3");
            textfirst.setText(String.valueOf(receivedText));
        }


        String ax = textfirst.getText().toString();
        a3 = Integer.parseInt(ax);
        String bx = textsecond.getText().toString();
        b3 = Integer.parseInt(bx);

        Integer cx = a3 + b3;


        textequals.setText(String.valueOf(cx));


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaptureMoney3.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }


    public boolean onTouchEvent(MotionEvent touchEvent){

        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                if (x1 < x2){
                    tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status == TextToSpeech.SUCCESS){
                                tts.setLanguage(Locale.US);
                                tts.setSpeechRate(1.0f);
                                tts.speak("Start Capture New Money", TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                    });
                    Intent i = new Intent(CaptureMoney3.this, OpenCamera.class);
                    startActivity(i);
                }else if(x1 > x2){
                    tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status == TextToSpeech.SUCCESS){
                                tts.setLanguage(Locale.US);
                                tts.setSpeechRate(1.0f);
                                tts.speak("Add Money", TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                    });
                    int c3 = a3 + b3;
                    Intent i = new Intent(CaptureMoney3.this, OpenCamera2.class);
                    i.putExtra("value_of_c3", c3);
                    i.putExtra("source2_activity2", "PreviousActivity2");
                    startActivity(i);
                }

                break;
        }
        return false;
    }





    public void classifyImage(Bitmap image){
        try {


            MoneyNewmodelv6 model = MoneyNewmodelv6.newInstance(getApplicationContext());


            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4  * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0 ,image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++){
                for (int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++];//RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            MoneyNewmodelv6.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Runs model inference and gets result.



            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;


            for(int i = 0; i < confidences.length; i++){
                if (confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;

                    //System.out.println(maxConfidence);

                }

            }



            String[] classes = {
                    "One Peso Coin",
                    "Five Peso Coin",
                    "Ten Peso Coin",
                    "Twenty Peso Coin",
                    "Twenty Peso Note",
                    "Fifty Peso Note",
                    "One Hundred Peso Note",
                    "Two Hundred Peso Note",
                    "Five Hundred Peso Note",
                    "One Thousand Peso Note",
                    "Invalid Money",
                    "Unrecognized Money"
            };
            txtResult.setText(classes[maxPos]);


            String  txtfirstdisplay = txtResult.getText().toString().trim();

            if (txtfirstdisplay.equals("One Peso Coin")){
                textsecond.setText("1");
            }else if(txtfirstdisplay.equals("Ten Peso Coin")){
                textsecond.setText("10");
            }else if(txtfirstdisplay.equals("Five Peso Coin")){
                textsecond.setText("5");
            }
            else if(txtfirstdisplay.equals("Twenty Peso Coin")){
                textsecond.setText("20");
            }
            else if(txtfirstdisplay.equals("Twenty Peso Note")){
                textsecond.setText("20");
            }
            else if(txtfirstdisplay.equals("Fifty Peso Note")){
                textsecond.setText("50");
            } else if(txtfirstdisplay.equals("One Hundred Peso Note")){
                textsecond.setText("100");
            }else if(txtfirstdisplay.equals("Two Hundred Peso Note")){
                textsecond.setText("200");
            }
            else if(txtfirstdisplay.equals("Five Hundred Peso Note")){
                textsecond.setText("500");
            }
            else if(txtfirstdisplay.equals("One Thousand Peso Note")){
                textsecond.setText("1000");
            }
            else if(txtfirstdisplay.equals("Invalid Money")){
                textsecond.setText("0");
            }
            else if(txtfirstdisplay.equals("Unrecognized Money")){
                textsecond.setText("0");
            }



            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS){
                        tts.setLanguage(Locale.US);
                        tts.setSpeechRate(1.0f);
                        tts.speak(txtResult.getText().toString()+" + Previous "+textfirst.getText().toString()+" = "+textequals.getText().toString(), TextToSpeech.QUEUE_ADD, null);
                    }
                }
            });








            // Releases model resources if no longer used.
            model.close();

        } catch (IOException e) {
            // TODO Handle the exception
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);




            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void hideNavigationBar() {
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(CaptureMoney3.this,"Invalid Press", Toast.LENGTH_SHORT).show();
    }

}