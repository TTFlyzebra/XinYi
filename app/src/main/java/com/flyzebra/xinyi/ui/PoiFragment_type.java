package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.flyzebra.xinyi.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FlyZebra on 2016/4/2.
 */
public class PoiFragment_type extends Fragment {
    private static final String ARG_TYPE = "TYPE";
    private static final String ARG_ID = "RESID";
    private String type;
    private int ResID;
    private String HTTPTAG_TYPE = "PoiFragment_type" + Math.random();
    private IHttp iHttp = MyVolley.getInstance();
    private MainActivity activity;
    private RefreshRecyclerView mRecyclerView;
    private List list;
    private DifrenceAdapter mAdapter;
    private View view;

    public PoiFragment_type() {
    }

    public static PoiFragment_type newInstance(String type, int ResID) {
        Bundle args = new Bundle();
        PoiFragment_type fragment = new PoiFragment_type();
        args.putString(ARG_TYPE, type);
        args.putInt(ARG_ID, ResID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString(ARG_TYPE);
            ResID = bundle.getInt(ARG_ID);
        }
        HTTPTAG_TYPE = HTTPTAG_TYPE + type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(activity).inflate(R.layout.poi_fragment_type, container, false);
        mRecyclerView = (RefreshRecyclerView) view.findViewById(R.id.poi_type_rv_01);
        mRecyclerView.setId(ResID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        if (list == null) {
            list = new ArrayList();
        }
        mAdapter = new DifrenceAdapter(activity, list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setListenerTopRefresh(new RefreshRecyclerView.ListenerTopRefresh() {
            @Override
            public void onRefrsh(View view) {
                iHttp.upListView(Constant.URL_TABLE_1, mAdapter, "mealinfo", HTTPTAG_TYPE, new IHttp.HttpResult() {
                    @Override
                    public void succeed(Object object) {
                        mRecyclerView.refreshSuccess();
                    }
                    @Override
                    public void faild(Object object) {
                        mRecyclerView.refreshFailed();
                    }

                    @Override
                    public void readDiskCache(Object data) {
                        mRecyclerView.refreshDisk();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        iHttp.upListView(Constant.URL_TABLE_1, mAdapter, "mealinfo", HTTPTAG_TYPE);
    }

    @Override
    public void onStop() {
        iHttp.cancelAll(HTTPTAG_TYPE);
        super.onStop();
    }
}
