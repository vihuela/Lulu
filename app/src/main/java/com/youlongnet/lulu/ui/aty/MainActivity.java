package com.youlongnet.lulu.ui.aty;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.base.BaseActivity;
import com.youlongnet.lulu.ui.manager.BottomManager;
import com.youlongnet.lulu.ui.manager.ContentManager;
import com.youlongnet.lulu.ui.manager.TopManager;
import com.youlongnet.lulu.ui.utils.BusProvider;
import com.youlongnet.lulu.ui.utils.NotifyProvider;
import com.youlongnet.lulu.ui.widget.TabLayout;

import butterknife.InjectView;

public class MainActivity extends BaseActivity {
    @InjectView(R.id.main_middle)
    protected FrameLayout mMiddleContain;
    @InjectView(R.id.main_contain)
    protected LinearLayout mTotalContain;
    @InjectView(R.id.main_bottom)
    protected TabLayout mBottom;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_main);
        initLayout();
        initData();
    }

    private void initLayout() {
        /*观察者方式*/
        ContentManager.getInstance().addObservers(TopManager.getInstance(), BottomManager.getInstance());
        /*初始化布局*/
        TopManager.getInstance().setLayout(mTotalContain);
        BottomManager.getInstance().setLayout(mBottom);
        ContentManager.getInstance().setLayout(getSupportFragmentManager(), mMiddleContain);
		/*初始化相关工具*/
        NotifyProvider.getInstance().init(this);
		/*事件总线方式*/
        BusProvider.getInstance().registers(ContentManager.getInstance(), TopManager.getInstance(), BottomManager.getInstance(), NotifyProvider.getInstance());
    }

    private void initData() {
        TopManager.getInstance().changeUi(TopManager.UI_MESSAGE);
        // ContentManager.getInstance().setCurrentItem(3);/*切换至"我"的模块*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContentManager.getInstance().deleteObservers();
        BusProvider.getInstance().unregisters(ContentManager.getInstance(), TopManager.getInstance(), BottomManager.getInstance(), NotifyProvider.getInstance());
        Runtime.getRuntime().exit(0);
    }
}
