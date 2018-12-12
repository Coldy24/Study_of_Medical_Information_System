package com.dof.jaeseonlee.patient.DataProcess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;
/**
 * Created by 이재선 on 2018-12-10.
 */
public class SQLiteClass extends SQLiteOpenHelper {

    private Context mContext;
    private int hrm = 0;
    private String date = null;

    public SQLiteClass(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.e("SQLiteClass_onCreate","Create Database Table");
        sqLiteDatabase.execSQL("CREATE TABLE DataSet (_id INTEGER PRIMARY KEY AUTOINCREMENT, TIME TEXT, HRM INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void setHRM(int hrm){
        this.hrm = hrm;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void addDate(int hrm, String date){

        SQLiteDatabase db = getWritableDatabase();
        Log.e("sQLiteClass_addData","hrm = " + hrm + " date + " + date);
        setHRM(hrm);
        setDate(date);
        db.execSQL("INSERT INTO DataSet VALUES (null, '" + this.date + "', "+ this.hrm + ");");
        db.close();
        }





}
