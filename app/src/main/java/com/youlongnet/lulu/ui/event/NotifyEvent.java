package com.youlongnet.lulu.ui.event;

import android.app.Activity;

public class NotifyEvent {

    public String msg;
    public Activity aty;/*主布局的Fragment不用传*/
    public int groupLayoutResId;

    public NotifyEvent(String msg, Activity aty, int groupLayoutResId) {
        this.msg = msg;
        this.aty = aty;
        this.groupLayoutResId = groupLayoutResId;
    }

    public NotifyEvent(String msg, int groupLayoutResId) {
        this.msg = msg;
        this.groupLayoutResId = groupLayoutResId;
    }

}
