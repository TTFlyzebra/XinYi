package com.flyzebra.xinyi.model;

import android.content.Context;
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
import com.flyzebra.xinyi.openutils.BitmapCache;
import com.flyzebra.xinyi.utils.JsonUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/20.
 */
public class VolleyUtils implements IHttpUpdata {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    //---单例模式---->
    private VolleyUtils(){
    }
    public static VolleyUtils getInstance(){
        return VolleyUtilsHolder.sInstance;
    }
    private static class VolleyUtilsHolder{
        public static final VolleyUtils sInstance = new VolleyUtils();
    }
    //<---单例模式-----

    public static RequestQueue Init(Context context,int maxDiskCacheBytes) {
        mRequestQueue = Volley.newRequestQueue(context, maxDiskCacheBytes);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        return mRequestQueue;
    }

    public static void ShowImageView(String url, ImageView iv, int res1, int res2) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv, res1, res2);
        mImageLoader.get(url, listener);
    }

    public static void ShowImageView(String url, NetworkImageView iv, int res1, int res2) {
        iv.setDefaultImageResId(res1);
        iv.setErrorImageResId(res2);
        iv.setImageUrl(url, mImageLoader);
    }

    @Override
    public void upListView(final String url, final List<Map<String, Object>> list, final String jsonKey, final BaseAdapter adapter) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (list != null) {
                    list.clear();
                    JsonUtils.upListFromJsonObject(list, jsonObject, jsonKey);
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                upListView(url, list, jsonKey, adapter);
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }

    @Override
    public void upImageView(String url, ImageView iv) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv, R.drawable.image, R.drawable.image);
        mImageLoader.get(url, listener);
    }
}
