package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.flyzebra.xinyi.R;

/**
 * by FlyZebra on 2016/3/27.
 */
public class WelcomeActivity extends AppCompatActivity {
    private static final int delayMillis = 2000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
