package com.flyzebra.xinyi.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.data.UserInfo;
import com.flyzebra.xinyi.model.TestHttp;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.flyzebra.xinyi.utils.DiskUtils;
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
public class WelcomeActivity extends AppCompatActivity {
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
    private IHttp iHttp = MyVolley.getInstance();
    private String HTTPTAG = "Fragment" + Math.random();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_activity);
        ButterKnife.bind(this);

        List<Map<String, Object>> list = TestHttp.getViewPagerList3();
        String imageArray[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            imageArray[i] = new String((String) list.get(i).get(IAdapter.P2_IMG_URL));
        }

        welPlay3d.setImageUrlArray(imageArray)
                .setDuration(1000)
                .setShowMillis(2000)
                .setImageAlpha(0.95f)
                .setImagePadding(ResUtils.getMetrices(this).widthPixels / 10)
                .Init();
        welPlay3d.setOnItemClick(new Play3DImages.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                if (welIvFore.getVisibility() == View.VISIBLE) {
                    FlyLog.i("<WelcomeActivity> setOnItemClick position=" + position);
//                    goHome();
                    Toast.makeText(WelcomeActivity.this, "Click " + position, Toast.LENGTH_SHORT);
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
        welBtGoHome.setVisibility(visibility);
//        welIvGo1.setVisibility(visibility);
//        welIvGo2.setVisibility(visibility);
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
        iHttp.cancelAll(HTTPTAG);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

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

    private ProgressDialog waitPlg;

    IHttp.HttpResult upLoginInfo = new IHttp.HttpResult() {
        @Override
        public void succeed(Object object) {
            FlyLog.i("<WelcomeActivity>upLoginInfo->succeed:object-->" + object);
            if (object == null) {
                waitPlg.dismiss();
                Toast.makeText(WelcomeActivity.this, "未知错误，登陆失败！", Toast.LENGTH_SHORT).show();
                return;
            }
            String result = object.toString();
            UserInfo userInfo = GsonUtils.jsonToObject(result, UserInfo.class);
            userInfo.saveLocalUserInfo(WelcomeActivity.this);
            waitPlg.dismiss();
            if (userInfo != null) {
                startMainActivity(userInfo);
            }
        }

        @Override
        public void readDiskCache(Object data) {
            FlyLog.i("<WelcomeActivity>upLoginInfo->readDiskCache:data-->" + data.toString());
            waitPlg.setMessage("连接服务器超时.....");
            waitPlg.setCancelable(true);
        }

        @Override
        public void faild(Object object) {
            FlyLog.i("<WelcomeActivity>upLoginInfo->faild:object-->" + object.toString());
            waitPlg.setMessage("连接服务器超时.....");
            waitPlg.setCancelable(true);
        }
    };

    /**
     * 此处要考虑的情况为，如果已保存登陆信息，是否需要再次向服务器提交验证(需保存密码)
     * 或者只更新登陆信息
     */
    private void upLoignInfo(UserInfo userInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(userInfo.getId()));
        waitPlg.setMessage("正在登陆服务器.....");
        waitPlg.setCancelable(false);
        waitPlg.show();
        iHttp.postString(Constant.URL + "/API/User/upLoginInfo", params, HTTPTAG, upLoginInfo);
    }

    @OnClick(R.id.wel_bt_goHome)
    public void goHome() {
        waitPlg = new ProgressDialog(this);
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
