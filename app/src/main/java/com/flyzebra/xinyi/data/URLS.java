package com.flyzebra.xinyi.data;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * Created by Administrator on 2016/3/29.
 */
public class URLS {

    public static final String URL_TABLE = "http://192.168.1.88/ordermeal/table.jsp";

    public static final String URL_TABLE_1 = "http://192.168.1.88/ordermeal/table.jsp?get=mealinfo";
    //服务器地址
    public static final String HOST = "flyzebra.wicp.net";

    public static final String URL = "https://"+HOST+"/xinyi";
    //进入应用轮播图片的存放位置
    public static final String URL_WEL = URL + "/API/Welcome";
    //推荐商店
    public static final String URL_HHS = URL + "/API/HomeShop";
    //推荐产品
    public static final String URL_HPR = URL + "/API/HomeProduct";

    public static final String URL_PTYPE = URL + "/API/Ptype";

    public static final String URL_PRT = URL + "/API/Product";

}
