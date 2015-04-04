package com.youlongnet.lulu.ui.event;

public class SearchContentEvent {

    public boolean isSubmit;/*是否提交查询*/
    public String text;

    public SearchContentEvent(boolean isSubmit, String text) {
        this.isSubmit = isSubmit;
        this.text = text;
    }

}
