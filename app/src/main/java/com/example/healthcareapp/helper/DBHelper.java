package com.example.healthcareapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";

    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table users(PhoneNo TEXT primary key, FirstName TEXT,  LastName TEXT, EmailId TEXT, Password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String PhoneNo, String FirstName,String LastName,String EmailId,String Password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("PhoneNo", PhoneNo);
        contentValues.put("FirstName", FirstName);
        contentValues.put("LastName",LastName);
        contentValues.put("EmailId",EmailId);
        contentValues.put("Password",Password);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }

    public Boolean checkPhoneNo(String PhoneNo) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where PhoneNo = ?", new String[]{PhoneNo});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkPhoneNoPassword(String PhoneNo, String Password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where PhoneNo = ? and Password = ?", new String[] {PhoneNo,Password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}
