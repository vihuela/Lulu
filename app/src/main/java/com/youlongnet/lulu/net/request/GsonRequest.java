package com.youlongnet.lulu.net.request;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.youlongnet.lulu.net.utils.JsonUtil;
import com.youlongnet.lulu.utils.PreferenceHelper;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

public class GsonRequest<RESPONSE> extends Request<RESPONSE> {

    /*用于调试的配置*/
    private final static String DEBUG = "volley";
    private final Listener<RESPONSE> mListener;
    private Gson mGson = new Gson();
    private Class<RESPONSE> mClass;
    /*超时时间*/
    private int initialTimeoutMs = 5000;
    private boolean mDebug;
    private boolean mCache;
    private String mUrl;
    private Map<String, String> mParam;

    private Context mContext;


    public GsonRequest(Context ctx, boolean isCache, boolean isDebug, Map<String, String> param, int method, String url, Class<RESPONSE> clazz, Listener<RESPONSE> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mContext = ctx;
        this.mCache = isCache;
        this.mDebug = isDebug;
        this.mUrl = url;
        this.mParam = param;

        this.mClass = clazz;
        this.mListener = listener;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        /*超时之后不重试*/
        RetryPolicy retryPolicy = new DefaultRetryPolicy(initialTimeoutMs, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }

    @Override
    protected Response<RESPONSE> parseNetworkResponse(NetworkResponse response) {
        String jsonString = "";
        try {
            jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (this.mDebug) {
                TraversalMap(this.mParam);
                Log.d(DEBUG, "---RES：" + jsonString + "\n" + "--------------REQUEST END------------");
            }
            if (this.mCache) {
                PreferenceHelper.write(this.mContext, "gsonCache.xml", this.mUrl, jsonString);
            }
            return Response.success(this.mGson.fromJson(jsonString, this.mClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            /**
             * eg:{"errorCode":5,"errorMessge":"appkey\u65e0\u6548","data":""}
             * data is ""，GSON not handle
             */
            RESPONSE errorResponse = null;
            try {
                errorResponse = JsonUtil.convert(jsonString, this.mClass);
            } catch (Exception e1) {
                return Response.error(new ParseError(e));
            }
            return Response.success(errorResponse, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    @Override
    protected void deliverResponse(RESPONSE response) {
        this.mListener.onResponse(response);
    }

    final public void TraversalMap(Map<String, String> map) {

        StringBuilder builder = new StringBuilder();
        builder.append("--------------REQUEST START------------" + "\n");
        builder.append("--URL：" + mUrl + "\n" + "--REQ：" + "\n");

        for (Entry<String, String> entry : map.entrySet()) {
            builder.append("     " + entry.getKey() + "," + entry.getValue() + "\n");
        }
        Log.d(DEBUG, builder.toString());
    }
}