package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.URLS;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主页
 * Created by FlyZebra on 2016/2/29.
 */
public class HomeFragment extends BaseFragment implements IHttp.HttpResult {
    @Bind(R.id.home_rv_01)
    RefreshRecyclerView homeRv01;
    @Bind(R.id.newwork_error)
    LinearLayout newworkError;
    private List RLList;
    private List homeShopsList;//ViewPager商店展示
    private List homeHotsList;//首页热销产品推荐
    private List homeNewsList;//新品上架
    private List homeTimeShopList;//限时抢购
    private MainActivity activity;
    private View rootView;
    private HomeRLAdapter mAdapter;

    private int httpNum = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        homeRv01.setLayoutManager(new LinearLayoutManager(activity));
        if (RLList == null) {
            RLList = new ArrayList<Map<String, Object>>();
        }
        mAdapter = new HomeRLAdapter(activity, RLList);
        homeRv01.setAdapter(mAdapter);
        homeRv01.setHasFixedSize(true);
        //下拉刷新
        homeRv01.setListenerTopRefresh(new RefreshRecyclerView.ListenerTopRefresh() {
            @Override
            public void onRefrsh(View view) {
                httpNum = 4;
                iHttp.upRecyclerViewData(URLS.URL_HHS, homeShopsList, mAdapter, HTTPTAG, HomeFragment.this);
                iHttp.upRecyclerViewData(URLS.URL_HPR, homeHotsList, mAdapter, HTTPTAG, HomeFragment.this);
                iHttp.upRecyclerViewData(URLS.URL_HPR, homeNewsList, mAdapter, HTTPTAG, HomeFragment.this);
                iHttp.upRecyclerViewData(URLS.URL_HPR, homeTimeShopList, mAdapter, HTTPTAG, HomeFragment.this);
            }
        });

        homeRv01.setListenerLastItem(new RefreshRecyclerView.ListenerLastItem() {
            @Override
            public void onLastItem() {
//                iHttp.upListView(URLS.URL_TABLE_1, mAdapter, "mealinfo", true, HTTPTAG);
            }
        });

        homeShopsList = iHttp.readListFromCache(URLS.URL_HHS);
        if (homeShopsList == null) {
            homeShopsList = new ArrayList<Map<String, Object>>();
        }
        Map shops = new HashMap();
        shops.put(IAdapter.DATA, homeShopsList);
        shops.put(IAdapter.TYPE, IAdapter.H_VIEWPAGER_SHOP);
        RLList.add(shops);
        //更新数据
        iHttp.upRecyclerViewData(URLS.URL_HHS, homeShopsList, mAdapter, HTTPTAG);

        homeHotsList = iHttp.readListFromCache(URLS.URL_HPR);
        if (homeHotsList == null) {
            homeHotsList = new ArrayList();
        }
        Map hots = new HashMap();
        hots.put(IAdapter.DATA, homeHotsList);
        hots.put(IAdapter.TYPE, IAdapter.H_GRIDVIEW_HOTS);
        RLList.add(hots);
        //更新数据
        iHttp.upRecyclerViewData(URLS.URL_HPR, homeHotsList, mAdapter, HTTPTAG);


        homeNewsList = iHttp.readListFromCache(URLS.URL_HPR);
        if (homeNewsList == null) {
            homeNewsList = new ArrayList();
        }
        Map news = new HashMap();
        news.put(IAdapter.DATA, homeNewsList);
        news.put(IAdapter.TYPE, IAdapter.H_GRIDVIEW_NEWS);
        RLList.add(news);
        //更新数据
        iHttp.upRecyclerViewData(URLS.URL_HPR, homeNewsList, mAdapter, HTTPTAG);

        homeTimeShopList = iHttp.readListFromCache(URLS.URL_HPR);
        if (homeTimeShopList == null) {
            homeTimeShopList = new ArrayList();
        }
        Map timeShop = new HashMap();
        timeShop.put(IAdapter.DATA, homeTimeShopList);
        timeShop.put(IAdapter.TYPE, IAdapter.H_GRIDVIEW_TIMESHOP);
        RLList.add(timeShop);
        //更新数据
        iHttp.upRecyclerViewData(URLS.URL_HPR, homeTimeShopList, mAdapter, HTTPTAG);

        //recyclerViewList按类型排序
//        Collections.sort(recyclerViewList, new Comparator<Map<String, Object>>() {
//            @Override
//            public int compare(Map<String, Object> map1, Map<String, Object> map2) {
//                return ((String) map1.get(IAdapter.TYPE)).compareToIgnoreCase((String) map2.get(IAdapter.TYPE));
//            }
//        });
        //网络连接失败的重试按钮
        newworkError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iHttp.upRecyclerViewData(URLS.URL_HHS, homeShopsList, mAdapter, HTTPTAG, HomeFragment.this);
                iHttp.upRecyclerViewData(URLS.URL_HPR, homeHotsList, mAdapter, HTTPTAG, HomeFragment.this);
                iHttp.upRecyclerViewData(URLS.URL_HPR, homeNewsList, mAdapter, HTTPTAG, HomeFragment.this);
                iHttp.upRecyclerViewData(URLS.URL_HPR, homeTimeShopList, mAdapter, HTTPTAG, HomeFragment.this);
            }
        });

        return rootView;
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
            homeRv01.refreshSuccess();
        }
        homeRv01.setVisibility(View.VISIBLE);
        newworkError.setVisibility(View.GONE);
    }

    @Override
    public void failed(Object object) {
        httpNum--;
        if (httpNum <= 0) {
            homeRv01.refreshFailed();
        }
        newworkError.setVisibility(View.VISIBLE);
        homeRv01.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
