package com.youlongnet.lulu.ui.manager;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.adapters.ContentManagerAdapter;
import com.youlongnet.lulu.ui.base.BaseFragment;
import com.youlongnet.lulu.ui.frg.GiftFragment;
import com.youlongnet.lulu.ui.frg.GuidFragment;
import com.youlongnet.lulu.ui.frg.MessageFragment;
import com.youlongnet.lulu.ui.frg.MyFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnPageChange;

/*中间容器的管理类*/
public class ContentManager extends Observable {
    public static final int UI_ITEM = 0;/*item选择切换*/
    public static final int UI_ITEM_BADGE = 1;/*某个item的角标*/
    private static ContentManager instance = new ContentManager();
    @InjectView(R.id.viewpager)
    ViewPager mViewpager;
    private ArrayList<BaseFragment> mPagers = new ArrayList<BaseFragment>();
    private Context mContext;

    private ContentManager() {
        mPagers.clear();
        Collections.addAll(mPagers, new MessageFragment(), new GiftFragment(), new GuidFragment(), new MyFragment());
    }

    public static ContentManager getInstance() {
        return instance;
    }

    public void addObservers(Observer... observer) {
        for (int i = 0; i < observer.length; i++)
            super.addObserver(observer[i]);
    }

    public ViewPager getViewPager() {
        return this.mViewpager;
    }

    public void setLayout(FragmentManager fm, View middleContain) {
        ButterKnife.inject(this, middleContain);
        mContext = middleContain.getContext();
        mViewpager.setAdapter(new ContentManagerAdapter(fm, mPagers));
        mViewpager.setOffscreenPageLimit(3);/*修改默认缓存策略*/
    }

    public void setCurrentItem(int index) {
        mViewpager.setCurrentItem(index);
    }

    @OnPageChange(value = R.id.viewpager, callback = OnPageChange.Callback.PAGE_SELECTED)
    void onPageStateChanged(int state) {
        changeTopBottomUi(mViewpager.getCurrentItem());
    }

    @Override
    public void notifyObservers(Object data) {
        super.setChanged();
        super.notifyObservers(data);
    }

    /**
     * 通知顶部、底部修改UI
     *
     * @param 中间容器显示的item索引（已设置上中下联动）
     */
    public void changeTopBottomUi(int currentItem) {
        Message msg = new Message();
        msg.what = ContentManager.UI_ITEM;
        msg.obj = currentItem;
        notifyObservers(msg);
    }

    /**
     * 通知底部修改角标
     *
     * @param 底部item索引
     * @param 文本
     * @param 是否显示
     */
    public void changeBottomBadge(int which, String text, boolean isShow) {
        Message msg = new Message();
        msg.what = ContentManager.UI_ITEM_BADGE;
        msg.arg1 = which;
        msg.arg2 = isShow ? 1 : 0;
        msg.obj = text;
        notifyObservers(msg);
    }

    public void changeBottomBadge(int which, boolean isShow) {
        changeBottomBadge(which, 0 + "", isShow);
    }
}
