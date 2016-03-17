package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.flyzebra.xinyi.MyApp;
import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.utils.UserInfo;
import com.flyzebra.xinyi.openutils.QQUtil;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "com.flyzebra";
    private EditText lg_ed_us;
    private EditText lg_ed_ps;
    private ImageView lg_iv_qq;
    private Button lg_bt_lg;
    private com.tencent.connect.UserInfo mInfo;

    private static Tencent mTencent;
    private String QQ_APP_ID = "1105211644";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.login));
        setContentView(R.layout.login_activity);
        lg_ed_ps = (EditText) findViewById(R.id.lg_ed_ps);
        lg_ed_us = (EditText) findViewById(R.id.lg_ed_us);
        lg_iv_qq = (ImageView) findViewById(R.id.lg_iv_qq);
        lg_iv_qq.setOnClickListener(this);
        lg_bt_lg = (Button) findViewById(R.id.lg_bt_lg);
        lg_bt_lg.setOnClickListener(this);
        if (UserInfo.isLogin(this)) {
            MyApp.home_sv_x = 0;
            MyApp.home_sv_y = 0;
            MyApp.poi_rv_x = 0;
            MyApp.poi_rv_y = 0;
            StartHomeActivity();
        } else if (mTencent == null) {
            mTencent = Tencent.createInstance(QQ_APP_ID, this);
        }
    }

    private void StartHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeAcitivy.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lg_bt_lg:
                StartHomeActivity();
                break;
            case R.id.lg_iv_qq:
                if (!mTencent.isSessionValid()) {
                    mTencent.login(this, "all", loginListener);
                } else {
                    mTencent.logout(this);
                    updateUserInfo();
                }
                break;
        }
    }

    public static void initOpenidAndToken(JSONObject jsonObject) {
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
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.i(TAG,"JSONObject--"+values.toString());
            //登陆成功，写入TOKEN，跳转到主界面，存储用户信息
            initOpenidAndToken(values);
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            try {
                editor.putString("openid", values.getString("openid"));
                Log.i(TAG, "openid:" + values.getString("openid"));
                editor.putString("access_token", values.getString("access_token"));
                Log.i(TAG, "access_token:" + values.getString("access_token"));
                Log.i(TAG, "getAccessToken:" + mTencent.getAccessToken());
                Log.i(TAG,"getOpenID:"+mTencent.getOpenId());
                UserInfo.saveToServer(values.getString("openid"), values.getString("access_token"));
            } catch (JSONException e) {
                editor.clear();
                e.printStackTrace();
            }
            editor.commit();
            mTencent.logout(LoginActivity.this);
            StartHomeActivity();
        }
    };

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                QQUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                QQUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
//            QQUtil.showResultDialog(LoginActivity.this, response.toString(), "登录成功");
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {
        }

        @Override
        public void onError(UiError e) {
            QQUtil.toastMessage(LoginActivity.this, "onError: " + e.errorDetail);
            QQUtil.dismissDialog();
        }

        @Override
        public void onCancel() {
            QQUtil.toastMessage(LoginActivity.this, "onCancel: ");
            QQUtil.dismissDialog();
        }
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    new Thread() {
                        @Override
                        public void run() {
                            JSONObject json = (JSONObject) response;
                            if (json.has("figureurl")) {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = QQUtil.getbitmap(json.getString("figureurl_qq_2"));
                                } catch (JSONException e) {
                                }
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }.start();
                }

                @Override
                public void onCancel() {
                }
            };
            Log.i(TAG, "get userInfo");
            mInfo = new com.tencent.connect.UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        } else {
            lg_ed_us.setText("");
            lg_iv_qq.setImageResource(R.drawable.qq_login);
            Log.i(TAG, "no info");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        lg_ed_us.setText(response.getString("nickname"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (msg.what == 1) {
                Bitmap bitmap = (Bitmap) msg.obj;
                lg_iv_qq.setImageBitmap(bitmap);
            }
        }
    };
}


