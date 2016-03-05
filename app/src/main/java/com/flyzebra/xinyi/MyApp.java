package com.flyzebra.xinyi;

import android.app.Application;

import com.android.volley.toolbox.Volley;
import com.flyzebra.xinyi.openutils.UILImageUtils;
import com.flyzebra.xinyi.openutils.VolleyUtils;

/**
 * APP
 * Created by FlyZebra on 2016/2/29.
 */
public class MyApp extends Application {
    //记录HomeActivity中ScrollView的滚动位置，在LoginActivity中置0;
    public static int home_sv_x;
    public static int home_sv_y;
    public static int poi_rv_x;
    public static int poi_rv_y;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化UIL库
        UILImageUtils.initImageLoader(getApplicationContext());
        //初始化Volley
        VolleyUtils.Init(getApplicationContext());
    }
}
