package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FlyZebra on 2016/4/2.
 */
public class PoiFragment_type extends Fragment {
    private static final String ARG_TYPE = "TYPE";
    private String type;
    private String HTTPTAG_TYPE = "PoiFragment_type" + Math.random();
    private IHttp iHttp = MyVolley.getInstance();
    private MainActivity activity;
    private ListView listView;
    private List list;
    private TvIvAdapter mAdapter;
    private View view;

    public PoiFragment_type() {
    }

    public static PoiFragment_type newInstance(String type) {
        Bundle args = new Bundle();
        PoiFragment_type fragment = new PoiFragment_type();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        Bundle bundle = getArguments();
        type = bundle.getString(ARG_TYPE);
        HTTPTAG_TYPE = HTTPTAG_TYPE + type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(activity).inflate(R.layout.poi_fragment_type, container, false);
        listView = (ListView) view.findViewById(R.id.poi_type_rv_01);
        if (list == null) {
            list = new ArrayList();
        }
        mAdapter = new TvIvAdapter(activity, list, R.layout.home_listview_item,
                new int[]{R.id.tv01, R.id.tv02},
                new String[]{"mealname", "mealprice"},
                new int[]{R.id.iv01},
                new String[]{"mealimage"},
                null, new TvIvAdapter.SetImageView() {
            @Override
            public void setImageView(String url, ImageView iv) {
                iHttp.upImageView(activity, "http://192.168.1.88/ordermeal" + url, iv);
            }
        });
        listView.setAdapter(mAdapter);
        iHttp.upListView(Constant.URL_TABLE_1, mAdapter, "mealinfo", HTTPTAG_TYPE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        iHttp.cancelAll(HTTPTAG_TYPE);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
