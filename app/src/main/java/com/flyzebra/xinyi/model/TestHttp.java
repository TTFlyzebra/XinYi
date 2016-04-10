package com.flyzebra.xinyi.model;

import com.flyzebra.xinyi.ui.IAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/1.
 */
public class TestHttp {
    public static List<Map<String,Object>> getViewPagerList(){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        for(int i= 1;i<=4;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(IAdapter.P2_NAME, "测试图片1组" + i);
            map.put(IAdapter.P2_IMG_URL, "http://192.168.1.88/ordermeal/images/aa" + i + ".jpg");
            list.add(map);
        }
        return list;
    }

    public static List<Map<String, Object>> getViewPagerList1() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 4; i >= 1; i--) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(IAdapter.P2_NAME, "测试图片2组" + i);
            map.put(IAdapter.P2_IMG_URL, "http://192.168.1.88/ordermeal/images/welcome" + i + ".jpg");
            list.add(map);
        }
        return list;
    }

    public static List<Map<String, Object>> getViewPagerList2() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 4; i >= 1; i--) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(IAdapter.P2_NAME, "测试图片3组" + i);
            list.add(map);
        }
        return list;
    }

    public static List<Map<String, Object>> getHotsellsList() {
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= 2; i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("name", "超级计算机"+i);
            map1.put("price", Math.round(Math.random()*10000));
            map1.put("imagepath", "http://192.168.1.88/ordermeal/images/computer"+i+".png");
            list.add(map1);
        }
        return list;
    }

}
