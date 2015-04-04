package com.youlongnet.lulu.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

public class NoInterceptSuperRecyclerView extends SuperRecyclerView {

    public NoInterceptSuperRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

/*	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
		LinearLayout parent = (LinearLayout) this.getParent();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			parent.requestDisallowInterceptTouchEvent(false);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			parent.requestDisallowInterceptTouchEvent(false);
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}*/

}
