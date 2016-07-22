package com.example.preethakumaresan.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    List<String> namesList = new ArrayList<>();
    List<String> numberList = new ArrayList<>();
    List<Bitmap> imageList = new ArrayList<>();

    String[] names;
    String[] numbers;
    Bitmap[] images;

    DbHelper shit = new DbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObtainNames();
        names=new String[namesList.size()];
        numbers = new String[numberList.size()];
        images = new Bitmap[imageList.size()];
        for(int i=0; i<namesList.size(); ++i){
            names[i]=namesList.get(i);
            numbers[i]=numberList.get(i);
            images[i] = imageList.get(i);
        }

        ListAdapter listAdapter = new CustomAdapter(MainActivity.this,names,numbers,images);
        ListView contList = (ListView) findViewById(R.id.contList);
        contList.setAdapter(listAdapter);

        contList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = String.valueOf(parent.getItemAtPosition(position));
                ImageView curr_img = (ImageView) view.findViewById(R.id.imageView);
                String number = numbers[position];

                curr_img.buildDrawingCache();
                Bitmap curri = curr_img.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("curri", curri);
                Intent i = new Intent(MainActivity.this, Contact_Content.class);
                i.putExtra("name", name).putExtra("number", number).putExtras(extras);
               // i.putExtras(extras);
                startActivity(i);

            }
        });

        //Order contacts
        ((Button) findViewById(R.id.order)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.reverse(namesList);
                Collections.reverse(numberList);
                Collections.reverse(imageList);

                names = new String[namesList.size()];
                numbers = new String[numberList.size()];
                images=new Bitmap[imageList.size()];
                for (int i = 0; i < namesList.size(); ++i) {
                    names[i] = namesList.get(i);
                    numbers[i] = numberList.get(i);
                    images[i] = imageList.get(i);
                }

                ListAdapter myAdapter = new CustomAdapter(MainActivity.this, names, numbers,images);
                ListView contList = (ListView) findViewById(R.id.contList);
                contList.setAdapter(myAdapter);
            }
        });

        //Search for a specific name or number.
        ((Button) findViewById(R.id.ser_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numb = ((EditText) findViewById(R.id.ser_txt)).getText().toString();
                boolean found = false;
                Pattern Pat = Pattern.compile("^[0-9]+$");

                //If the input is a number, search for name.
                if(Pat.matcher(numb).matches()) {
                    for (int i = 0; i < numbers.length; i++) {
                        if (numbers[i].equals(numb)) {
                            Toast.makeText(MainActivity.this, "Name: " + names[i]+"\n Number: "+numbers[i], Toast.LENGTH_LONG).show();
                            found = true;
                            break;
                        }
                    }
                }

                //If input is a name, search for number
                else{
                    for(int i=0; i<names.length; ++i){
                        if(names[i].equalsIgnoreCase(numb)){
                            Toast.makeText(MainActivity.this, "Contact Found. Number: " + numbers[i]+"\n Name: "+names[i],Toast.LENGTH_LONG).show();
                            found = true;
                            break;
                        }
                    }
                }

            }
        });

    }
    public void ObtainNames(){
        Cursor res = shit.getAllData();
        if(res.getCount()==0){
            return;
        }
        while (res.moveToNext()){
            namesList.add(res.getString(0));
            numberList.add(res.getString(1));
            byte[] blob = res.getBlob(2);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(blob);
            Bitmap theImage= BitmapFactory.decodeStream(imageStream);
            imageList.add(theImage);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.AddIt:
                Intent i = new Intent(MainActivity.this,Add_Data.class);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}