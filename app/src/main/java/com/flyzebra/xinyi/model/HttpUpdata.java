package com.flyzebra.xinyi.model;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.flyzebra.xinyi.utils.VolleyUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/20.
 */
public class HttpUpdata implements IHttpUpdata {
    public static final String TAG = "com.flyzebra";

    //---单例模式---->
    private HttpUpdata(){
    }
    public static HttpUpdata getInstance(){
        return VolleyUtilsHolder.sInstance;
    }

    @Override
    public void upListView(final String url, final List<Map<String, Object>> list, final String jsonKey, final BaseAdapter adapter) {
        VolleyUtils.upListView(url, list, jsonKey, adapter);
    }
    //<---单例模式-----

    @Override
    public void upImageView(Context context,String url, ImageView iv) {
        VolleyUtils.upImageView(url,iv);
//        ImageLoader.getInstance().displayImage(url, iv, UILImageUtils.getDisplayImageOptions(R.drawable.image, R.drawable.image, R.drawable.image));
        //Picasso方式显示图片
//        Picasso.with(context).load(url).placeholder(R.drawable.image).error(R.drawable.image).into(iv);
    }

    @Override
    public void cancelAll(Object tag) {
        VolleyUtils.cancelAll(tag);
    }

    private static class VolleyUtilsHolder {
        public static final HttpUpdata sInstance = new HttpUpdata();
    }
}
