package com.flyzebra.xinyi.model.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;

import java.util.ArrayList;

/**
 * Created by FlyZebra on 2016/3/28.
 */
public class MyLoginBaidu extends LoginFrontiaBase implements ILogin {

    public MyLoginBaidu(Context context, LoginResult loginResult) {
        super(context, loginResult);
    }

    @Override
    public void login() {
        ArrayList<String> scope = new ArrayList<String>();
        scope.add(Scope_Basic);
        scope.add(Scope_Netdisk);
        mAuthorization.authorize((Activity) context, MediaType.BAIDU.toString(), scope, authorizationListener);
    }

    @Override
    public void logout() {
        super.logout();
    }

    @Override
    public void loginSucccessd(FrontiaUser result) {
        userInfo.setOpenid(result.getId());
        userInfo.setLoginwith("BAIDU");
    }

    @Override
    public void onActivtyResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public boolean checkIsLogined() {
        return false;
    }

}
