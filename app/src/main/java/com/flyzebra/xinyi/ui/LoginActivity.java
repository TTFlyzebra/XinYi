package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.ILogin;
import com.flyzebra.xinyi.model.QQLogin;
import com.tencent.connect.common.Constants;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "com.flyzebra";
    private ILogin mQQLogin;
    private EditText lg_ed_us;
    private EditText lg_ed_ps;
    private ImageView lg_iv_qq;
    private Button lg_bt_lg;

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

        mQQLogin = new QQLogin(this, new QQLogin.loginResult() {
            @Override
            public void loginSuccees(Object response) {
                StartMainActivity();
            }
            @Override
            public void loginFaild() {
            }
            @Override
            public void loginCancel() {
            }
        });
    }

    private void StartMainActivity() {
        Intent intent = new Intent(LoginActivity.this, BuyActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lg_bt_lg:
                StartMainActivity();
                break;
            case R.id.lg_iv_qq:
                mQQLogin.login();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            mQQLogin.onActivtyResult(requestCode,resultCode,data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


