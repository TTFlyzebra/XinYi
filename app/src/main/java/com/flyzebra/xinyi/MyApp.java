package com.flyzebra.xinyi;

import android.app.Application;

import com.flyzebra.xinyi.utils.VolleyUtils;

/**
 * APP
 * Created by FlyZebra on 2016/2/29.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化UIL库
//        UILImageUtils.initImageLoader(getApplicationContext());
        //初始化Volley
        VolleyUtils.Init(getApplicationContext(), 50 * 1024 * 1024);
    }
}

