package com.youlongnet.lulu.ui.aty;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.base.BaseActivity;
import com.youlongnet.lulu.ui.event.NotifyEvent;
import com.youlongnet.lulu.ui.manager.TopManager;
import com.youlongnet.lulu.ui.utils.BusProvider;
import com.youlongnet.lulu.ui.utils.NotifyProvider;

import butterknife.InjectView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity {
    @InjectView(R.id.main_contain)
    LinearLayout mContain;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_chat);
        TopManager.getInstance().changeUi(mContain, TopManager.UI_Default);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar currentToolBar = (Toolbar) TopManager.getInstance().getCurrentToolBar(mContext);
        try {
            currentToolBar.setTitle(getIntent().getStringExtra("title"));

        } catch (Exception e) {
        }
    }

    @OnClick(R.id.tv)
    public void onClick(View v) {
        BusProvider.getInstance().post(new NotifyEvent("fuck", this, R.id.main));
    }
}
