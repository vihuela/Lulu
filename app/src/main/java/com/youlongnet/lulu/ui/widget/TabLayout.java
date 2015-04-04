package com.youlongnet.lulu.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 简单的Tab指示器，配合shape
 *
 * @author lyao
 */
public class TabLayout extends LinearLayout implements OnClickListener {
    protected Handler handler = new Handler();
    protected OnSelectedIndexChangedListener mOnSelectedIndexChangedListener;
    protected int mSelectedIndex = -1;

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnSelectedIndexChangedListener(OnSelectedIndexChangedListener mOnSelectedIndexChangedListener) {
        this.mOnSelectedIndexChangedListener = mOnSelectedIndexChangedListener;
    }

    @Override
    protected void onFinishInflate() {
        for (int i = 0, size = this.getChildCount(); i < size; i++) {
            View item = this.getChildAt(i);
            if (item instanceof RelativeLayout) {
                ((RelativeLayout) item).setOnClickListener(this);
                if (i == 0) {
                    item.setSelected(true);
                    mSelectedIndex = 0;
                }
            }
        }
    }

    /**
     * 每个子View点击时回调
     */
    @Override
    public void onClick(final View v) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (mSelectedIndex == indexOfChild(v))
                    return;
                ((RelativeLayout) getChildAt(mSelectedIndex)).setSelected(false);// 复原当前item
                ((RelativeLayout) getChildAt(indexOfChild(v))).setSelected(true);// 修改选中item
                mSelectedIndex = indexOfChild(v);// 获取子View的索引
                if (mOnSelectedIndexChangedListener != null)
                    mOnSelectedIndexChangedListener.onSelectedIndexChanged(v, mSelectedIndex);
            }
        });
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        if (selectedIndex < 0 || selectedIndex >= this.getChildCount()) {
            return;
        }
        if (mSelectedIndex == selectedIndex) {
            return;
        }
        if (mSelectedIndex >= 0) {
            this.getChildAt(mSelectedIndex).setSelected(false);
        }
        mSelectedIndex = selectedIndex;
        this.getChildAt(mSelectedIndex).setSelected(true);
        if (mOnSelectedIndexChangedListener != null) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    if (mOnSelectedIndexChangedListener != null) {
                        mOnSelectedIndexChangedListener.onSelectedIndexChanged(null, mSelectedIndex);
                    }
                }
            });
        }
    }

    /**
     * 开放获取选中的子ViewIndex
     */
    public static interface OnSelectedIndexChangedListener {
        public void onSelectedIndexChanged(View v, int selectedIndex);
    }
}
