package com.youlongnet.lulu.bean;

import java.util.ArrayList;
import java.util.Random;

public class Message {

    private final static String[] URLS = new String[]{
            "http://a.hiphotos.baidu.com/image/pic/item/09fa513d269759ee75aabcccb0fb43166d22df3b.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/aa64034f78f0f736487277f10855b319eac413b5.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/adaf2edda3cc7cd9053deed03b01213fb80e91a3.jpg",
            "http://d.hiphotos.baidu.com/image/pic/item/5d6034a85edf8db1882010630b23dd54564e74ae.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/77094b36acaf2edd4d2dfbf68f1001e9380193d5.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/d62a6059252dd42ae0a1bcaa013b5bb5c9eab803.jpg"};
    private final static String[] TITLES = new String[]{"动漫同人会", "安卓开发组", "屌丝汽车群", "游龙项目组", "技术专属群", "老王不可欺"};
    private final static String[] CONTENTS = new String[]{"老王:不要这样子", "老王:我就要这样子", "老王:不可以这样子？", "老王:我怕这样子？", "老王:我喜欢这样子", "老王:为什么要这样子？"};
    private final static String[] TIMES = new String[]{"13:45", "16:45", "17:45", "18:45", "19:45", "20:45"};
    public String headUrl;
    public String title;
    public String content;
    public String time;
    public Message(String headUrl, String title, String content, String time) {
        this.headUrl = headUrl;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public static ArrayList<Message> getVirtulData() {
        ArrayList<Message> messages = new ArrayList<Message>();
        messages.clear();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(6);
            messages.add(new Message(URLS[index], TITLES[index], CONTENTS[index], TIMES[index]));
        }
        return messages;
    }

}
