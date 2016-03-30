package com.flyzebra.xinyi.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.flyzebra.xinyi.model.IHttp;
import com.flyzebra.xinyi.model.MyOkHttp;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/22.
 */
public class BuyFragment extends Fragment {
    private String HTTPTAG = "BuyFragment" + Math.random();
    private MainActitity activity;
    private List<Map<String, Object>> list;
    private PullToRefreshListView listView;
    private TvIvAdapter adapter;
    private IHttp iHttp = MyOkHttp.getInstance();

    public BuyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActitity) getActivity();
        activity.toolBar.setTitle("购物车");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.buy_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        // 标题的文字需在setSupportActionBar之前，不然会无效
//        mToolbar.setTitle("购物车");
//        mToolbar.setLogo(R.drawable.ic_love);
//        activity.setSupportActionBar(mToolbar);
//        activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        listView = (PullToRefreshListView) view.findViewById(R.id.buy_lv_01);

        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        //上拉下拉刷新
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute();
            }
        });

        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
//                list.addAll(list);
                Toast.makeText(activity, "End of List!", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });

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
        iHttp.execute(IHttp.Builder.getInstance().setUrl(Constant.URL_TABLE + "?get=mealinfo").setView(listView).setJsonKey("mealinfo").setTag(HTTPTAG));
//        iHttp.upListView(Constant.URL_TABLE + "?get=mealinfo", listView, "mealinfo", HTTPTAG);
    }

    @Override
    public void onDestroy() {
        iHttp.cancelAll(HTTPTAG);
        super.onDestroy();
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
