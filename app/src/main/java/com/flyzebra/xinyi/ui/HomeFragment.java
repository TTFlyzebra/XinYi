package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.IHttp;
import com.flyzebra.xinyi.model.TestHttp;
import com.flyzebra.xinyi.view.IChildView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.Map;

/**
 * 主页
 * Created by FlyZebra on 2016/2/29.
 */
public class HomeFragment extends Fragment {
    //ViewPager自动轮播
    private final int delayMillis = 5000;
    private IHttp iHttp = MyHttp.getInstance();
    private String HTTPTAG = "Fragment" + Math.random();
    //ViewPage List;Key字包含图片名字=name，图片路径=path
    private Toolbar mToolbar;
    private MainActitity activity;

    private PullToRefreshScrollView view;
    private LinearLayout childParent;
    private IChildView childViewPager;
    private IChildView childViewPager1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActitity) getActivity();
        activity.toolBar.setTitle("主页");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (PullToRefreshScrollView) inflater.inflate(R.layout.home_fragment, container, false);
        //上拉刷新
        view.setMode(PullToRefreshBase.Mode.BOTH);
        view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                childViewPager.addData(TestHttp.getViewPagerList());
                childViewPager1.addData(TestHttp.getViewPagerList());
                view.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                childViewPager.setData(new ArrayList<Map<String, Object>>());
                childViewPager1.setData(new ArrayList<Map<String, Object>>());
                view.onRefreshComplete();
            }
        });
        childParent = (LinearLayout) view.findViewById(R.id.home_root);
        //添加子窗口View
        childViewPager = new ViewPagerChildView(activity);
        childViewPager.setData(TestHttp.getViewPagerList());
        childViewPager1 = (IChildView) view.findViewById(R.id.viewpager);
        childViewPager1.setData(TestHttp.getViewPagerList());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        iHttp.cancelAll(HTTPTAG);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
}
