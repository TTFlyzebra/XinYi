package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.GetHttp;

/**
 *
 * Created by Administrator on 2016/5/8.
 */
public abstract class BaseFragment extends Fragment{
    protected String HTTPTAG = "Fragment"+Math.random();
    protected IHttp iHttp;
    protected MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        iHttp = activity.iHttp;
    }

    @Override
    public void onStop() {
        super.onStop();
        iHttp.cancelAll(HTTPTAG);
    }
}
