package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;

/**
 * Created by FlyZebra on 2016/4/8.
 */
public class ProductInfoActivity extends BaseActivity {
    public static final String IMG_URL = "BITMAP";
    public static final String TEXT = "TEXT";
    private String imgUrl;
    private String text;

    private ImageView iv01;
    private TextView tv01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productinfo_activity);
        iv01 = (ImageView) findViewById(R.id.pinfo_iv_01);
        tv01 = (TextView) findViewById(R.id.pinfo_tv_01);
        Intent intent = getIntent();
        if (intent != null) {
            imgUrl = intent.getStringExtra(IMG_URL);
            iHttp.upImageView(this, Constant.URL+imgUrl, iv01);
            text = intent.getStringExtra(TEXT);
            tv01.setText(text);
        }
    }
}
