package com.youlongnet.lulu.ui.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.Manager;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.squareup.otto.Subscribe;
import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.event.NotifyEvent;

public class NotifyProvider {

    private static NotifyProvider instance = new NotifyProvider();
    private Activity mActivity;
    private Configuration mConfig;
    private NiftyNotificationView mNotificationView;
    private Activity mCurrentAty;

    public static NotifyProvider getInstance() {
        return instance;
    }

    public void init(@NonNull Activity aty) {
        this.mActivity = aty;
        mConfig = new Configuration.Builder()
                .setAnimDuration(700)
                .setDispalyDuration(1500)
                .setBackgroundColor("#FFBDC3C7")
                .setTextColor("#FF444444")
                .setIconBackgroundColor("#FFFFFFFF")
                .setTextPadding(5)                      //dp
                .setViewHeight(48)                      //dp
                .setTextLines(2)                        //setViewHeight 和 setTextLines 一起用
                .setTextGravity(Gravity.CENTER)        //Gravity.CENTER,contain icon Gravity.CENTER_VERTICAL
                .build();
    }

    @Subscribe
    public void Notify(NotifyEvent event) {
       /* if(mNotificationView!=null && mCurrentAty == event.aty)
            return;*/
        mCurrentAty = event.aty == null ? mActivity : event.aty;
        mNotificationView = NiftyNotificationView.build(mCurrentAty, event.msg, Effects.flip, event.groupLayoutResId, mConfig)
                .setIcon(R.drawable.error_notify)               //可以仅仅是文本
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mActivity, "干嘛啊！！！", Toast.LENGTH_SHORT).show();
                    }
                });
        mNotificationView.show();
    }
}
