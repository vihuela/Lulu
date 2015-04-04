package com.youlongnet.lulu.ui.aty;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.base.BaseActivity;
import com.youlongnet.lulu.ui.manager.TopManager;
import com.youlongnet.lulu.ui.utils.RevealBackgroundView;
import com.youlongnet.lulu.utils.DensityUtils;

import butterknife.InjectView;

public class UserInfoActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener {
    @InjectView(R.id.main_contain)
    LinearLayout mContain;
    @InjectView(R.id.iv)
    ImageView mHeadImageView;
    @InjectView(R.id.rbg)
    RevealBackgroundView mRevealBgView;
    int actionBarSize;
    Toolbar mCurrentToolBar;
    private int[] mStartLocs;
    private String mUrl;
    private DecelerateInterpolator mInterpolator;

    public static void startUserInfoActivity(@NonNull Context ctx, @NonNull int[] startLocation, @NonNull String url) {
        Intent intent = new Intent(ctx, UserInfoActivity.class);
        intent.putExtra("startLoc", startLocation);
        intent.putExtra("url", url);
        ctx.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentToolBar = TopManager.getInstance().getCurrentToolBar(mContext);
        mCurrentToolBar.setTitle("用户详情");
        mCurrentToolBar.setTranslationY(-actionBarSize);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_userinfo);
        TopManager.getInstance().changeUi(mContain, TopManager.UI_Default);
        actionBarSize = DensityUtils.dip2px(mContext, 56);
        mInterpolator = new DecelerateInterpolator();
        mStartLocs = getIntent().getIntArrayExtra("startLoc");
        mUrl = getIntent().getStringExtra("url");
        mRevealBgView.setOnStateChangeListener(this);
        mRevealBgView.setFillPaintColor(getResources().getColor(R.color.background_material_light));
        mRevealBgView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRevealBgView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRevealBgView.startFromLocation(mStartLocs);
                return true;
            }
        });
    }

    @Override
    public void onStateChange(int state) {
        switch (state) {
            case RevealBackgroundView.STATE_FINISHED:
                mCurrentToolBar.animate()
                        .setInterpolator(mInterpolator)
                        .setDuration(400)
                        .translationY(0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                loadHeadImage();
                            }
                        })
                        .start();
                break;
            default:
                break;
        }
    }

    private void loadHeadImage() {
        mHeadImageView.setScaleX(0);
        mHeadImageView.setScaleY(0);
        Picasso.with(mContext)
                .load(mUrl)
                .placeholder(R.drawable.img_rect_placeholder)
                .error(R.drawable.error)
                .into(mHeadImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mHeadImageView.animate()
                                .scaleX(1)
                                .scaleY(1)
                                .setInterpolator(mInterpolator)
                                .setDuration(400)
                                .start();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
