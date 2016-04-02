package com.flyzebra.xinyi.model.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.JsonUtils;
import com.flyzebra.xinyi.view.RefreshRecyclerView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by FlyZebra on 2016/3/30.
 */
public class MyOkHttp implements IHttp {
    /**
     * 跟主线程通信用
     */
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    //初始化OkHttpClient
    private static OkHttpClient mOkHttpClient;

    private MyOkHttp() {
    }

    public static MyOkHttp getInstance() {
        return MyOkHttpHolder.sInstance;
    }

    public static OkHttpClient getHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (MyOkHttp.class) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(12, TimeUnit.SECONDS).readTimeout(12, TimeUnit.SECONDS);
                mOkHttpClient = builder.build();
            }
        }
        return mOkHttpClient;
    }

    @Override
    public void upImageView(Context context, String url, ImageView iv) {
        Picasso.with(context).load(url).into(iv);
    }

    @Override
    public <T extends View> void upListView(String url, T view, Object tag) {
        Builder builder = new Builder().setUrl(url).setView(view).setTag(tag);
        execute(builder);
    }

    @Override
    public <T extends View> void upListView(String url, T view, String jsonKey, Object tag) {
        Builder builder = new Builder().setUrl(url).setView(view).setJsonKey(jsonKey).setTag(tag);
        execute(builder);
    }

    @Override
    public <T extends View> void upListView(String url, HttpAdapter adapter, String jsonKey, Object tag) {
        Builder builder = new Builder().setUrl(url).setAdapter(adapter).setJsonKey(jsonKey).setTag(tag);
        execute(builder);
    }

    @Override
    public void cancelAll(Object tag) {
    }

    public void execute(final Builder builder) {
        FlyLog.i("<MyOkHttp>-->execute:url=" + builder.url);
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .tag(builder.tag)
                .url(builder.url)
                .build();
        getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (builder.result != null) {
                    builder.result.faild(e);
                }
                execute(builder);
                FlyLog.i("<MyOkHttp>-->execute:url=" + builder.url);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                if (builder.result != null) {
                    builder.result.succeed(res);
                }
                if (builder.view != null) {
                    if (builder.view instanceof ListView) {
                        final ListView listView = (ListView) builder.view;
                        final HttpAdapter adapter = (HttpAdapter) listView.getAdapter();
                        notifyListView(adapter, res, builder.jsonKey);
                    } else if (builder.view instanceof PullToRefreshListView) {
                        final PullToRefreshListView pullToRefreshListView = (PullToRefreshListView) builder.view;
                        final HttpAdapter adapter = (HttpAdapter) (pullToRefreshListView.getAdapter());
                        notifyListView(adapter, res, builder.jsonKey);
                    } else if (builder.view instanceof RecyclerView) {
                        final RecyclerView recyclerView = (RecyclerView) builder.view;
                        final HttpAdapter adapter = (HttpAdapter) recyclerView.getAdapter();
                        notifyListView(adapter, res, builder.jsonKey);
                    } else if (builder.view instanceof RefreshRecyclerView) {
                        final RefreshRecyclerView recyclerView = (RefreshRecyclerView) builder.view;
                        final HttpAdapter adapter = (HttpAdapter) recyclerView.getAdapter();
                        notifyListView(adapter, res, builder.jsonKey);
                    }
                } else if (builder.adapter != null) {
                    notifyListView(builder.adapter, res, builder.jsonKey);
                }
            }
        });
    }

    private void notifyListView(final HttpAdapter adapter, final String res, final String jsonKey) {
        try {
            JsonUtils.getList(adapter.getList(), new JSONObject(res), jsonKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private static class MyOkHttpHolder {
        public static final MyOkHttp sInstance = new MyOkHttp();
    }

}

