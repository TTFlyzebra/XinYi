package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.model.IHttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/26.
 */
public class PoiFragment extends Fragment {
    private static final String HTTPTAG = "PoiFragment" + Math.random();
    private MainActitity actitity;
    private RecyclerView recyclerView;
    private List<Map<String, Object>> list;
    private DifrenceAdapter adapter;
    private IHttp iHttp = MyHttp.getInstance();

    public PoiFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actitity = (MainActitity) getActivity();
        View view = View.inflate(actitity, R.layout.poi_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.poi_rv_01);
        recyclerView.setLayoutManager(new LinearLayoutManager(actitity));
        if (list == null) {
            list = new ArrayList<>();
        }
        adapter = new DifrenceAdapter(actitity, list);
        recyclerView.setAdapter(adapter);
        iHttp.upListView(Constant.URL_TABLE + "?get=mealinfo", recyclerView, "mealinfo", HTTPTAG);
    }

    @Override
    public void onDestroy() {
        iHttp.cancelAll(HTTPTAG);
        super.onDestroy();
    }
}
