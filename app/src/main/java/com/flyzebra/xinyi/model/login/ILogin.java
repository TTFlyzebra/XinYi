package com.flyzebra.xinyi.model.login;

import android.content.Intent;

/**
 * Created by FlyZebra on 2016/3/21.
 */
public interface ILogin {
    void login();

    void logout();

    void onActivtyResult(int requestCode, int resultCode, Intent data);

    boolean checkIsLogined();

    interface LoginResult {
        void loginSuccees(UserInfo userInfo);

        void loginFaild();

        void loginCancel();
    }
}