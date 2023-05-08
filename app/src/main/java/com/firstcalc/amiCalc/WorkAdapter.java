package com.firstcalc.amiCalc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkAdapter extends BaseAdapter {

    Context context;

    WorkData[] workData={
            new WorkData(R.drawable.p1,"고용노동부","1350","http://www.moel.go.kr/"),
            new WorkData(R.drawable.p2,"국민건강보험","1577-1000","https://www.nhis.or.kr/"),
            new WorkData(R.drawable.p3,"근로복지공단","1588-0075","https://www.kcomwel.or.kr/"),
            new WorkData(R.drawable.p4,"잡플래닛","02-1644-5641","https://www.jobplanet.co.kr"),
            new WorkData(R.drawable.p5,"잡코리아","1588-9350","https://www.jobkorea.co.kr"),
            new WorkData(R.drawable.p6,"사람인","02-2025-4733","https://www.saramin.co.kr"),
            new WorkData(R.drawable.p7,"대법원","1544-0770","https://www.scourt.go.kr/"),
            new WorkData(R.drawable.p8,"직업훈련","1350","https://www.hrd.go.kr")
    };

    WorkAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return workData.length;
    }

    @Override
    public Object getItem(int i) {//인덱스에 해당하는 아이템
        return null;
    }

    @Override
    public long getItemId(int i) {  //항목의 행 ID를 반환
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        /*imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,250));*/
        imageView.setLayoutParams(new GridView.LayoutParams(600,250));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        /*imageView.setImageResource(centerInfo[i]);*/
        imageView.setImageResource(workData[i].icon);

        int pos = i;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = (View)View.inflate(context, R.layout.workdialog,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(context);

                TextView workTvTitle = (TextView)dialogView.findViewById(R.id.workTvTitle);
                ImageView workIvDialog = (ImageView)dialogView.findViewById(R.id.workIvDialog);
                TextView workTvHome = (TextView)dialogView.findViewById(R.id.workTvHome);
                TextView workTvNumber = (TextView)dialogView.findViewById(R.id.workTvNumber);

                workTvTitle.setText(workData[pos].name);
                workIvDialog.setImageResource(workData[pos].icon);
                workTvHome.setText("홈페이지 연결");
                workTvNumber.setText("전화 연결");

                dlg.setTitle("센터 안내");
                dlg.setView(dialogView);
                dlg.setNegativeButton("닫기",null);
                dlg.show();

                workTvHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(workData[pos].address));
                        context.startActivity(intent);
                    }
                });

                workTvNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+workData[pos].number));
                        context.startActivity(intent);
                    }
                });
            }
        });

        return imageView;
    }
}