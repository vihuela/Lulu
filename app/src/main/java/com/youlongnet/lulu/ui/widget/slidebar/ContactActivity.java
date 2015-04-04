package com.youlongnet.lulu.ui.widget.slidebar;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.base.BaseActivity;
import com.youlongnet.lulu.ui.manager.TopManager;
import com.youlongnet.lulu.ui.widget.slidebar.Sidebar.inSortFinishInterface;

import java.util.List;

import butterknife.InjectView;

/**
 * 注意,网络图片需要单独进行处理；对应数据需要指定
 */
public class ContactActivity extends BaseActivity implements inSortFinishInterface {

    private static String[] names = new String[]{
            "123", "#13231", "adfger", "adfafsf", "a", "a", "zz", "在中间", "紫色方法", "dfgdfg", "23456",
            "sdf", "dfgdfg", "是大sd法官", "给对方", "吃饭的人", "热水", "合同", "而外", "电饭锅", "sdfgsf",
            "违反规划", "tyyugh", "是否呜呜呜", "未发生", "啊这种发", "而特特", "jghjg", "形成vb", "范围", "爱的",
            "而然地方", "dfgerg", "dfetyedf", "我而去", "啊啊啊的", "ewerf", "regergeg", "发", "是的", "个",
            "于预定", "dfgerg", "雾非雾水电费", "玩儿玩儿", "dfgergs", "文房四士", "委屈委屈", "文房四士", "为范围发生", "无法违法",
            "魏文峰", "我前期任务", "圣斗士星矢", "委曲求全", "ergdfg", "有任务", "曾多次到位", "全球最", "dfgerweww", "wef我",
            "水电费官方", "为范围", "的风格", "受欺负", "而他的", "会飞的", "ergaedf", "额滴神", "范玮琪", "违法哇"};
    @InjectView(R.id.main_contain)
    LinearLayout mContain;
    private ListView listView;
    private Sidebar sidebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_contact);
        TopManager.getInstance().changeUi(mContain, TopManager.UI_Default);
        listView = getView(R.id.list);
        sidebar = getView(R.id.sidebar);
        sidebar.setListView(listView, names, this);//这里的数据要去换

    }

    @Override
    protected void onResume() {
        super.onResume();
        TopManager.getInstance().getCurrentToolBar(mContext).setTitle("联系人检索");
    }

    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    @Override
    public void finishInterface(ListView listView, List<User> list) {

    }


}
