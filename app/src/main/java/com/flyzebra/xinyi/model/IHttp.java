package com.flyzebra.xinyi.model;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/20.
 */
public interface IHttp {
    void upListView(String url, List<Map<String, Object>> list, String jsonKey, BaseAdapter adapter, Object tag);

    void upListView(String url, Result result);

    void upImageView(Context context, String url, ImageView iv);

    void cancelAll(Object tag);

    interface Result {
        void succeed(Object object);

        void faild(Object object);
    }
}
