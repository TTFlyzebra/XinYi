package com.flyzebra.xinyi.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/1.
 */
public class HttpGetData {
    public static List<Map<String,Object>> getViewPagerList(){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("name", "1");
        map1.put("path", "http://192.168.1.88/ordermeal/images/aa1.jpg");
        list.add(map1);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("name", "2");
        map2.put("path", "http://192.168.1.88/ordermeal/images/aa2.jpg");
        list.add(map2);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("name", "3");
        map3.put("path", "http://192.168.1.88/ordermeal/images/aa3.jpg");
        list.add(map3);
        Map<String, Object> mp4 = new HashMap<String, Object>();
        mp4.put("name", "4");
        mp4.put("path", "http://192.168.1.88/ordermeal/images/aa4.jpg");
        list.add(mp4);
        return list;
    }

    public static List<Map<String, Object>> getHotsellsList() {
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("name", "超级计算机"+i);
            map1.put("price", "8888.00元");
            map1.put("imagepath", "http://192.168.1.88/ordermeal/images/compter.png");
            list.add(map1);
        }
        return list;
    }
}
