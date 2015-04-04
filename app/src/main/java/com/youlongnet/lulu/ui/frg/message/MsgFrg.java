package com.youlongnet.lulu.ui.frg.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

import com.youlongnet.lulu.R;
import com.youlongnet.lulu.bean.Message;
import com.youlongnet.lulu.ui.adapters.MessageAdapter;
import com.youlongnet.lulu.ui.aty.ChatActivity;
import com.youlongnet.lulu.ui.aty.UserInfoActivity;
import com.youlongnet.lulu.ui.base.BaseRecycleViewFragment;
import com.youlongnet.lulu.ui.utils.JumpToActivity;

public class MsgFrg extends BaseRecycleViewFragment<MessageAdapter> {

    private Handler mHandler;

    @Nullable
    @Override
    protected LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    protected int getRefreshConfig() {
        return BaseRecycleViewFragment.LOADMORE;
    }

    @Override
    public int setLayout() {
        return R.layout.frg_message_submsg;
    }

    /*适配器需为RecyclerView.Adapter的子类*/
    @Override
    protected MessageAdapter getAdapter() {
        return new MessageAdapter(mContext);
    }

    /*列表已初始化完毕等待初始化数据*/
    @Override
    protected void initData(View view) {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.setItemClickListener(new MessageAdapter.OnMessageItemClickListener() {
                    @Override
                    public void onItemClick(Message item) {
                        Intent intent = new Intent(mContext, ChatActivity.class);
                        intent.putExtra("title", item.title);
                        JumpToActivity.jumpTo(mContext, intent);
                    }

                    @Override
                    public void onAvatorClick(View v, String url) {
                        final int[] startLocs = new int[2];
                        v.getLocationOnScreen(startLocs);
                        startLocs[0] += v.getWidth() / 2;
                        UserInfoActivity.startUserInfoActivity(mContext, startLocs, url);
                        ((Activity) mContext).overridePendingTransition(0, 0);
                    }
                });
                mAdapter.addAll(Message.getVirtulData());
            }
        }, 2000);
    }

    /*下拉刷新时回调（如果有设置监听），只要刷新数据源之后刷新圈圈才会消失 */
    @Override
    protected void onRefreshing() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.reset(Message.getVirtulData());
            }
        }, 1500);

    }

    /*加载更多时时回调（如果有设置监听）*/
    @Override
    protected void onLoadMore(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addAll(Message.getVirtulData());
                loadCircleFinish();
            }
        }, 1500);
    }
}
