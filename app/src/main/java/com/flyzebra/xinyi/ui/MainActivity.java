package com.flyzebra.xinyi.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyzebra.googleui.SlidingTabLayout;
import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.UserInfo;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.utils.DrawerLayoutUtils;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.ResUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by FlyZebra on 2016/3/17.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int AC_REQUESTCODE_01 = 88;
    public UserInfo userInfo;
    public SlidingTabLayout toolBar_stl;
    public String[] fragmentName = {"HomeFragment", "ProductFragment", "BuyFragment", "AboutmeFragment", "SetFragment"};
    public String[] fragmentTitle = {"首页", "商城", "购物车", "我的", "设置"};
    public DrawerLayout mDrawerLayout;
    private Toolbar toolBar;
    private TextView toolBar_title;
    private ImageView toolBar_scan;
    private RelativeLayout toolBar_searth;
    private int[] imageViewResID = {R.id.main_iv_home, R.id.main_iv_poi, R.id.main_iv_buy, R.id.main_iv_user, R.id.main_iv_more};
    private int[] textViewResID = {R.id.main_tv_home, R.id.main_tv_poi, R.id.main_tv_buy, R.id.main_tv_user, R.id.main_tv_more};
    private int[] linearLayoutResID = {R.id.main_ll_home, R.id.main_ll_poi, R.id.main_ll_buy, R.id.main_ll_user, R.id.main_ll_more};
    private int[] imageViewSrcResID_Off = {R.drawable.ic_menu_home, R.drawable.ic_menu_poi, R.drawable.ic_menu_buy, R.drawable.ic_menu_user, R.drawable.ic_menu_more};
    private int[] imageViewSrcResID_On = {R.drawable.ic_menu_home_on, R.drawable.ic_menu_poi_on, R.drawable.ic_menu_buy_on, R.drawable.ic_menu_user_on, R.drawable.ic_menu_more_on};
    private ColorStateList textColer_Off;
    private int textColor_On;
    private List<ImageView> iv_list = new ArrayList<>();
    private List<TextView> tv_list = new ArrayList<>();
    private List<LinearLayout> ll_list = new ArrayList<>();

    private int cerrent_fragment = 0;
    private boolean isOpen_left = false;
    private boolean exitstate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textColer_Off = ResUtils.getColorStateList(this, R.color.home_select);
        textColor_On = ResUtils.getColor(this, R.color.menu_select_on);

        initToolbar(this);
        initView(this);

        //恢复保存的状态，保存的数据为当前打开的是那个页面(fragment)，按钮选中状态
        if (savedInstanceState != null) {
            
            cerrent_fragment = savedInstanceState.getInt("cerrent_fragment");
            for (int i = 0; i < ll_list.size(); i++) {
                if (cerrent_fragment == i) {
                    iv_list.get(i).setImageResource(imageViewSrcResID_On[i]);
                    tv_list.get(i).setTextColor(textColor_On);
                } else {
                    iv_list.get(i).setImageResource(imageViewSrcResID_Off[i]);
                    tv_list.get(i).setTextColor(textColer_Off);
                }
            }
            isOpen_left = savedInstanceState.getBoolean("mDrawerLayoutisopen", mDrawerLayout.isDrawerOpen(Gravity.LEFT));
        }

        //获取用户信息
        Intent intent = getIntent();
        if (intent != null) {
            userInfo = (UserInfo) intent.getSerializableExtra("UserInfo");
        }

        iHttp.getString("https://api.github.com/", HTTPTAG, new IHttp.HttpResult() {
            @Override
            public void succeed(Object object) {
                FlyLog.i("FFFFhttps://api.github.com/"+object.toString());
            }

            @Override
            public void failed(Object object) {

            }
        });

        //DrawerLayout
        DrawerLayoutUtils.setDrawerLeftEdgeSize(this, mDrawerLayout, 0.1f);

        //设置阴影
        mDrawerLayout.setScrimColor(ResUtils.getColor(this, R.color.drawerscrimColor));
