package com.flyzebra.xinyi.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flyzebra.xinyi.MyApp;
import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.universal.TvIvAdapter;
import com.flyzebra.xinyi.utils.HttpUtils;
import com.flyzebra.xinyi.utils.ImageUtils;
import com.flyzebra.xinyi.view.AutoSizeWithChildViewPager;
import com.flyzebra.xinyi.view.CountItemForViewPager;
import com.flyzebra.xinyi.view.GridViewForScrollView;
import com.flyzebra.xinyi.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 主页
 * Created by FlyZebra on 2016/2/29.
 */
public class HomeFragment extends Fragment {
    //ViewPage List;Key字包含图片名字=name，图片路径=path
    private List<Map<String, Object>> viewPager_list;
    private List<Map<String, Object>> gridview_list;
    private List<Map<String, Object>> listview_list;

    //控件定义
    private AutoSizeWithChildViewPager viewPager;
    private HomeVPAdapter mViewPagerAdapter;
    private CountItemForViewPager countItemForViewPager;
    private GridViewForScrollView gridview;
    private TvIvAdapter homeGridViewAdapter;
    private TvIvAdapter homeListViewAdapter;
    private ScrollView sv;
    private ListViewForScrollView listview;
    private LinearLayout list_empty;
    private ImageView list_empty_iv;

    //ViewPager自动轮播
    private final int delayMillis = 5000;
    public static int current_viewpager = 0;//需要在HomeViewPagerAdapter中使用所在定义成静态
    private Handler playsHander = new Handler();
    private Runnable playsTask = new Runnable() {
        @Override
        public void run() {
            current_viewpager++;
            if (current_viewpager >= viewPager_list.size()) {
                current_viewpager = 0;
            }
            viewPager.setCurrentItem(current_viewpager);
            playsHander.postDelayed(playsTask, delayMillis);
        }
    };

    //Toolbar
    private Toolbar mToolbar;
    private MainActitity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActitity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //toolbar
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        // 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitle("主页");
        mToolbar.setLogo(R.drawable.ic_love);
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        viewPager_list = HttpUtils.getViewPagerList(); //获取ViewPager显示的数据内容
        viewPager = (AutoSizeWithChildViewPager) view.findViewById(R.id.home_viewpager);
        //自定义ViewPager导航条
        countItemForViewPager = (CountItemForViewPager) view.findViewById(R.id.home_civp);
        countItemForViewPager.setSumItem(viewPager_list.size());

        mViewPagerAdapter = new HomeVPAdapter(activity, viewPager_list, countItemForViewPager);
        viewPager.setAdapter(mViewPagerAdapter);

        //--GirdView处理部分
        gridview_list = HttpUtils.getHotsellsList(); //从HTTP服务器获取GridView显示的数据内容
        gridview = (GridViewForScrollView) view.findViewById(R.id.home_gv_home);
        homeGridViewAdapter = new TvIvAdapter(activity, gridview_list, R.layout.home_gridview,
                new int[]{R.id.tv01, R.id.tv02},
                new String[]{"name", "price"},
                new int[]{R.id.iv01},
                new String[]{"imagepath"},
                null, new TvIvAdapter.SetImageView() {
            @Override
            public void setImageView(String url, ImageView iv) {
                ImageUtils.ShowImageView(url, iv);
            }
        });
        gridview.setAdapter(homeGridViewAdapter);

        //--ListView处理部分
        if (listview_list == null) {
            listview_list = new ArrayList<Map<String, Object>>();
        }
        listview = (ListViewForScrollView) view.findViewById(R.id.home_lv_home);
        homeListViewAdapter = new TvIvAdapter(activity, listview_list, R.layout.home_listview,
                new int[]{R.id.tv01, R.id.tv02},
                new String[]{"mealname", "mealprice"},
                new int[]{R.id.iv01},
                new String[]{"mealimage"},
                null, new TvIvAdapter.SetImageView() {
            @Override
            public void setImageView(String url, ImageView iv) {
                ImageUtils.ShowImageView("http://192.168.1.88/ordermeal" + url, iv);
            }
        });
        listview.setAdapter(homeListViewAdapter);

        //设置没有数据时ListView的显示
        list_empty = (LinearLayout) view.findViewById(R.id.empty_view);
        listview.setEmptyView(list_empty);
        list_empty_iv = (ImageView) view.findViewById(R.id.empty_view_iv);
        final AnimationDrawable animationDrawable = (AnimationDrawable) list_empty_iv.getDrawable();
        list_empty_iv.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });

        list_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtils.upAdapter("http://192.168.1.88/ordermeal/table.jsp?get=mealinfo", listview_list, homeListViewAdapter);
            }
        });

        //更新数据
        HttpUtils.upAdapter("http://192.168.1.88/ordermeal/table.jsp?get=mealinfo", listview_list, homeListViewAdapter);

        //处理滚动条，默认会滚动到底部
        sv = (ScrollView) view.findViewById(R.id.home_sv_home);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(MyApp.home_sv_x, MyApp.home_sv_y);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //开启ViewPager轮播
        playsHander.postDelayed(playsTask, delayMillis);
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApp.home_sv_x = sv.getScrollX();
        MyApp.home_sv_y = sv.getScrollY();
        playsHander.removeCallbacks(playsTask);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
}
