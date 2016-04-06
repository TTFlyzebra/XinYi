package com.flyzebra.xinyi.data;

import com.flyzebra.xinyi.model.http.IHttp;

import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public interface IAdapter<T extends List> extends IHttp.HttpAdapter {
    String DATA = "DATA";
    String VIEWPAGER = "VIEWPAGER";
    String TYPE = "TYPE";
    //RecyclearView TYPE
    int H_VIEWPAGER = 1;
    int H_HOTBUY = 2;
    int H_GRIDVIEW = 3;
    int H_RCLIST = 100;
    String P1_NAME = "mealname";
    String P1_PRICE = "mealprice";
    String P1_IMG_URL = "mealimage";
}
