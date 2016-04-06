package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.data.IAdapter;
import com.flyzebra.xinyi.model.TestHttp;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.flyzebra.xinyi.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页
 * Created by FlyZebra on 2016/2/29.
 */
public class HomeFragment extends Fragment {
    //ViewPager自动轮播
    private IHttp iHttp = MyVolley.getInstance();
    private String HTTPTAG = "Fragment" + Math.random();

    private MainActivity activity;

    private View view;
    private RefreshRecyclerView recyclerView;
    private MultiRListAdapter mAdapter;
    private List recyclerViewList;
    private List childViepagerList;
    private List childGridVidepager;

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
        if (recyclerViewList == null) {
            recyclerViewList = new ArrayList<Map<String, Object>>();
        }
        mAdapter = new MultiRListAdapter(activity, recyclerViewList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        //下拉刷新
        recyclerView.setListenerTopRefresh(new RefreshRecyclerView.ListenerTopRefresh() {
            @Override
            public void onRefrsh(View view) {
                iHttp.upListView(Constant.URL_TABLE_1, mAdapter, "mealinfo", true, HTTPTAG);
            }
        });

//        recyclerView.addItemDecoration(new MyItemDcoration());
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //初始数据
        //ViewPager轮播
        childViepagerList = TestHttp.getViewPagerList();
        for (int i = 0; i < 5; i++) {
            Map vp_map = new HashMap();
            vp_map.put(IAdapter.DATA, childViepagerList);
            vp_map.put(IAdapter.TYPE, IAdapter.VIEWPAGER);
            recyclerViewList.add(vp_map);
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setToolbar(0);
    }

    @Override
    public void onDestroy() {
        iHttp.cancelAll(HTTPTAG);
        super.onDestroy();
    }

}
