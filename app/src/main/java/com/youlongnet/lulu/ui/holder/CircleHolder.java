package com.youlongnet.lulu.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.youlongnet.lulu.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CircleHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.tv_Title)
    public TextView mTitle;

    public CircleHolder(View item) {
        super(item);
        ButterKnife.inject(this, item);
    }


}
