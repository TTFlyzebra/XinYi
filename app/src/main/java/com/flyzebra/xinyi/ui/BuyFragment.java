package com.flyzebra.xinyi.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.HttpUpdata;
import com.flyzebra.xinyi.model.IHttpUpdata;
import com.flyzebra.xinyi.universal.TvIvAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/22.
 */
public class BuyFragment extends Fragment {
    private static final String TAG = "com.flyzebra";
    private IHttpUpdata iHttpUpdata = HttpUpdata.getInstance();

    private MainActitity activity;
    private List<Map<String, Object>> list;
    private PullToRefreshListView listView;
    private TvIvAdapter adapter;

    private Toolbar mToolbar;

    public BuyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActitity) getActivity();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.buy_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        // 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitle("购物车");
        mToolbar.setLogo(R.drawable.ic_love);
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        listView = (PullToRefreshListView) view.findViewById(R.id.buy_lv_01);

        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        //上拉下拉刷新
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute();
                Log.i(TAG, "onPullDownToRefresh");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute();
                Log.i(TAG, "onPullUpToRefresh");
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
        adapter = new TvIvAdapter(activity, list, R.layout.home_listview,
                new int[]{R.id.tv01, R.id.tv02},
                new String[]{"mealname", "mealprice"},
                new int[]{R.id.iv01},
                new String[]{"mealimage"},
                null, new TvIvAdapter.SetImageView() {
            @Override
            public void setImageView(String url, ImageView iv) {
                iHttpUpdata.upImageView(activity, "http://192.168.1.88/ordermeal" + url, iv);
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

        iHttpUpdata.upListView("http://192.168.1.88/ordermeal/table.jsp?get=mealinfo", list, "mealinfo", adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        iHttpUpdata.cancelAll("http://192.168.1.88/ordermeal/table.jsp?get=mealinfo");
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
