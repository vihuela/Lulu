package com.youlongnet.lulu.ui.frg;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.youlongnet.lulu.R;
import com.youlongnet.lulu.net.luluRequst.AddAddressRequest;
import com.youlongnet.lulu.net.luluRequst.AlertPullRequest;
import com.youlongnet.lulu.net.luluRequst.BaseResponse;
import com.youlongnet.lulu.net.luluRequst.WeatherRequest;
import com.youlongnet.lulu.net.queue.Net;
import com.youlongnet.lulu.net.utils.NetHelper;
import com.youlongnet.lulu.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

public class GuidFragment extends BaseFragment {


    @InjectView(R.id.tv_content)
    TextView mTvContent;

    @Override
    public int setLayout() {
        return R.layout.frg_guid;
    }

    @Override
    public void initData(View view) {

    }


    @OnClick({R.id.postString, R.id.postJsonString, R.id.get1, R.id.get2, R.id.example})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postString:
                postString();
                break;
            case R.id.postJsonString:
                postJsonString();
                break;
            case R.id.get1:
                get1();
                break;
            case R.id.get2:
                get2();
                break;
            case R.id.example:
                example();
                break;
        }
    }

    @Override
    protected void onRealPause() {
        super.onRealPause();
    }

    private void example() {
        AddAddressRequest request = new AddAddressRequest(mContext);
        AddAddressRequest.RequestBean param = new AddAddressRequest.RequestBean();
        param.frist_name = "ywh";
        param.last_name = "lyao";
        param.address = "罗湖区莲塘";
        param.key = "123456";
        param.note = "我是测试接口";
        param.time = "2015年4月27日";
        param.phone_number = "1565978564";
        param.tax_id = "1314520";
        param.user_id = "54321";
        request.setParameter(param)
                .setCache(true)
                .setDebug(true)
                .setLoop(3000);
        request.setCallbacks(new NetHelper.NetCallback<String>() {
            @Override
            public void onCompleted(VolleyError e, String s) {
                if (s != null)
                    System.out.println(s);
                if (e != null) {
                    Toast.makeText(mContext, e.networkResponse.statusCode + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void get1() {
        mTvContent.setText("");
        Net.with(mContext)
                .load("http://www.weather.com.cn/data/sk/101010100.html")
                .setMethod("get")
                .setDebug(true)
                .setCache(true)
                .setSpliceParam(false)
                .setLoop(3000)
                .setCallback(new NetHelper.NetCallback<WeatherRequest.WeatherRes>() {
                    @Override
                    public void onCompleted(VolleyError e, WeatherRequest.WeatherRes weatherRes) {
                        if (weatherRes != null) {
                            mTvContent.setText(weatherRes.weatherinfo.getCity() + " " + weatherRes.weatherinfo.getTemp());
                        }
                        if (e != null) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void get2() {
        mTvContent.setText("");
        WeatherRequest request = new WeatherRequest(mContext);
        request.setDebug(true)
                .setCache(true);
        request.setCallbacks(new NetHelper.NetCallback<WeatherRequest.WeatherRes>() {
            @Override
            public void onCompleted(VolleyError e, WeatherRequest.WeatherRes weatherRes) {
                if (weatherRes != null) {
                    mTvContent.setText(weatherRes.weatherinfo.getCity() + " " + weatherRes.weatherinfo.getTemp());
                }
                if (e != null) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void postJsonString() {
        mTvContent.setText("");
        AlertPullRequest request = new AlertPullRequest(mContext);
        /*构建参数*/
        AlertPullRequest.AlertInfoReq req = new AlertPullRequest.AlertInfoReq();
        AlertPullRequest.Settings settings = new AlertPullRequest.Settings();
        Collections.addAll(settings.alerts,
                new AlertPullRequest.SettingItem("0", "OKCoin.cn", true, 1200, 0),
                new AlertPullRequest.SettingItem("1", "OKCoin.com", false, 1900, 300));
        req.setting = settings;

        request.setParameter(req)
                .setCache(true)
                .setDebug(true);
        //链式加载泛型无法起到限制作用
        request.setCallbacks(new NetHelper.NetCallback<AlertPullRequest.AlertInfoRes>() {
            @Override
            public void onCompleted(VolleyError e, AlertPullRequest.AlertInfoRes alertInfoRes) {
                if (alertInfoRes != null) {
                    mTvContent.append("errorCode:" + alertInfoRes.errorCode + " " + "info:" + alertInfoRes.info + " " + "result:" + alertInfoRes.result);
                }
                if (e != null) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void postString() {
        mTvContent.setText("");
        Map<String, String> param = new HashMap<>();
        param.put("type", "1");
        param.put("sub_type", "3");

        Net.with(mContext).
                load("http://lulutest.19196.com/1.0/hall.getHallLink")
                .setParameter(param)
                .setDebug(true)
                .setTag("circleFrg")
                .cancelAll()
                .setCache(true)
                .setCallback(new NetHelper.NetCallback<BaseResponse.SlideView>() {

                    @Override
                    public void onCompleted(VolleyError e, BaseResponse.SlideView response) {
                        if (response != null) {
                            ArrayList<String> images = new ArrayList<>();
                            for (BaseResponse.SlideItemInfo info : response.data) {
                                mTvContent.append(info.hall_image + "\n");
                            }
                        }
                        if (e != null) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
