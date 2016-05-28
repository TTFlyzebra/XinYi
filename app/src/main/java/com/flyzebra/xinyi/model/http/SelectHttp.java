package com.flyzebra.xinyi.model.http;

import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyOkHttp;

/**
 *
 * Created by Administrator on 2016/5/18.
 */
public class SelectHttp {
    public static IHttp getIHttp(){
        return MyVolley.getInstance();
    }
}
