/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youlongnet.lulu.ui.widget.slidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.youlongnet.lulu.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sidebar extends View {
    private Paint paint;
    private TextView header;
    private float height;
    private ListView mListView;
    private Context context;

    private ContactAdapter contactAdapter;
    private boolean misadapter = false;
    private String[] marray;
    private inSortFinishInterface msortFinish;
    /**
     * 我们存放用户的集合
     */
    private List<User> contactList = new ArrayList<User>();
    ;
    private ContactAdapter adapter;
    private String[] adapterSections;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mListView.setAdapter(contactAdapter);
                misadapter = true;
                adapter = (ContactAdapter) mListView.getAdapter();
                adapterSections = (String[]) adapter.getSections();
                if (msortFinish != null) {//如果实现了回调
                    msortFinish.finishInterface(mListView, contactList);
                }
            }
        }
    };
    private String[] sections;

    public Sidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //这里的数据要根据我们传入的去替换,这里是实现了排序,就是在子线程中对数据的处理,
    public void setListView(ListView listView, String[] array, inSortFinishInterface sortFinish) {
        mListView = listView;
        marray = array;
        msortFinish = sortFinish;
        //在这里实现handler的处理
        new Thread(new Runnable() {//由于处理的数据是耗时的,所以我们需要
            @Override
            public void run() {
                contactList.clear();//记得每次从新获取数据的时候要去清理下原本的内存
                User user = null;
                for (int i = 0; i < marray.length; i++) {
                    char c = Hanyu.getInstance().getFirstStringPinYin(marray[i]);
                    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {    //判定
                        user = new User(marray[i], String.valueOf(c));
                    } else {
                        user = new User(marray[i], "#");
                    }
                    contactList.add(user);
                }
                // 排序,根据
                Collections.sort(contactList);
                contactAdapter = new ContactAdapter(context, R.layout.row_contact, contactList);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }

    private void init() {
        sections = new String[]{"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.DKGRAY);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(sp2px(context, 10));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float center = getWidth() / 2;
        height = getHeight() / sections.length;
        for (int i = sections.length - 1; i > -1; i--) {
            canvas.drawText(sections[i], center, height * (i + 1), paint);
        }
    }

    private int sectionForPoint(float y) {
        int index = (int) (y / height);
        if (index < 0) {
            index = 0;
        }
        if (index > sections.length - 1) {
            index = sections.length - 1;
        }
        return index;
    }

    private void setHeaderTextAndscroll(MotionEvent event) {
        if (mListView == null) {
            //check the mListView to avoid NPE. but the mListView shouldn't be null
            //need to check the call stack later
            return;
        }
        if (!misadapter) {//如果我们手指点击的时候还没加载好不进行操作
            return;
        }
        String headerString = sections[sectionForPoint(event.getY())];
        header.setText(headerString);
        /*这两句代码最好不要删除,由于原本的执行效率不高,才放在这里的*/
        //ContactAdapter adapter = (ContactAdapter) mListView.getAdapter();
        //String[] adapterSections = (String[]) adapter.getSections();
        try {
            for (int i = adapterSections.length - 1; i > -1; i--) {
                if (adapterSections[i].equalsIgnoreCase(headerString)) {//忽略大小写
                    mListView.setSelection(adapter.getPositionForSection(i));
                    break;
                }
            }
        } catch (Exception e) {
            Log.e("setHeaderTextAndscroll", e.getMessage());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (header == null) {
                    header = (TextView) ((View) getParent()).findViewById(R.id.floating_header);
                }
                setHeaderTextAndscroll(event);
                header.setVisibility(View.VISIBLE);
                setBackgroundResource(R.drawable.sidebar_background_pressed);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                setHeaderTextAndscroll(event);
                return true;
            }
            case MotionEvent.ACTION_UP:
                header.setVisibility(View.INVISIBLE);
                setBackgroundColor(Color.TRANSPARENT);
                return true;
            case MotionEvent.ACTION_CANCEL:
                header.setVisibility(View.INVISIBLE);
                setBackgroundColor(Color.TRANSPARENT);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public interface inSortFinishInterface {
        public void finishInterface(ListView listView, List<User> list);
    }

}
