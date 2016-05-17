package com.flyzebra.xinyi.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.data.UserInfo;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.GsonUtils;
import com.flyzebra.xinyi.utils.ResUtils;
import com.flyzebra.xinyi.view.Play3DImages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * by FlyZebra on 2016/3/27.
 */
public class WelcomeActivity extends BaseActivity {
    @Bind(R.id.wel_play3d)
    Play3DImages welPlay3d;
    @Bind(R.id.wel_iv_fore)
    ImageView welIvFore;
    @Bind(R.id.wel_iv_pasue)
    ImageView welIvPasue;
    @Bind(R.id.wel_iv_next)
    ImageView welIvNext;
    @Bind(R.id.wel_bt_goHome)
    TextView welBtGoHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        waitPlg = new ProgressDialog(this);
        waitPlg.setMessage("正在读取信息.....");
        waitPlg.setCancelable(false);
        waitPlg.show();

        //初始化轮播
        welPlay3d.setDuration(1000)
                .setShowMillis(2000)
                .setImageAlpha(0.95f)
                .setImagePadding(ResUtils.getMetrices(WelcomeActivity.this).widthPixels / 10);

        iHttp.getString(Constant.URL_WEL, HTTPTAG, new IHttp.HttpResult() {
            @Override
            public void succeed(Object object) {
                List<Map<String, Object>> list = GsonUtils.json2List(object.toString());
                //如果没有广告信息，进入APP应用
                if (list == null) {
                    goHome();
                }else{
                    setWelPlay3d(list);
                }
            }
            @Override
            public void failed(Object object) {
                //从磁盘读取轮播数据
                List<Map<String, Object>> imgList = iHttp.readListFromCache(Constant.URL_WEL);
                if(imgList!=null){
                    setWelPlay3d(imgList);
                }else{
                    goHome();
                }
            }
        });
    }


    public void setWelPlay3d(List<Map<String, Object>> imgList) {
        //旋转动画初始化
        welPlay3d.setImageUrlList(imgList,"imageurl").Init();
        waitPlg.dismiss();
        welPlay3d.setOnItemClick(new Play3DImages.OnItemClick(){
            @Override
            public void onItemClick(int position) {
                if (welIvFore.getVisibility() == View.VISIBLE) {
                    FlyLog.i("<WelcomeActivity> setOnItemClick position=" + position);
                    Toast.makeText(WelcomeActivity.this, "Click " + position, Toast.LENGTH_SHORT).show();
                    delayHideView(0);
                    setViewVisble(View.GONE);
                } else {
                    setViewVisble(View.VISIBLE);
                    delayHideView(10000);
                }
            }
        });
    }

    private Runnable hideViewTask = new Runnable() {
        @Override
        public void run() {
            setViewVisble(View.GONE);
        }
    };
    private Handler mHandler = new Handler();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (welIvFore.getVisibility() == View.VISIBLE) {
                    delayHideView(0);
                    setViewVisble(View.GONE);
                } else {
                    setViewVisble(View.VISIBLE);
                    delayHideView(10000);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void setViewVisble(int visibility) {
        welIvFore.setVisibility(visibility);
        welIvPasue.setVisibility(visibility);
        welIvNext.setVisibility(visibility);
//        welBtGoHome.setVisibility(visibility);
    }

    @Override
    protected void onStart() {
        super.onStart();
        delayHideView(2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(hideViewTask);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    /**
     * 延时关闭轮播菜单
     * @param delayMillis
     */
    private void delayHideView(long delayMillis) {
        mHandler.removeCallbacks(hideViewTask);
        mHandler.postDelayed(hideViewTask, delayMillis);
    }

    @OnClick(R.id.wel_iv_pasue)
    public void submitPasue() {
        delayHideView(10000);
        if (!welPlay3d.isPlay()) {
            welPlay3d.playAnimition(0);
            welIvPasue.setImageResource(R.drawable.ic_pause_48dp);
        } else {
            welPlay3d.finishAnimition(300);
            welIvPasue.setImageResource(R.drawable.ic_play_48dp);
        }
    }

    @OnClick(R.id.wel_iv_next)
    public void submitNext() {
        delayHideView(10000);
        welIvPasue.setImageResource(R.drawable.ic_play_48dp);
        welPlay3d.pauseShowNextImage(500);
    }

    @OnClick(R.id.wel_iv_fore)
    public void submitFore() {
        delayHideView(10000);
        welIvPasue.setImageResource(R.drawable.ic_play_48dp);
        welPlay3d.pauseShowForeImage(500);
    }


    /*  * 以下使用本地存存储的用户信息登陆    */
    private ProgressDialog waitPlg;

    IHttp.HttpResult upLoginInfo = new IHttp.HttpResult() {
        @Override
        public void succeed(Object object) {
            FlyLog.i("<WelcomeActivity>upLoginInfo->succeed:object-->" + object);
            if (object == null) {
                waitPlg.dismiss();
                Toast.makeText(WelcomeActivity.this, "未知错误，登陆失败！", Toast.LENGTH_SHORT).show();
                startMainActivity(null);
                return;
            }
            String result = object.toString();
            UserInfo userInfo = GsonUtils.json2Object(result, UserInfo.class);
            if(userInfo==null){
                waitPlg.dismiss();
                Toast.makeText(WelcomeActivity.this, "获取用户信息失败！", Toast.LENGTH_SHORT).show();
//                startMainActivity(null);
                return;
            }
            userInfo.saveLocalUserInfo(WelcomeActivity.this);
            waitPlg.dismiss();
            if (userInfo != null) {
                startMainActivity(userInfo);
            }
        }
        @Override
        public void failed(Object object) {
            FlyLog.i("<WelcomeActivity>upLoginInfo->failed:object-->" + object.toString());
            waitPlg.setMessage("连接服务器超时.....");
            waitPlg.setCancelable(true);
        }
    };

    /**
     * 向服务器提交用户信息，记录用户登陆信息
     * 此处要考虑的情况为，如果已保存登陆信息，是否需要再次向服务器提交验证(需保存密码)
     * 或者只更新登陆信息
     */
    private void upLoignInfo(UserInfo userInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(userInfo.getId()));
        waitPlg.setMessage("正在登陆服务器.....");
        waitPlg.setCancelable(false);
        waitPlg.show();
        FlyLog.i("<WelcomeActivity>upLoignInfo:id=" + userInfo.getId());
        iHttp.postString(Constant.URL + "/API/User/upLoginInfo", params, HTTPTAG, upLoginInfo);
    }

    @OnClick(R.id.wel_bt_goHome)
    public void goHome() {
        /**
         * 如果本地没有缓存用户登陆信息，需要重新登陆或注册用户
         */
        UserInfo userInfo = UserInfo.getLocalUserInfo(WelcomeActivity.this);
        if (userInfo != null) {
            upLoignInfo(userInfo);
        } else {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void startMainActivity(UserInfo userInfo) {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserInfo", userInfo);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
