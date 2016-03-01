package com.flyzebra.xinyi;

import android.app.Application;

import com.flyzebra.xinyi.data.ImageLoaderConfig;

/**
 * APP
 * Created by FlyZebra on 2016/2/29.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfig.initImageLoader(getApplicationContext());
    }
}
