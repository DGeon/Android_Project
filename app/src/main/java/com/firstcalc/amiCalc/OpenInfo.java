package com.firstcalc.amiCalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class OpenInfo extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openinfo);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);

        //slider메뉴

        //slider버튼
        Intent intentget = getIntent();
        intentget.getExtras();

        TextView menubarTvId = (TextView) findViewById(R.id.menubarTvId);
        String queryId = intentget.getExtras().getString("loginId");
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
                SharedPreferences prefs = getSharedPreferences("AutoLogin",0);
                SharedPreferences.Editor edit = prefs.edit();

                //삭제
                edit.remove("id");
                edit.remove("pw");
                edit.commit();
                Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);
            }
        });
        //오픈소스
        TextView menubarTvOpen = (TextView)findViewById(R.id.menubarTvOpen);
        menubarTvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getApplicationContext(), OpenInfo.class);
                intent.putExtra("loginId", queryId);
                startActivity(intent);
                drawerLayout.closeDrawer(drawerView);

            }
        });
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
