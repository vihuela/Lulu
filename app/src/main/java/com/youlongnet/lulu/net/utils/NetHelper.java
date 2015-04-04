package com.youlongnet.lulu.net.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.youlongnet.lulu.net.request.GsonRequest;
import com.youlongnet.lulu.utils.PreferenceHelper;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * @author lyao
 */
@SuppressLint("HandlerLeak")
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class NetHelper implements INetHelper {
    protected String mBaseUrl;
    protected Class mResponseClass;
    protected RequestQueue mRequestQueue;
    protected int mMethod = Method.POST;
    protected Object mRequestTag;
    protected boolean mDebug;
    protected StringBuilder mBuilder;
    protected Map<String, String> mParam;
    protected Context mContext;
    protected Handler mHandler;
    protected long mLoopTime;
    protected GsonRequest mGsonReq;
    protected boolean mCache;
    private Gson mGson = new Gson();

    public NetHelper(Context ctx) {
        this.mContext = ctx;
        this.mRequestQueue = VolleyUtils.getInstance(this.mContext);
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mRequestQueue.add(mGsonReq);
                mHandler.sendEmptyMessageDelayed(0, mLoopTime);
            }
        };
    }

    /*全局接口的通用参数配置*/
    final protected Map<String, String> spliceParaMap(Map<String, String> originParam) {
        if (!originParam.containsKey("_api_token"))
            originParam.put("_api_token", "14af186967ea0a2ad92f0b483ae48471");
        if (!originParam.containsKey("_api_key"))
            originParam.put("_api_key", "fa1f58046f169f08d3ebf086a1139912");
        if (!originParam.containsKey("_api_time"))
            originParam.put("_api_time", "130b60b65b41080750475c832f843f22");
        return originParam;
    }

    final protected int setMethodType(RequestType type) {
        return type == null ? Method.POST : type == RequestType.GET ? Method.GET : Method.POST;
    }

    final protected <RESPONSE> Class<RESPONSE> setResponseClass(NetCallback<RESPONSE> callBack) {
        return (Class<RESPONSE>) ((ParameterizedType) callBack.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }

    final public <RESPONSE> RESPONSE getCacheJsonString() {
        String cacheJson = PreferenceHelper.readString(this.mContext, "gsonCache.xml", mBaseUrl);
        return TextUtils.isEmpty(cacheJson) ? null : (RESPONSE) mGson.fromJson(cacheJson, this.mResponseClass);
    }

    public enum RequestType {
        GET, POST
    }

    public static interface NetCallback<RESPONSE> {
        void onCompleted(VolleyError e, RESPONSE response);
    }
}
