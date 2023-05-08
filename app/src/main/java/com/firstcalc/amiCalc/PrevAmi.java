package com.firstcalc.amiCalc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PrevAmi extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    GridView prevGridView;
    String queryId, queryDate, queryStime, queryEtime, queryContent;
    int queryOverTime;
    EditText prevEt,nextEt;
    String sdate, edate;
    MyAmiDB myAmiDB;
    TextView prevTvDate, prevTvAttendance, prevTvleavework;
    RecyclerView recyclerView;
    PrevAdapter adapter;
    ArrayList<Integer> count = new ArrayList<>();
    int arrcheck;
    int pre2cnt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prevami);

        prevEt = (EditText)findViewById(R.id.prevEtPrev);
        nextEt = (EditText)findViewById(R.id.prevEtNext);

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

        Calendar c = Calendar.getInstance();
        int iYear = c.get(Calendar.YEAR);
        int iMonth = c.get(Calendar.MONTH)+1;
        int iDay = c.get(Calendar.DAY_OF_MONTH);
        String sYear, sMonth, sDay;
        sYear = Integer.toString(iYear);
        if(iMonth<=9){
            sMonth = "0"+iMonth;
        }else{
            sMonth= Integer.toString(iMonth);
        }
        if(iDay <=9) {
            sDay = "0"+iDay;
        }else{
            sDay = Integer.toString(iDay);
        }

        prevEt.setText(sYear+"-"+sMonth+"-01");
        nextEt.setText(sYear+"-"+sMonth+"-"+sDay);


        sdate = prevEt.getText().toString();
        edate = nextEt.getText().toString();

        myAmiDB = new MyAmiDB(PrevAmi.this, queryId);


        myAmiDB.getSelectAll(sdate,edate);//고민1*/



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


    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);//옵션
        recyclerView.setLayoutManager(layoutManager);
        count.clear();
/*        System.out.println(myAmiDB.cnt);*/
            adapter = new PrevAdapter(this, queryId);
            for (int i = 0; i < myAmiDB.getSelectAll(sdate, edate).size(); i++) {
                adapter.addItem(new PrevData(
                        myAmiDB.getSelectAll(sdate, edate).get(i).getDate(),
                        myAmiDB.getSelectAll(sdate, edate).get(i).getAttendance(),
                        myAmiDB.getSelectAll(sdate, edate).get(i).getLeavework(),
                        myAmiDB.getSelectAll(sdate, edate).get(i).getContent()

                ));
                count.add(myAmiDB.getSelectAll(sdate, edate).get(i).getOvertime());
            }
        recyclerView.setAdapter(adapter);
    }

    TextWatcher textWatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            sdate = prevEt.getText().toString();
            edate = nextEt.getText().toString();
            myAmiDB.getSelectAll(sdate,edate);
            onResume();
        }
    };
    public void onClick(View view) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat dateformet = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat yearformet = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthformet = new SimpleDateFormat("MM");
        SimpleDateFormat dayformet = new SimpleDateFormat("dd");

        EditText prevEtPrev = (EditText)findViewById(R.id.prevEtPrev);
        EditText nextEtPrev = (EditText)findViewById(R.id.prevEtNext);

        int dyear, dmonth, dday;
        switch (view.getId()) {
            case R.id.prevEtPrev:

                try {
                    Date pDate = dateformet.parse(prevEtPrev.getText().toString());
                    dyear = Integer.parseInt(yearformet.format(pDate));
                    dmonth = Integer.parseInt(monthformet.format(pDate));
                    dday = Integer.parseInt(dayformet.format(pDate));

                DatePickerDialog prevDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                       monthOfYear = monthOfYear+1;
                        String month, day;
                        if(monthOfYear<=9){
                            month = "0"+monthOfYear;
                        }else {
                            month = Integer.toString(monthOfYear);
                        }
                        if(dayOfMonth<=9){
                            day = "0"+dayOfMonth;
                        }else{
                            day = Integer.toString(dayOfMonth);
                        }
                        prevEt.setText( year+ "-" + month + "-" + day);
                    }
                }, dyear, dmonth-1, dday);
                prevDate.show();
                }catch (Exception e){
                    e.printStackTrace();
                }


                break;
            case R.id.prevEtNext:
                try {
                    Date pDate = dateformet.parse(nextEtPrev.getText().toString());
                    dyear = Integer.parseInt(yearformet.format(pDate));
                    dmonth = Integer.parseInt(monthformet.format(pDate));
                    dday = Integer.parseInt(dayformet.format(pDate));
                DatePickerDialog nextDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        String month, day;
                        if(monthOfYear<=9){
                            month = "0"+monthOfYear;
                        }else {
                            month = Integer.toString(monthOfYear);
                        }
                        if(dayOfMonth<=9){
                            day = "0"+dayOfMonth;
                        }else{
                            day = Integer.toString(dayOfMonth);
                        }
                        nextEt.setText( year+ "-" + month + "-" + day);
                    }
                }, dyear, dmonth-1, dday);
                nextDate.show();
                nextEt.addTextChangedListener(textWatcher);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.prevTvPay:
                onResume();
                Intent intent = new Intent(getApplicationContext(),PayCheck.class);
                intent.putExtra("loginId", queryId);
                intent.putIntegerArrayListExtra("arr", count);
                intent.putExtra("pre",arrcheck);
                startActivity(intent);
                count.clear();

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
}