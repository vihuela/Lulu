package com.youlongnet.lulu.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.holder.CircleHolder;

import java.util.ArrayList;

public class CircleAdapter extends Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> mCircle = new ArrayList<>();
    private Context mContext;

    public CircleAdapter(Context ctx) {
        this.mContext = ctx;
    }

    @Override
    public int getItemCount() {
        return mCircle.size();
    }

    public ArrayList<String> getDataSource() {
        return this.mCircle;
    }

    public void setDataSource(ArrayList<String> messages) {
        this.mCircle = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int itemType) {
        View item = View.inflate(mContext, R.layout.item_circle, null);
        CircleHolder holder = new CircleHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        CircleHolder ch = (CircleHolder) holder;
        ch.mTitle.setText(mCircle.get(pos));
    }
}
