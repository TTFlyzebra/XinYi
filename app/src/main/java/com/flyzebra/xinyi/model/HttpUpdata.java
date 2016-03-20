package com.flyzebra.xinyi.model;

import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.flyzebra.xinyi.utils.VolleyUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/20.
 */
public class HttpUpdata implements IHttpUpdata {

    //---单例模式---->
    private HttpUpdata(){
    }
    public static HttpUpdata getInstance(){
        return VolleyUtilsHolder.sInstance;
    }
    private static class VolleyUtilsHolder{
        public static final HttpUpdata sInstance = new HttpUpdata();
    }
    //<---单例模式-----

    @Override
    public void upListView(final String url, final List<Map<String, Object>> list, final String jsonKey, final BaseAdapter adapter) {
        VolleyUtils.upListView(url,list,jsonKey,adapter);
    }

    @Override
    public void upImageView(String url, ImageView iv) {
        VolleyUtils.upImageView(url,iv);
    }
}
