package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.URLS;
import com.flyzebra.xinyi.model.http.GetHttp;
import com.flyzebra.xinyi.model.http.MyVolley;


/**
 * Created by Administrator on 2016/5/29.
 */
public class SetFragment extends BaseFragment implements View.OnClickListener {
    private TextView setTv01;
    private TextView setTv02;
    private TextView setTv03;
    private TextView setTv04;
    public SetFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setTv01 = (TextView) view.findViewById(R.id.set_tv_01);
        setTv02 = (TextView) view.findViewById(R.id.set_tv_02);
        setTv03 = (TextView) view.findViewById(R.id.set_tv_03);
        setTv04 = (TextView) view.findViewById(R.id.set_tv_04);

        setTv01.setOnClickListener(this);
        setTv02.setOnClickListener(this);
        setTv03.setOnClickListener(this);
        setTv04.setOnClickListener(this);

        if (URLS.URL.indexOf("https") == 0) {
            setTv01.setText("切换使用Http（当前使用Https）");
        } else {
            setTv01.setText("切换使用Https（当前使用Http）");
        }

        if (iHttp instanceof MyVolley) {
            setTv02.setText("切换使用OkHttp（当前使用Volley）");
        } else {
            setTv02.setText("切换使用Volley（当前使用OkHttp）");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setToolbar(4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_tv_01:
                if (URLS.URL.indexOf("https") == 0) {
                    URLS.URL = URLS.URL.replaceFirst("https://", "http://");
                    setTv01.setText("切换使用Https（当前使用Http）");
                } else {
                    URLS.URL = URLS.URL.replaceFirst("http://", "https://");
                    setTv01.setText("切换使用Http （当前使用Https）");
                }
                break;
            case R.id.set_tv_02:
                if (GetHttp.MODE == GetHttp.OKHTTP) {
                    GetHttp.MODE = GetHttp.VOLLEY;
                    iHttp = GetHttp.getIHttp();
                    setTv02.setText("切换使用OkHttp（当前使用Volley）");
                } else {
                    GetHttp.MODE = GetHttp.OKHTTP;
                    iHttp = GetHttp.getIHttp();
                    setTv02.setText("切换使用Volley（当前使用OkHttp）");
                }
                break;
            case R.id.set_tv_03:
                Intent intent1 = new Intent(activity, MywebActivity.class);
                startActivity(intent1);
                break;
            case R.id.set_tv_04:
                Intent intent2 = new Intent(activity, BaiduMapActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
