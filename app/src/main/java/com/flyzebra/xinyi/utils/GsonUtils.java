package com.flyzebra.xinyi.utils;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/4/23.
 */
public class GsonUtils {
    public static <T> T jsonToObject(String jsonStr,Class<T> cls){
        Gson gson = new Gson();
        return gson.fromJson(jsonStr,cls);
    }
}
