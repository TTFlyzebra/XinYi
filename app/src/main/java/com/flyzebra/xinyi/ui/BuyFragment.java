package com.flyzebra.xinyi.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/22.
 */
public class BuyFragment extends BaseFragment {
    private MainActivity activity;
    private List<Map<String, Object>> list;
    private PullToRefreshListView listView;
    private TvIvAdapter adapter;

    public BuyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.buy_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (PullToRefreshListView) view.findViewById(R.id.buy_lv_01);
        if (list == null) {
            list = new ArrayList<Map<String, Object>>();
        } else {
            list.clear();
        }
        adapter = new TvIvAdapter(activity, list, R.layout.home_listview_item,
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
        listView.setAdapter(adapter);

        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        //上拉下拉刷新
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                iHttp.upListView(Constant.URL_TABLE_1, adapter, "mealinfo", HTTPTAG, new IHttp.HttpResult() {
                    @Override
                    public void succeed(Object object) {
                        listView.onRefreshComplete();
                    }

                    @Override
                    public void failed(Object object) {
                        listView.onRefreshComplete();
                    }
                });
            }
        });

        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Toast.makeText(activity, "End of List!", Toast.LENGTH_SHORT).show();
            }
        });

        //设置没有数据时ListView的显示
        LinearLayout list_empty = (LinearLayout) view.findViewById(R.id.empty_view);
        listView.setEmptyView(list_empty);
        ImageView list_empty_iv = (ImageView) view.findViewById(R.id.empty_view_iv);

        final AnimationDrawable animationDrawable = (AnimationDrawable) list_empty_iv.getDrawable();
        list_empty_iv.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });
        Map<String, String> params = new HashMap<>();
//        iHttp.execute(IHttp.Builder.getInstance().setUrl(Constant.URL_TABLE + "?get=mealinfo").setView(listView).setJsonKey("mealinfo").setTag(HTTPTAG));
        iHttp.upListView(Constant.URL_TABLE + "?get=mealinfo", adapter, "mealinfo", HTTPTAG);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setToolbar(2);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            listView.onRefreshComplete();
            super.onPostExecute(strings);
        }
    }
}
