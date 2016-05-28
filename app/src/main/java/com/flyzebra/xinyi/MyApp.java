package com.flyzebra.xinyi;

import com.baidu.frontia.FrontiaApplication;
import com.flyzebra.xinyi.model.http.MyOkHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.squareup.leakcanary.LeakCanary;

/**
 * APP
 * Created by FlyZebra on 2016/2/29.
 */
public class MyApp extends FrontiaApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        //初始化UIL库
//        UILImageUtils.initImageLoader(getApplicationContext());
        //初始化Volley
        MyVolley.Init(getApplicationContext());
        MyOkHttp.Init(getApplicationContext());

    }

}

