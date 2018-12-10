package com.dof.jaeseonlee.patient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.Date;
/**
 * Created by 이재선 on 2018-12-10.
 */
public class SQLiteClass extends SQLiteOpenHelper {

    private Context context;
    private int hrm = 0;
    private String date = null;

    public SQLiteClass(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE TABLE DataSet( ID integer primary key autoincrement");
        stringBuffer.append(" TIME DATETIME ");
        stringBuffer.append("HRM integer )");

        sqLiteDatabase.execSQL(stringBuffer.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void setHRM(int hrm){
        this.hrm = hrm;
    }

    public void setDate(Date date){
        this.date = date.toString();
    }

    public void addDate(int hrm, Date date){

        SQLiteDatabase db = getWritableDatabase();

        setHRM(hrm);
        setDate(date);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Insert into DataSet (");
        stringBuffer.append("TIME HRM )");
        stringBuffer.append("VALUES (?,?)");

        db.execSQL(stringBuffer.toString(),
                new Object[]{
                        this.hrm, this.date
                });
        }
}
