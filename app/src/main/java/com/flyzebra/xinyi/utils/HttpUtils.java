package com.flyzebra.xinyi.utils;

import android.content.Context;
import android.widget.BaseAdapter;

import com.flyzebra.xinyi.openutils.VolleyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/1.
 */
public class HttpUtils {
    public static List<Map<String,Object>> getViewPagerList(){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        for(int i= 1;i<=4;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", String.valueOf(i));
            map.put("path", "http://192.168.1.88/ordermeal/images/aa" + i + ".jpg");
            list.add(map);
        }
        return list;
    }

    public static List<Map<String, Object>> getHotsellsList() {
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 8; i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("name", "超级计算机"+i);
            map1.put("price", "8888.00元");
            map1.put("imagepath", "http://192.168.1.88/ordermeal/images/compter.png");
            list.add(map1);
        }
        return list;
    }

    public static List<Map<String, Object>> gettestNewList() {
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 8; i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("mealname", "超级计算机"+i);
            map1.put("maelprice", "8888.00元");
            map1.put("mealimage", "/images/compter.png");
            list.add(map1);
        }
        return list;
    }

    public static void upAdapter(Context context,String url,List<Map<String,Object>> list ,BaseAdapter adapter){
        VolleyUtils.upAdapter(context,url,list,adapter);
    }
}
