package com.youlongnet.lulu.ui.frg.message;

import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.youlongnet.lulu.R;
import com.youlongnet.lulu.net.luluRequst.BaseResponse;
import com.youlongnet.lulu.net.queue.Net;
import com.youlongnet.lulu.net.utils.NetHelper;
import com.youlongnet.lulu.ui.adapters.CircleAdapter;
import com.youlongnet.lulu.ui.base.BaseRecycleViewFragment;
import com.youlongnet.lulu.ui.event.NotifyEvent;
import com.youlongnet.lulu.ui.utils.BusProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CircleFrg extends BaseRecycleViewFragment<CircleAdapter> {

    @Override
    protected int setLayout() {

        return R.layout.frg_circle;
    }

    @Override
    protected void initData(View view) {
    }

    @Override
    protected LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    protected CircleAdapter getAdapter() {
        return new CircleAdapter(mContext);
    }

    @Override
    protected int getRefreshConfig() {
        return BaseRecycleViewFragment.REFRESH;
    }

    @Override
    protected void onRefreshing() {
        loadData();
//		loadData1();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void loadData1() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("type", "1");
        param.put("sub_type", "3");

        Net.with(mContext).
                load("http://lulutest.19196.com/1.0/hall.getHallPost")
                .setParameter(param)
                .setDebug(true)
                .setTag("circleFrg")
                .setCache(true)
                .setCallback(new NetHelper.NetCallback<BaseResponse.HallPost>() {

                    @Override
                    public void onCompleted(VolleyError e, BaseResponse.HallPost response) {
                        System.out.println();
                    }

                });
    }

    private void loadData() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("type", "1");
        param.put("sub_type", "3");

        Net.with(mContext).
                load("http://lulutest.19196.com/1.0/hall.getHallLink")
                .setParameter(param)
                .setDebug(true)
                .setTag("circleFrg")
                /*.cancelAll()*/
                .setCache(true)
                .setCallback(new NetHelper.NetCallback<BaseResponse.SlideView>() {

                    @Override
                    public void onCompleted(VolleyError e, BaseResponse.SlideView response) {
                        if (response != null) {
                            ArrayList<String> images = new ArrayList<String>();
                            for (BaseResponse.SlideItemInfo info : response.data) {
                                images.add(info.hall_image);
                            }
                            mAdapter.setDataSource(images);
                            mAdapter.notifyDataSetChanged();
                            mRecycler.hideError();
                        }
                        // 以下暂未封装
                        if (e != null) {
                            loadCircleFinish();
                            /*根据当前适配器的条目是否为0判断*/
                            mRecycler.showError();
                            mRecycler.getErrorView().setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(mContext, "触发条件", Toast.LENGTH_SHORT).show();
                                }
                            });
                            String msg = e.getClass().getSimpleName() + "错误";
                            BusProvider.getInstance().post(new NotifyEvent(msg, R.id.contain));
                        }
                    }
                });
    }

    @Override
    protected void onLoadMore(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {

    }
}