//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolBar, R.string.abc_action_bar_home_description, R.string.abc_action_bar_home_description_format);
//        mDrawerToggle.syncState();
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.addDrawerListener(new DrawerLayoutUtils().getLeftDrawerListener(mDrawerLayout));
        ReplaceFragment("LeftMenuFragment", R.id.left_drawer_menu);
        ReplaceFragment(fragmentName[cerrent_fragment], R.id.main_fl_01);
    }

    private void initToolbar(Context context) {
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        toolBar_title = (TextView) toolBar.findViewById(R.id.toobar_tv_title);
        toolBar_searth = (RelativeLayout) findViewById(R.id.toolbar_rl_searth);
        toolBar_stl = (SlidingTabLayout) findViewById(R.id.toolbar_stl_01);
        toolBar_scan = (ImageView) findViewById(R.id.toolbar_iv_scan);
        toolBar_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanRQCodeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("cerrent_fragment", cerrent_fragment);
        outState.putBoolean("mDrawerLayoutisopen", mDrawerLayout.isDrawerOpen(Gravity.LEFT));
        super.onSaveInstanceState(outState);
    }

    private void initView(Context context) {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_dl_01);
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
                cerrent_fragment = i;
            } else {
                iv_list.get(i).setImageResource(imageViewSrcResID_Off[i]);
                tv_list.get(i).setTextColor(textColer_Off);
            }
        }
    }

    public boolean ReplaceFragment(String classname, int resId) {
        try {
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            Class<?> c1 = Class.forName(MainActivity.class.getPackage().getName() + "." + classname);
            Fragment fragmentRe = (Fragment) c1.newInstance();
            if (fragmentName[cerrent_fragment].equals(classname)) {
                Class<?> c2 = Class.forName(MainActivity.class.getPackage().getName() + "." + fragmentName[cerrent_fragment]);
                Fragment fragmentRm = (Fragment) c2.newInstance();
                transaction1.remove(fragmentRm);
            }
            transaction1.replace(resId, fragmentRe, classname);
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_exit:
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        return super.onCreateOptionsMenu(menu);
//    }


    public void setToolbar(int index) {
        toolBar_title.setText(fragmentTitle[index]);
        switch (index) {
            case 0:
                toolBar_stl.setVisibility(View.GONE);
                toolBar_searth.setVisibility(View.VISIBLE);
                toolBar_title.setVisibility(View.VISIBLE);
                toolBar_scan.setVisibility(View.VISIBLE);
                break;
            case 1:
                toolBar_stl.setVisibility(View.VISIBLE);
                toolBar_searth.setVisibility(View.GONE);
                toolBar_title.setVisibility(View.GONE);
                toolBar_scan.setVisibility(View.GONE);
                break;
            case 2:
                toolBar_stl.setVisibility(View.GONE);
                toolBar_searth.setVisibility(View.GONE);
                toolBar_title.setVisibility(View.VISIBLE);
                toolBar_scan.setVisibility(View.GONE);
                break;
            case 3:
                toolBar_stl.setVisibility(View.GONE);
                toolBar_searth.setVisibility(View.GONE);
                toolBar_title.setVisibility(View.VISIBLE);
                toolBar_scan.setVisibility(View.GONE);
                break;
            case 4:
                toolBar_stl.setVisibility(View.GONE);
                toolBar_searth.setVisibility(View.GONE);
                toolBar_title.setVisibility(View.VISIBLE);
                toolBar_scan.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onStart() {
        FlyLog.i("<MainActivity>onStart.");
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FlyLog.i("<MainActivity>onResume:mDrawerLayout is Open=" + mDrawerLayout.isDrawerOpen(Gravity.LEFT));
        if (isOpen_left) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == AC_REQUESTCODE_01) {
            if (resultCode == RESULT_OK) {
                String text = intent.getStringExtra("CODETEXT");
                Bitmap bm = intent.getParcelableExtra("BITMAP");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (exitstate) {
            exitstate = false;
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "再按一次退出!", Toast.LENGTH_SHORT).show();
            exitstate = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    exitstate = false;
                }
            }).start();
        }
    }
}
