package com.youlongnet.lulu.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * @author lyao
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected FragmentManager mFragmentManager;
    private View mRootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mFragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = View.inflate(mContext, setLayout(), null);
        ButterKnife.inject(this, mRootView);
        initWidget();
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(mRootView);
    }

    protected abstract int setLayout();

    protected abstract void initData(View view);

    protected void initWidget() {
    }

}
