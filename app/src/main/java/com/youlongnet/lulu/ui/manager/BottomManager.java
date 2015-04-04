package com.youlongnet.lulu.ui.manager;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.utils.BadgeView;
import com.youlongnet.lulu.ui.widget.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;
import butterknife.ButterKnife.Action;
import butterknife.InjectView;

public class BottomManager implements Observer {

    private static BottomManager instance = new BottomManager();
    TabLayout mTabLayout;
    @InjectView(R.id.gb_message)
    TextView mTvMessage;
    @InjectView(R.id.gb_gift)
    TextView mTvGift;
    @InjectView(R.id.gb_guid)
    TextView mTvGuid;
    @InjectView(R.id.gb_my)
    TextView mTvMy;
    BadgeView mBadgeMessage, mBadgeGift, mBadgeGuid, mBadgeMy;
    List<BadgeView> mBadges = new ArrayList<BadgeView>();
    private Context mContext;
    private BottomManager() {
    }

    public static BottomManager getInstance() {
        return instance;
    }

    public void setLayout(View view) {
        mTabLayout = (TabLayout) view;
        mContext = mTabLayout.getContext();
        ButterKnife.inject(this, mTabLayout);
        mTabLayout.setOnSelectedIndexChangedListener(new TabLayout.OnSelectedIndexChangedListener() {
            @Override
            public void onSelectedIndexChanged(View v, int selectedIndex) {
                ContentManager.getInstance().setCurrentItem(selectedIndex);
            }
        });
        /*初始化角标*/
        initBadge();
    }

    private void initBadge() {
        mBadgeMessage = new BadgeView(mContext, mTvMessage);
        mBadgeGift = new BadgeView(mContext, mTvGift);
        mBadgeGuid = new BadgeView(mContext, mTvGuid);
        mBadgeMy = new BadgeView(mContext, mTvMy);
        mBadges.clear();
        Collections.addAll(mBadges, mBadgeMessage, mBadgeGift, mBadgeGuid, mBadgeMy);
        ButterKnife.apply(mBadges, new Action<BadgeView>() {
            @Override
            public void apply(BadgeView badge, int pos) {
            }
        });
    }

    /*接收修改UI通知*/
    @Override
    public void update(Observable observable, Object data) {
        Message msg = (Message) data;
        switch (msg.what) {
            case ContentManager.UI_ITEM:
                mTabLayout.setSelectedIndex((Integer) msg.obj);
                break;
            case ContentManager.UI_ITEM_BADGE:
                updateBadge(msg.arg1, (String) msg.obj, msg.arg2 == 1 ? true : false);
                break;
        }
    }

    private void updateBadge(int which, String num, boolean isShow) {
        String numStr = num + "";
        switch (which) {
            case TopManager.UI_MESSAGE:
                setBadge(mBadgeMessage, numStr, isShow);
                break;
            case TopManager.UI_GIFT:
                setBadge(mBadgeGift, numStr, isShow);
                break;
            case TopManager.UI_GUID:
                setBadge(mBadgeGuid, numStr, isShow);
                break;
            case TopManager.UI_MY:
                setBadge(mBadgeMy, numStr, isShow);
                break;
        }

    }

    private void setBadge(BadgeView badge, String numStr, boolean isShow) {
        badge.setText(numStr);
        if (isShow)
            badge.show();
        else
            badge.hide();
    }
}
