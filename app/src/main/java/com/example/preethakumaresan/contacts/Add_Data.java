package com.example.preethakumaresan.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by PREETHA KUMARESAN on 21-07-2016.
 */
public class Add_Data extends AppCompatActivity {
    EditText name;
    EditText number;
    DbHelper shit;

    Bitmap bitmap = null;
    byte[] photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        name= (EditText) findViewById(R.id.nameAdd);
        number = (EditText) findViewById(R.id.numberAdd);
        shit= new DbHelper(this);

        ((Button) findViewById(R.id.addData)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String na = name.getText().toString();
                String nu = number.getText().toString();

                if(bitmap == null) {
                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                photo = baos.toByteArray();
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                Bitmap theImage= BitmapFactory.decodeStream(imageStream);
                ImageView img_here = (ImageView) findViewById(R.id.imageView2);
                img_here.setImageBitmap(theImage);
                //stops here

                if(na.equals("")||nu.equals("")){
                    Toast.makeText(Add_Data.this,"U have to enter something!!",Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean inser = shit.insertData(na,nu,photo);

                    }

                Intent i = new Intent(Add_Data.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        //ause inbuilt camera
        ((Button) findViewById(R.id.tak_pik)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,1);
            }
        });
    }

    //onActivityResult for using the camera image and put it in the imageview and send it to the intents
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            Bundle bdn = data.getExtras();
            Bitmap btm = (Bitmap) bdn.get("data");
            bitmap = btm;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            photo = baos.toByteArray();

            ImageView img_here = (ImageView) findViewById(R.id.imageView2);
            img_here.setImageBitmap(btm);
        }
    }
}
