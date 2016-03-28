package com.flyzebra.xinyi.model.login;

import android.content.Context;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaAuthorizationListener.UserInfoListener;
import com.flyzebra.xinyi.utils.FlyLog;

/**
 * Created by Administrator on 2016/3/28.
 */
public abstract class LoginFrontiaBase {
    protected final static String Scope_Basic = "basic";
    protected final static String Scope_Netdisk = "netdisk";
    private final static String Baidu_APP_KEY = "G2omcGDXcPOXMIUhkCwH6MNcYagNAlFZ";
    protected static boolean isInit = false;
    protected FrontiaAuthorization mAuthorization;
    protected Context context;
    protected ILogin.LoginResult loginResult;
    protected UserInfo userInfo;
    protected AuthorizationListener authorizationListener = new AuthorizationListener() {
        @Override
        public void onSuccess(FrontiaUser result) {
            Frontia.setCurrentAccount(result);
            loginSucccessd(result);
            getUserInfo();
            FlyLog.i("<LoginFrontiaBase>onSuccess: social id=" + result.getId() + "token=" + result.getAccessToken() + "expired=" + result.getExpiresIn());
        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            loginResult.loginFaild();
            FlyLog.i("<LoginFrontiaBase>onFailure-> errCode=" + errCode + ", errMsg=" + errMsg);
        }

        @Override
        public void onCancel() {
            loginResult.loginCancel();
            FlyLog.i("<LoginFrontiaBase>cancel");
        }
    };

    public LoginFrontiaBase(Context context, ILogin.LoginResult loginResult) {
        this.context = context;
        this.loginResult = loginResult;
        if (!isInit) {
            Frontia.init(context.getApplicationContext(), Baidu_APP_KEY);
            isInit = true;
        }
        mAuthorization = Frontia.getAuthorization();
        userInfo = new UserInfo();
    }

    public abstract void loginSucccessd(FrontiaUser result);

    public void logout() {
        boolean resultAll = mAuthorization.clearAllAuthorizationInfos();
        if (resultAll) {
            Frontia.setCurrentAccount(null);
            FlyLog.i("<LoginFrontiaBase>所有登录退出成功");
        } else {
            FlyLog.i("<LoginFrontiaBase>所有登录退出失败");
        }
    }

    private void getUserInfo() {
        mAuthorization.getUserInfo(FrontiaAuthorization.MediaType.BAIDU.toString(), new UserInfoListener() {
            @Override
            public void onSuccess(FrontiaUser.FrontiaUserDetail result) {
                FlyLog.i("<MyLoginBaidu>getUserInfo->onSucces:result = username:" + result.getName()
                        + "birthday:" + result.getBirthday()
                        + "city:" + result.getCity()
                        + "province:" + result.getProvince()
                        + "sex:" + result.getSex()
                        + "pic url:" + result.getHeadUrl());
                userInfo.setUserName(result.getName());
                userInfo.setUserPhotoUrl(result.getHeadUrl());
                loginResult.loginSuccees(userInfo);
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                FlyLog.i("<LoginFrontiaBase>getUserInfo->onFailure");
                loginResult.loginSuccees(userInfo);
            }
        });
    }
}
