package com.firstcalc.amiCalc;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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

import java.text.DecimalFormat;
import java.util.Calendar;

public class Join extends AppCompatActivity {

    MyMemberDB myMemberDB = new MyMemberDB(Join.this);
    EditText joinEtID, joinEtPw, joinEtBasic, joinEtMeal, joinEtSTime;
    int joinbasic, joinmeal;
    String[] items = {"7시간", "8시간", "9시간", "10시간","11시간","12시간"};
    int wtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        joinEtBasic = (EditText) findViewById(R.id.joinEtBasic);
        joinEtMeal = (EditText) findViewById(R.id.joinEtMeal);
        joinEtBasic.addTextChangedListener(new CustomTextWatcher(joinEtBasic, "Basic"));
        joinEtMeal.addTextChangedListener(new CustomTextWatcher(joinEtMeal, "Meal"));
        
        //업무시간
        Spinner spinner = (Spinner) findViewById(R.id.joinSpWTime);

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
        spinner.setSelection(2);
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
        joinEtSTime  = (EditText) findViewById(R.id.joinEtSTime);
        joinEtSTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Join.this, new TimePickerDialog.OnTimeSetListener() {
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
                        joinEtSTime.setText(time+selectedHour + ":" + min+selectedMinute);
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    public void onClick(View view) {
        SQLiteDatabase db;
        ContentValues values;

        joinEtID = (EditText) findViewById(R.id.joinEtID);
        joinEtPw = (EditText) findViewById(R.id.joinEtPw);


        EditText joinEtPwC = (EditText) findViewById(R.id.joinEtPwCheck);

    /*    String joinId = joinEtID.getText().toString();
        String joinPw = joinEtPw.getText().toString();
        String joinBasic = joinEtBasic.getText().toString();
        String joinMeal = joinEtMeal.getText().toString();*/


        /*long time = Long.parseLong("09:00");
        Time t = new Time(time);
        SimpleDateFormat Format = new SimpleDateFormat("HH:MM");
        System.out.println(Format.format(t));*/

        Intent intent = null;
        switch (view.getId()) {
            case R.id.joinBtnJoin:
                if (!joinEtPw.getText().toString().equals(joinEtPwC.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    joinEtPw.setFocusable(true);
                    joinEtPw.setText("");
                    joinEtPwC.setText("");
                } else if (joinEtPw.getText().toString().equals("") || joinEtPwC.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "공백으로는 불가 합니다.", Toast.LENGTH_SHORT).show();
                    joinEtPw.setFocusable(true);
                    joinEtPw.setText("");
                    joinEtPwC.setText("");
                }else if (joinbasic>100000000) {
                    Toast.makeText(getApplicationContext(), "연봉 1억 초과는 불가합니다.", Toast.LENGTH_SHORT).show();
                    joinEtBasic.setFocusable(true);
                    joinEtBasic.setText("");
                }else if (joinmeal>1200000){
                    Toast.makeText(getApplicationContext(), "비과세액 1,200,000원 초과는 불가합니다.", Toast.LENGTH_SHORT).show();
                    joinEtMeal.setFocusable(true);
                    joinEtMeal.setText("");
                } else if (joinEtPw.getText().toString().equals(joinEtPwC.getText().toString())) {
                    db = myMemberDB.getWritableDatabase();

                    values = new ContentValues();
                    values.put("ID", joinEtID.getText().toString());
                    values.put("PW", joinEtPw.getText().toString());
                    values.put("BASIC", joinbasic);
                    values.put("MEAL", joinmeal);
                    values.put("STIME", joinEtSTime.getText().toString());
                    values.put("WTIME", wtime);

                    if (db.insert("member", null, values) != -1) {
                        db.close();
                        myMemberDB.close();

                        Toast.makeText(getApplicationContext(), "회원가입에 성공하셨습니다.", Toast.LENGTH_LONG).show();
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "중복된 회원입니다 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.joinBtnPrev:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
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

            protected String makeStringComma (String str, String name){    // 천단위 콤마설정.
                if (str.length() == 0) {
                    return "";
                }
                if (name.equals("Basic")) {
                    joinbasic = Integer.parseInt(str);
                }else if (name.equals("Meal")){
                    joinmeal = Integer.parseInt(str);
                }

                long value = Long.parseLong(str);
                DecimalFormat format = new DecimalFormat("###,###");
                return format.format(value);
            }
        }
    }
