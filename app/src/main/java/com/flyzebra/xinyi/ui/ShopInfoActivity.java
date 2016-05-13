package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * FlyZebra
 * Created by Administrator on 2016/5/11.
 */
public class ShopInfoActivity extends BaseActivity {

    public static final String IMG_URL = "BITMAP";
    public static final String TEXT = "TEXT";
    @Bind(R.id.sinfo_tv_01)
    TextView sinfoTv01;
    @Bind(R.id.sinfo_iv_01)
    ImageView sinfoIv01;
    private String imgUrl;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopinfo_activity);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            imgUrl = intent.getStringExtra(IMG_URL);
            iHttp.upImageView(this, Constant.URL + imgUrl, sinfoIv01);
            text = intent.getStringExtra(TEXT);
            sinfoTv01.setText(text);
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
