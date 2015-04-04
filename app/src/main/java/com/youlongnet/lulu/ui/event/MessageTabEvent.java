package com.youlongnet.lulu.ui.event;

public class MessageTabEvent {
    public int toolBarType;/*哪个toolBar*/
    public String tabIndex;/*要处理的是哪个item*/

    public MessageTabEvent(int toolBarType, String tabIndex) {
        this.toolBarType = toolBarType;
        this.tabIndex = tabIndex;
    }


}
