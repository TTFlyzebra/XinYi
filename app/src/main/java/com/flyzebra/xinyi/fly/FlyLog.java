package com.flyzebra.xinyi.fly;

import android.util.Log;


/**
 * Created by FlyZebra on 2016/3/24.
 */
public class FlyLog {
    public static final String TAG = "com.flyzebra";
    public static String[] filter = {"<VolleyUtils>"};
    public static void i(String logString) {
        for (int i = 0; i < filter.length; i++) {
            if (logString.indexOf(filter[i]) == 0) {
                return;
            }
        }
        Thread thread = Thread.currentThread();
        Log.i(TAG, "[" + thread.getName() + "][" + thread.getId() + "]" + logString);
    }
}
