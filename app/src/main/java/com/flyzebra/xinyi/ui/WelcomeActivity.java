package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.TestHttp;
import com.flyzebra.xinyi.utils.FlyLog;

import java.util.List;
import java.util.Map;

/**
 * by FlyZebra on 2016/3/27.
 */
public class WelcomeActivity extends AppCompatActivity {
    private static final int delayMillis = 2000;
    int count = 0;
    private ChildViewPager viewPagerChildView;
    private List<Map<String, Object>> list;
    private Handler mHandler = new Handler();
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            count++;
            if (count >= list.size()) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            mHandler.postDelayed(this, delayMillis);
            FlyLog.i("<WelcomeActivity>postDelayed");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewPagerChildView = new ChildViewPager(this, (ViewGroup) null, R.layout.child_viewpager_fullscreen);
        viewPagerChildView.setPadding(0, 0, 0, 0);
        setContentView(viewPagerChildView);
        list = TestHttp.getViewPagerList1();
        viewPagerChildView.setData(list);
        viewPagerChildView.setDelayMillis(delayMillis);
        mHandler.postDelayed(task, delayMillis);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(task);
        super.onDestroy();
    }
}
