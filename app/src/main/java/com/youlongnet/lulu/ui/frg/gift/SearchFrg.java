package com.youlongnet.lulu.ui.frg.gift;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.base.BaseFragment;
import com.youlongnet.lulu.ui.event.SearchContentEvent;
import com.youlongnet.lulu.ui.utils.BusProvider;

import butterknife.InjectView;

public class SearchFrg extends BaseFragment {

    @InjectView(R.id.tv_Title)
    TextView tv_Title;

    @Override
    protected int setLayout() {
        return R.layout.frg_search;
    }

    @Override
    protected void initData(View view) {

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void changeUi(SearchContentEvent event) {
        if (event.isSubmit) {
            /*执行查询*/
            Toast.makeText(mContext, "执行搜索", Toast.LENGTH_SHORT).show();
        } else {
            tv_Title.setText(event.text);
        }
    }

}
