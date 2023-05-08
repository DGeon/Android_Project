package com.firstcalc.amiCalc;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ami extends AppCompatActivity {
    MyMemberDB myMemberDB = new MyMemberDB(this);
    private long backKeyPressedTime = 0;
    private DrawerLayout drawerLayout;
    private View drawerView;
    long finaltime;
    Date date;
    String queryId, sTime, eTime, amidate,stimedate,etimedate, dbstime;
    EditText amiEvStartDate;
    MyAmiDB myAmiDB = new MyAmiDB(Ami.this);
    int oTime, wtime;
    String[] otstrr = {"07:00","08:00","09:00","10:00","11:00","12:00"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ami);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        //slider메뉴

        //slider버튼
        Intent intentget = getIntent();
        intentget.getExtras();

        TextView menubarTvId = (TextView) findViewById(R.id.menubarTvId);
        queryId = intentget.getExtras().getString("loginId");
        menubarTvId.setText(queryId+"님");

        TextView menuTvBtn = (TextView) findViewById(R.id.menuTvBtn);
        menuTvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });
        //근태기록
        TextView menubarTvPami = (TextView)findViewById(R.id.menubarTvPami);
        menubarTvPami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
                Intent intent =  new Intent(getApplicationContext(), PrevAmi.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);

            }
        });
        //퇴근
        TextView menubarTvAmi = (TextView)findViewById(R.id.menubarTvAmi);
        menubarTvAmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
                Intent intent =  new Intent(getApplicationContext(), Ami.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);

            }
        });

        //버스
        TextView menubarTvBus = (TextView)findViewById(R.id.menubarTvBus);
        menubarTvBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
                Intent intent =  new Intent(getApplicationContext(), Bus.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);

            }
        });
        //노동부
        TextView menubarTvWork = (TextView)findViewById(R.id.menubarTvWork);
        menubarTvWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
                Intent intent =  new Intent(getApplicationContext(), Work.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);

            }
        });

        //마이페이지
        TextView menubarTvMypage = (TextView)findViewById(R.id.menubarTvMypage);
        menubarTvMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
                Intent intent =  new Intent(getApplicationContext(), MyPage.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);

            }
        });

        //로그아웃
        TextView menubarTvLogout = (TextView)findViewById(R.id.menubarTvLogout);
        menubarTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
                SharedPreferences prefs = getSharedPreferences("AutoLogin",0);
                SharedPreferences.Editor edit = prefs.edit();

                //삭제
                edit.remove("id");
                edit.remove("pw");
                edit.commit();
                Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        //오픈소스
        TextView menubarTvOpen = (TextView)findViewById(R.id.menubarTvOpen);
        menubarTvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
                Intent intent =  new Intent(getApplicationContext(), OpenInfo.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);

            }
        });

        SQLiteDatabase db;
        ContentValues values;
        String[] memberarr= {"ID","PW","BASIC","MEAL","STIME","WTIME"};
        Cursor cur;

        db = myMemberDB.getReadableDatabase(); //검색할때

        cur = db.query("member",memberarr,"ID=?",new String[]{queryId},null,null,null);

        int stime_column = cur.getColumnIndex("STIME");
        int wtime_column = cur.getColumnIndex("WTIME");
        while(cur.moveToNext()){
            dbstime = cur.getString(stime_column);
            wtime = cur.getInt(wtime_column);
        }
        cur.close();
        myMemberDB.close();

        TextView amiTvDate = (TextView)findViewById(R.id.amiTvDate);
        amiEvStartDate = (EditText)findViewById(R.id.amiEvStartDate);
        amiEvStartDate.setText(dbstime);
        long now = System.currentTimeMillis();
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡNOW값 " +now);
        /*Date date = new Date(now);
        SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");*/
        date = new Date(now);
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡDATE값 " +date);
        SimpleDateFormat endformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat startformet = new SimpleDateFormat("yyyy-MM-dd");
        eTime = endformat.format(date);
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡeTime값 " +eTime);
        sTime = startformet.format(date)+" "+amiEvStartDate.getText().toString();
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡsTime값 " +sTime);
        //출근시간
        String weekTime = startformet.format(date) + " "+ otstrr[wtime];
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡweekkTime값 " +weekTime);
        try{

            finaltime = cal(sTime,eTime,weekTime);
            System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡfinaltime값 " +finaltime);
            Date d = new Date(cal(sTime,eTime,weekTime));
            SimpleDateFormat hourstime = new SimpleDateFormat("HH");
            SimpleDateFormat mintime = new SimpleDateFormat("mm");
            System.out.println("값 계산 시작-------------------------------- ");
            System.out.println(Integer.parseInt(hourstime.format(finaltime))*60);
            System.out.println(Integer.parseInt(mintime.format(finaltime)));
            oTime = (Integer.parseInt(hourstime.format(finaltime))*60) + Integer.parseInt(mintime.format(finaltime))-1080;
            System.out.println("값 계산 끝-------------------------------- ");
            System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡoTime값 " +oTime);
            if(oTime<0){
                oTime=0;
            }
            System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡoTime값 " +oTime);
            /*System.out.println(oTime);*/
        }catch (Exception e){
            e.printStackTrace();
        }
        amiTvDate.setText(endformat.format(date));
    }



    public long cal(String sdate, String edate, String weekDate) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date sDate = format.parse(sdate);
        Date eDate = format.parse(edate);
        Date wDate = format.parse(weekDate);
        long calDate =  eDate.getTime() - sDate.getTime() - wDate.getTime();
/*        System.out.println(format.format(eDate));
        System.out.println(format.format(sDate));
        System.out.println(format.format(fcal));
        Log.d("칼",String.valueOf(fcal));*/
        return calDate;
    }

    public void onClick(View view){
        SQLiteDatabase db;
        ContentValues values;

        Intent intent = null;
        switch (view.getId()){
            case R.id.amiBtnWorkoff:
                try {
                    EditText amiEtContent = (EditText) findViewById(R.id.amiEtContent);


                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    amidate = dateformat.format(date);
                    stimedate = amiEvStartDate.getText().toString();
                    etimedate = timeformat.format(date);

                    db = myAmiDB.getWritableDatabase();
                    values = new ContentValues();
                    values.put("ID", queryId);
                    values.put("AMIDATE", amidate);
                    values.put("STIME", sTime);
                    values.put("ETIME", eTime);
                    values.put("CONTENT", amiEtContent.getText().toString());
                    values.put("OVERTIME", oTime);

                    if (db.insert("ami", null, values) == -1) {
                        Toast.makeText(getApplicationContext(), "중복된 업무일지입니다.", Toast.LENGTH_LONG).show();
                        db.close();
                        myAmiDB.close();
                        intent = new Intent(getApplicationContext(), PrevAmi.class);
                        intent.putExtra("loginId", queryId);
                    } else {
                       /* db.insert("ami", null, values);*/
                        Toast.makeText(getApplicationContext(), "업무일지 기록되었습니다.", Toast.LENGTH_SHORT).show();
                        db.close();
                        myAmiDB.close();
                    intent = new Intent(getApplicationContext(), Bus.class);
                    intent.putExtra("loginId", queryId);
                    intent.putExtra("busCheck", "Check");
                    finish();
                    }
                }
                    catch (SQLiteConstraintException e){
            e.getMessage();
        }

                break;
            case R.id.amiBtnCancel:

                intent = new Intent(getApplicationContext(), PrevAmi.class);
                intent.putExtra("loginId", queryId);
                break;
        }


        if(intent!=null){
            startActivity(intent);
        }
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };
    //키보드 포커스 아웃 시 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }
}