package com.youlongnet.lulu.ui.frg.gift;

import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.adapters.GiftAdapter;
import com.youlongnet.lulu.ui.base.BaseRecycleViewFragment;

import java.util.ArrayList;

public class GiftFrg extends BaseRecycleViewFragment<GiftAdapter> {

    @Override
    public int setLayout() {
        return R.layout.frg_gift;
    }

    @Override
    protected LayoutManager getLayoutManager() {
        return null;

    }

    @Override
    protected GiftAdapter getAdapter() {
        return new GiftAdapter(mContext);
    }

    @Override
    protected int getRefreshConfig() {
        return BaseRecycleViewFragment.REFRESH_AND_LOADMORE;
    }

    @Override
    protected void onRefreshing() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onLoadMore(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {

    }

    @Override
    protected void initData(View view) {
        ArrayList<String> strList = new ArrayList<String>();
        strList.add("条目类型一");
        strList.add("条目类型一");
        strList.add("条目类型一");
        strList.add("条目类型一");
        strList.add("条目类型一");
        strList.add("条目类型一");
        strList.add("条目类型一");
        mAdapter.setDataSource(strList);
    }

}
