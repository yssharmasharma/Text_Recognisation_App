package com.example.textapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int Record_code=1000;
    Button btn_gettext;
    EditText txtget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_gettext=(Button)findViewById(R.id.btn_gettext);
        txtget=(EditText)findViewById(R.id.txtget);



    }

    public void textReco(View view) {

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,Record_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Record_code){

            if (resultCode==RESULT_OK){

                Bitmap photo=(Bitmap)data.getExtras().get("data");
                textRecognisation(photo);

            }
            else if (resultCode==RESULT_CANCELED){
                Toast.makeText(this,"activity canel by user",Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this,"Filed to capture",Toast.LENGTH_SHORT).show();
            }
        }



    }

    private void textRecognisation(Bitmap photo) {

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(photo);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {


                                displayTextFromImage(firebaseVisionText);

//                                for (FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks()) {
//                                    String blockText = block.getText();
//                                    Point[] blockCornerPoints = block.getCornerPoints();
//                                    Rect blockFrame = block.getBoundingBox();
//
//
//
//                                    Toast.makeText(MainActivity.this,"Recognised test is : "+blockText,Toast.LENGTH_SHORT).show();
//
//                                    for (FirebaseVisionText.Line line: block.getLines()) {
//                                        ///
//
//
//                                        for (FirebaseVisionText.Element element: line.getElements()) {
//                                            Toast.makeText(MainActivity.this,"Element: "+element.getText(),Toast.LENGTH_SHORT).show();
//
//
//                                        }
//                                    }
//                                }
//
                          }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this,"fail to recognise given text",Toast.LENGTH_SHORT).show();
                                    }
                                });

    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.TextBlock> blockList =firebaseVisionText.getTextBlocks();
        if(blockList.size()==0)
        {
            Toast.makeText(this,"No Text Found in Image: ",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(FirebaseVisionText.TextBlock block:firebaseVisionText.getTextBlocks())
            {
                String text=block.getText();
                txtget.setText(text);
            }
        }
    }
    }

