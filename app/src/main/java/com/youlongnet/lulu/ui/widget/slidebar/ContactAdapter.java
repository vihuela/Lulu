package com.youlongnet.lulu.ui.widget.slidebar;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.youlongnet.lulu.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<User> implements SectionIndexer {

    private Context context;
    private int res;
    private List<User> userList;
    private List<String> list;//去存放对应的头字母,就是说第一哥字母
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;

    public ContactAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res = resource;
        this.userList = objects;
    }

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        list = new ArrayList<String>();
        list.add("#");
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        int count = getCount();
        for (int i = 1; i < count; i++) {//对应的头
            String letter = getItem(i).getHeader();
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);
    }

    /* 利用索引得到位置
     getPositionForSection(1) 返回3
     getPositionForSection(2) 返回6
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        return positionOfSection.get(sectionIndex);
    }

    /*利用位置得到索引
     getSectionForPosition(2) 返回 0
     getSectionForPosition(3) 返回 1
     */
    @Override
    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public User getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, res, null);
            holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.unreadMsgView = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.nameTextview = (TextView) convertView.findViewById(R.id.name);
            holder.tvHeader = (TextView) convertView.findViewById(R.id.header);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        User user = getItem(position);
        if (user == null)
            Log.d("ContactAdapter", position + "");
        String username = user.getName();//这里我改成了得到名称
        String header = user.getHeader();
        //这里就是去判断前一个头和当前头是否相识
        if (position == 0 || header != null && !header.equals(getItem(position - 1).getHeader())) {//不同
            if ("".equals(header)) {//头是空
                holder.tvHeader.setVisibility(View.GONE);
            } else {
                holder.tvHeader.setVisibility(View.VISIBLE);
                holder.tvHeader.setText(header);
            }
        } else {//相同
            holder.tvHeader.setVisibility(View.GONE);
        }
        holder.nameTextview.setText(username);
        //到时候要处理图片就在这里获得网络请求的图片
        holder.avatar.setImageResource(R.drawable.sl_default_avatar);
        return convertView;
    }

    class ViewHolder {
        public ImageView avatar;
        public TextView unreadMsgView;
        public TextView nameTextview;
        public TextView tvHeader;
    }

}
