package com.flyzebra.xinyi.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.flyzebra.xinyi.utils.VolleyUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/20.
 */
public class MyHttp implements IHttp {
    private MyHttp() {
    }

    public static MyHttp getInstance() {
        return HttpHolder.sInstance;
    }

    @Override
    public void upListView(final String url, final List<Map<String, Object>> list, final String jsonKey, final BaseAdapter adapter, Object tag) {
        VolleyUtils.upListView(url, list, jsonKey, adapter, tag);
    }

    @Override
    public void upListView(String url, List<Map<String, Object>> list, String jsonKey, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, Object tag) {
        VolleyUtils.upListView(url, list, jsonKey, adapter, tag);
    }

    @Override
    public void upListView(String url, Result result) {
        VolleyUtils.upListView(url, result);
    }

    @Override
    public void upImageView(Context context, String url, ImageView iv) {
        VolleyUtils.upImageView(url, iv);
//        ImageLoader.getInstance().displayImage(url, iv, UILImageUtils.getDisplayImageOptions(R.drawable.image, R.drawable.image, R.drawable.image));
        //Picasso方式显示图片
//        Picasso.with(context).load(url).placeholder(R.drawable.image).error(R.drawable.image).into(iv);
    }

    @Override
    public void cancelAll(Object tag) {
        VolleyUtils.cancelAll(tag);
    }

    private static class HttpHolder {
        public static final MyHttp sInstance = new MyHttp();
    }


}
