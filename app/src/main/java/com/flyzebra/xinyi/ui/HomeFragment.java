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
public class HomeFragment extends Fragment {
    //ViewPager自动轮播
    private IHttp iHttp = MyVolley.getInstance();
    private String HTTPTAG = "Fragment" + Math.random();

    private MainActivity activity;

    private View view;
    private RefreshRecyclerView recyclerView;
    private MultiRListAdapter mAdapter;
    private List recyclerViewList;
    private List itemList;
    private List childViewPagerList;
    private List childGridViewList;

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
                recyclerView.refreshFinish();
            }
        });

        recyclerView.setListenerLastItem(new RefreshRecyclerView.ListenerLastItem() {
            @Override
            public void onLastItem() {
                iHttp.upListView(Constant.URL_TABLE_1, mAdapter, "mealinfo", true, HTTPTAG);
            }
        });

//        recyclerView.addItemDecoration(new MyItemDcoration());
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //初始化数据
        //ViewPager轮播
        if (itemList == null) {
            itemList = new ArrayList();
        }
        childViewPagerList = TestHttp.getViewPagerList();
        Map vvv = new HashMap();
        vvv.put(IAdapter.DATA, childViewPagerList);
        vvv.put(IAdapter.TYPE, IAdapter.VIEWPAGER);
        recyclerViewList.add(vvv);

        childGridViewList = TestHttp.getViewPagerList();
        Map ggg = new HashMap();
        ggg.put(IAdapter.DATA, childGridViewList);
        ggg.put(IAdapter.TYPE, IAdapter.GRIDVIEW);
        recyclerViewList.add(ggg);

        for (int i = 0; i < 10; i++) {
            Map vp_map = new HashMap();
            vp_map.put(IAdapter.DATA, TestHttp.getViewPagerList());
            vp_map.put(IAdapter.TYPE, IAdapter.VIEWPAGER);
            recyclerViewList.add(vp_map);
            Map gv_map = new HashMap();
            gv_map.put(IAdapter.DATA, TestHttp.getViewPagerList1());
            gv_map.put(IAdapter.TYPE, IAdapter.GRIDVIEW);
            recyclerViewList.add(gv_map);
        }
//        Collections.sort(recyclerViewList, new Comparator<Map<String, Object>>() {
//            @Override
//            public int compare(Map<String, Object> map1, Map<String, Object> map2) {
//               return ((String) map1.get(IAdapter.TYPE)).compareToIgnoreCase((String) map2.get(IAdapter.TYPE));
//            }
//        });
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
    public void onStop() {
        super.onStop();
        FlyLog.i("<HomeFragment>onStop");
    }

    @Override
    public void onDestroy() {
        FlyLog.i("<HomeFragment>onDestroy");
        iHttp.cancelAll(HTTPTAG);
        super.onDestroy();
    }

}
