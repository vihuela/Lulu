package com.youlongnet.lulu.ui.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 侧边栏的统一加载器
 *
 * @author lyao
 */
public class DrawerLayoutInstaller {

    public static final int DEFAULT_LEFT_DRAWER_WIDTH_DP = 240;

    public static DrawerBuilder from(Activity activity) {
        return new DrawerBuilder(activity);
    }

    public static class DrawerBuilder {

        private Activity activity;
        private int drawerRootResId;
        private Toolbar toolbar;
        private View drawerLeftView;
        private int drawerLeftWidth;
        private ActionBarDrawerToggle mDrawerToggle;

        public DrawerBuilder(Activity activity) {
            this.activity = activity;
        }

        @SuppressWarnings("all")
        private DrawerBuilder() {
            throw new RuntimeException("Not supported. Use DrawerBuilder(Activity activity) instead.");
        }

        /**
         * 侧边栏rootView
         */
        public DrawerBuilder drawerRoot(int drawerRootResId) {
            this.drawerRootResId = drawerRootResId;
            return this;
        }

        /**
         * 绑定侧边栏，并添加导航指示器
         */
        public DrawerBuilder withNavigationIconToggler(Toolbar toolbar) {
            this.toolbar = toolbar;
            return this;
        }

        /**
         * 侧边侧栏
         */
        public DrawerBuilder drawerLeftView(View drawerLeftView) {
            this.drawerLeftView = drawerLeftView;
            return this;
        }

        /**
         * 侧边栏宽度(dip)
         */
        public DrawerBuilder drawerLeftWidth(int width) {
            this.drawerLeftWidth = width;
            return this;
        }

        public DrawerLayout build() {
            DrawerLayout drawerLayout = createDrawerLayout();
            addDrawerToActivity(drawerLayout);
            setupToggler(drawerLayout);
            setupDrawerLeftView(drawerLayout);
            return drawerLayout;
        }

        private DrawerLayout createDrawerLayout() {
            if (drawerRootResId != 0) {
                return (DrawerLayout) LayoutInflater.from(activity).inflate(drawerRootResId, null);
            } else {
                DrawerLayout drawerLayout = new DrawerLayout(activity);

                /*内容View*/
                FrameLayout contentView = new FrameLayout(activity);
                drawerLayout.addView(contentView, new DrawerLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                /*左侧边栏*/
                FrameLayout leftDrawer = new FrameLayout(activity);

                int drawerWidth = drawerLeftWidth != 0 ? drawerLeftWidth : DEFAULT_LEFT_DRAWER_WIDTH_DP;

                final ViewGroup.LayoutParams leftDrawerParams = new DrawerLayout.LayoutParams(
                        /*px->dip*/
                        (int) (drawerWidth * Resources.getSystem().getDisplayMetrics().density),
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Gravity.START
                );
                drawerLayout.addView(leftDrawer, leftDrawerParams);
                return drawerLayout;
            }
        }

        /**
         * 先从activity的根视图移除activity布局，添加activity布局到侧边栏主布局中，然后在将侧边栏添加到根视图
         *
         * @param 侧边栏
         */
        private void addDrawerToActivity(DrawerLayout drawerLayout) {
            /*获取当前activity的根视图*/
            ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
            /*获取侧边栏内容布局*/
            ViewGroup drawerContentRoot = (ViewGroup) drawerLayout.getChildAt(0);
            /*获取activity内容区域的可视布局，即activity的布局的根布局(etc.FrameLayout)*/
            View contentView = rootView.getChildAt(0);
            /*先移除activity当前布局*/
            rootView.removeView(contentView);
            /*将activity根布局添加到侧边栏的主布局*/
            drawerContentRoot.addView(contentView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            /*将侧边栏添加到根视图*/
            rootView.addView(drawerLayout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
        }

        private void setupToggler(final DrawerLayout drawerLayout) {
            if (toolbar != null) {
                // 创建返回键，并实现打开关/闭监听 需要在Drawer生成之后才有效
                mDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, 0, 0) {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }
                };
                //显示切换开关，否则显示默认箭头
                mDrawerToggle.syncState();
                drawerLayout.setDrawerListener(mDrawerToggle);
            }
        }

        /*使用Drawerlayout 的侧边栏布局添加侧边栏*/
        private void setupDrawerLeftView(DrawerLayout drawerLayout) {
            if (drawerLeftView != null) {
                ((ViewGroup) drawerLayout.getChildAt(1))
                        .addView(drawerLeftView, new DrawerLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        ));
            }
        }
    }
}