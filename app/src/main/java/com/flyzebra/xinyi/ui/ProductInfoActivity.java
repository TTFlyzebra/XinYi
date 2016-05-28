package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.URLS;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.ResUtils;
import com.flyzebra.xinyi.utils.SerializableMap;
import com.flyzebra.xinyi.view.pullzoom.PullToZoomScrollViewEx;

import net.sourceforge.simcpux.PayActivity;

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
    private Map<String,Object> product;


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
        toolbar.setTitle(ResUtils.getString(this, R.string.product_info));
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

        //浮动菜单
        View floatView = LayoutInflater.from(this).inflate(R.layout.pull_float_view, null, false);
       ((TextView) floatView.findViewById(R.id.prod_float_tv01)).setText("￥" + product.get(IAdapter.PR1_PRICE) + "元");
//        TextView tv02 = (TextView) floatView.findViewById(R.id.prod_float_tv02);
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
