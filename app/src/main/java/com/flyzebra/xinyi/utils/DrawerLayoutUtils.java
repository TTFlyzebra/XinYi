package com.flyzebra.xinyi.utils;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.DisplayMetrics;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/3/18.
 */
public class DrawerLayoutUtils {
    /**
     * 设置响应弹出菜单的边界范围
     * @param activity
     * @param drawerLayout
     * @param displayWidthPercentage
     */
    public static void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null)  return;
        try {
            // find ViewDragHelper and set it accessible
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            // find edgesize and set is accessible
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            // set new edgesize
            // Point displaySize = new Point();
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    /**
     * 内容和左侧菜单一起移动
     *
     * @param drawerLayout
     * @return
     */
    public leftDrawerListener getLeftDrawerListener(DrawerLayout drawerLayout) {
        return new leftDrawerListener(drawerLayout);
    }

    /**
     * 内容和左侧菜单一起移动
     */
    public class leftDrawerListener implements DrawerLayout.DrawerListener {
        private DrawerLayout drawerLayout;

        leftDrawerListener(DrawerLayout drawerLayout) {
            this.drawerLayout = drawerLayout;
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            int width = drawerView.getMeasuredWidth();
            View contentView = drawerLayout.getChildAt(0);
            ViewHelper.setTranslationX(contentView, width * slideOffset);
            ViewHelper.setPivotX(contentView, 0);
            contentView.invalidate();
        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }
}
