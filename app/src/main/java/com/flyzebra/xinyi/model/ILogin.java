package com.flyzebra.xinyi.model;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by FlyZebra on 2016/3/21.
 */
public interface ILogin {
    void login();
    void logout();
    void createUserInfo();
    void onActivtyResult(int requestCode, int resultCode, Intent data);
}
