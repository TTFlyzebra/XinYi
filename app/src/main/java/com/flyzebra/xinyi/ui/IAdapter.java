package com.flyzebra.xinyi.ui;

import com.flyzebra.xinyi.model.http.IHttp;

import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public interface IAdapter<T extends List> extends IHttp.HttpAdapter {
    String DATA = "DATA";
    String TYPE = "TYPE";

    //RecyclearView TYPE
    int H_VIEWPAGER_SHOP = 1;
    int H_GRIDVIEW_HOTS = 2;
    int H_GRIDVIEW_NEWS = 3;
    int H_GRIDVIEW_TIMESHOP = 4;
    int H_RCLIST = 100;

    String P1_NAME = "mealname";
    String P1_PRICE = "mealremark";
    String P1_IMG_URL = "mealimage";

    String SHOP_NAME = "shopname";
    String SHOP_IMGURL = "imgurl";

    String PR1_NAME = "name";
    String PR1_DESCRIBE = "describe";
    String PR1_IMGURL = "imgurl";
    String PR1_PRICE = "price";
    String PR1_PTYPE_ID = "ptype_id";
    String PR1_PTYPE_NAME = "ptype_name";
    String PR1_PATTR = "pattr";
    String PR1_PATTR_NAME = "name";
    String PR1_PATTR_COLOR = "color";

    String PTYPE_NAME = "ptype_name";
    String PTYPE_ID = "id";

}
