package com.flyzebra.xinyi.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/20.
 */
public interface IHttp {
    /**
     * @param url
     * @param list
     * @param jsonKey
     * @param adapter
     * @param tag     用来执行取消网络请求
     */
    void upListView(String url, List<Map<String, Object>> list, String jsonKey, BaseAdapter adapter, Object tag);

    void upListView(String url, List<Map<String, Object>> list, String jsonKey, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, Object tag);

    void upListView(String url, Result result);

    void upImageView(Context context, String url, ImageView iv);

    void cancelAll(Object tag);

    interface Result {
        void succeed(Object object);

        void faild(Object object);
    }
}
