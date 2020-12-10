package com.abhishek.myliber;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

class SqliteHelper extends SQLiteOpenHelper {
    public SqliteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public void querydata(String sql){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        sqLiteDatabase.execSQL(sql);

    }
    public void insertLatlong(String lattitude,String longitude){
        SQLiteDatabase database=getWritableDatabase();
        String sql="INSERT INTO LATLONG VALUES (?,?)";
        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,lattitude);
        statement.bindString(2,longitude);
        statement.executeInsert();
    }
    public void insertData(String id, String firstname, String lastname, byte[] image){
        SQLiteDatabase database=getWritableDatabase();
        String sql="INSERT INTO PROFILE VALUES (?,?,?,?)";
        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, id);
        statement.bindString(2,firstname);
        statement.bindString(3,lastname);
        statement.bindBlob(4,image);
        statement.executeInsert();
    }
    public Cursor getData(String sql){
        SQLiteDatabase database=getReadableDatabase();
        return database.rawQuery(sql,null);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
