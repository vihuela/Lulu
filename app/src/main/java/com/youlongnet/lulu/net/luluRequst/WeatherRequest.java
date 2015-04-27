package com.youlongnet.lulu.net.luluRequst;

import android.content.Context;

import com.youlongnet.lulu.net.queue.Net;
import com.youlongnet.lulu.net.utils.INoProguard;

public class WeatherRequest extends Net<WeatherRequest.WeatherReq, WeatherRequest.WeatherRes> {

    /**
     * weatherinfo : {"SD":"22%","isRadar":"1","time":"10:35","WSE":"2","WS":"2级","WD":"西南风","njd":"暂无实况","qy":"1015","Radar":"JC_RADAR_AZ9010_JB","temp":"9","cityid":"101010100","city":"北京"}
     */
    public WeatherRequest(Context ctx) {
        super(ctx, "http://www.weather.com.cn/data/sk/101010100.html", "get");
    }


    public static class WeatherReq implements INoProguard {
    }

    public static class WeatherRes implements INoProguard {

        public WeatherinfoEntity weatherinfo;
    }


    public class WeatherinfoEntity {
        /**
         * SD : 22%
         * isRadar : 1
         * time : 10:35
         * WSE : 2
         * WS : 2级
         * WD : 西南风
         * njd : 暂无实况
         * qy : 1015
         * Radar : JC_RADAR_AZ9010_JB
         * temp : 9
         * cityid : 101010100
         * city : 北京
         */
        private String SD;
        private String isRadar;
        private String time;
        private String WSE;
        private String WS;
        private String WD;
        private String njd;
        private String qy;
        private String Radar;
        private String temp;
        private String cityid;
        private String city;

        public String getSD() {
            return SD;
        }

        public void setSD(String SD) {
            this.SD = SD;
        }

        public String getIsRadar() {
            return isRadar;
        }

        public void setIsRadar(String isRadar) {
            this.isRadar = isRadar;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getWSE() {
            return WSE;
        }

        public void setWSE(String WSE) {
            this.WSE = WSE;
        }

        public String getWS() {
            return WS;
        }

        public void setWS(String WS) {
            this.WS = WS;
        }

        public String getWD() {
            return WD;
        }

        public void setWD(String WD) {
            this.WD = WD;
        }

        public String getNjd() {
            return njd;
        }

        public void setNjd(String njd) {
            this.njd = njd;
        }

        public String getQy() {
            return qy;
        }

        public void setQy(String qy) {
            this.qy = qy;
        }

        public String getRadar() {
            return Radar;
        }

        public void setRadar(String Radar) {
            this.Radar = Radar;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getCityid() {
            return cityid;
        }

        public void setCityid(String cityid) {
            this.cityid = cityid;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

    }
}
