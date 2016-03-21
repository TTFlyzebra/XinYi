package com.flyzebra.xinyi.model;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/20.
 */
public interface IHttpUpdata {
    void upListView(String url, List<Map<String, Object>> list,String jsonKey,BaseAdapter adapter);
    void upImageView(Context context,String url, ImageView iv);
}
