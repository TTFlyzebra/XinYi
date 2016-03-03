package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.flyzebra.xinyi.MyApp;
import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.adapter.TvIvAdapter;
import com.flyzebra.xinyi.data.HttpUtils;
import com.flyzebra.xinyi.data.ImageUtils;
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
public class HomeAcitivy extends BaseActivity {
    //ViewPage List;Key字包含图片名字=name，图片路径=path
    private List<Map<String, Object>> viewPager_list;
    private List<Map<String, Object>> gridview_list;
    private List<Map<String, Object>> listview_list;

    //控件定义
    private AutoSizeWithChildViewPager viewPager;
    private HomeViewPagerAdapter mViewPagerAdapter;
    private CountItemForViewPager countItemForViewPager;
    private GridViewForScrollView gridview;
    private TvIvAdapter homeGridViewAdapter;
    private TvIvAdapter homeListViewAdapter;
    private ScrollView sv;
    private ListViewForScrollView listview;

    //ViewPager自动轮播
    private final int delayMillis = 5000;
    public static int current_viewpager = 0;//需要在HomeViewPagerAdapter中使用所在定义成静态
    private Handler playsHander = new Handler(); 
    private Runnable playsTask = new Runnable(){
        @Override
        public void run() {
            current_viewpager++;
            if(current_viewpager>=viewPager_list.size()){
                current_viewpager=0;
            }
            viewPager.setCurrentItem(current_viewpager);
            playsHander.postDelayed(playsTask, delayMillis);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        base_bt_01.setImageResource(R.drawable.ic_menu_deal_on);
        base_tv_01.setTextColor(getResources().getColor(R.color.menu_select_on));
    }


    @Override
    public void onCreateAndaddView(LinearLayout root) {
        LayoutInflater lf = LayoutInflater.from(this);
        LinearLayout view = (LinearLayout) lf.inflate(R.layout.home_view, null);
        root.addView(view);

        //--ViewPager处理部分
        viewPager_list = HttpUtils.getViewPagerList(); //获取ViewPager显示的数据内容
        viewPager = (AutoSizeWithChildViewPager) view.findViewById(R.id.home_viewpager);
        //自定义ViewPager导航条
        countItemForViewPager = (CountItemForViewPager) view.findViewById(R.id.home_civp);
        countItemForViewPager.setSumItem(viewPager_list.size());

        mViewPagerAdapter = new HomeViewPagerAdapter(this,viewPager_list,countItemForViewPager);
        viewPager.setAdapter(mViewPagerAdapter);

        //--GirdView处理部分
        gridview_list = HttpUtils.getHotsellsList(); //从HTTP服务器获取GridView显示的数据内容
        gridview = (GridViewForScrollView) view.findViewById(R.id.home_gv_01);
        homeGridViewAdapter = new TvIvAdapter(this,gridview_list,R.layout.home_gridview,
                new int[]{R.id.tv01,R.id.tv02},
                new String[]{"name","price"},
                new int[]{R.id.iv01},
                new String[]{"imagepath"},
                null,new TvIvAdapter.SetImageView(){
            @Override
            public void setImageView(String url, ImageView iv) {
                ImageUtils.ShowImageView(url, iv);
            }
        });
        gridview.setAdapter(homeGridViewAdapter);

        //--ListView处理部分
        if(listview_list==null){
            listview_list = new ArrayList<Map<String, Object>>();
        }
        listview = (ListViewForScrollView) view.findViewById(R.id.home_lv_01);
        homeListViewAdapter = new TvIvAdapter(this,listview_list,R.layout.home_listview,
                new int[]{R.id.tv01,R.id.tv02},
                new String[]{"mealname","mealprice"},
                new int[]{R.id.iv01},
                new String[]{"mealimage"},
                null,new TvIvAdapter.SetImageView(){
            @Override
            public void setImageView(String url, ImageView iv) {
//                ImageUtils.ShowImageView("http://192.168.1.88/ordermeal" + url, iv);
            }
        });
        listview.setAdapter(homeListViewAdapter);
//        HttpUtils.upAdapter("http://192.168.1.88/ordermeal/table.jsp?get=mealinfo", listview_list, homeListViewAdapter);

        //处理滚动条，默认会滚动到底部
        sv = (ScrollView) findViewById(R.id.home_sv_01);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(MyApp.home_sv_x, MyApp.home_sv_y);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启ViewPager轮播
        playsHander.postDelayed(playsTask, delayMillis);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApp.home_sv_x = sv.getScrollX();
        MyApp.home_sv_y = sv.getScrollY();
        playsHander.removeCallbacks(playsTask);
    }
}
