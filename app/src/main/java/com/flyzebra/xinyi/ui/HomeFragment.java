package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.flyzebra.xinyi.view.RefreshRecycleryView;

import java.util.ArrayList;
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

    //ViewPage List;Key字包含图片名字=name，图片路径=path
    private MainActitity activity;

    private View view;
    private RefreshRecycleryView recyclerView;
    private DifrenceAdapter adapter;
    private List<Map<String, Object>> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActitity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = (RefreshRecycleryView) view.findViewById(R.id.home_lv_01);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        if (list == null) {
            list = new ArrayList<Map<String, Object>>();
        }
        adapter = new DifrenceAdapter(activity, list);
        recyclerView.setAdapter(adapter);
//        iHttp.upListView(Constant.URL_TABLE + "?get=mealinfo", recyclerView, "mealinfo", HTTPTAG);
        recyclerView.setListenerTopRefresh(new RefreshRecycleryView.ListenerTopRefresh() {
            @Override
            public void onRefrsh(View view) {
                iHttp.execute(new IHttp.Builder().setTag(HTTPTAG).setUrl(Constant.URL_TABLE + "?get=mealinfo").setView(recyclerView).setJsonKey("mealinfo").setResult(new IHttp.Result() {
                    @Override
                    public void succeed(Object object) {
                        recyclerView.refreshFinish();
                    }

                    @Override
                    public void faild(Object object) {
                        recyclerView.refreshFinish();
                    }
                }));
            }
        });

        recyclerView.setRefrshBottom(false);
        recyclerView.setListenerBottomRefresh(new RefreshRecycleryView.ListenerBottomRefresh() {
            @Override
            public void onRefrsh(View view) {
                list.addAll(list);
                adapter.notifyDataSetChanged();
                recyclerView.refreshFinish();
            }
        });

        recyclerView.setListenerLastItem(new RefreshRecycleryView.ListenerLastItem() {
            @Override
            public void onLastItem() {
                list.addAll(list);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new MyItemDcoration());
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        iHttp.cancelAll(HTTPTAG);
        super.onDestroy();
    }

}
