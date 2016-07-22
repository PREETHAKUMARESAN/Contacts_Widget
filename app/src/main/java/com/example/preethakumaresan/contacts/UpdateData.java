package com.example.preethakumaresan.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by PREETHA KUMARESAN on 21-07-2016.
 */
public class UpdateData extends AppCompatActivity {

    DbHelper shit = new DbHelper(this);
    EditText nu;
    Button up,upPic;
    Bitmap theImage;
    byte[] image_he;
    ImageView image_here;

    String numb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        nu = (EditText) findViewById(R.id.phoneda);
        up=(Button) findViewById(R.id.confItUp);
        upPic = (Button) findViewById(R.id.upPic);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        final String name  = b.getString("nameIt");
        numb = b.getString("numb");
        nu.setText(numb);

        theImage = (Bitmap) i.getParcelableExtra("img");
        setTitle(name);
        image_here = (ImageView) findViewById(R.id.imageView3);
        image_here.setImageBitmap(theImage);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        theImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        image_he = baos.toByteArray();

        //Update button is clicked
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numb = nu.getText().toString();
                if (numb.equals("")) {
                    Toast.makeText(UpdateData.this, "Enter a new number or please enter the old number itself", Toast.LENGTH_LONG).show();
                } else {
                    boolean updated = shit.update_data(name, numb, image_he);
                }
                Intent i = new Intent(UpdateData.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        //Update picture button is clicked.
        upPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//for camera usage
                startActivityForResult(i,1);
            }
        });
    }

    //Function that captures image, and updates the DB.
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            Bundle bdn = data.getExtras();
            Bitmap btm = (Bitmap) bdn.get("data");
            image_here.setImageBitmap(btm);
            theImage = btm;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            theImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            image_he = baos.toByteArray();
        }
    }


}
