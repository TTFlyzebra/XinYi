package com.flyzebra.xinyi.ui;

import android.support.v7.app.AppCompatActivity;

import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;

/**
 * Created by Administrator on 2016/5/8.
 */
public class BaseActivity extends AppCompatActivity{
    protected String HTTPTAG = getPackageName()+ Math.random();
    protected IHttp iHttp = MyVolley.getInstance();

    @Override
    protected void onStop() {
        super.onStop();
        iHttp.cancelAll(HTTPTAG);
    }
}
