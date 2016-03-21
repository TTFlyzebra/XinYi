package com.flyzebra.xinyi.utils;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.flyzebra.xinyi.R;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/20.
 */
public class VolleyUtils {
    private static final String TAG = "com.flyzebra";
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    public static RequestQueue Init(Context context,int maxDiskCacheBytes) {
        mRequestQueue = Volley.newRequestQueue(context, maxDiskCacheBytes);
        mImageLoader = new ImageLoader(mRequestQueue, new VolleyBitmapCache());
        return mRequestQueue;
    }

    public static void upImageView(String url, NetworkImageView iv, int res1, int res2) {
        iv.setDefaultImageResId(res1);
        iv.setErrorImageResId(res2);
        iv.setImageUrl(url, mImageLoader);
    }

    public static void upListView(final String url, final List<Map<String, Object>> list, final String jsonKey, final BaseAdapter adapter) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (list != null) {
                    list.clear();
                    JsonUtils.getlistfromjsonobject(list, jsonObject, jsonKey);
                }
                adapter.notifyDataSetChanged();
                Log.i(TAG, "VolleyUtils--->upListView");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "upListView--->onErrorResponse");
                upListView(url, list, jsonKey, adapter);
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }

    public static void upImageView(String url, ImageView iv) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv, R.drawable.image, R.drawable.image);
        mImageLoader.get(url, listener);
    }
}
