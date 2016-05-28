package com.flyzebra.xinyi.model.http;

import com.flyzebra.xinyi.data.URLS;
import com.flyzebra.xinyi.data.UserInfo;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2016/5/19.
 */
public interface XinYiServer {
    @GET("/API/HomeShop")
    Call<List<Map>> listHomeShop(@Path("shop_id") int shop_id);
}
