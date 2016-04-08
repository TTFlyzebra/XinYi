package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;

import com.flyzebra.xinyi.R;

/**
 * Created by Administrator on 2016/4/6.
 */
public class NoDevelopActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Slide());
        setContentView(R.layout.nofinish);
    }
}
