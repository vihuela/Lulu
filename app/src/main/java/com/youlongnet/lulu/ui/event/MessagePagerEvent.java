package com.youlongnet.lulu.ui.event;

public class MessagePagerEvent {
    public int pagerIndex;/*要处理的是哪个item*/

    public MessagePagerEvent(int pagerIndex) {
        this.pagerIndex = pagerIndex;
    }
}
