package com.youlongnet.lulu.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.youlongnet.lulu.R;
import com.youlongnet.lulu.bean.Message;
import com.youlongnet.lulu.ui.holder.MessageHolder;
import com.youlongnet.lulu.ui.utils.CircleTransformation;
import com.youlongnet.lulu.utils.DensityUtils;

import java.util.ArrayList;

public class MessageAdapter extends Adapter<ViewHolder> implements View.OnClickListener {

    private ArrayList<Message> mMessages = new ArrayList<>();
    private Context mContext;
    private int mAvatorSize;
    private OnMessageItemClickListener itemClickListener;

    public MessageAdapter(Context ctx) {
        this.mContext = ctx;
        this.mAvatorSize = DensityUtils.dip2px(mContext, 56);
    }

    public void setItemClickListener(@NonNull OnMessageItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public ArrayList<Message> getDataSource() {
        return this.mMessages;
    }

    public void setDataSource(ArrayList<Message> messages) {
        this.mMessages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int itemType) {
        View item = View.inflate(mContext, R.layout.item_message, null);
        MessageHolder holder = new MessageHolder(item);
        item.setOnClickListener(this);
        holder.mImgHeadPhoto.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        Message item = mMessages.get(pos);
        MessageHolder messageHolder = (MessageHolder) holder;
        /*填充数据*/
        messageHolder.mTitle.setText(item.title + ":" + pos);
        messageHolder.mTime.setText(item.time);
        messageHolder.mContent.setText(item.content);
        /*头像加载*/
        Picasso.with(mContext)
                .load(item.headUrl)
                .centerCrop()
                .resize(mAvatorSize, mAvatorSize)
                .placeholder(R.drawable.img_circle_placeholder)
                .error(R.drawable.img_circle_placeholder)
                .transform(new CircleTransformation())
                .into(messageHolder.mImgHeadPhoto);
        /*设置Tag,方便获取数据*/
        holder.itemView.setTag(item);
        messageHolder.mImgHeadPhoto.setTag(item.headUrl);
    }


    public void reset(ArrayList<Message> newMessages) {
        mMessages.clear();
        mMessages.addAll(newMessages);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Message> newMessages) {
        int startIndex = mMessages.size();
        mMessages.addAll(newMessages);
        notifyItemRangeInserted(startIndex, newMessages.size());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_message:
                itemClickListener.onItemClick((Message) v.getTag());
                break;
            case R.id.tv_head:
                itemClickListener.onAvatorClick(v,(String) v.getTag());
                break;
        }
    }

    public static interface OnMessageItemClickListener {
        void onItemClick(Message item);

        void onAvatorClick(View v,String url);
    }
}
