package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页
 * Created by FlyZebra on 2016/2/29.
 */
public class HomeFragment extends BaseFragment {
    //ViewPager自动轮播

    private static List RLList;
    private static List HomeShopsList;
    private static List homeHotsList;
    private static List homeNewsList;
    private MainActivity activity;
    private View view;
    private RefreshRecyclerView recyclerView;
    private HomeRLAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = (RefreshRecyclerView) view.findViewById(R.id.home_lv_01);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        if (RLList == null) {
            RLList = new ArrayList<Map<String, Object>>();
        }
        mAdapter = new HomeRLAdapter(activity, RLList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        //下拉刷新
        recyclerView.setListenerTopRefresh(new RefreshRecyclerView.ListenerTopRefresh() {
            @Override
            public void onRefrsh(View view) {
                iHttp.upMultiRLData(Constant.URL_HS, HomeShopsList, mAdapter, HTTPTAG);
                iHttp.upMultiRLData(Constant.URL_PR, homeHotsList, mAdapter, HTTPTAG);
                iHttp.upMultiRLData(Constant.URL_PR, homeNewsList, mAdapter, HTTPTAG);
                recyclerView.refreshSuccess();
            }
        });

        recyclerView.setListenerLastItem(new RefreshRecyclerView.ListenerLastItem() {
            @Override
            public void onLastItem() {
//                iHttp.upListView(Constant.URL_TABLE_1, mAdapter, "mealinfo", true, HTTPTAG);
            }
        });

        //添加ViewPager//商店展示
        if (HomeShopsList == null) {
            //添加个不存在的图像占位
            HomeShopsList = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<>();
            map.put(IHomeAdapter.SHOP_IMGURL, "");
            map.put(IHomeAdapter.SHOP_NAME, "");
            HomeShopsList.add(map);
            Map shops = new HashMap();
            shops.put(IHomeAdapter.DATA, HomeShopsList);
            shops.put(IHomeAdapter.TYPE, IHomeAdapter.H_VIEWPAGER_SHOP);
            RLList.add(shops);
            //更新数据
            iHttp.upMultiRLData(Constant.URL_HS, HomeShopsList, mAdapter, HTTPTAG);

        }
        //更新数据
        //添加热销产品//产品展示
        if (homeHotsList == null) {
            homeHotsList = new ArrayList();
            Map hots = new HashMap();
            hots.put(IHomeAdapter.DATA, homeHotsList);
            hots.put(IHomeAdapter.TYPE, IHomeAdapter.H_GRIDVIEW_HOTS);
            RLList.add(hots);
            iHttp.upMultiRLData(Constant.URL_PR, homeHotsList, mAdapter, HTTPTAG);
        }


        //更新数据
        //添加热销产品//产品展示
        if (homeNewsList == null) {
            homeNewsList = new ArrayList();
            Map news = new HashMap();
            news.put(IHomeAdapter.DATA, homeNewsList);
            news.put(IHomeAdapter.TYPE, IHomeAdapter.H_GRIDVIEW_NEWS);
            RLList.add(news);
            iHttp.upMultiRLData(Constant.URL_PR, homeNewsList, mAdapter, HTTPTAG);
        }

        //添加最新产品

        //recyclerViewList按类型排序
//        Collections.sort(recyclerViewList, new Comparator<Map<String, Object>>() {
//            @Override
//            public int compare(Map<String, Object> map1, Map<String, Object> map2) {
//                return ((String) map1.get(IAdapter.TYPE)).compareToIgnoreCase((String) map2.get(IAdapter.TYPE));
//            }
//        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setToolbar(0);
    }

}
