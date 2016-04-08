package com.flyzebra.xinyi.ui;

import com.flyzebra.xinyi.model.http.IHttp;

import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public interface IAdapter<T extends List> extends IHttp.HttpAdapter {
    String DATA = "DATA";
    String TYPE = "TYPE";

    String VIEWPAGER = "1";
    String GRIDVIEW = "2";

    //RecyclearView TYPE
    int H_VIEWPAGER = 1;
    int H_GRIDVIEW = 2;
    int H_RCLIST = 100;

    String P1_NAME = "mealname";
    String P1_PRICE = "mealremark";
    String P1_IMG_URL = "mealimage";

    String P2_NAME = "name";
    String P2_IMG_URL = "path";
}
