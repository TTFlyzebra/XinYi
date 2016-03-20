package com.flyzebra.xinyi.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by FlyZebra on 2016/2/28.
 */
public class UserInfo {
    public static boolean isLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("openid",null)==null||sharedPreferences.getString("access_token",null)==null){
            return false;
        }
        return true;
    }

    /**
     * 根据QQ登陆返回值帮用户注册帐号
     * @param openid
     * @param access_token
     * @return
     */
    public static boolean saveToServer(String openid, String access_token) {
        return false;
    }
}
