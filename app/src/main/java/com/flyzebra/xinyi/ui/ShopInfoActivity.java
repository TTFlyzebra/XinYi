package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.SerializableMap;
import com.flyzebra.xinyi.view.pullzoom.PullToZoomScrollViewEx;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * FlyZebra
 * Created by Administrator on 2016/5/11.
 */
public class ShopInfoActivity extends BaseActivity {

    public static final String SHOP = "SHOP";
    @Bind(R.id.scroll_view)
    PullToZoomScrollViewEx scrollView;
    private Map<String,Object> shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopinfo);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            SerializableMap serializableMap = (SerializableMap) bundle.get(SHOP);
            shop = serializableMap.getMap();
        }

        loadViewForCode();
        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        scrollView.getPullRootView().findViewById(R.id.tv_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlyLog.i("zhuwenwu, onClick -->");
            }
        });

        scrollView.getPullRootView().findViewById(R.id.tv_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlyLog.i("zhuwenwu, onClick -->");
            }
        });

        scrollView.getPullRootView().findViewById(R.id.tv_test3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FlyLog.i("zhuwenwu, onClick -->");
            }
        });
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (mScreenWidth*0.75f));
        scrollView.setHeaderLayoutParams(localObject);

    }

    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.pull_head_view, null, false);
        TextView textView01 = (TextView) headView.findViewById(R.id.pull_head_tv01);
        textView01.setText((CharSequence) shop.get(IAdapter.SHOP_NAME));

        View zoomView = LayoutInflater.from(this).inflate(R.layout.pull_zoom_view, null, false);
        iHttp.upImageView(this, Constant.URL+shop.get(IAdapter.SHOP_IMGURL),(ImageView)zoomView);

        View contentView = LayoutInflater.from(this).inflate(R.layout.pull_content_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
