package com.firstcalc.amiCalc;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyAmiDB extends SQLiteOpenHelper {

    int cnt;
    String id;

    public MyAmiDB(Context context){
        super(context, "amiDB.db",null,1);
    }
    public MyAmiDB(Context context, String id){
        super(context, "amiDB.db",null,1);
        this.id = id;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ami(ID TEXT, AMIDATE TEXT PRIMARY KEY, STIME TEXT, ETIME TEXT, CONTENT TEXT, OVERTIME INTEGER);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists ami");
    }

    public ArrayList<PrevData> getSelectAll(String sdate, String edate) {
        ArrayList<PrevData> listami = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from ami where id='"+id+"' and AMIDATE BETWEEN '"+ sdate + "' and '" + edate +"' order by AMIDATE;",null,null);
        /*Cursor cursor = db.rawQuery("SELECT * FROM ami WHERE ID='"+id+"'and ;",null ,null);*/


        if(cursor.getCount() !=0){
            cnt = cursor.getCount();
            while (cursor.moveToNext()){
                String amidate = cursor.getString(cursor.getColumnIndex("AMIDATE"));
                String stime = cursor.getString(cursor.getColumnIndex("STIME"));
                String etime = cursor.getString(cursor.getColumnIndex("ETIME"));
                String content = cursor.getString(cursor.getColumnIndex("CONTENT"));
                int overtime = cursor.getInt(cursor.getColumnIndex("OVERTIME"));

                PrevData prevData = new PrevData();
                prevData.setDate(amidate);
                prevData.setAttendance(stime);
                prevData.setLeavework(etime);
                prevData.setContent(content);
                prevData.setOvertime(overtime);
                listami.add(prevData);
            }
        }
        cursor.close();
        return listami;
    }
}