package com.youlongnet.lulu.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.youlongnet.lulu.ui.base.BaseFragment;

import java.util.ArrayList;

public class ContentManagerAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseFragment> mPagers;

    public ContentManagerAdapter(FragmentManager fm, ArrayList<BaseFragment> pagers) {
        super(fm);
        this.mPagers = pagers;
    }

    @Override
    public Fragment getItem(int pos) {
        return this.mPagers.get(pos);
    }

    @Override
    public int getCount() {
        return this.mPagers.size();
    }

}
