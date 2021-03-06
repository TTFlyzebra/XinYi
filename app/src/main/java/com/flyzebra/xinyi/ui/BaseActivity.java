package com.flyzebra.xinyi.ui;

import android.support.v7.app.AppCompatActivity;

import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.GetHttp;

/**
 *
 * Created by Administrator on 2016/5/8.
 */
public abstract class BaseActivity extends AppCompatActivity{
    protected String HTTPTAG = "Activity"+Math.random();
    protected IHttp iHttp = GetHttp.getIHttp();

    @Override
    protected void onStop() {
        super.onStop();
        iHttp.cancelAll(HTTPTAG);
    }
}
