package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * 主页
 * Created by FlyZebra on 2016/2/29.
 */
public class HomeFragment extends Fragment {
    //ViewPager自动轮播
    private final int delayMillis = 5000;
    private IHttp iHttp = MyVolley.getInstance();
    private String HTTPTAG = "Fragment" + Math.random();
    //ViewPage List;Key字包含图片名字=name，图片路径=path
    private MainActitity activity;

    private View view;
    private RefreshRecycleryView recyclerView;
    private DifrenceAdapter adapter;
    private List list;
    private int down_y, move_y;
    private boolean enbaleParentMove;
    private View headerView;
    private boolean isMoveUp;

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
            list = new ArrayList<>();
        }
        adapter = new DifrenceAdapter(activity, list);
        recyclerView.setAdapter(adapter);
        iHttp.upListView(Constant.URL_TABLE + "?get=mealinfo", recyclerView, "mealinfo", HTTPTAG);
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
