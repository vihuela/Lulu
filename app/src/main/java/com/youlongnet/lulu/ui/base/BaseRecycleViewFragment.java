package com.youlongnet.lulu.ui.base;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.malinskiy.superrecyclerview.swipe.SparseItemRemoveAnimator;
import com.malinskiy.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener;
import com.youlongnet.lulu.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.InjectView;
import butterknife.Optional;

/**
 * 通用的下拉刷新，自动加载的列表UI
 * 注意子类的xml文件必须使用SuperRecyclerView，且id必须为recycleView
 *
 * @author lyao
 */
public abstract class BaseRecycleViewFragment<ADAPTER extends RecyclerView.Adapter<ViewHolder>> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        SwipeDismissRecyclerViewTouchListener.DismissCallbacks, OnMoreListener {
    /*仅下拉刷新*/
    public static final int REFRESH = 0;
    /*仅加载更多*/
    public static final int LOADMORE = 1;
    /*两者都有*/
    public static final int REFRESH_AND_LOADMORE = 2;
    @Optional
    @InjectView(R.id.recycleView)
    protected SuperRecyclerView mRecycler;
    protected ADAPTER mAdapter;
    protected boolean isNeedRefresh;
    protected boolean isNeedLoadMore;

    /*滑动删除暂未对子类开启*/
    final protected boolean isSwipeToDismissEnabled() {
        return false;
    }

    @Nullable
    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract ADAPTER getAdapter();

    @RefreshConfig
    protected abstract int getRefreshConfig();

    protected abstract void onRefreshing();

    protected abstract void onLoadMore(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition);

    @Override
    protected void initWidget() {
        if (mRecycler == null)
            throw new RuntimeException("please ensure " + this.getClass().getSimpleName() + " 's xml layout has R.id.recycleView");
        switch (getRefreshConfig()) {
            case BaseRecycleViewFragment.REFRESH:
                isNeedRefresh = !(isNeedLoadMore = false);
                break;
            case BaseRecycleViewFragment.LOADMORE:
                isNeedRefresh = !(isNeedLoadMore = true);
                break;
            case BaseRecycleViewFragment.REFRESH_AND_LOADMORE:
                isNeedRefresh = isNeedLoadMore = true;
                break;
        }
        if (isNeedRefresh) mRecycler.setRefreshListener(this);
        if (isNeedLoadMore) mRecycler.setupMoreListener(this, 1);/*剩下一条时加载更多*/
        /*下拉刷新样式*/
        mRecycler.setRefreshingColorResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2, R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        /*启用滑动删除*/
        if (isSwipeToDismissEnabled()) {
            mRecycler.setupSwipeToDismiss(this);
            SparseItemRemoveAnimator mSparseAnimator = new SparseItemRemoveAnimator();
            mRecycler.getRecyclerView().setItemAnimator(mSparseAnimator);
        }
        /*配置manager于adapter*/
        LayoutManager mManager = getLayoutManager() == null ? new LinearLayoutManager(mContext) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        } : getLayoutManager();
        mAdapter = getAdapter();

        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(mManager);
    }

    @Override
    final public void onRefresh() {
        onRefreshing();
    }

    @Override
    final public boolean canDismiss(int position) {
        return true;
    }

    @Override
    final public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
        Toast.makeText(mContext, "item滑动了", Toast.LENGTH_SHORT).show();
        /* for (int position : reverseSortedPositions) {
                mSparseAnimator.setSkipNext(true);
		        mAdapter.remove(position);
		    }*/
    }

    @Override
    final public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        /*不让默认的加载更多进度条显示*/
        mRecycler.hideMoreProgress();
        setSwipeRefreshLoadingState(mRecycler.getSwipeToRefresh());
        onLoadMore(overallItemsCount, itemsBeforeMore, maxLastVisiblePosition);
    }

    /*让小圈圈消失*/
    protected void loadCircleFinish() {
        // mRecycler.hideMoreProgress();
        setSwipeRefreshLoadedState(mRecycler.getSwipeToRefresh());
    }

    private void setSwipeRefreshLoadingState(@NonNull SwipeRefreshLayout mSwipeRefreshLayout) {
        mSwipeRefreshLayout.setRefreshing(true);
        /*仅用于单独的swipeLayout防止多次刷新时使用*/
        /*mSwipeRefreshLayout.setEnabled(false);*/
    }

    private void setSwipeRefreshLoadedState(@NonNull SwipeRefreshLayout mSwipeRefreshLayout) {
        mSwipeRefreshLayout.setRefreshing(false);
        /*仅用于单独的swipeLayout防止多次刷新时使用*/
        /* mSwipeRefreshLayout.setEnabled(true);*/
    }

    @IntDef(flag = false, value = {REFRESH, LOADMORE, REFRESH_AND_LOADMORE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RefreshConfig {
    }
}
