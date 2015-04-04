package com.youlongnet.lulu.ui.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.event.GiftPageEvent;
import com.youlongnet.lulu.ui.event.MessagePagerEvent;
import com.youlongnet.lulu.ui.event.MessageTabEvent;
import com.youlongnet.lulu.ui.event.SearchContentEvent;
import com.youlongnet.lulu.ui.frg.GiftFragment;
import com.youlongnet.lulu.ui.utils.BusProvider;
import com.youlongnet.lulu.ui.utils.JumpToActivity;
import com.youlongnet.lulu.ui.view.ToolbarWrapper;
import com.youlongnet.lulu.ui.widget.AndroidSegmentedControlView;
import com.youlongnet.lulu.ui.widget.slidebar.ContactActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import butterknife.ButterKnife;

public class TopManager implements Observer {
    public static final int UI_Default = -1;
    /*消息模块的ToolBar*/
    public static final int UI_MESSAGE = 0;
    /*礼包模块的ToolBar*/
    public static final int UI_GIFT = 1;
    /*公会模块的ToolBar*/
    public static final int UI_GUID = 2;
    /*我模块的ToolBar*/
    public static final int UI_MY = 3;
    /*搜索模块的Toolbar*/
    @Deprecated
    public static final int UI_SEARCH = 4;
    private static TopManager instance = new TopManager();
    private Context mContext;
    private ViewGroup mParent;
    private OnExcuteImmediate onExcuteImmediate;
    /*消息模块的Tab*/
    private AndroidSegmentedControlView mMessageTab;
    /*主布局的缓存ToolbarLayout*/
    private SparseArray<ToolbarWrapper> mCacheToolbars;

    private TopManager() {
    }

    public static TopManager getInstance() {
        return instance;
    }

    /**
     * 用于在changeUi之后立刻执行的操作
     *
     * @param onExcuteImmediate
     */
    public void setOnChangeUiAfterExcute(OnExcuteImmediate onExcuteImmediate) {
        this.onExcuteImmediate = onExcuteImmediate;
    }

    /**
     * 获取当前Activity内容区域的Toolbar：建议仅获取一次
     * 不能在changeUi()之后立刻使用，如若需可以使用OnExcuteImmediate
     *
     * @param ctx
     * @return
     */
    public Toolbar getCurrentToolBar(@NonNull Context ctx) {
        ViewGroup rootView = (ViewGroup) ((Activity) ctx).findViewById(android.R.id.content);
        ViewGroup contentGroupView = (ViewGroup) rootView.getChildAt(0);/*当前屏幕真正布局*/
        return (Toolbar) ButterKnife.findById(contentGroupView, R.id.toolbar);
    }

    public TopManager setLayout(@NonNull ViewGroup contentView) {
        this.mParent = contentView;
        this.mContext = mParent.getContext();
        this.mCacheToolbars = new SparseArray<>();
        return this;
    }

    /**
     * 适合主布局发起的修改
     *
     * @param type
     */
    public void changeUi(@UITypes int type) {
        if (mParent != null)
            changeUiOperate(mParent, type);
        else {
            System.err.println("主布局设置了？！");
        }
    }

    /**
     * 随意一个ViewGroup布局发起的修改
     *
     * @param contentView
     * @param type
     */
    public void changeUi(@NonNull ViewGroup contentView, @UITypes int type) {
        changeUiOperate(contentView, type);
    }

