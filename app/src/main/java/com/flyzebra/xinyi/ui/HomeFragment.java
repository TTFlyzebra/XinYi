package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页
 * Created by FlyZebra on 2016/2/29.
 */
public class HomeFragment extends BaseFragment implements IHttp.HttpResult {
    private List RLList;
    private List homeShopsList;//ViewPager商店展示
    private List homeHotsList;//首页热销产品推荐
    private List homeNewsList;//新品上架
    private List homeTimeShopList;//限时抢购
    private MainActivity activity;
    private View view;
    private RefreshRecyclerView recyclerView;
    private HomeRLAdapter mAdapter;

    private int httpNum = 0;

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
                httpNum = 4;
                iHttp.upRecyclerViewData(Constant.URL_HHS, homeShopsList, mAdapter, HTTPTAG, HomeFragment.this);
                iHttp.upRecyclerViewData(Constant.URL_HPR, homeHotsList, mAdapter, HTTPTAG, HomeFragment.this);
                iHttp.upRecyclerViewData(Constant.URL_HPR, homeNewsList, mAdapter, HTTPTAG, HomeFragment.this);
                iHttp.upRecyclerViewData(Constant.URL_HPR, homeTimeShopList, mAdapter, HTTPTAG, HomeFragment.this);
            }
        });

        recyclerView.setListenerLastItem(new RefreshRecyclerView.ListenerLastItem() {
            @Override
            public void onLastItem() {
//                iHttp.upListView(Constant.URL_TABLE_1, mAdapter, "mealinfo", true, HTTPTAG);
            }
        });

        homeShopsList = iHttp.readListFromCache(Constant.URL_HHS);
        if (homeShopsList == null) {
            homeShopsList = new ArrayList<Map<String, Object>>();
        }
        Map shops = new HashMap();
        shops.put(IAdapter.DATA, homeShopsList);
        shops.put(IAdapter.TYPE, IAdapter.H_VIEWPAGER_SHOP);
        RLList.add(shops);
        //更新数据
        iHttp.upRecyclerViewData(Constant.URL_HHS, homeShopsList, mAdapter, HTTPTAG);

        homeHotsList = iHttp.readListFromCache(Constant.URL_HPR);
        if (homeHotsList == null) {
            homeHotsList = new ArrayList();
        }
        Map hots = new HashMap();
        hots.put(IAdapter.DATA, homeHotsList);
        hots.put(IAdapter.TYPE, IAdapter.H_GRIDVIEW_HOTS);
        RLList.add(hots);
        //更新数据
        iHttp.upRecyclerViewData(Constant.URL_HPR, homeHotsList, mAdapter, HTTPTAG);


        homeNewsList = iHttp.readListFromCache(Constant.URL_HPR);
        if (homeNewsList == null) {
            homeNewsList = new ArrayList();
        }
        Map news = new HashMap();
        news.put(IAdapter.DATA, homeNewsList);
        news.put(IAdapter.TYPE, IAdapter.H_GRIDVIEW_NEWS);
        RLList.add(news);
        //更新数据
        iHttp.upRecyclerViewData(Constant.URL_HPR, homeNewsList, mAdapter, HTTPTAG);

        homeTimeShopList = iHttp.readListFromCache(Constant.URL_HPR);
        if (homeTimeShopList == null) {
            homeTimeShopList = new ArrayList();
        }
        Map timeShop = new HashMap();
        timeShop.put(IAdapter.DATA, homeTimeShopList);
        timeShop.put(IAdapter.TYPE, IAdapter.H_GRIDVIEW_TIMESHOP);
        RLList.add(timeShop);
        //更新数据
        iHttp.upRecyclerViewData(Constant.URL_HPR, homeTimeShopList, mAdapter, HTTPTAG);

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

    @Override
    public void succeed(Object object) {
        httpNum--;
        if (httpNum <= 0) {
            recyclerView.refreshSuccess();
        }
    }

    @Override
    public void failed(Object object) {
        httpNum--;
        if (httpNum <= 0) {
            recyclerView.refreshFailed();
        }
    }
}
