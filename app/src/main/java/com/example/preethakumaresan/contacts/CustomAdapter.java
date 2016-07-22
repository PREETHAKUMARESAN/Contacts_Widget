package com.example.preethakumaresan.contacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

/**
 * Created by PREETHA KUMARESAN on 21-07-2016.
 */
public class CustomAdapter extends ArrayAdapter<String> {
    public String[] numbs;
    public Bitmap[] imgs;
    Context context;

    public CustomAdapter(Context context,String[] names,String[] numbers,Bitmap[] photos) {
        super(context,R.layout.custom_row,names);
        this.numbs = numbers;
        this.context = context;
        this.imgs = photos;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_row, parent, false);
        final String currName = getItem(position);

        TextView na = (TextView) customView.findViewById(R.id.nameInList);
        ImageView ima = (ImageView) customView.findViewById(R.id.imageView);
        TextView nu = (TextView) customView.findViewById(R.id.numbInList);
        Button upda = (Button) customView.findViewById(R.id.upData);
        Button dele = (Button) customView.findViewById(R.id.del);

        //Delete a contact directly from listview
        dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(context);
                alertdialogbuilder.setMessage("Are u sure u want to delete?").setCancelable(false).setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbHelper db = new DbHelper(context);
                        Integer i = db.delete_data(currName);
                        if (i > 0) {
                            Intent p = new Intent(context, MainActivity.class);
                            context.startActivity(p);
                        }
                    }
                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertdialogbuilder.create();
                alertDialog.show();

            }
        });

        //Update a contact using the button
        upda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(context);
                alertdialogbuilder.setMessage("Are u sure u want to change the contact details??").setCancelable(false).setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DbHelper db = new DbHelper(context);
                        Bitmap bitmap = imgs[position];
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        //byte[] photo = baos.toByteArray();

                        Intent i = new Intent(context, UpdateData.class);
                        i.putExtra("nameIt", currName).putExtra("img",bitmap).putExtra("numb",numbs[position]);
                        context.startActivity(i);
                    }
                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertdialogbuilder.create();
                alertDialog.show();
            }
        });

        na.setText(currName);
        nu.setText(numbs[position]);
        ima.setImageBitmap(imgs[position]);
        return customView;
    }
}
