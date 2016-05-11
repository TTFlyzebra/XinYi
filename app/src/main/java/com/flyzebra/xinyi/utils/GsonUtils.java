package com.flyzebra.xinyi.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/4/23.
 */
public class GsonUtils {
    public static <T> T json2Object(String jsonStr, Class<T> cls){
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonStr,cls);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static List<Map<String,Object>> json2List(String jsonStr){
        try {
            Gson gson = new Gson();
            List<Map<String,Object>> list = gson.fromJson(jsonStr, new TypeToken<List<Map<String,Object>>>(){}.getType());
            return list;
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

}
