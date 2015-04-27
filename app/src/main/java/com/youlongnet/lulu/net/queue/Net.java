package com.youlongnet.lulu.net.queue;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.youlongnet.lulu.net.request.GsonRequest;
import com.youlongnet.lulu.net.utils.INoProguard;
import com.youlongnet.lulu.net.utils.NetHelper;

import java.util.Map;

/**
 * POST参数为String：
 * Net.with(mContext)
 * .load("http://lulutest.19196.com/1.0/hall.getHallLink")
 * .setParameter(param)
 * .setDebug(true)
 * .setTag("circleFrg")
 * .cancelAll()
 * .setCache(true)
 * .setCallback(new NetHelper.NetCallback<BaseResponse.SlideView>() {
 *
 * @Override public void onCompleted(VolleyError e, BaseResponse.SlideView response) {
 * if (response != null) {
 * <p/>
 * }
 * if (e != null) {
 * Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
 * }
 * }
 * });
 */

/**
 * 对volley简单封装，支持get/post、post参数key = val，支持缓存
 *
 * @author lyao
 */
public class Net<REQUEST extends INoProguard, RESPONSE> extends NetHelper<REQUEST, RESPONSE> {

    private Net(Context ctx) {
        super(ctx);
    }

    /**
     * 适合接口类化适用
     *
     * @param ctx  上下文
     * @param url  访问路径
     * @param type 请求方式
     */
    public Net(@NonNull Context ctx, @NonNull String url, String type) {
        this(ctx);
        this.load(url);
        this.setMethod(type);
    }

    public Net(Context ctx, String url) {
        this(ctx);
        this.load(url);
        this.setMethod("post");
    }


    public static Net with(@NonNull Context ctx) {
        return new Net(ctx);
    }

    public Net load(@NonNull String url) {
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

    /**
     * 使用Post参数为String使用，拼接通用参数
     *
     * @param splice
     * @return
     */
    public Net setSpliceParam(boolean splice) {
        super.mSplice = splice;
        return this;
    }

    /**
     * 适用Post参数为String
     *
     * @param param map
     * @return
     */
    public Net setParameter(Map<String, String> param) {
        super.spliceParaMap(param);
        return this;
    }


    /**
     * 适用Post参数为Json String
     *
     * @param param 请求Bean
     * @return
     */
    public Net setParameter(REQUEST param) {
        super.setRequestParam(param);
        return this;
    }

    public Net setMethod(@RequestType String type) {
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

    /**
     * 适用Post参数为Json String(RESPONSE的约束来自内部)
     *
     * @param callBack
     */
    public void setCallbacks(final NetCallback<RESPONSE> callBack) {
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
    }

    /**
     * 适用Post参数为String(RESPONSE的约束来自外部)
     *
     * @param callBack
     * @param <RESPONSE>
     * @return
     */
    public <RESPONSE> void setCallback(final NetCallback<RESPONSE> callBack) {
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
