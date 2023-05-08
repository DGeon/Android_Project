package com.firstcalc.amiCalc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Bus extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;

    //버스API부분
    EditText edit;
    XmlPullParser xpp;
    ListView list;
    String data, queryId, bus;
    ListAdapter adapter;
    ArrayList<String> firstbusarr;
    ArrayList<String> secondbusarr;
    ArrayList<String> busnumarr;
    ImageButton busIbtnSerch;
    Button busBtnNumFa, busBtnNumFa1;
    //버스API부분

    MyMemberDB myMemberDB = new MyMemberDB(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus);

        firstbusarr = new ArrayList<>();
        secondbusarr = new ArrayList<>();
        busnumarr = new ArrayList<>();

        busBtnNumFa = (Button)findViewById(R.id.busBtnNumFa);
        busBtnNumFa1 = (Button)findViewById(R.id.busBtnNumFa1);
        busIbtnSerch = (ImageButton)findViewById(R.id.busIbtnSerch);




        //버스API
        edit= (EditText)findViewById(R.id.busEtNum);
        list = (ListView)findViewById(R.id.listView);
        adapter = new ListAdapter(Bus.this);
        list.setAdapter(adapter);



        //버스API

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        //slider메뉴

        //slider버튼
        Intent intentget = getIntent();
        intentget.getExtras();

        TextView menubarTvId = (TextView) findViewById(R.id.menubarTvId);
        queryId = intentget.getExtras().getString("loginId");
        String amicheck = intentget.getExtras().getString("busCheck");
        System.out.println(amicheck+"000000000000000000000000000000000000000000000");
        SQLiteDatabase db;

        String[] memberarr= {"BUS"};
        Cursor cur;
        db = myMemberDB.getReadableDatabase();

        cur = db.query("member",memberarr,"ID=?",new String[]{queryId},null,null,null);
        int bus_column = cur.getColumnIndex("BUS");

        while(cur.moveToNext()){
            bus = cur.getString(bus_column);
        }
        if(bus==null) {
            busBtnNumFa1.setText("즐겨찾기");
        }else{
            busBtnNumFa1.setText(bus);
        }
        if(amicheck!=null && !busBtnNumFa1.getText().toString().equals("즐겨찾기")){

            // 배포 시 살릴 것 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            /*busBtnNumFa1.callOnClick();*/
        }

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

    }
    //버스API
    public void onClick(View v){

        switch (v.getId()){
            case R.id.busBtnNumFa:
                if(!edit.getText().toString().equals("")){
                    SQLiteDatabase db;
                    ContentValues values;

                    db = myMemberDB.getWritableDatabase();

                    values = new ContentValues();
                    values.put("BUS", edit.getText().toString());
                    db.update("member", values, "ID=?", new String[]{queryId});
                    db.close();
                    myMemberDB.close();
                    busBtnNumFa1.setText(edit.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(),"정류장을 검색해주세요.",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.busBtnNumFa1:
                SQLiteDatabase db;

                String[] memberarr= {"BUS"};
                Cursor cur;
                db = myMemberDB.getReadableDatabase();

                cur = db.query("member",memberarr,"ID=?",new String[]{queryId},null,null,null);
                int bus_column = cur.getColumnIndex("BUS");

                while(cur.moveToNext()){
                    bus = cur.getString(bus_column);
                }

                edit.setText(bus);
                busIbtnSerch.callOnClick();

                break;
            case R.id.busIbtnSerch:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data=getXmlData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                /*text.setText(data);*/
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    String getXmlData(){
        StringBuffer buffer=new StringBuffer();
        String str = edit.getText().toString();
        String queryUrl="";
       if(firstbusarr.size()>0){
            firstbusarr.clear();
            secondbusarr.clear();
            busnumarr.clear();
        }
           queryUrl="http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?serviceKey=4chyTuzqZyZy3L0j6TzepfD0SEeHJM5J7yGtEX%2F8HQeD4fZofd%2B%2FseXdgoveey8ibGuH9KHKmqTkZc3ztsbvVQ%3D%3D&arsId="+str;
        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
            String tag;
            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기
                        if (tag.equals("arrmsg1")) {
                            xpp.next();
                            firstbusarr.add(xpp.getText());
                        } else if (tag.equals("arrmsg2")) {
                            xpp.next();
                            secondbusarr.add(xpp.getText());
                        } else if (tag.equals("rtNm")) {
                            xpp.next();
                            busnumarr.add(xpp.getText());
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();
                        if(tag.equals("item")) buffer.append("\n");
                        break;
                }
                eventType= xpp.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public class ListAdapter extends ArrayAdapter<String> {

        private final Activity context;

        ListAdapter(Activity context) {
            super(context,R.layout.listitem,busnumarr);
            this.context = context;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listitem,null,true);

            TextView fristbus = (TextView)rowView.findViewById(R.id.fristbus);
            TextView secondbus = (TextView)rowView.findViewById(R.id.secondbus);
            TextView busnum = (TextView)rowView.findViewById(R.id.busnum);

            fristbus.setText(firstbusarr.get(position));
            secondbus.setText(secondbusarr.get(position));
            busnum.setText(busnumarr.get(position));

            return rowView;
        }
    }

    //버스API


    public static boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
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

    /*public boolean tableCheck() {
        boolean result = false;
        SQLiteDatabase db;
        db = myMemberDB.getReadableDatabase();

        String[] memberarr= {"BUS"};
        Cursor cur = db.query("member",memberarr,"ID=?",new String[]{queryId},null,null,null);
        if (cur.getCount() > 0) {
            result = true;
        }
        cur.close();
        return result;
    }*/
}
