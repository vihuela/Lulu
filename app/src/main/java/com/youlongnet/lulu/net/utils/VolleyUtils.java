package com.youlongnet.lulu.net.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyUtils {
    private static RequestQueue instance = null;

    private VolleyUtils() {
    }

    public static RequestQueue getInstance(Context ctx) {
        if (instance == null) {
            synchronized (VolleyUtils.class) {
                if (instance == null)
                    instance = Volley.newRequestQueue(ctx);
            }
        }
        return instance;
    }
}
