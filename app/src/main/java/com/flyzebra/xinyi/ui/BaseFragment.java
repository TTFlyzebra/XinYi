package com.flyzebra.xinyi.ui;

import android.support.v4.app.Fragment;

import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyOkHttp;
import com.flyzebra.xinyi.model.http.MyVolley;

/**
 * Created by Administrator on 2016/5/8.
 */
public abstract class BaseFragment extends Fragment{
    protected String HTTPTAG = "Fragment"+Math.random();
    protected IHttp iHttp = MyOkHttp.getInstance();

    @Override
    public void onStop() {
        super.onStop();
        iHttp.cancelAll(HTTPTAG);
    }
}
