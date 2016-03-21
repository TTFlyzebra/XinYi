package com.flyzebra.xinyi.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.flyzebra.xinyi.ui.LoginActivity;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;


/**
 *
 * Created by FlyZebra on 2016/3/21.
 */
public class QQLogin implements ILogin {
    private static final String TAG ="com.flyzebra" ;
    private static Tencent mTencent;
    private String QQ_APP_ID = "1105211644";
    private Context context;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public QQLogin(Context context,loginResult mLoginResult) {
        this.context = context;
        this.mLoginResult = mLoginResult;
        if (mTencent == null) {
            mTencent = Tencent.createInstance(QQ_APP_ID, context);
        }
    }

    @Override
    public void login() {
        if (!mTencent.isSessionValid()) {
            mTencent.login((Activity) context, "all", loginIUlistener);
        } else {
            mTencent.logout(context);
        }
    }

    @Override
    public void logout() {
        mTencent.logout(context);
    }

    @Override
    public void createUserInfo() {

    }

    @Override
    public void onActivtyResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"mBaseIUlistener"+loginIUlistener);
        Tencent.onActivityResultData(requestCode,resultCode,data,loginIUlistener);
    }

    private loginResult mLoginResult;

    public interface loginResult {
        void loginSuccees(Object response);

        void loginFaild();

        void loginCancel();
    }

    BaseIUlistener loginIUlistener = new BaseIUlistener(){
        @Override
        public void doComplete(final JSONObject jsonObject) {
            //登陆成功，写入TOKEN，跳转到主界面，存储用户信息
            try {
                String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
                if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                    mTencent.setAccessToken(token, expires);
                    mTencent.setOpenId(openId);
                }
            } catch (Exception e) {
            }
            mTencent.logout(context);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mLoginResult.loginSuccees(jsonObject);
                }
            });
        }
    };

    public class BaseIUlistener implements IUiListener {
        @Override
        public void onComplete(final Object response) {
            Log.i(TAG,"QQLogin-->onComplete()");
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
            doComplete((JSONObject) response);
        }

        public void doComplete(JSONObject values) {
        }

        @Override
        public void onError(UiError uiError) {
            Log.i(TAG,"QQLogin-->onError()");
            mLoginResult.loginFaild();
        }

        @Override
        public void onCancel() {
            Log.i(TAG,"QQLogin-->onCancel()");
            mLoginResult.loginCancel();
        }
    }
}
