package com.flyzebra.xinyi.model;

import android.content.Intent;

import com.flyzebra.xinyi.data.UserInfo;

/**
 * Created by FlyZebra on 2016/3/21.
 */
public interface ILogin {
    void login();

    void logout();

    void createUserInfo(UserInfo userInfo);

    void onActivtyResult(int requestCode, int resultCode, Intent data);

    boolean checkIsLogined();
}