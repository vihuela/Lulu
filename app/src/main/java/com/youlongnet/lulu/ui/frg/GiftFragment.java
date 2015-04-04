package com.youlongnet.lulu.ui.frg;

import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.otto.Subscribe;
import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.base.BaseFragment;
import com.youlongnet.lulu.ui.event.GiftPageEvent;
import com.youlongnet.lulu.ui.frg.gift.GiftFrg;
import com.youlongnet.lulu.ui.frg.gift.SearchFrg;
import com.youlongnet.lulu.ui.utils.BusProvider;

public class GiftFragment extends BaseFragment {

    public static final int PAGE_GIFT = 0;
    public static final int PAGE_SEARCH = 1;
    /*prevent onStart() invoke many times so save pageIndex*/
    private int currentPage = PAGE_GIFT;

    @Override
    protected void initWidget() {
        super.initWidget();
        BusProvider.getInstance().register(this);
        mFragmentManager.beginTransaction()
                .add(R.id.contain, new GiftFrg(), "giftFrg")
                .add(R.id.contain, new SearchFrg(), "searFrg")
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void changeUi(GiftPageEvent event) {
        final Fragment cacheGiftFrg = mFragmentManager.findFragmentByTag("giftFrg");
        final Fragment cacheSearFrg = mFragmentManager.findFragmentByTag("searFrg");
        switch (event.pageIndex) {
            case PAGE_GIFT:
                mFragmentManager.beginTransaction()
                        .show(cacheGiftFrg)
                        .hide(cacheSearFrg)
                        .commit();
                currentPage = PAGE_GIFT;
                break;
            case PAGE_SEARCH:
                mFragmentManager.beginTransaction()
                        .show(cacheSearFrg)
                        .hide(cacheGiftFrg)
                        .commit();
                currentPage = PAGE_SEARCH;
                break;

        }

    }

    @Override
    public int setLayout() {
        return R.layout.frg_gift_subgift;
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().post(new GiftPageEvent(currentPage));
    }


    @Override
    protected void initData(View view) {
        /*mFragmentManager commit after onActivityCreated() so replace use onStart()*/
    }

}
