package com.youlongnet.lulu.ui.base;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;

import butterknife.ButterKnife;

public class BaseActivity extends ActionBarActivity {

    protected Context mContext;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
        mContext = this;
    }
}
