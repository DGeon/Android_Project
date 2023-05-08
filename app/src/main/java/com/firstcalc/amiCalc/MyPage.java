package com.firstcalc.amiCalc;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MyPage extends AppCompatActivity {

    MyMemberDB myMemberDB = new MyMemberDB(this);
    private DrawerLayout drawerLayout;
    private View drawerView;
    String queryId;
    String id, pw, basic, meal, stime;
    int mypagebasic, mypagemeal, wtime;
    EditText mypageEtId, mypageEtBasic, mypageEtMeal, mypageEtSTime;
    String[] items = {"7시간", "8시간", "9시간", "10시간","11시간","12시간"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);

        //업무시간
        Spinner spinner = (Spinner) findViewById(R.id.mypageSpWTime);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items
        ){
            //평시 텍스트
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "basicfont.ttf");
                ((TextView) v).setTextSize(12);
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setGravity(Gravity.RIGHT);
                ((TextView) v).setTextColor(Color.rgb(118,118,118));
                return v;
            }
            //드롭다운 텍스트
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), "basicfont.ttf");
                ((TextView) v).setTextSize(12);
                ((TextView) v).setTypeface(externalFont);
                v.setBackgroundColor(Color.WHITE);
                ((TextView) v).setTextColor(Color.rgb(118,118,118));
                return v;
            }
        };

        // 드롭다운 클릭 시 선택 창
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 스피너에 어댑터 설정
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wtime = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //출근시간
        mypageEtSTime = (EditText) findViewById(R.id.mypageEtSTime);
        mypageEtSTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MyPage.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time, min;
                        if(selectedHour<=9){
                            time = "0";
                        }else
                        {
                            time = "";
                        }
                        if(selectedMinute<=9){
                            min="0";
                        }else
                        {
                            min="";
                        }
                        mypageEtSTime.setText(time+selectedHour + ":" + min+selectedMinute);
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        //slider메뉴
        Intent intentget = getIntent();
        intentget.getExtras();

        TextView menubarTvId = (TextView) findViewById(R.id.menubarTvId);
        queryId = intentget.getExtras().getString("loginId");
        menubarTvId.setText(queryId+"님");

        SQLiteDatabase db;
        ContentValues values;
        String[] memberarr= {"ID","PW","BASIC","MEAL","STIME","WTIME"};
        Cursor cur;

        db = myMemberDB.getReadableDatabase(); //검색할때

        cur = db.query("member",memberarr,"ID=?",new String[]{queryId},null,null,null);

        int id_column = cur.getColumnIndex("ID");
        int pw_column = cur.getColumnIndex("PW");
        int basic_column = cur.getColumnIndex("BASIC");
        int meal_column = cur.getColumnIndex("MEAL");
        int stime_column = cur.getColumnIndex("STIME");
        int wtime_column = cur.getColumnIndex("WTIME");

        while(cur.moveToNext()){
            id = cur.getString(id_column);
            pw = cur.getString(pw_column);
            basic = cur.getString(basic_column);
            meal = cur.getString(meal_column);
            stime = cur.getString(stime_column);
            wtime = cur.getInt(wtime_column);
        }

        mypageEtId = (EditText)findViewById(R.id.mypageEtId);
        mypageEtBasic = (EditText)findViewById(R.id.mypageEtBasic);
        mypageEtMeal = (EditText)findViewById(R.id.mypageEtMeal);

        DecimalFormat decimalFormat = new DecimalFormat("###,###");

        mypageEtId.setText(id);
        mypageEtBasic.setText(decimalFormat.format(Integer.parseInt(basic)));
        mypageEtMeal.setText(decimalFormat.format(Integer.parseInt(meal)));

        mypageEtBasic.addTextChangedListener(new CustomTextWatcher(mypageEtBasic, "Basic"));
        mypageEtMeal.addTextChangedListener(new CustomTextWatcher(mypageEtMeal, "Meal"));
        mypageEtSTime.setText(stime);
        spinner.setSelection(wtime);

        cur.close();
        myMemberDB.close();



        //slider버튼
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
                Intent intent =  new Intent(getApplicationContext(), PrevAmi.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);
            }
        });
        //퇴근
        TextView menubarTvAmi = (TextView)findViewById(R.id.menubarTvAmi);
        menubarTvAmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getApplicationContext(), Ami.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);
            }
        });

        //버스
        TextView menubarTvBus = (TextView)findViewById(R.id.menubarTvBus);
        menubarTvBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getApplicationContext(), Bus.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);
            }
        });
        //노동부
        TextView menubarTvWork = (TextView)findViewById(R.id.menubarTvWork);
        menubarTvWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getApplicationContext(), Work.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);
            }
        });

        //마이페이지
        TextView menubarTvMypage = (TextView)findViewById(R.id.menubarTvMypage);
        menubarTvMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getApplicationContext(), MyPage.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);
            }
        });

        //로그아웃
        TextView menubarTvLogout = (TextView)findViewById(R.id.menubarTvLogout);
        menubarTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

    public void onClick(View view){
        SQLiteDatabase db;
        ContentValues values;
        String[] memberarr= {"ID","PW","BASIC","MEAL","STIME","WTIME"};
        Cursor cur;

        Intent intent = null;
        switch (view.getId()){
            case R.id.mypageBtnUpdate:
                db=myMemberDB.getWritableDatabase();
                values = new ContentValues();
                EditText mypageEtPw =(EditText)findViewById(R.id.mypageEtPw);
                EditText mypageEtPwC =(EditText)findViewById(R.id.mypageEtPwCheck);

                if (!mypageEtPw.getText().toString().equals(mypageEtPwC.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    mypageEtPw.setFocusable(true);
                    mypageEtPw.setText("");
                    mypageEtPwC.setText("");
                } else if (mypageEtPw.getText().toString().equals("") || mypageEtPwC.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "공백으로는 불가 합니다.", Toast.LENGTH_SHORT).show();
                    mypageEtPw.setFocusable(true);
                    mypageEtPw.setText("");
                    mypageEtPwC.setText("");
                } else if (mypageEtPw.getText().toString().equals(mypageEtPwC.getText().toString())) {
                    values.put("PW", mypageEtPw.getText().toString());
                    values.put("BASIC", Integer.parseInt(mypageEtBasic.getText().toString().replace(",","")));
                    values.put("MEAL", Integer.parseInt(mypageEtMeal.getText().toString().replace(",","")));
                    values.put("STIME", mypageEtSTime.getText().toString());
                    values.put("WTIME", wtime);

                    db.update("member", values, "ID=?", new String[]{queryId});
                    db.close();
                    myMemberDB.close();
                    Toast.makeText(getApplicationContext(), "회원정보 수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = getSharedPreferences("AutoLogin",0);
                    SharedPreferences.Editor edit = prefs.edit();

                    //삭제
                    edit.remove("id");
                    edit.remove("pw");
                    edit.commit();
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mypageBtnPrev:
                onBackPressed();
                break;
            case R.id.mypageBtnDelete:
                SharedPreferences prefs = getSharedPreferences("AutoLogin",0);
                SharedPreferences.Editor edit = prefs.edit();

                //삭제
                edit.remove("id");
                edit.remove("pw");
                edit.commit();

                db = myMemberDB.getWritableDatabase();
                db.delete("member","ID=?",new String[]{queryId});
                db.close();
                myMemberDB.close();

                MyAmiDB myAmiDB = new MyAmiDB(this);
                db = myAmiDB.getWritableDatabase();
                db.delete("ami","ID=?",new String[]{queryId});
                db.close();
                myAmiDB.close();
                Toast.makeText(getApplicationContext(),"회원 탈퇴가 완료 되었습니다.",Toast.LENGTH_SHORT).show();
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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
    public class CustomTextWatcher implements TextWatcher {

        private EditText editText;
        String strAmount = "";
        String name;

        CustomTextWatcher(EditText et, String name) {
            editText = et;
            this.name = name;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(strAmount)) {
                strAmount = makeStringComma(s.toString().replace(",", ""),name);
                editText.setText(strAmount);
                Editable editable = editText.getText();
                Selection.setSelection(editable, strAmount.length());
            }
        }
        @Override
        public void afterTextChanged (Editable s){

        }

        protected String makeStringComma (String str, String name){
            if (str.length() == 0) {
                return "";
            }
            if (name.equals("Basic")) {
                mypagebasic = Integer.parseInt(str);
            }else if (name.equals("Meal")){
                mypagemeal = Integer.parseInt(str);
            }

            long value = Long.parseLong(str);
            DecimalFormat format = new DecimalFormat("###,###");
            return format.format(value);
        }
    }

}