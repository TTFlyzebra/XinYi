package com.flyzebra.xinyi.model.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.flyzebra.xinyi.data.UserInfo;
import com.flyzebra.xinyi.utils.FlyLog;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by FlyZebra on 2016/3/21.
 */
public class MyLoginQQ implements ILogin {
    private static Tencent mTencent;
    private String QQ_APP_ID = "1105211644";
    private Context context;
    private com.tencent.connect.UserInfo QQUserInfo;
    private UserInfo userInfo;
    private LoginResult mLoginResult;
    private IUiListener userInfoListener = new IUiListener() {
        @Override
        public void onError(UiError e) {
        }
        @Override
        public void onComplete(final Object response) {
            FlyLog.i("<MyLoginQQ>loginIUlistener" + response.toString());
            //获取QQ昵称
            final JSONObject json = (JSONObject) response;
            String name = null;
            if (json.has("nickname")) {
                try {
                    name = json.getString("nickname");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //获取QQ图像URL
            String url = null;
            if (json.has("figureurl")) {
                try {
                    url = json.getString("figureurl_qq_2");
                } catch (JSONException e) {
                }
            }
            userInfo.setNickname(name);
            userInfo.setPictureurl(url);
            userInfo.setLoginwith("QQ");
            mLoginResult.loginSuccees(userInfo);
        }

        @Override
        public void onCancel() {
        }
    };
    private IUiListener loginIUlistener = new IUiListener() {
        @Override
        public void onComplete(final Object response) {
            if (null == response) {
//                QQUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                mLoginResult.loginFaild();
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
//                QQUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                mLoginResult.loginFaild();
                return;
            }
            //登陆成功，写入TOKEN，跳转到主界面，存储用户信息
            try {
                String token = jsonResponse.getString(Constants.PARAM_ACCESS_TOKEN);
                String expires = jsonResponse.getString(Constants.PARAM_EXPIRES_IN);
                String openId = jsonResponse.getString(Constants.PARAM_OPEN_ID);
                if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                    mTencent.setAccessToken(token, expires);
                    mTencent.setOpenId(openId);
                }
                userInfo.setOpenid(openId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            mTencent.logout(context);
            FlyLog.i("<MyLoginQQ>loginIUlistener" + jsonResponse.toString());
            getUserInfo();
        }

        @Override
        public void onError(UiError uiError) {
            mLoginResult.loginFaild();
        }

        @Override
        public void onCancel() {
            mLoginResult.loginCancel();
        }
    };

    public MyLoginQQ(Context context, LoginResult mLoginResult) {
        this.context = context;
        this.mLoginResult = mLoginResult;
        if (mTencent == null) {
            mTencent = Tencent.createInstance(QQ_APP_ID, context);
        }
        userInfo = new UserInfo();
    }

    @Override
    public void login() {
        if (!mTencent.isSessionValid()) {
            mTencent.login((Activity) context, "all", loginIUlistener);
        } else {
//            getUserInfo();
            mTencent.logout(context);
            mTencent.login((Activity) context, "all", loginIUlistener);
        }
    }

    @Override
    public void logout() {
        mTencent.logout(context);
    }

    @Override
    public void onActivtyResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, loginIUlistener);
    }

    @Override
    public boolean checkIsLogined() {
        return false;
    }

    private void getUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            QQUserInfo = new com.tencent.connect.UserInfo(context, mTencent.getQQToken());
            QQUserInfo.getUserInfo(userInfoListener);
        }
    }

}
