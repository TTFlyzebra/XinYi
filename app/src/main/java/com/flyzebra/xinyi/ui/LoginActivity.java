package com.flyzebra.xinyi.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.URLS;
import com.flyzebra.xinyi.data.UserInfo;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.login.ILogin;
import com.flyzebra.xinyi.model.login.MyLoginBaidu;
import com.flyzebra.xinyi.model.login.MyLoginQQ;
import com.flyzebra.xinyi.model.login.MyLoginSina;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.GsonUtils;
import com.flyzebra.xinyi.utils.MD5;
import com.tencent.connect.common.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ILogin mLogin;
    private EditText lg_ed_us;
    private EditText lg_ed_ps;
    private Button lg_bt_lg;
    private Button lg_bt_rg;
    private int[] lg_IdRes = {R.id.lg_iv_baidu, R.id.lg_iv_qq, R.id.lg_iv_sina};
    private List<ImageView> lg_iv_list = new ArrayList<>();
    private ProgressDialog waitPlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lg_ed_ps = (EditText) findViewById(R.id.lg_ed_ps);
        lg_ed_us = (EditText) findViewById(R.id.lg_ed_us);
        lg_bt_lg = (Button) findViewById(R.id.lg_bt_lg);
        lg_bt_lg.setOnClickListener(this);
        lg_bt_rg = (Button) findViewById(R.id.lg_bt_rg);
        lg_bt_rg.setOnClickListener(this);
        for (int i = 0; i < lg_IdRes.length; i++) {
            lg_iv_list.add((ImageView) findViewById(lg_IdRes[i]));
            lg_iv_list.get(i).setOnClickListener(loginOnClick);
        }
        waitPlg = new ProgressDialog(this);
    }

    /**
     * 用户登陆网络请求的返回结果
     */
    IHttp.HttpResult loginUser = new IHttp.HttpResult() {
        @Override
        public void succeed(Object object) {
            FlyLog.i("<LoginActivity>loginUser->succeed:object-->" + object);
            if (object == null) {
                waitPlg.dismiss();
                Toast.makeText(LoginActivity.this, "未知错误，登陆失败！", Toast.LENGTH_SHORT).show();
                return;
            }
            String result = object.toString();
            if (result.equals("no user")) {
                waitPlg.dismiss();
                Toast.makeText(LoginActivity.this, "用户名不存在！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (result.equals("pw error")) {
                waitPlg.dismiss();
                Toast.makeText(LoginActivity.this, "登陆密码错误！", Toast.LENGTH_SHORT).show();
                return;
            }

            if (result.equals("us exist")) {
                waitPlg.dismiss();
                Toast.makeText(LoginActivity.this, "用户已存在！", Toast.LENGTH_SHORT).show();
                return;
            }

            UserInfo userInfo = GsonUtils.json2Object(result, UserInfo.class);
            waitPlg.dismiss();
            if (userInfo != null) {
                userInfo.saveLocalUserInfo(LoginActivity.this);
                startMainActivity(userInfo);
            }else{
                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void failed(Object object) {
            FlyLog.i("<LoginActivity>loginUser->failed:object-->" + object.toString());
            waitPlg.dismiss();
        }
    };

    /**
     * 使用第三方登陆的情况向服务器请求注册帐号
     *
     * @param userInfo
     */
    private void loginWithOther(final UserInfo userInfo) {
        waitPlg.setMessage("正请求通过第三方完成登陆.....");
        waitPlg.setCancelable(false);
        waitPlg.show();
        iHttp.postString(URLS.URL + "/API/User/login", userInfo.toMap(), HTTPTAG, loginUser);
        FlyLog.i("<LoginActivity>loginWithOther"+ userInfo.toMap());
    }


    //使用第三方登陆第三方网络请求返回的结果
    private ILogin.LoginResult loginResult = new ILogin.LoginResult() {
        @Override
        public void loginSuccees(UserInfo userInfo) {
            loginWithOther(userInfo);
        }
        @Override
        public void loginFaild() {
            waitPlg.dismiss();
        }
        @Override
        public void loginCancel() {
            waitPlg.dismiss();
        }
    };

    private View.OnClickListener loginOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //百度方式登陆
                case R.id.lg_iv_baidu:
                    mLogin = new MyLoginBaidu(LoginActivity.this, loginResult);
                    mLogin.login();
                    break;
                //QQ方式登陆
                case R.id.lg_iv_qq:
                    mLogin = new MyLoginQQ(LoginActivity.this, loginResult);
                    mLogin.login();
                    break;
                //新浪
                case R.id.lg_iv_sina:
                    mLogin = new MyLoginSina(LoginActivity.this, loginResult);
                    mLogin.login();
                    break;
            }
        }
    };

    /**
     * 校验输入的用户名和密码
     * @return
     */
    private Map<String, String> getSendParames() {
        String loginname = lg_ed_us.getText().toString();
        if (loginname.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }
        String loginword = lg_ed_ps.getText().toString();
        if (loginword.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }
        //对密码进行加密
        loginword = MD5.encode(loginword);
        Map<String, String> params = new HashMap<String, String>();
        params.put("loginname", loginname);
        params.put("loginword", loginword);
        return params;
    }

    /**
     * 使用APP客户端登陆
     */
    private void loginLocal() {
        Map<String, String> params = getSendParames();
        if (params != null) {
            waitPlg.setMessage("正在验证登陆信息.....");
            waitPlg.setCancelable(false);
            waitPlg.show();
            iHttp.postString(URLS.URL + "/API/User/login", params, HTTPTAG, loginUser);
        }
    }

    /**
     * 使用APP客户端注册
     */
    private void registerLocal() {
        final Map<String, String> params = getSendParames();
        if (params != null) {
            final String pswd = lg_ed_ps.getText().toString();
            View view  = LayoutInflater.from(this).inflate(R.layout.login_reg_dialog, null);
            TextView title = (TextView) view.findViewById(R.id.lg_dlg_tv1);
            TextView message = (TextView) view.findViewById(R.id.lg_dlg_tv2);
            Button bt1 = (Button) view.findViewById(R.id.lg_dlg_bt1);
            Button bt2 = (Button) view.findViewById(R.id.lg_dlg_bt2);
            title.setTextColor(0xffff0000);
            title.setText("密码将进行不可逆算法加密，请牢记!");
            message.setTextColor(0xff000000);
            message.setText(Html.fromHtml("您设置的密码为：<font color=red>" + pswd + "</font>"));
            final AlertDialog dlg = new AlertDialog.Builder(this)
                    .setView(view)
                    .show();
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    waitPlg.setMessage("正在向服务器提交申请.....");
                    waitPlg.setCancelable(false);
                    waitPlg.show();
                    iHttp.postString(URLS.URL + "/API/User/register", params, HTTPTAG, loginUser);
                    dlg.dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.cancel();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lg_bt_lg:
                loginLocal();
                break;
            case R.id.lg_bt_rg:
                registerLocal();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //QQ登陆方式，需添加以下代码
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            if (mLogin != null) {
                mLogin.onActivtyResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startMainActivity(UserInfo userInfo) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserInfo", userInfo);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}


