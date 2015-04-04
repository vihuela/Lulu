package com.youlongnet.lulu.app;

import android.app.Application;
import android.content.Context;

public class LuLuApplication extends Application {

    public static Context applicationContext;
    public static String currentUserNick = "";
    private static LuLuApplication instance;
    public final String PREF_USERNAME = "username";

    // public static MyHXSDKHelper hxSDKHelper = new MyHXSDKHelper();

    public static LuLuApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        // hxSDKHelper.onInit(applicationContext);
    }

}
