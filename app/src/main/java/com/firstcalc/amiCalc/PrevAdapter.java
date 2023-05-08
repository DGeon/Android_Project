package com.firstcalc.amiCalc;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PrevAdapter extends RecyclerView.Adapter<PrevAdapter.ViewHolder> implements OnPrevItemClickListener {

    ArrayList<PrevData> items = new ArrayList<PrevData>();
    OnPrevItemClickListener listener;
    String queryId, queryDate, queryStime, queryEtime, queryContent;
    int queryOverTime;
    String sdate, edate;
    MyAmiDB myAmiDB;
    Context context;

    ArrayList<Integer> count = new ArrayList<>();

    public PrevAdapter(Context context, String queryId){
        this.context = context;
        this.queryId = queryId;
    }
    @NonNull
    @Override
    public PrevAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.prevtable, viewGroup, false);
        return new PrevAdapter.ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(PrevAdapter.ViewHolder viewHolder, int position) {
        PrevData item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(PrevData item){
        items.add(item);
    }

    public void setItems(ArrayList<PrevData> items){
        this.items=items;
    }

    public PrevData getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, PrevData item){
        items.set(position,item);
    }

    public void setOnItemClickListener(OnPrevItemClickListener listener){
        this.listener = listener;
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void updateData(ArrayList<PrevData> viewModels) {
        items.clear();
        items.addAll(viewModels);
        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView prevTvDate, prevTvAttendance, prevTvleavework;

        public ViewHolder (View itemView, final OnPrevItemClickListener listener){
            super(itemView);

               Button prevamibtnUpdate = itemView.findViewById(R.id.prevBtnUpdate);
                prevamibtnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position =getAdapterPosition();
                        queryDate = items.get(position).getDate();
                        queryStime = items.get(position).getAttendance();
                        queryEtime = items.get(position).getLeavework();
                        queryContent = items.get(position).getContent();
                        queryOverTime = items.get(position).getOvertime();

                        Intent intent =  new Intent(itemView.getContext(), AmiUpdate.class);
                        intent.putExtra("loginId", queryId);
                        intent.putExtra("prevDate", queryDate);
                        intent.putExtra("prevStime", queryStime);
                        intent.putExtra("prevEtime", queryEtime);
                        intent.putExtra("prevContent", queryContent);
                        intent.putExtra("prevOvertime", queryOverTime);
                        context.startActivity(intent);
                    }
                });

               Button pamibtnDelete = itemView.findViewById(R.id.prevBtnDelete);
                pamibtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position =getAdapterPosition();
                        myAmiDB = new MyAmiDB(context);
                        SQLiteDatabase db;
                        db = myAmiDB.getWritableDatabase();
                        queryDate = items.get(position).getDate();
                        queryStime = items.get(position).getAttendance();
                        queryEtime = items.get(position).getLeavework();
                        queryContent = items.get(position).getContent();

                        db.delete("ami","AMIDATE=? and ETIME=?",new String[]{queryDate, queryEtime});
                        db.close();
                        removeItem(position);
                    }
                });

            prevTvDate = itemView.findViewById(R.id.prevTvDate);
            prevTvAttendance = itemView.findViewById(R.id.prevTvAttendance);
            prevTvleavework = itemView.findViewById(R.id.prevTvleavework);

        }
        public void setItem(PrevData item){
            prevTvDate.setText(item.getDate());
            String slength = item.getAttendance();
            prevTvAttendance.setText(slength.substring(slength.length()-5,slength.length()));
            String elength = item.getLeavework();
            prevTvleavework.setText(elength.substring(elength.length()-5,elength.length()));
        }
    }
}