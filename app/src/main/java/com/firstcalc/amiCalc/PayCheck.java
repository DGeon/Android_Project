package com.firstcalc.amiCalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PayCheck extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    DecimalFormat decimalFormat;
    EditText payEtId, payEtBasic, payEtMeal, payEtFamily, payEt20th, payEtNp, payEtHi, payEtNi, payEtEi, payEtEit, payEtLit, payEtOt, payEtYpay, payEtpay;
    String queryId, id, pw, basic, meal;
    MyAmiDB myAmiDB;
    MyMemberDB myMemberDB = new MyMemberDB(this);
    int payTemp, familycnt;
    double np, hi, ni, ei, lit, ot, ypay, tpay, overtime;
    int eit;
    int i;
    int overpay;
    ArrayList<Integer> countC = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paycheck);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);

        //slider메뉴
        //slider버튼
        Intent intentget = getIntent();
        intentget.getExtras();

        TextView menubarTvId = (TextView) findViewById(R.id.menubarTvId);
        queryId = intentget.getExtras().getString("loginId");
        countC = intentget.getIntegerArrayListExtra("arr");
        int arrcheck = intentget.getExtras().getInt("pre");
        System.out.println(countC.size());
        if(arrcheck==0){
            i=2;
        }else{
            i=2;
        }
        for(i=0; i< countC.size(); i++){
           overtime+=countC.get(i);
            System.out.println(countC.get(i) + " : " + i +"번째");
        }
        System.out.println(overtime);
        menubarTvId.setText(queryId+"님");

        SQLiteDatabase db;
        String[] memberarr= {"ID","PW","BASIC","MEAL"};
        Cursor cur;

        db = myMemberDB.getReadableDatabase(); //검색할때

        cur = db.query("member",memberarr,"ID=?",new String[]{queryId},null,null,null);

        int id_column = cur.getColumnIndex("ID");
        int basic_column = cur.getColumnIndex("BASIC");
        int meal_column = cur.getColumnIndex("MEAL");

        while(cur.moveToNext()){
            id = cur.getString(id_column);
            basic = cur.getString(basic_column);
            meal = cur.getString(meal_column);
        }

        payEtId = (EditText)findViewById(R.id.payEtId);
        payEtBasic = (EditText)findViewById(R.id.payEtBasic);
        payEtMeal = (EditText)findViewById(R.id.payEtMeal);
        payEtFamily = (EditText)findViewById(R.id.payEtFamily);
        payEt20th = (EditText)findViewById(R.id.payEt20th);
        payEtNp = (EditText)findViewById(R.id.payEtNp);
        payEtHi = (EditText)findViewById(R.id.payEtHi);
        payEtNi = (EditText)findViewById(R.id.payEtNi);
        payEtEi = (EditText)findViewById(R.id.payEtEi);
        payEtEit = (EditText)findViewById(R.id.payEtEit);
        payEtLit = (EditText)findViewById(R.id.payEtLit);
        payEtOt = (EditText)findViewById(R.id.payEtOt);
        payEtYpay = (EditText)findViewById(R.id.payEtYpay);
        payEtpay = (EditText)findViewById(R.id.payEtpay);
        //포멧
        decimalFormat = new DecimalFormat("###,###");
        payEtId.setText(queryId);
        payEtBasic.setText(decimalFormat.format(Integer.parseInt(basic)));
        payEtMeal.setText(decimalFormat.format(Integer.parseInt(meal)));
        

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
                SharedPreferences prefs = getSharedPreferences("AutoLogin",0);
                SharedPreferences.Editor edit = prefs.edit();

                //삭제
                edit.remove("id");
                edit.remove("pw");
                edit.commit();
                drawerLayout.closeDrawer(drawerView);
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

    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.payBtnPrev:
                Intent intent =  new Intent(getApplicationContext(), PrevAmi.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                break;
            case  R.id.payBtnSerch:
                //비과세액 제외 연봉
                Double basicpay = Double.parseDouble(basic);
                BigDecimal bigDecimal1 = new BigDecimal(((((basicpay/12)/209)/60)*1.5)*overtime);
                bigDecimal1 = bigDecimal1.setScale(-1,BigDecimal.ROUND_DOWN);
                overpay = bigDecimal1.intValue();
                int total = (Integer.parseInt(basic)/12) +overpay;
                payTemp = total - (Integer.parseInt(meal)/12);
                System.out.println(total+"토탈");
                System.out.println(payTemp+"페이템");

                //국민연금
                np = (payTemp*0.045);
                payEtNp.setText(decimalFormat.format(np));
                //건강보험(3.43%)
                hi = (payTemp*0.0343);
                payEtHi.setText(decimalFormat.format(hi));
                //요양보험(11.52%)
                ni = hi*0.1152;
                payEtNi.setText(decimalFormat.format(ni));
                //고용보험(0.8%)
                ei = (payTemp*0.008);
                payEtEi.setText(decimalFormat.format(ei));
                //근로소득세(간이세액-지방소득세10%)
                int tex = Math.abs(payTemp/1000);

                BigDecimal bigDecimal = new BigDecimal(tex);
                bigDecimal = bigDecimal.setScale(-1,BigDecimal.ROUND_DOWN);


                String fc = Integer.toString(bigDecimal.intValue());
                System.out.println(fc + "FC");
                if(fc.indexOf("1",2)!=-1 || fc.indexOf("3",2)!=-1 || fc.indexOf("5",2)!=-1 || fc.indexOf("7",2)!=-1 || fc.indexOf("9",2)!=-1){
                    familycnt = bigDecimal.intValue()-10;
                    System.out.println("찾음" + fc.indexOf("1",3));

                }else
                {
                    familycnt = bigDecimal.intValue();
                    System.out.println("못찾음" + fc.indexOf("1",3));
                }

                System.out.println(familycnt+"텍스");
                /*payEtFamily.addTextChangedListener(textWatcher);*/
                getVal(familycnt,Integer.parseInt(payEtFamily.getText().toString()),Integer.parseInt(payEt20th.getText().toString()));
                //연장근로수당
                payEtOt.setText(decimalFormat.format(overpay));

                payEtEit.setText(decimalFormat.format(eit));
                //지방소득세
                lit = eit*0.1;
                payEtLit.setText(decimalFormat.format(lit));
                //년 예상 실수령
                tpay = total-np-hi-ni-ei-eit-lit;
                ypay = tpay*12;
                payEtYpay.setText(decimalFormat.format(ypay));
                //월 실수령
                payEtpay.setText(decimalFormat.format(tpay));
                break;

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

    public void getVal(int price, int people, int descendent) {
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM data1 WHERE A = '"+price+"'; ",null,null);


        while (cursor.moveToNext())
        {
            eit = cursor.getInt(1+people+descendent);
            System.out.println(eit);
        }
        cursor.close();
        dbHelper.close();

    }

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
