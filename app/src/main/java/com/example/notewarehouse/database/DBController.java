package com.example.notewarehouse.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBController extends SQLiteOpenHelper {
    public DBController(Context context) {
        super(context, "notewarehouse", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user (id_user text, nama text, email text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        onCreate(db);
    }

    public void insertData(HashMap<String, String> queryValues){
        SQLiteDatabase basisData = this.getWritableDatabase();
        ContentValues nilai = new ContentValues();
        nilai.put("id_user",queryValues.get("id_user"));
        nilai.put("nama",queryValues.get("nama"));
        nilai.put("email",queryValues.get("email"));
        basisData.insert("user", null, nilai);
        basisData.close();
    }

    public HashMap<String, String> findData(){
        HashMap<String, String> respon = new HashMap<String, String>();

        String query = "SELECT * FROM user";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                respon.put("id_user",cursor.getString(0));
                respon.put("nama",cursor.getString(1));
                respon.put("email",cursor.getString(2));
            }while(cursor.moveToNext());
        }
        return respon;
    }

    public void deleteData(String id){
        SQLiteDatabase basisData = this.getWritableDatabase();
        basisData.delete("user", "id_user = ?", new String[]{id});
        basisData.close();
    }
}
