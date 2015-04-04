package com.youlongnet.lulu.ui.utils;

import android.content.Context;
import android.content.Intent;

public class JumpToActivity {
    public static <T> void jumpTo(Context ctx, Class<?> clazz) {
        Intent intent = new Intent(ctx, clazz);
        ctx.startActivity(intent);
    }

    public static <T> void jumpTo(Context ctx, Intent intent) {
        ctx.startActivity(intent);
    }
}
