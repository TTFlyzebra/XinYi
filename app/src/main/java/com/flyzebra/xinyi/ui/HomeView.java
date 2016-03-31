package com.flyzebra.xinyi.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.TestHttp;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.flyzebra.xinyi.view.GridViewForScrollView;
import com.flyzebra.xinyi.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 主页
 * Created by FlyZebra on 2016/2/29.
 */
public class HomeView extends Fragment {
    private static final String TAG = "com.flyzebra";
    public static int current_viewpager = 0;//需要在HomeViewPagerAdapter中使用所在定义成静态
    //ViewPager自动轮播
    private final int delayMillis = 5000;
    private IHttp iHttp = MyVolley.getInstance();
    //ViewPage List;Key字包含图片名字=name，图片路径=path
    private List<Map<String, Object>> viewPager_list;
    private List<Map<String, Object>> gridview_list;
    private List<Map<String, Object>> listview_list;
    //控件定义
    private GridViewForScrollView gridview;
    private TvIvAdapter homeGridViewAdapter;
    private TvIvAdapter homeListViewAdapter;
    private ScrollView sv;
    private ListViewForScrollView listview;
    private LinearLayout list_empty;
    private ImageView list_empty_iv;
    //Toolbar
    private Toolbar mToolbar;
    private MainActitity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActitity) getActivity();
        activity.toolBar.setTitle("主页");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //toolbar
//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        // 标题的文字需在setSupportActionBar之前，不然会无效
//        mToolbar.setTitle("主页");
//        mToolbar.setLogo(R.drawable.ic_love);
//        activity.setSupportActionBar(mToolbar);
//        activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        //--GirdView处理部分
        gridview_list = TestHttp.getHotsellsList(); //从HTTP服务器获取GridView显示的数据内容
        gridview = (GridViewForScrollView) view.findViewById(R.id.home_gv_home);
        homeGridViewAdapter = new TvIvAdapter(activity, gridview_list, R.layout.home_gridview,
                new int[]{R.id.tv01, R.id.tv02},
                new String[]{"name", "price"},
                new int[]{R.id.iv01},
                new String[]{"imagepath"},
                null, new TvIvAdapter.SetImageView() {
            @Override
            public void setImageView(String url, ImageView iv) {
                iHttp.upImageView(activity, url, iv);
            }
        });
        gridview.setAdapter(homeGridViewAdapter);

        //--ListView处理部分
        if (listview_list == null) {
            listview_list = new ArrayList<Map<String, Object>>();
        } else {
            listview_list.clear();
        }
        listview = (ListViewForScrollView) view.findViewById(R.id.home_lv_home);
        homeListViewAdapter = new TvIvAdapter(activity, listview_list, R.layout.home_listview_item,
                new int[]{R.id.tv01, R.id.tv02},
                new String[]{"mealname", "mealprice"},
                new int[]{R.id.iv01},
                new String[]{"mealimage"},
                null, new TvIvAdapter.SetImageView() {
            @Override
            public void setImageView(String url, ImageView iv) {
                iHttp.upImageView(activity, "http://192.168.1.88/ordermeal" + url, iv);
            }
        });
        listview.setAdapter(homeListViewAdapter);
        iHttp.upListView("http://192.168.1.88/ordermeal/table.jsp?get=mealinfo", listview, "mealinfo", "HomeView");

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
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        iHttp.cancelAll("HomeView");
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
