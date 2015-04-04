package com.youlongnet.lulu.net.luluRequst;

import com.youlongnet.lulu.net.utils.INoProguard;

import java.util.List;

public class BaseResponse {

    /*轮播图响应Bean---------------------------------*/
    public static class SlideView extends UniversalResponse {
        public List<SlideItemInfo> data;
    }

    /*大图列表响应Bean----------------------------------------------------*/
    public static class HallPost extends UniversalResponse {
        public List<HallPostItemInfo> data;
    }

    public class SlideItemInfo implements INoProguard {
        public int hall_link_id;
        public String hall_link_name;
        public String hall_link;
        public String hall_image;
        public String type;
        public String sub_type;
        public String hall_level;
        public String post_id;
        public String news_id;
        public String game_id;
        public String sociaty_id;
        public String add_time;
    }

    public class HallPostItemInfo implements INoProguard {
        public String game_name;
        public String image;
        public String news_id;
        public String post_id;
        public String title;
    }
    /*--------------------------------------------------------------------*/

}
