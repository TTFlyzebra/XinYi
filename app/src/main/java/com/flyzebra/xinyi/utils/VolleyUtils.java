package com.flyzebra.xinyi.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.IHttp;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by FlyZebra on 2016/3/20.
 */
public class VolleyUtils {
    public static Set<Object> set_upListView = new HashSet<>();
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static long RETRY_TIME = 2500;

    public static RequestQueue Init(Context context, int maxDiskCacheBytes) {
        mRequestQueue = Volley.newRequestQueue(context, maxDiskCacheBytes);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        return mRequestQueue;
    }

    public static void upImageView(String url, NetworkImageView iv, int res1, int res2) {
        iv.setDefaultImageResId(res1);
        iv.setErrorImageResId(res2);
        iv.setImageUrl(url, mImageLoader);
    }

    public static void upImageView(String url, ImageView iv) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv, R.drawable.image, R.drawable.image);
        FlyLog.i("<VolleyUtils>url=" + url + "[ImageView:" + iv + "]");
        mImageLoader.get(url, listener);
    }

    public static void upListView(final String url, final List<Map<String, Object>> list, final String jsonKey, final BaseAdapter adapter, final Object tag) {
        //判断任务是否已取消
        set_upListView.add(tag);
        FlyLog.i("<VolleyUtils>upListView:url=" + url + "[list=" + list + ",jsonKey=" + jsonKey + ",adapter=" + adapter + "]");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (list != null) {
                    list.clear();
                    JsonUtils.getList(list, jsonObject, jsonKey);
                }
                adapter.notifyDataSetChanged();
                FlyLog.i("<VolleyUtils>upListView->onResponse:tag=" + tag);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                FlyLog.i("<VolleyUtils>upListView->onErrorResponse:tag=" + tag);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (set_upListView.contains(tag)) {
                            upListView(url, list, jsonKey, adapter, tag);
                        }
                    }
                }, RETRY_TIME);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 1, 1f));
        jsonObjectRequest.setTag(tag);
        mRequestQueue.add(jsonObjectRequest);
    }


    public static void upListView(final String url, final List<Map<String, Object>> list, final String jsonKey, final RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, final Object tag) {
        //判断任务是否已取消
        set_upListView.add(tag);
        FlyLog.i("<VolleyUtils>upListView:url=" + url + "[list=" + list + ",jsonKey=" + jsonKey + ",adapter=" + adapter + "]");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (list != null) {
                    JsonUtils.getList(list, jsonObject, jsonKey);
                }
                adapter.notifyDataSetChanged();
                FlyLog.i("<VolleyUtils>upListView->onResponse:tag=" + tag);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                FlyLog.i("<VolleyUtils>upListView->onErrorResponse:tag=" + tag);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (set_upListView.contains(tag)) {
                            upListView(url, list, jsonKey, adapter, tag);
                        }
                    }
                }, RETRY_TIME);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 1, 1f));
        jsonObjectRequest.setTag(tag);
        mRequestQueue.add(jsonObjectRequest);
    }

    public static void upListView(String url, final IHttp.Result result) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                result.succeed(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                result.faild(volleyError);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 1, 1f));
        jsonObjectRequest.setTag(url);
        mRequestQueue.add(jsonObjectRequest);
    }

    public static void cancelAll(Object tag) {
        FlyLog.i("<VolleyUtils>cancelAll:tag=" + tag);
        set_upListView.remove(tag);
        mRequestQueue.cancelAll(tag);
    }
}
