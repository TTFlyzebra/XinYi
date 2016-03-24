package com.flyzebra.xinyi.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/3/24.
 */
public class FlyLog {
    public static final String TAG = "com.flyzebra";

    public static void i(String logString) {
        Thread thread = Thread.currentThread();
        Log.i(TAG, "[Thread][" + thread.getName() + "]:" + logString);
    }
}
