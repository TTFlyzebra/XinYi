package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.UserInfo;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.flyzebra.xinyi.utils.DrawerLayoutUtils;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.ResUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FlyZebra on 2016/3/17.
 */
public class MainActitity extends AppCompatActivity implements View.OnClickListener {
    public IHttp iHttp = MyVolley.getInstance();
    public UserInfo userInfo;

    public Toolbar toolBar;
    private DrawerLayout mDrawerLayout;

    private int[] imageViewResID = {R.id.main_iv_home, R.id.main_iv_poi, R.id.main_iv_buy, R.id.main_iv_user, R.id.main_iv_more};
    private int[] textViewResID = {R.id.main_tv_home, R.id.main_tv_poi, R.id.main_tv_buy, R.id.main_tv_user, R.id.main_tv_more};
    private int[] linearLayoutResID = {R.id.main_ll_home, R.id.main_ll_poi, R.id.main_ll_buy, R.id.main_ll_user, R.id.main_ll_more};

    private int[] imageViewSrcResID_Off = {R.drawable.ic_menu_home, R.drawable.ic_menu_poi, R.drawable.ic_menu_buy, R.drawable.ic_menu_user, R.drawable.ic_menu_more};
    private int[] imageViewSrcResID_On = {R.drawable.ic_menu_home_on, R.drawable.ic_menu_poi_on, R.drawable.ic_menu_buy_on, R.drawable.ic_menu_user_on, R.drawable.ic_menu_more_on};
    private ColorStateList textColer_Off;
    private int textColor_On;

    private String[] fragmentName = {"HomeFragment", "PoiFragment", "BuyFragment", "HomeFragment", "BuyFragment"};

    private List<ImageView> iv_list = new ArrayList<>();
    private List<TextView> tv_list = new ArrayList<>();
    private List<LinearLayout> ll_list = new ArrayList<>();
    private ActivityOnTounEvent activityOnTounEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        textColer_Off = ResUtils.getColorStateList(this, R.color.color_select);
        textColor_On = ResUtils.getColor(this, R.color.menu_select_on);
        initView();
        //获取用户信息
        Intent intent = getIntent();
        if (intent != null) {
            userInfo = (UserInfo) intent.getSerializableExtra("UserInfo");
        }

        //DrawerLayout
        DrawerLayoutUtils.setDrawerLeftEdgeSize(this, mDrawerLayout, 0.1f);
        mDrawerLayout.setScrimColor(ResUtils.getColor(this, R.color.drawerscrimColor));
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolBar, R.string.abc_action_bar_home_description, R.string.abc_action_bar_home_description_format);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.addDrawerListener(new DrawerLayoutUtils().getLeftDrawerListener(mDrawerLayout));
        ReplaceFragment("LeftMenuFragment", R.id.left_drawer_menu);
        ReplaceFragment(fragmentName[0], R.id.main_fl_01);
    }

    public void setActivityOnTounEvent(ActivityOnTounEvent activityOnTounEvent) {
        this.activityOnTounEvent = activityOnTounEvent;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
//                FlyLog.i("<dispatchTouchEvent ACTION_MOVE");
                break;
        }
//        activityOnTounEvent.onTount(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_dl_01);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //底部导航栏
        for (int ResId : textViewResID) {
            tv_list.add((TextView) findViewById(ResId));
        }
        for (int ResId : imageViewResID) {
            iv_list.add((ImageView) findViewById(ResId));
        }
        for (int ResId : linearLayoutResID) {
            ll_list.add((LinearLayout) findViewById(ResId));
        }
        for (LinearLayout layout : ll_list) {
            layout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int ResId = v.getId();
        for (int i = 0; i < ll_list.size(); i++) {
            if (ResId == linearLayoutResID[i]) {
                iv_list.get(i).setImageResource(imageViewSrcResID_On[i]);
                tv_list.get(i).setTextColor(textColor_On);
                ReplaceFragment(fragmentName[i], R.id.main_fl_01);
            } else {
                iv_list.get(i).setImageResource(imageViewSrcResID_Off[i]);
                tv_list.get(i).setTextColor(textColer_Off);
            }
        }
    }

    public boolean ReplaceFragment(String classname, int resId) {
        try {
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            Class<?> c = Class.forName(MainActitity.class.getPackage().getName() + "." + classname);
            Fragment fragment = (Fragment) c.newInstance();
            transaction1.replace(resId, fragment);
            transaction1.commit();
            return true;
        } catch (ClassNotFoundException e) {
            FlyLog.i("ReplaceFragment->" + e.toString());
            return false;
        } catch (InstantiationException e) {
            FlyLog.i("ReplaceFragment->" + e.toString());
            return false;
        } catch (IllegalAccessException e) {
            FlyLog.i("ReplaceFragment->" + e.toString());
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public interface ActivityOnTounEvent {
        void onTount(MotionEvent ev);
    }
}
