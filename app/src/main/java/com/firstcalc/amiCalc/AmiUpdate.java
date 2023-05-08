package com.firstcalc.amiCalc;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AmiUpdate extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    String queryId, date;
    EditText amiupEtSTime, amiupEtETime, amiupEtContent;
    MyAmiDB myAmiDB = new MyAmiDB(this);
    int overTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amiupdate);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);

        amiupEtSTime = (EditText) findViewById(R.id.amiupEtSTime);
        amiupEtETime = (EditText) findViewById(R.id.amiupEtETime);
        amiupEtContent = (EditText) findViewById(R.id.amiupEtContent);

        Intent intentget = getIntent();
        intentget.getExtras();

        queryId = intentget.getExtras().getString("loginId");
        date = intentget.getExtras().getString("prevDate");
        amiupEtSTime.setText(intentget.getExtras().getString("prevStime"));
        amiupEtETime.setText(intentget.getExtras().getString("prevEtime"));
        amiupEtContent.setText(intentget.getExtras().getString("prevContent"));
        /*int overTime = intentget.getExtras().getInt("prevOvertime");*/

        //slider메뉴

        //slider버튼

        TextView menubarTvId = (TextView) findViewById(R.id.menubarTvId);
        menubarTvId.setText(queryId + "님");

        TextView menuTvBtn = (TextView) findViewById(R.id.menuTvBtn);
        menuTvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });
        //근태기록
        TextView menubarTvPami = (TextView) findViewById(R.id.menubarTvPami);
        menubarTvPami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrevAmi.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);
            }
        });
        //퇴근
        TextView menubarTvAmi = (TextView) findViewById(R.id.menubarTvAmi);
        menubarTvAmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Ami.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);
            }
        });

        //버스
        TextView menubarTvBus = (TextView) findViewById(R.id.menubarTvBus);
        menubarTvBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Bus.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);
            }
        });
        //노동부
        TextView menubarTvWork = (TextView) findViewById(R.id.menubarTvWork);
        menubarTvWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Work.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);
            }
        });

        //마이페이지
        TextView menubarTvMypage = (TextView) findViewById(R.id.menubarTvMypage);
        menubarTvMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPage.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);
            }
        });

        //로그아웃
        TextView menubarTvLogout = (TextView) findViewById(R.id.menubarTvLogout);
        menubarTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = getSharedPreferences("AutoLogin",0);
                SharedPreferences.Editor edit = prefs.edit();

                //삭제
                edit.remove("id");
                edit.remove("pw");
                edit.commit();
                drawerLayout.closeDrawer(drawerView);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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



    }

    public long cal(String sdate, String edate, String weekDate) throws ParseException {
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

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.updateBtnWorkoff:
                SQLiteDatabase db;
                ContentValues values;

                try {
                    //출근시간 불러와야됨
                    String weekTime = date + " 09:00";
                    long over = cal(amiupEtSTime.getText().toString(), amiupEtETime.getText().toString(),weekTime);
                    SimpleDateFormat hourstime = new SimpleDateFormat("HH");
                    SimpleDateFormat mintime = new SimpleDateFormat("mm");
                    overTime = (Integer.parseInt(hourstime.format(over))*60) + Integer.parseInt(mintime.format(over))-1080;

                }catch (Exception e){

                }


                db = myAmiDB.getWritableDatabase();

                values = new ContentValues();
                values.put("STIME", amiupEtSTime.getText().toString());
                values.put("ETIME", amiupEtETime.getText().toString());
                values.put("CONTENT", amiupEtContent.getText().toString());
                values.put("OVERTIME", overTime);

                db.update("ami", values, "ID=? and AMIDATE=?", new String[]{queryId, date});
                db.close();
                myAmiDB.close();
                intent = new Intent(getApplicationContext(), PrevAmi.class);
                intent.putExtra("loginId", queryId);
                break;
            case R.id.updateBtnCancel:
                intent = new Intent(getApplicationContext(), PrevAmi.class);
                intent.putExtra("loginId", queryId);
                break;
        }

        if (intent != null) {
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


}

