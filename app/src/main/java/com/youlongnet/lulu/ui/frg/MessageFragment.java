package com.youlongnet.lulu.ui.frg;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.squareup.otto.Subscribe;
import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.base.BaseFragment;
import com.youlongnet.lulu.ui.event.MessagePagerEvent;
import com.youlongnet.lulu.ui.event.MessageTabEvent;
import com.youlongnet.lulu.ui.frg.message.CircleFrg;
import com.youlongnet.lulu.ui.frg.message.MsgFrg;
import com.youlongnet.lulu.ui.manager.TopManager;
import com.youlongnet.lulu.ui.utils.BusProvider;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.InjectView;
import butterknife.OnPageChange;

public class MessageFragment extends BaseFragment {

    @InjectView(R.id.viewpager)
    ViewPager mViewpager;
    private ArrayList<BaseFragment> mPagers = new ArrayList<BaseFragment>();
    private Toolbar currentToolbar;

    @Override
    public int setLayout() {
        return R.layout.frg_message;
    }

    @Override
    protected void initWidget() {
        BusProvider.getInstance().register(this);
        currentToolbar = TopManager.getInstance().getCurrentToolBar(mContext);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void initData(View view) {
        mPagers.clear();
        Collections.addAll(mPagers, new MsgFrg(), new CircleFrg());
        /*sub frg use getChildFragmentManager()*/
        mViewpager.setAdapter(new MessageAdapter(getChildFragmentManager(), mPagers));
    }

    @Subscribe
    public void onCurrentItemChange(MessagePagerEvent event) {
        mViewpager.setCurrentItem(event.pagerIndex);
    }

    @OnPageChange(value = R.id.viewpager, callback = OnPageChange.Callback.PAGE_SELECTED)
    public void pageChangeListener(int index) {
        BusProvider.getInstance().post(new MessageTabEvent(TopManager.UI_MESSAGE, "" + index));
        currentToolbar.setSubtitle(index == 0 ? "消息" : "圈子");
    }

    private class MessageAdapter extends FragmentPagerAdapter {
        private ArrayList<BaseFragment> mPagers;

        public MessageAdapter(FragmentManager fm, ArrayList<BaseFragment> pagers) {
            super(fm);
            this.mPagers = pagers;
        }

        @Override
        public Fragment getItem(int position) {
            return mPagers.get(position);
        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

    }
}
