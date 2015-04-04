package com.youlongnet.lulu.net.queue;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.youlongnet.lulu.net.request.GsonRequest;
import com.youlongnet.lulu.net.utils.NetHelper;

import java.util.Map;

/**
 * 对volley简单封装，支持get/post、post参数key = val，支持缓存
 *
 * @author lyao
 */
@SuppressWarnings({"unchecked"})
public class Net extends NetHelper {

    private Net(Context ctx) {
        super(ctx);
    }

    public static Net with(Context ctx) {
        return new Net(ctx);
    }

    public Net load(String url) {
        super.mBaseUrl = url;
        return this;
    }

    public Net setCache(boolean cache) {
        super.mCache = cache;
        return this;
    }

    public Net setDebug(boolean debug) {
        super.mDebug = debug;
        return this;
    }

    public Net setParameter(Map<String, String> param) {
        super.mParam = super.spliceParaMap(param);
        return this;
    }

    public Net setMethod(RequestType type) {
        super.mMethod = super.setMethodType(type);
        return this;
    }

    public Net setTag(Object tag) {
        super.mRequestTag = tag;
        return this;
    }

    public Net setLoop(long time) {
        super.mLoopTime = time;
        return this;
    }

    public Net cancleRequest() {
        if (super.mGsonReq != null) {
            super.mGsonReq.cancel();
            super.mHandler.removeCallbacksAndMessages(null);
        }
        return this;
    }

    /**
     * 不能取消循环请求
     */
    public Net cancelAllWithTag(Object tag) {
        if (tag != null)
            super.mRequestQueue.cancelAll(tag);
        return this;
    }

    /**
     * 取消网络队列中所有请求(包含循环请求)
     */
    public Net cancelAll() {
        super.mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
        super.mHandler.removeCallbacksAndMessages(null);
        return this;
    }

    public <RESPONSE> Net setCallback(final NetCallback<RESPONSE> callBack) {
        super.mResponseClass = super.setResponseClass(callBack);
        GsonRequest<RESPONSE> gsonRequest = new GsonRequest<RESPONSE>(super.mContext, super.mCache, super.mDebug, super.mParam, super.mMethod, super.mBaseUrl, super.mResponseClass,
                new Response.Listener<RESPONSE>() {
                    @Override
                    public void onResponse(RESPONSE response) {
                        if (response != null && callBack != null)
                            callBack.onCompleted(null, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (Net.super.mCache) {
                    RESPONSE cacheBean = Net.super.getCacheJsonString();
                    if (cacheBean != null) callBack.onCompleted(null, cacheBean);
                }
                callBack.onCompleted(error, null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                        /*sub thread*/
                return Net.super.mParam;
            }
        };
        if (super.mRequestTag != null)
            gsonRequest.setTag(super.mRequestTag);
        super.mGsonReq = gsonRequest;
        super.mRequestQueue.add(super.mGsonReq);
        if (super.mLoopTime != 0)
            super.mHandler.sendEmptyMessageDelayed(0, super.mLoopTime);
        return this;
    }

    @Override
    public void onStop() {
        this.cancelAll();
    }

    @Override
    public void onDestroy() {
        this.cancelAll();
    }
}
