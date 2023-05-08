package com.firstcalc.amiCalc;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface OnPrevItemClickListener {
    public void onItemClick(RecyclerView.ViewHolder holder, View view, int position);
}
