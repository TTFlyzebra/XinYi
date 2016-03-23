package com.flyzebra.xinyi.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by FlyZebra on 2016/3/21.
 */
public class QQLogin implements ILogin {
    private static final String TAG = "com.flyzebra";
    private static Tencent mTencent;
    private String QQ_APP_ID = "1105211644";
    private Context context;
    //    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private com.tencent.connect.UserInfo QQUserInfo;
    private UserInfo userInfo;

    private loginResult mLoginResult;
    private IUiListener userInfoListener = new IUiListener() {
        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onComplete(final Object response) {
            Log.i(TAG, "loginIUlistener" + response.toString());
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
            userInfo.setUserName(name);
            userInfo.setUserPhotoUrl(url);
            createUserInfo(userInfo);
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
                userInfo.setUserOpenID(openId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            mTencent.logout(context);
            Log.i(TAG, "loginIUlistener" + jsonResponse.toString());
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

    public QQLogin(Context context, loginResult mLoginResult) {
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
            getUserInfo();
        }
    }

    @Override
    public void logout() {
        mTencent.logout(context);
    }

    @Override
    public void createUserInfo(UserInfo userInfo) {

        //创建用户成功后执行登陆成功的操作
        mLoginResult.loginSuccees(userInfo);
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
        Log.i(TAG, "getUserInfo()");
        if (mTencent != null && mTencent.isSessionValid()) {
            QQUserInfo = new com.tencent.connect.UserInfo(context, mTencent.getQQToken());
            QQUserInfo.getUserInfo(userInfoListener);
            Log.i(TAG, "QQUserInfo.getUserInfo(userInfoListener);");
        }
    }

    public interface loginResult {
        void loginSuccees(UserInfo userInfo);

        void loginFaild();

        void loginCancel();
    }

}
