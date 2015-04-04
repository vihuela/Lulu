package com.youlongnet.lulu.ui.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.youlongnet.lulu.R;

public class KJSwipeRefreshLayout extends SwipeRefreshLayout {

    public KJSwipeRefreshLayout(Context context) {
        super(context);
    }

    public KJSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 设置刷新时颜色
        this.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled()) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!isEnabled()) {
            return false;
        }
        return super.onTouchEvent(arg0);
    }
}