    /**
     * @param layout根布局
     * @param type
     */
    private void changeUiOperate(final ViewGroup contentView, int type) {
        ToolbarWrapper existToolbarLayout = null;
        int index = 0;
        int key = TopManager.UI_Default;
        for (int i = 0; i < contentView.getChildCount(); i++) {
            if (contentView.getChildAt(i) instanceof ToolbarWrapper) {
                existToolbarLayout = (ToolbarWrapper) contentView.getChildAt(i);
                break;
            }
        }
        if (existToolbarLayout != null) {
            index = contentView.indexOfChild(existToolbarLayout);
            contentView.removeView(existToolbarLayout);
        }
        /*具体添加哪种类型的toolBar*/
        switch (type) {
            case TopManager.UI_MESSAGE:
                key = TopManager.UI_MESSAGE;
                existToolbarLayout = mCacheToolbars.get(TopManager.UI_MESSAGE, changeToMessage());
                mMessageTab = ButterKnife.findById(existToolbarLayout, R.id.ascv);
                mMessageTab.setOnSelectionChangedListener(new AndroidSegmentedControlView.OnSelectionChangedListener() {
                    @Override
                    public void newSelection(String identifier, String value) {
                        BusProvider.getInstance().post(new MessagePagerEvent(Integer.valueOf(value)));
                    }
                });
                break;
            case TopManager.UI_GIFT:
                key = TopManager.UI_GIFT;
                existToolbarLayout = mCacheToolbars.get(TopManager.UI_GIFT, changeToGift());
                break;
            case TopManager.UI_GUID:
                key = TopManager.UI_GUID;
                existToolbarLayout = mCacheToolbars.get(TopManager.UI_GUID, changeToGuid());
                break;
            case TopManager.UI_MY:
                key = TopManager.UI_MY;
                existToolbarLayout = mCacheToolbars.get(TopManager.UI_MY, changeToMy());
                break;
            case TopManager.UI_Default:
                existToolbarLayout = changeToBack(contentView);
                break;
        }
        contentView.addView(existToolbarLayout, index);
        /*仅处理主布局的Fragment*/
        if (key != TopManager.UI_Default) mCacheToolbars.append(key, existToolbarLayout);
        if (onExcuteImmediate != null) {
            contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    onExcuteImmediate.afterChangeUi((Toolbar) ButterKnife.findById(contentView, R.id.toolbar));
                }
            });

        }
    }

    /*这个方法的变量每次切换之后都会被重置*/
    private ToolbarWrapper changeToMessage() {
        ToolbarWrapper item = (ToolbarWrapper) View.inflate(mContext, R.layout.item_message_top, null);
        Toolbar tb = ButterKnife.findById(item, R.id.toolbar);
        tb.setTag("messageToolbar");
        tb.hideOverflowMenu();
        tb.setTitle("消息");
        tb.setSubtitle("消息");
        tb.inflateMenu(R.menu.message);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                switch (arg0.getItemId()) {
                    case R.id.action_edit:
                    /*修改角标*/
                        int which = new Random().nextInt(4);
                        int num = new Random().nextInt(10);
                        boolean isShow = new Random().nextBoolean();
                        ContentManager.getInstance().changeBottomBadge(which, num + "", isShow);
                        break;
                    case R.id.actionShowContactList:
                        JumpToActivity.jumpTo(mContext, ContactActivity.class);
                }

                return false;
            }
        });
        return item;
    }

    /*此处需要contentView因为finish需要依赖发起者*/
    private ToolbarWrapper changeToBack(final ViewGroup contentView) {
        final ActionBarActivity aty = (ActionBarActivity) contentView.getContext();
        ToolbarWrapper contain = (ToolbarWrapper) View.inflate(mContext, R.layout.view_toolbar, null);
        Toolbar newTb = ButterKnife.findById(contain, R.id.toolbar);
        newTb.setTag("backToolbar");
        aty.setSupportActionBar(newTb);/*替换toolBar为actionBar*/
        final ActionBar supportActionBar = aty.getSupportActionBar();
        supportActionBar.setHomeButtonEnabled(true); // 设置返回键可用
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        /*supportActionBar.setHomeAsUpIndicator(R.drawable.ic_launcher);*//*设置返回键样式*/
        newTb.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                aty.finish();
            }
        });
        return contain;
    }

    private ToolbarWrapper changeToGuid() {
        ToolbarWrapper contain = (ToolbarWrapper) View.inflate(mContext, R.layout.view_toolbar, null);
        Toolbar newTb = ButterKnife.findById(contain, R.id.toolbar);
        newTb.setTag("guidToolbar");
        newTb.setTitle("公会");
        newTb.inflateMenu(R.menu.guid);
        newTb.hideOverflowMenu();
        newTb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                Toast.makeText(mContext, arg0.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return contain;
    }

    private ToolbarWrapper changeToGift() {
        ToolbarWrapper contain = (ToolbarWrapper) View.inflate(mContext, R.layout.view_toolbar, null);
        Toolbar newTb = ButterKnife.findById(contain, R.id.toolbar);
        newTb.setTag("giftToolbar");
        newTb.setTitle("礼包");
        newTb.inflateMenu(R.menu.gift);
        newTb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BusProvider.getInstance().post(new GiftPageEvent(GiftFragment.PAGE_SEARCH));
                return true;
            }
        });
        MenuItem searchItem = newTb.getMenu().findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        /*搜索框文本改变回调*/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                BusProvider.getInstance().post(new SearchContentEvent(true, query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                BusProvider.getInstance().post(new SearchContentEvent(false, newText));
                return true;
            }
        });
        /*searchView展开关闭回调*/
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                BusProvider.getInstance().post(new GiftPageEvent(GiftFragment.PAGE_GIFT));
                return true;
            }
        });
        return contain;
    }

    private ToolbarWrapper changeToMy() {
        ToolbarWrapper contain = (ToolbarWrapper) View.inflate(mContext, R.layout.view_toolbar, null);
        Toolbar newTb = ButterKnife.findById(contain, R.id.toolbar);
        newTb.setTag("myToolbar");
        newTb.setTitle("我");
        newTb.inflateMenu(R.menu.my);
        newTb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                Toast.makeText(mContext, arg0.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return contain;
    }

    /*接收修改UI通知*/
    @Override
    public void update(Observable observable, Object data) {
        Message msg = (Message) data;
        switch (msg.what) {
            case ContentManager.UI_ITEM:
                changeUi((Integer) msg.obj);
                break;
            case ContentManager.UI_ITEM_BADGE:

                break;
        }
    }

    /*接受修改Tab通知*/
    @Subscribe
    public void updateTab(MessageTabEvent event) {
        if (event.toolBarType == UI_MESSAGE && mMessageTab != null) {
            /*这里仅修改Tab选项，不触发事件*/
            mMessageTab.setByValue(false, "" + event.tabIndex);
        }
    }

    @IntDef(flag = true, value = {UI_Default, UI_MESSAGE, UI_GIFT, UI_GUID, UI_MY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UITypes {
    }

    public static interface OnExcuteImmediate {
        void afterChangeUi(Toolbar toolBar);
    }
}
