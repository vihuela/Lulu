package com.youlongnet.lulu.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.holder.GiftHolder;
import com.youlongnet.lulu.ui.holder.LineHolder;

import java.util.ArrayList;

public class GiftAdapter extends Adapter<RecyclerView.ViewHolder> {

    private final static int ITEM_LINE = 0;
    private final static int ITEM_CONTENT = 10;
    private ArrayList<String> mGifts = new ArrayList<>();
    private Context mContext;

    public GiftAdapter(Context ctx) {
        this.mContext = ctx;
    }

    @Override
    public int getItemCount() {
        return mGifts.size();
    }

    public ArrayList<String> getDataSource() {
        return this.mGifts;
    }

    public void setDataSource(ArrayList<String> messages) {
        this.mGifts = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int itemType) {
        if (itemType == ITEM_LINE) {
            TextView item = new TextView(mContext);
            item.setText("条目类型二：");
            item.setTextColor(Color.parseColor("#50ff0000"));
            return new LineHolder(item);
        } else {
            View item = View.inflate(mContext, R.layout.item_gift, null);
            return new GiftHolder(item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? ITEM_CONTENT : ITEM_LINE;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        if (holder instanceof GiftHolder) {
            String item = mGifts.get(pos);
            GiftHolder messageHolder = (GiftHolder) holder;
            /*填充数据*/
            messageHolder.mTitle.setText(item + ":" + pos);
            /*设置Tag,方便获取数据*/
            holder.itemView.setTag(item);
        }
    }
}
