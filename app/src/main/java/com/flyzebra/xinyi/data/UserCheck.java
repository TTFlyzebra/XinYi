package com.flyzebra.xinyi.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/2/28.
 */
public class UserCheck {
    public static boolean isLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("openid",null)==null||sharedPreferences.getString("access_token",null)==null){
            return false;
        }
        return true;
    }
}
