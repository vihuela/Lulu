package com.youlongnet.lulu.net.luluRequst;

import android.content.Context;

import com.youlongnet.lulu.net.queue.Net;
import com.youlongnet.lulu.net.utils.INoProguard;

import java.util.ArrayList;

public class AlertPullRequest extends Net<AlertPullRequest.AlertInfoReq, AlertPullRequest.AlertInfoRes> {

    public AlertPullRequest(Context ctx) {
        super(ctx,"https://www.okcoin.com/api/setPriceAlert.do");
    }

    public static class AlertInfoReq implements INoProguard {
        public int isClient = 3;
        public Settings setting = new Settings();
    }

    public static class Settings implements INoProguard {
        public boolean istest = false;
        public String appkey = "53181d4a56240bec3600075d";
        public String app_master_secret = "rbcmie1mxe2zok5nub2ooqckhuoj3xne";
        /**
         * 根据不同设备生成不同标识
         */
        public String device_token = "AsALdBLvu56hStGzUAOJI-Njz7acHUZ5QR63xgZeDVS_";
        public int clientType = 0;// android 0 ios 1
        /**
         * 默认英文，根据用户环境修改
         */
        public String lang = "en_us";
        /**
         * 根据用户选择的item确定
         */
        public ArrayList<SettingItem> alerts = new ArrayList<SettingItem>();

    }

    public static class SettingItem implements INoProguard {
        public String market_from;// 市场id
        public String market_name;// 市场名称
        public boolean effect;// 是否预警
        public double hight;// 价格上限
        public double low;// 价格下限

        public SettingItem(String market_from, String market_name, boolean effect, double hight, double low) {
            this.market_from = market_from;
            this.market_name = market_name;
            this.effect = effect;
            this.hight = hight;
            this.low = low;
        }

        public SettingItem(String market_from, boolean effect) {
            this.market_from = market_from;
            this.effect = effect;
        }
    }

    public static class AlertInfoRes implements INoProguard{
        public int errorCode;
        public String result;
        public String info;
    }
}
