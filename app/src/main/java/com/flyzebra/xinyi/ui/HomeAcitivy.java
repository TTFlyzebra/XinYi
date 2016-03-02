package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.flyzebra.xinyi.MyApp;
import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.http.HttpGetData;
import com.flyzebra.xinyi.view.AutoSizeWithChildViewPager;
import com.flyzebra.xinyi.view.CountItemForViewPager;
import com.flyzebra.xinyi.view.GridViewForScrollView;

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

    //控件定义
    private AutoSizeWithChildViewPager viewPager;
    private HomeViewPagerAdapter mViewPagerAdapter;
    private CountItemForViewPager countItemForViewPager;
    private GridViewForScrollView gridview;
    private HomeGridViewAdapter homeGridViewAdapter;
    private ScrollView sv;

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
        viewPager_list = HttpGetData.getViewPagerList(); //获取ViewPager显示的数据内容
        viewPager = (AutoSizeWithChildViewPager) view.findViewById(R.id.home_viewpager);
        //自定义ViewPager导航条
        countItemForViewPager = (CountItemForViewPager) view.findViewById(R.id.home_civp);
        countItemForViewPager.setSumItem(viewPager_list.size());

        mViewPagerAdapter = new HomeViewPagerAdapter(this,viewPager_list,countItemForViewPager);
        viewPager.setAdapter(mViewPagerAdapter);

        //--GirdView处理部分
        gridview_list = HttpGetData.getHotsellsList(); //从HTTP服务器获取GridView显示的数据内容
        gridview = (GridViewForScrollView) view.findViewById(R.id.home_gv_01);
        homeGridViewAdapter = new HomeGridViewAdapter(this,gridview_list,R.layout.home_gridview);
        gridview.setAdapter(homeGridViewAdapter);
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
        //轮播
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
