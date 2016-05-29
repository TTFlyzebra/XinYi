package com.flyzebra.xinyi.model.http;

import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyOkHttp;

/**
 * Created by Administrator on 2016/5/18.
 */
public class GetHttp {
    public static int MODE = 0;
    public static final int VOLLEY = 0;
    public static final int OKHTTP = 1;

    public static IHttp getIHttp() {
        switch (MODE) {
            case VOLLEY:
                return MyVolley.getInstance();
            case OKHTTP:
                return MyOkHttp.getInstance();
            default:
                return MyVolley.getInstance();
        }
    }
}
