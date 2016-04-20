package com.flyzebra.xinyi.ui;

import android.content.Intent;
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
import com.flyzebra.xinyi.model.TestHttp;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.ResUtils;
import com.flyzebra.xinyi.view.Play3DImages;

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
//    @Bind(R.id.wel_iv_go1)
//    ImageView welIvGo1;
//    @Bind(R.id.wel_iv_go2)
//    ImageView welIvGo2;

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
                    FlyLog.i("<Play3DImages> setOnItemClick position=" + position);
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

    @OnClick(R.id.wel_bt_goHome)
    public void goHome() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
