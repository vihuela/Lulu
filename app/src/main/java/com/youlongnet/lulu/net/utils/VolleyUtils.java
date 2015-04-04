package com.youlongnet.lulu.net.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, String> reqBean2Map(Object reqBean) {
        if (reqBean == null) {
            return null;
        }
        Map<String, String> Parmap = new HashMap<String, String>();
        Class<?> objectClass = reqBean.getClass();
        Field[] fields = objectClass.getFields();
        for (int i = 0, size = fields.length; i < size; i++) {
            Field field = fields[i];
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object value = field.get(reqBean);
                if (value == null) {
                    continue;
                }
                /*volley内部已经编码，无需这里处理*/
                Parmap.put(field.getName(), JsonUtil.convert(value));
            } catch (Exception e) {
            }
        }
        return Parmap;
    }

    public static String reqBean2jsonString(Object reqBean) {
        if (reqBean == null) {
            return "";
        }
        boolean firstParam = true;
        StringBuilder sb = new StringBuilder();
        Class<?> objectClass = reqBean.getClass();
        Field[] fields = objectClass.getFields();
        for (int i = 0, size = fields.length; i < size; i++) {
            Field field = fields[i];
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object value = field.get(reqBean);
                if (value == null) {
                    continue;
                }
                if (firstParam) {
                    firstParam = false;
                } else {
                    sb.append("&");
                }
                sb.append(field.getName());
                sb.append("=");
                sb.append(URLEncoder.encode(JsonUtil.convert(value), "UTF-8"));
            } catch (Exception e) {

            }
        }
        return sb.toString();
    }
}
