package com.flyzebra.xinyi.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.utils.DrawerLayoutUtils;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by FlyZebra on 2016/3/17.
 */
public class MainActitity extends AppCompatActivity {
    public static final String TAG = "com.flyzebra";

    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ReplaceFragment("HomeFragment", R.id.main_fl_01);
        ReplaceFragment("LeftMenuFragment", R.id.left_drawer_menu);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_dl_01);
        DrawerLayoutUtils.setDrawerLeftEdgeSize(this, mDrawerLayout, 0.1f);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View left_menu = mDrawerLayout.getChildAt(0);
//                float scale = 1 - slideOffset;
//                float rightScale = 0.8f + scale * 0.2f;
//                float leftScale = 1 - 0.3f * scale;
//                ViewHelper.setScaleX(drawerView, leftScale);
//                ViewHelper.setScaleY(drawerView, leftScale);
//                ViewHelper.setAlpha(drawerView, 0.6f + 0.4f * (1 - scale));
                ViewHelper.setTranslationX(left_menu,drawerView.getMeasuredWidth() * slideOffset);
                ViewHelper.setPivotX(left_menu,0);
                ViewHelper.setPivotY(left_menu,left_menu.getMeasuredHeight() / 2);
                left_menu.invalidate();
//                left_menu.setScaleX(rightScale);
//                left_menu.setScaleY(rightScale);
//                TranslateAnimation tAnim;
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
        });
    }

    public boolean ReplaceFragment(String classname, int resId) {
        try {
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            Class<?> c = Class.forName("com.flyzebra.xinyi.ui." + classname);
            Fragment fragment = (Fragment) c.newInstance();
            transaction1.replace(resId, fragment);
            transaction1.commit();
            return true;
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "ReplaceFragment->" + e.toString());
            return false;
        } catch (InstantiationException e) {
            Log.e(TAG, "ReplaceFragment->" + e.toString());
            return false;
        } catch (IllegalAccessException e) {
            Log.e(TAG, "ReplaceFragment->" + e.toString());
            return false;
        }
    }
}
