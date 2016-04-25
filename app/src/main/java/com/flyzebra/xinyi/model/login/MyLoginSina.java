package com.flyzebra.xinyi.model.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;

/**
 *
 * Created by FlyZebra on 2016/3/28.
 */
public class MyLoginSina extends LoginFrontiaBase implements ILogin {
    public final static String SINA_APP_KEY = "1950806839";

    public MyLoginSina(Context context, LoginResult loginResult) {
        super(context, loginResult);
    }

    @Override
    public void login() {
        mAuthorization.enableSSO(MediaType.SINAWEIBO.toString(), SINA_APP_KEY);
        mAuthorization.authorize((Activity) context, MediaType.SINAWEIBO.toString(), authorizationListener);
    }

    @Override
    public void logout() {
        super.logout();//父类的该方法退出所有登录
    }

    @Override
    public void loginSucccessd(FrontiaUser result) {
        userInfo.setOpenid(result.getId());
        userInfo.setLoginwith("SINA");
    }

    @Override
    public void onActivtyResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public boolean checkIsLogined() {
        return false;
    }

}
