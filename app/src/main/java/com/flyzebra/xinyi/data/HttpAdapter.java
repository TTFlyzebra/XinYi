package com.flyzebra.xinyi.data;

import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public interface HttpAdapter<T extends List> {
    String DATA = "DATA";
    String VIEWPAGER = "VIEWPAGER";
    String TYPE = "TYPE";
    //RecyclearView TYPE
    int H_VIEWPAGER = 1;
    int H_HOTBUY = 2;
    int H_RCLIST = 100;
    String P1_NAME = "mealname";
    String P1_PRICE = "mealprice";
    String P1_IMG_URL = "mealimage";

    T getList();

    void notifyDataSetChanged();
}
