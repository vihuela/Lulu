package com.youlongnet.lulu.net.utils;

import android.content.Context;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyUtils {
    private static RequestQueue mQueue = null;
    private static Handler mHandler = null;

    private VolleyUtils() {
    }

    public static RequestQueue getQueue(Context ctx) {
        if (mQueue == null) {
            synchronized (VolleyUtils.class) {
                if (mQueue == null)
                    mQueue = Volley.newRequestQueue(ctx);
            }
        }
        return mQueue;
    }

    public static Handler getHandler() {
        if (mHandler == null) {
            synchronized (VolleyUtils.class) {
                if (mHandler == null)
                    mHandler = new Handler();
            }
        }
        return mHandler;
    }

}
