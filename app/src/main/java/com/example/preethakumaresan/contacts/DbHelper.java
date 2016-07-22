package com.example.preethakumaresan.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PREETHA KUMARESAN on 21-07-2016.
 */
public class DbHelper  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contacts";
    public static final String TABLE_NAME = "contact";
    public static final String COL_1 = "Name";
    public static final String COL_2 = "Number";
    public static final String COL_3 = "Img";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create a table with the following columns
        db.execSQL("create table contact (NAME TEXT , NUMBER TEXT,IMG BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    //Add a new contact
    public boolean insertData(String name, String number,byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,name);
        contentValues.put(COL_2,number);
        contentValues.put(COL_3,img);
        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result==-1)
            return false;
        else
            return true;
    }

    //Get all contacts to populate the listview
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from contact order by "+COL_1, null);
        return res;
    }

    //Update a sepcific contact
    public boolean update_data(String name, String number,byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,name);
        contentValues.put(COL_2,number);
        contentValues.put(COL_3,img);
        long res = db.update("contact", contentValues, "Name = ?", new String[]{name});//name is fixed. create an id if u want to change the name as well
        if(res == 0)
            return false;
        else
           return true;
    }

    public Integer delete_data(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contact","Name = ?",new String[]{ name });
    }
}
