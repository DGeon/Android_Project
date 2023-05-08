package com.firstcalc.amiCalc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyMemberDB extends SQLiteOpenHelper {

    public MyMemberDB(Context context){
        super(context, "ami.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE member(ID TEXT PRIMARY KEY, PW TEXT, BASIC INTEGER, MEAL INTEGER, BUS TEXT, STIME TEXT, WTIME INTEGER);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists member");
    }


}