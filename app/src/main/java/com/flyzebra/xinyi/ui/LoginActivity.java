package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.UserInfo;
import com.flyzebra.xinyi.model.login.ILogin;
import com.flyzebra.xinyi.model.login.MyLoginBaidu;
import com.flyzebra.xinyi.model.login.MyLoginQQ;
import com.flyzebra.xinyi.model.login.MyLoginSina;
import com.flyzebra.xinyi.utils.ResUtils;
import com.tencent.connect.common.Constants;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ILogin mLogin;
    private EditText lg_ed_us;
    private EditText lg_ed_ps;
    private Button lg_bt_lg;
    private int[] lg_IdRes = {R.id.lg_iv_baidu, R.id.lg_iv_qq, R.id.lg_iv_sina};
    private List<ImageView> lg_iv_list = new ArrayList<>();
    private ILogin.LoginResult loginResult = new ILogin.LoginResult() {
        @Override
        public void loginSuccees(UserInfo userInfo) {
            startMainActivity(userInfo);
        }

        @Override
        public void loginFaild() {
        }

        @Override
        public void loginCancel() {
        }
    };
    private View.OnClickListener loginOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //QQ方式登陆
                case R.id.lg_iv_baidu:
                    mLogin = new MyLoginBaidu(LoginActivity.this, loginResult);
                    mLogin.login();
                    break;
                case R.id.lg_iv_qq:
                    mLogin = new MyLoginQQ(LoginActivity.this, loginResult);
                    mLogin.login();
                    break;
                case R.id.lg_iv_sina:
                    mLogin = new MyLoginSina(LoginActivity.this, loginResult);
                    mLogin.login();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(ResUtils.getString(this, R.string.login));
        setContentView(R.layout.login_activity);

        lg_ed_ps = (EditText) findViewById(R.id.lg_ed_ps);
        lg_ed_us = (EditText) findViewById(R.id.lg_ed_us);
        lg_bt_lg = (Button) findViewById(R.id.lg_bt_lg);
        lg_bt_lg.setOnClickListener(this);
        for (int i = 0; i < lg_IdRes.length; i++) {
            lg_iv_list.add((ImageView) findViewById(lg_IdRes[i]));
            lg_iv_list.get(i).setOnClickListener(loginOnClick);
        }
    }

    private void startMainActivity(UserInfo userInfo) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserInfo", userInfo);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lg_bt_lg:
                startMainActivity(null);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            if (mLogin != null) {
                mLogin.onActivtyResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


