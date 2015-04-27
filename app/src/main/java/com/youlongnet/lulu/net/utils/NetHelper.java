package com.youlongnet.lulu.net.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.youlongnet.lulu.net.request.GsonRequest;
import com.youlongnet.lulu.utils.PreferenceHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyao
 */
@SuppressLint("HandlerLeak")
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class NetHelper<REQUEST, RESPONSE> implements INetHelper<REQUEST, RESPONSE> {
    protected String mBaseUrl;
    protected Class mResponseClass;
    protected Class mRequestClass;
    protected RequestQueue mRequestQueue;
    protected int mMethod = Request.Method.POST;
    protected Object mRequestTag;
    protected boolean mDebug;
    protected Map<String, String> mParam;
    protected Context mContext;
    protected Handler mHandler;
    protected long mLoopTime;
    protected GsonRequest mGsonReq;
    protected boolean mCache;
    protected boolean mSplice = true;
    protected Gson mGson;

    {
        mGson = new Gson();
    }

    public NetHelper() {
    }

    public NetHelper(Context ctx) {
        this.mContext = ctx;
        this.mRequestQueue = VolleyUtils.getInstance(this.mContext);
        try {
            this.mRequestClass = (Class<REQUEST>) ((ParameterizedType) (NetHelper.this.getClass().getGenericSuperclass())).getActualTypeArguments()[0];
        } catch (Exception e) {
            /*REQUEST空，Post参数为String*/
        }
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mRequestQueue.add(mGsonReq);
                mHandler.sendEmptyMessageDelayed(0, mLoopTime);
            }
        };
    }

    public static RequestQueue getRequestQueue(Context ctx) {
        return VolleyUtils.getInstance(ctx);
    }

    /*全局接口的通用参数配置*/
    final protected void spliceParaMap(Map<String, String> originParam) {
        this.mParam = originParam;
        if (!this.mSplice) return;
        if (!originParam.containsKey("_api_token"))
            originParam.put("_api_token", "14af186967ea0a2ad92f0b483ae48471");
        if (!originParam.containsKey("_api_key"))
            originParam.put("_api_key", "fa1f58046f169f08d3ebf086a1139912");
        if (!originParam.containsKey("_api_time"))
            originParam.put("_api_time", "130b60b65b41080750475c832f843f22");

    }

    final protected int setMethodType(String type) {
        return TextUtils.isEmpty(type) ? Request.Method.POST : "get".equalsIgnoreCase(type) ? Request.Method.GET : Request.Method.POST;
    }

    final protected <RESPONSE> Class<RESPONSE> setResponseClass(NetCallback<RESPONSE> callBack) {
        if (this.mRequestClass != null)
            /*有请求Bean对象，请求由请求实例发起*/
            return (Class<RESPONSE>) ((ParameterizedType) (NetHelper.this.getClass().getGenericSuperclass())).getActualTypeArguments()[1];
        else
            return (Class<RESPONSE>) ((ParameterizedType) callBack.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }

    final public <RESPONSE> RESPONSE getCacheJsonString() {
        String cacheJson = PreferenceHelper.readString(this.mContext, "gsonCache.xml", mBaseUrl);
        return TextUtils.isEmpty(cacheJson) ? null : (RESPONSE) mGson.fromJson(cacheJson, this.mResponseClass);
    }

    final protected void setRequestParam(Object object) {
        if (object != null) {
            Class<?> objectClass = object.getClass();
            Field[] fields = objectClass.getFields();
            this.mParam = new HashMap<>();
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (value == null) {
                        continue;
                    }
                    this.mParam.put(field.getName(), mGson.toJson(value));
//                    this.mParam.put(field.getName(), JsonUtil.convert(value));
                } catch (Exception e) {
                }
            }
        }
    }


    @StringDef({"get", "post"})
    @Retention(RetentionPolicy.SOURCE)
    protected @interface RequestType {
    }

    public static interface NetCallback<RESPONSE> {
        void onCompleted(VolleyError e, RESPONSE response);
    }

}
