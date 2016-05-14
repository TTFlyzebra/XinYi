package com.flyzebra.xinyi.model.http;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.toolbox.HttpHeaderParser;
import com.flyzebra.xinyi.ui.IAdapter;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.JsonUtils;
import com.flyzebra.xinyi.view.RefreshRecyclerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.DiskLruCache;
import okhttp3.internal.Util;

/**
 *
 * Created by FlyZebra on 2016/3/30.
 */
public class MyOkHttp implements IHttp {
    /**
     * 跟主线程通信用
     */
    private static Context mContext;
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    //初始化OkHttpClient
    private static OkHttpClient mOkHttpClient;

    public static Map<Object,Set<Call>> map_Call = new HashMap<>();
    private static final int OK = 1;
    private static final int FAIL = 2;
    public static final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");
    private MyOkHttp() {
    }

    public static void Init(Context context) {
        mContext = context;
    }

    public static MyOkHttp getInstance() {
        return MyOkHttpHolder.sInstance;
    }

    public static OkHttpClient getHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (MyOkHttp.class) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .cache(new Cache(getDiskCacheDir("okhttp"), 50 * 1024 * 1024))
                        .connectTimeout(12, TimeUnit.SECONDS)
                        .readTimeout(12, TimeUnit.SECONDS);
                mOkHttpClient = builder.build();
            }
        }
        return mOkHttpClient;
    }

    @Override
    public void getString(String url, final Object tag, final HttpResult result) {
        FlyLog.i("<MyOkHttp>-->getString:url=" + url);
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .tag(tag)
                .url(url)
                .build();
        Call call = getHttpClient().newCall(request);
        addCallSet(call,tag);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                removeCallSet(call, tag);
                sendResult(result, e, FAIL);
                FlyLog.i("<MyOkHttp>-->getString->onFailure:e=" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                removeCallSet(call, tag);
                String res = response.body().string();
                sendResult(result, res, OK);
                FlyLog.i("<MyOkHttp>-->getString->onResponse:res=" + res);
            }
        });
    }

    @Override
    public void postString(String url, Map<String, String> params, final Object tag, final HttpResult result) {
        FlyLog.i("<MyOkHttp>-->postString:url=" + url);
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody formBody = builder.build();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .tag(tag)
                .url(url)
                .post(formBody)
                .build();
        Call call = getHttpClient().newCall(request);
        addCallSet(call,tag);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                removeCallSet(call, tag);
                sendResult(result, e, FAIL);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                removeCallSet(call, tag);
                String res = response.body().string();
                sendResult(result, res, OK);
            }
        });
    }

    @Override
    public void upImageView(Context context, String url, ImageView iv) {
        Picasso.with(context).load(url).into(iv);
    }

    @Override
    public void upImageView(Context context, String url, ImageView iv, int LoadResId) {
        Picasso.with(context).load(url).placeholder(LoadResId).error(LoadResId).into(iv);
    }

    //*ListView RecyclearView调用部分
    @Override
    public void upListView(String url, HttpAdapter adapter, String jsonKey, Object tag) {
        upListView(url, adapter, jsonKey, false, tag);
    }

    @Override
    public void upListView(String url, HttpAdapter adapter, String jsonKey, Object tag, HttpResult result) {
        upListView(url, adapter, jsonKey, false, tag, result);
    }

    @Override
    public void upListView(String url, HttpAdapter adapter, String jsonKey, boolean isAdd, Object tag) {
        upListView(url, adapter, jsonKey, isAdd, tag, null);
    }

    @Override
    public void upListView(String url, final HttpAdapter adapter, final String jsonKey, final boolean isAdd, final Object tag, final HttpResult result) {
        FlyLog.i("<MyOkHttp>-->upListView:url=" + url);
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .tag(tag)
                .url(url)
                .build();
        Call call = getHttpClient().newCall(request);
        addCallSet(call,tag);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                removeCallSet(call, tag);
                sendResult(result, e, FAIL);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                removeCallSet(call, tag);
                String res = response.body().string();
                sendResult(result, res, OK);
                try {
                    List<Map<String, Object>> jsonList = JsonUtils.json2List(new JSONObject(res), jsonKey);
                    notifyData(isAdd, adapter.getList(), jsonList, adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void upRecyclerViewData(String url, List list, HttpAdapter adapter, Object tag) {
        upRecyclerViewData(url, list, adapter, tag, null);
    }

    @Override
    public void upRecyclerViewData(String url, final List list, final HttpAdapter adapter, final Object tag, final HttpResult result) {
        FlyLog.i("<MyOkHttp>-->upRecyclerViewData:url=" + url);
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .tag(tag)
                .url(url)
                .build();
        Call call = getHttpClient().newCall(request);
        addCallSet(call,tag);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                removeCallSet(call, tag);
                sendResult(result, e, FAIL);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                removeCallSet(call, tag);
                String res = response.body().string();
                sendResult(result, res, OK);
                try {
                    List<Map<String, Object>> jsonList = JsonUtils.json2List(new JSONArray(res));
                    notifyData(false, list, jsonList, adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FlyLog.i("<MyOkHttp>-->upRecyclerViewData:url=" + res);
            }
        });
    }

    /**
     *
     * @param url
     * @return
     */
    @Override
    public List<Map<String, Object>> readListFromCache(String url) {
        String res = readDiskCache(url);
        if(res!=null){
            try {
                return JsonUtils.json2List(new JSONArray(res));
            } catch (JSONException e) {
            }
        }
        return null;
    }

    @Override
    public void cancelAll(Object tag) {
        Set<Call> set = map_Call.get(tag);
        if(set!=null){
            for(Iterator<Call> it = set.iterator();it.hasNext();){
                it.next().cancel();
            }
            set.clear();
        }
        set = null;
    }

    private void addCallSet(Call call,Object tag){
        Set<Call> set = map_Call.get(tag);
        if(set==null){
            set = new HashSet<Call>();;
        }
        set.add(call);
    }

    private void removeCallSet(Call call,Object tag){
        Set<Call> set = map_Call.get(tag);
        if(set!=null){
            set.remove(call);
            if(set.isEmpty()){
                set = null;
            }
        }
    }

    public void execute(final Builder builder) {
        FlyLog.i("<MyOkHttp>-->execute:url=" + builder.url);
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .tag(builder.tag)
                .url(builder.url)
                .build();
        Call call = getHttpClient().newCall(request);
        addCallSet(call,builder.tag);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (builder.result != null) {
                    builder.result.failed(e);
                    removeCallSet(call, builder.tag);
                }
                //重复执行
                execute(builder);
                FlyLog.i("<MyOkHttp>-->execute:url=" + builder.url);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                removeCallSet(call, builder.tag);
                String res = response.body().string();
                if (builder.result != null) {
                    builder.result.succeed(res);
                }
                if (builder.view != null) {
                    if (builder.view instanceof ListView) {
                        final ListView listView = (ListView) builder.view;
                        final IAdapter adapter = (IAdapter) listView.getAdapter();
                        notifyListView(adapter, res, builder.jsonKey);
                    } else if (builder.view instanceof RecyclerView) {
                        final RecyclerView recyclerView = (RecyclerView) builder.view;
                        final IAdapter adapter = (IAdapter) recyclerView.getAdapter();
                        notifyListView(adapter, res, builder.jsonKey);
                    } else if (builder.view instanceof RefreshRecyclerView) {
                        final RefreshRecyclerView recyclerView = (RefreshRecyclerView) builder.view;
                        final IAdapter adapter = (IAdapter) recyclerView.getAdapter();
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
            JsonUtils.json2List(adapter.getList(), new JSONObject(res), jsonKey);
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

    public static File getDiskCacheDir(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public void sendResult(final HttpResult result,final  Object object, int type) {
        if (result != null) {
            switch (type) {
                case OK:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            result.succeed(object);
                        }
                    });
                    break;
                case FAIL:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            result.failed(object);
                       }
                   });
                    break;
            }
        }
    }

    private void notifyData(final boolean isAdd, final List list ,final List jsonList,final HttpAdapter adapter) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isAdd) {
                    list.clear();
                }
                list.addAll(jsonList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 使用反射读取磁盘缓存
     * @param url
     * @return
     */
    public static String readDiskCache(String url) {
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
        Response response = null;
        java.lang.reflect.Method get;
        try {
            Class cls = getHttpClient().cache().getClass();
            get = cls.getDeclaredMethod("get", Request.class);
            get.setAccessible(true);
            response = (Response)get.invoke(getHttpClient().cache(), request);
        } catch (NoSuchMethodException e) {
            FlyLog.i("<MyOkHttp>-->readListFromCache->NoSuchMethodException");
        } catch (InvocationTargetException e) {
            FlyLog.i("<MyOkHttp>-->readListFromCache->InvocationTargetException");
        } catch (IllegalAccessException e) {
            FlyLog.i("<MyOkHttp>-->readListFromCache->IllegalAccessException");
        }
        if (response != null) {
            try {
                return response.body().string();
            } catch (IOException e) {
            }
        }
        return null;
    }
}

