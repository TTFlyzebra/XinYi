package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.utils.FlyLog;
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

    private MainActivity activity;

    private View view;
    private RefreshRecyclerView recyclerView;
    private MultiRListAdapter mAdapter;
    private static List RLList;
    private static List HomeShopList;
    private static List homeHotList;

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
        mAdapter = new MultiRListAdapter(activity, RLList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        //下拉刷新
        recyclerView.setListenerTopRefresh(new RefreshRecyclerView.ListenerTopRefresh() {
            @Override
            public void onRefrsh(View view) {
                iHttp.upMultiRLData(Constant.URL_HS, HomeShopList, mAdapter, HTTPTAG);
                iHttp.upMultiRLData(Constant.URL_PR, homeHotList, mAdapter, HTTPTAG);
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
        if(HomeShopList ==null){
            HomeShopList = new ArrayList();
            Map cvp = new HashMap();
            cvp.put(IAdapter.DATA, HomeShopList);
            cvp.put(IAdapter.TYPE, IAdapter.VIEWPAGER);
            RLList.add(cvp);
            iHttp.upMultiRLData(Constant.URL_HS, HomeShopList, mAdapter, HTTPTAG);

        }
        //更新数据
        //添加ChildGridView//产品展示
        if(homeHotList ==null){
            homeHotList = new ArrayList();
            Map ggg = new HashMap();
            ggg.put(IAdapter.DATA, homeHotList);
            ggg.put(IAdapter.TYPE, IAdapter.GRIDVIEW);
            RLList.add(ggg);
            iHttp.upMultiRLData(Constant.URL_PR, homeHotList, mAdapter, HTTPTAG);
        }

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
