package com.flyzebra.xinyi.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.URLS;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.ResUtils;
import com.flyzebra.xinyi.utils.SerializableMap;
import com.flyzebra.xinyi.view.AttrChildGridView;
import com.flyzebra.xinyi.view.ChildGridView;
import com.flyzebra.xinyi.view.ChildViewPager;
import com.flyzebra.xinyi.view.pullzoom.PullToZoomScrollViewEx;
import com.flyzebra.xinyi.wxapi.PayActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FlyZebra on 2016/4/8.
 */
public class ProductInfoActivity extends BaseActivity {
    public static final String PRODUCT = "PRODUCT";
    @Bind(R.id.scroll_view)
    PullToZoomScrollViewEx scrollView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private Map<String, Object> product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productinfo);
        ButterKnife.bind(this);
        //初始化轮播
        Intent intent = getIntent();
        if (intent != null) {
            SerializableMap serializableMap = (SerializableMap) intent.getSerializableExtra(PRODUCT);
            product = serializableMap.getMap();
        }
        toolbar.setTitle((String) product.get(IAdapter.PR1_NAME));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadViewForCode();
        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (mScreenWidth * 0.75f));
        scrollView.setHeaderLayoutParams(localObject);

    }

    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);

        View headView = LayoutInflater.from(this).inflate(R.layout.pull_head_view, null, false);
        TextView textView01 = (TextView) headView.findViewById(R.id.pull_head_tv01);
        textView01.setText((CharSequence) product.get(IAdapter.PR1_NAME));

        View zoomView = LayoutInflater.from(this).inflate(R.layout.pull_zoom_view, null, false);
        iHttp.upImageView(this, URLS.URL + product.get(IAdapter.PR1_IMGURL), (ImageView) zoomView);

        View contentView = LayoutInflater.from(this).inflate(R.layout.pull_content_prod_iew, null, false);
        ((TextView) contentView.findViewById(R.id.prod_cont_tv01)).setText((String) product.get(IAdapter.PR1_NAME));
        ((TextView) contentView.findViewById(R.id.prod_cont_tv02)).setText((String) product.get(IAdapter.PR1_DESCRIBE));

        AttrChildGridView attrChildGridView = new AttrChildGridView(ProductInfoActivity.this);
        attrChildGridView.init(R.layout.product_item_01,
                new int[]{R.id.p_item_01_tv_01, R.id.p_item_01_tv_02, R.id.p_item_01_tv_03},
                new String[]{IAdapter.PR1_NAME, IAdapter.PR1_DESCRIBE, IAdapter.PR1_PRICE},
                new int[]{R.id.p_item_01_iv_01},
                new String[]{IAdapter.PR1_IMGURL})
                .setColumn(1)
                .setTitleImage(R.drawable.qq_ico01)
                .setTitle("同类产品");

        attrChildGridView.setShowImageSrc(new ChildGridView.ShowImageSrc() {
            @Override
            public void setImageSrcWithUrl(String url, ImageView iv) {
                iHttp.upImageView(ProductInfoActivity.this, URLS.URL + url, iv);
            }
        });
        attrChildGridView.setOnItemClick(new ChildGridView.OnItemClick() {
            @Override
            public void onItemClidk(Map<String, Object> data, View v) {
                Intent intent = new Intent(ProductInfoActivity.this, ProductInfoActivity.class);
                SerializableMap serializableMap = new SerializableMap();
                serializableMap.setMap(data);
                Bundle bundle = new Bundle();
                bundle.putSerializable(PRODUCT, serializableMap);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        List<Map<String, Object>> otherList = iHttp.readListFromCache(URLS.URL_HPR);
        if (otherList == null) {
            otherList = new ArrayList();
        }
        attrChildGridView.setData(otherList);
        ((ViewGroup) contentView).addView(attrChildGridView);

        //浮动菜单
        View floatView = LayoutInflater.from(this).inflate(R.layout.pull_float_view, null, false);
        ((TextView) floatView.findViewById(R.id.prod_float_tv01)).setText("￥" + product.get(IAdapter.PR1_PRICE) + "元");
        //加入购物车
        TextView tv02 = (TextView) floatView.findViewById(R.id.prod_float_tv02);
        tv02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //立即购买
        TextView tv03 = (TextView) floatView.findViewById(R.id.prod_float_tv03);
        tv03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductInfoActivity.this, PayActivity.class));
            }
        });

        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setFloatView(floatView);
        scrollView.setScrollContentView(contentView);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
