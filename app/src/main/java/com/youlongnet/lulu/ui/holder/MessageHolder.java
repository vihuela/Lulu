package com.youlongnet.lulu.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.adapters.interfaces.ViewItemClickListener;
import com.youlongnet.lulu.ui.adapters.interfaces.ViewItemLongClickListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MessageHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.tv_head)
    public ImageView mImgHeadPhoto;
    @InjectView(R.id.tv_Title)
    public TextView mTitle;
    @InjectView(R.id.tv_content)
    public TextView mContent;
    @InjectView(R.id.tv_time)
    public TextView mTime;

    public MessageHolder(View item) {
        super(item);
        ButterKnife.inject(this, item);
    }

}
