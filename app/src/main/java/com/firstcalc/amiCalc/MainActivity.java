package com.firstcalc.amiCalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private long backKeyPressedTime = 0;
    private Toast toast;
    MyMemberDB myMemberDB = new MyMemberDB(MainActivity.this);

    String queryId, queryPw;
    EditText loginEtPhone, loginEtPW;
    CheckBox loginCbAutologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginEtPhone = (EditText) findViewById(R.id.loginEtPhone);
        loginEtPW = (EditText) findViewById(R.id.loginEtPW);
        loginCbAutologin = (CheckBox)findViewById(R.id.loginCbAutologin);
        Button loginBtnLogin = (Button)findViewById(R.id.loginBtnLogin);
        if(savedInstanceState == null){
            SharedPreferences prefs = getSharedPreferences("AutoLogin",0);
            String id = prefs.getString("id", "");
            String pw = prefs.getString("pw", "");
            loginEtPhone.setText(id);
            loginEtPW.setText(pw);
            if(loginEtPhone.getText().toString().equals("")){
                loginCbAutologin.setChecked(false);
            }else{
                    loginCbAutologin.setChecked(true);
                    loginBtnLogin.callOnClick();
                    finish();
            }

        }


        loginCbAutologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String autoid = loginEtPhone.getText().toString();
                String autopw = loginEtPW.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("AutoLogin",0);
                SharedPreferences.Editor edit =sharedPreferences.edit();

                edit.putString("id", autoid);
                edit.putString("pw", autopw);

                edit.apply();
            }
        });
    }

    public void onClick(View view) {

        SQLiteDatabase db;
        ContentValues values;
        String[] memberstr = {"ID", "PW"};
        Cursor cur;

        loginEtPhone = (EditText) findViewById(R.id.loginEtPhone);
        loginEtPW = (EditText) findViewById(R.id.loginEtPW);

        Intent intent = null;
        switch (view.getId()) {
            case R.id.loginBtnJoin:
                intent = new Intent(getApplicationContext(), Join.class);
                startActivity(intent);
                break;
            case R.id.loginBtnLogin:

                db = myMemberDB.getReadableDatabase();
                String loginId = loginEtPhone.getText().toString();
                String loginPw = loginEtPW.getText().toString();

                cur = db.query("member", memberstr, "id=?", new String[]{loginId}, null, null, null);
                int id_colum = cur.getColumnIndex("ID");
                int pw_colum = cur.getColumnIndex("PW");

                while (cur.moveToNext()) {
                    queryId = cur.getString(id_colum);
                    queryPw = cur.getString(pw_colum);
                }
                int logincnt=1;
                if (queryId == null) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show();
                    logincnt++;
                    if(logincnt>=5){
                        loginCbAutologin.setChecked(false);
                    }

                    cur.close();
                    myMemberDB.close();
                } else {
                    if (queryId.equals(loginId) && queryPw.equals(loginPw)) {
                        Toast.makeText(getApplicationContext(), queryId + "님 환영 합니다.", Toast.LENGTH_LONG).show();
                        intent = new Intent(getApplicationContext(), Ami.class);
                        intent.putExtra("loginId", queryId);
                        startActivity(intent);
                        cur.close();
                        myMemberDB.close();
                        finish();//로그인시 로그인 페이지 종료
                    } else {
                        Toast.makeText(getApplicationContext(), "아이디와 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                }
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
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }
}