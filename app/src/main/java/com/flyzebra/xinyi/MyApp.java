package com.flyzebra.xinyi;

import android.app.Application;

import com.flyzebra.xinyi.data.UILImageUtils;

/**
 * APP
 * Created by FlyZebra on 2016/2/29.
 */
public class MyApp extends Application {
    //记录HomeActivity中ScrollView的滚动位置，在LoginActivity中置0;
    public static int home_sv_x;
    public static int home_sv_y;

    @Override
    public void onCreate() {
        super.onCreate();
        UILImageUtils.initImageLoader(getApplicationContext());
    }
}
